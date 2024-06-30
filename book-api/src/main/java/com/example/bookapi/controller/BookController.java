package com.example.bookapi.controller;

import com.example.bookapi.dto.request.BookRequest;
import com.example.bookapi.dto.response.BookResponse;
import com.example.bookapi.dto.response.BorrowedBookResponse;
import com.example.bookapi.dto.response.PageResponse;
import com.example.bookapi.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping()
    public ResponseEntity<BookResponse> saveBook(
            @Valid @RequestBody BookRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.save(request, connectedUser));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<BookResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "keyword", defaultValue = "", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooks(keyword, page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page, size, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<BookResponse> updateShareableStatus(
            @PathVariable("book-id") Long id,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.updateShareableStatus(id, connectedUser));
    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<BookResponse> updateArchivedStatus(
            @PathVariable("book-id") Long id,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.updateArchivedStatus(id, connectedUser));
    }

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Long> borrowBook(
            @PathVariable("book-id") Long id,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.borrowBook(id, connectedUser));
    }

    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Long> returnedBorrowBook(
            @PathVariable("book-id") Long id,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.returnBorrowedBook(id, connectedUser));
    }

    @PatchMapping("/borrow/return/approve/{book-id}")
    public ResponseEntity<Long> approveReturnBorrowBook(
            @PathVariable("book-id") Long bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.approveReturnBorrowedBook(bookId, connectedUser));
    }

    @PostMapping(value = "/cover/{book-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadBookCoverPicture(
            @PathVariable("book-id") Long bookId,
            @RequestParam("file") MultipartFile file,
            Authentication connectedUser
    ) {
        bookService.uploadBookCoverPicture(file, connectedUser, bookId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/cover/{book-id}")
    public ResponseEntity<?> getBookCoverPicture(
            @PathVariable("book-id") Long bookId
    ) {
        return ResponseEntity.ok(bookService.getBookCoverPicture(bookId));
    }
}
