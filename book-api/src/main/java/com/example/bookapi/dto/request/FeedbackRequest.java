package com.example.bookapi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackRequest {

    @Min(value = 1, message = "Rate must be greater than 0")
    @Max(value = 5, message = "Rate must be less than 6")
    private Double rate;

    @NotBlank(message = "Comment must not be empty")
    private String comment;

    @JsonProperty("book_id")
    private Long bookId;
}
