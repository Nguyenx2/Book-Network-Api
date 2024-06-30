package com.example.bookapi.service.impl;

import com.example.bookapi.dto.request.BookRequest;
import com.example.bookapi.dto.response.BookResponse;
import com.example.bookapi.dto.response.BorrowedBookResponse;
import com.example.bookapi.dto.response.PageResponse;
import com.example.bookapi.entity.Book;
import com.example.bookapi.entity.BookTransactionHistory;
import com.example.bookapi.entity.User;
import com.example.bookapi.exception.OperationNotPermittedException;
import com.example.bookapi.repository.BookRepository;
import com.example.bookapi.repository.BookTransactionHistoryRepository;
import com.example.bookapi.service.BookService;
import com.example.bookapi.utils.FileUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository transactionHistoryRepository;

    @Override
    @Transactional
    public BookResponse save(BookRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = Book.builder()
                .title(request.getTitle())
                .authorName(request.getAuthorName())
                .description(request.getDescription())
                .shareable(request.isShareable())
                .build();
        book.setArchived(false);
        book.setOwner(user);
        return BookResponse.fromBook(bookRepository.save(book));
    }

    @Override
    public BookResponse findById(Long id) {
        Book book = findBookById(id);
        return BookResponse.fromBook(book);
    }

    @Override
    public PageResponse<BookResponse> findAllBooks(String keyword, int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(keyword, pageable, user.getId());
        List<BookResponse> bookResponses = books.stream()
                .map(BookResponse::fromBook)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findByOwnerId(pageable, user.getId());
        List<BookResponse> bookResponses = books.stream()
                .map(BookResponse::fromBook)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks =
                transactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(BorrowedBookResponse::fromBorrowedBook)
                .toList();
        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks =
                transactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(BorrowedBookResponse::fromBorrowedBook)
                .toList();
        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    @Override
    @Transactional
    public BookResponse updateShareableStatus(Long id, Authentication connectedUser) {
        Book book = findBookById(id);
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot update book shareable status");
        }
        book.setShareable(!book.isShareable());
        return BookResponse.fromBook(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookResponse updateArchivedStatus(Long id, Authentication connectedUser) {
        Book book = findBookById(id);
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot update book archived status");
        }
        book.setArchived(!book.isArchived());
        return BookResponse.fromBook(bookRepository.save(book));
    }

    @Override
    @Transactional
    public Long borrowBook(Long id, Authentication connectedUser) {
        Book book = findBookById(id);
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException
                    ("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }
        boolean isAlreadyBorrowed = transactionHistoryRepository.isAlreadyBorrowedByUser(id, user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("The requested book is already borrowed");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    @Override
    @Transactional
    public Long returnBorrowedBook(Long id, Authentication connectedUser) {
        Book book = findBookById(id);
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException
                    ("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }
        BookTransactionHistory bookTransactionHistory =
                transactionHistoryRepository.findByBookIdAndUserId(id, user.getId())
                        .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));
        bookTransactionHistory.setReturned(true);
        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    @Override
    @Transactional
    public Long approveReturnBorrowedBook(Long id, Authentication connectedUser) {
        Book book = findBookById(id);
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException
                    ("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }
        BookTransactionHistory bookTransactionHistory =
                transactionHistoryRepository.findByBookIdAndOwnerId(id, user.getId())
                        .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet. You cannot approve its return"));
        bookTransactionHistory.setReturnApproved(true);
        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    @Override
    @Transactional
    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Long bookId) {
        Book book = findBookById(bookId);
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot upload book cover picture");
        }
        if (book.getBookCover() != null) {
            String oldBookCover = book.getBookCover();
            FileUtils.deleteFile(oldBookCover);
        }
        String bookCover = FileUtils.storeFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }

    @Override
    public Resource getBookCoverPicture(Long bookId) {
        Book book = findBookById(bookId);
        return FileUtils.getFile(book.getBookCover());
    }

    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find book with id = " + id));
    }
}
