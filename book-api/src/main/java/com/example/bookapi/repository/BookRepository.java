package com.example.bookapi.repository;

import com.example.bookapi.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE :keyword IS NULL OR :keyword = ''" +
            "OR b.title LIKE '%:keyword%;' " +
            "AND b.archived = false " +
            "AND b.shareable = true " +
            "AND b.owner.id != :id")
    Page<Book> findAllDisplayableBooks(String keyword, Pageable pageable, Long id);

    @Query("SELECT b FROM Book b WHERE b.owner.id = :id")
    Page<Book> findByOwnerId(Pageable pageable, Long id);
}
