package com.example.bookapi.repository;

import com.example.bookapi.entity.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long> {
    @Query("SELECT h FROM BookTransactionHistory h " +
            "WHERE h.user.id = :id")
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Long id);

    @Query("SELECT h FROM BookTransactionHistory h " +
            "WHERE h.book.owner.id = :id")
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Long id);

    @Query("SELECT (COUNT(*) > 0) AS isBorrowed " +
            "FROM BookTransactionHistory b " +
            "WHERE b.user.id = :userId " +
            "AND b.book.id = :bookId " +
            "AND b.returnApproved = false")
    boolean isAlreadyBorrowedByUser(Long bookId, Long userId);

    @Query("SELECT transaction " +
            "FROM BookTransactionHistory transaction " +
            "WHERE transaction.user.id = :userId " +
            "AND transaction.book.id = :bookId " +
            "AND transaction.returned = false " +
            "AND transaction.returnApproved = false")
    Optional<BookTransactionHistory> findByBookIdAndUserId(Long bookId, Long userId);

    @Query("SELECT transaction FROM BookTransactionHistory transaction " +
            "WHERE transaction.book.owner.id = :userId " +
            "AND transaction.book.id = :bookId " +
            "AND transaction.returned = true " +
            "AND transaction.returnApproved = false")
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Long bookId, Long userId);
}
