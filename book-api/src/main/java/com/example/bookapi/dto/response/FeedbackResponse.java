package com.example.bookapi.dto.response;

import com.example.bookapi.entity.Feedback;
import com.example.bookapi.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponse {
    private Double rate;
    private String comment;
    private User user;

    public static FeedbackResponse fromFeedback(Feedback feedback) {
        return FeedbackResponse.builder()
                .rate(feedback.getRate())
                .comment(feedback.getComment())
                .user(feedback.getUser())
                .build();
    }
}
