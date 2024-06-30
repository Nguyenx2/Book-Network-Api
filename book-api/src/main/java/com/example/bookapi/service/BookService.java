package com.example.bookapi.service;

import com.example.bookapi.dto.request.BookRequest;
import com.example.bookapi.dto.response.BookResponse;
import com.example.bookapi.dto.response.BorrowedBookResponse;
import com.example.bookapi.dto.response.PageResponse;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
    BookResponse save(BookRequest request, Authentication connectedUser);

    BookResponse findById(Long id);

    PageResponse<BookResponse> findAllBooks(String keyword, int page, int size, Authentication connectedUser);

    PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser);

    BookResponse updateShareableStatus(Long id, Authentication connectedUser);

    BookResponse updateArchivedStatus(Long id, Authentication connectedUser);

    Long borrowBook(Long id, Authentication connectedUser);

    Long returnBorrowedBook(Long id, Authentication connectedUser);

    Long approveReturnBorrowedBook(Long id, Authentication connectedUser);

    void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Long bookId);

    Resource getBookCoverPicture(Long bookId);
}