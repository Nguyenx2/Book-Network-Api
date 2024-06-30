package com.example.bookapi.dto.response;

import com.example.bookapi.entity.BookTransactionHistory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {

    private String title;
    @JsonProperty("author_name")
    private String authorName;
    private String description;
    private double rate;
    private boolean returned;
    @JsonProperty("return_approved")
    private boolean returnApproved;

    public static BorrowedBookResponse fromBorrowedBook(BookTransactionHistory transactionHistory) {
        return BorrowedBookResponse.builder()
                .title(transactionHistory.getBook().getTitle())
                .authorName(transactionHistory.getBook().getAuthorName())
                .description(transactionHistory.getBook().getDescription())
                .rate(transactionHistory.getBook().getRate())
                .returned(transactionHistory.isReturned())
                .returnApproved(transactionHistory.isReturnApproved())
                .build();
    }
}
