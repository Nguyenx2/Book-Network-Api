package com.example.bookapi.entity;

import com.example.bookapi.entity.Base.BaseEntityWithUpdater;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "book_transaction_histories")
public class BookTransactionHistory extends BaseEntityWithUpdater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean returned;
    private boolean returnApproved;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
