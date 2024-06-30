package com.example.bookapi.service;

import com.example.bookapi.dto.request.FeedbackRequest;
import com.example.bookapi.dto.response.FeedbackResponse;
import com.example.bookapi.dto.response.PageResponse;
import org.springframework.security.core.Authentication;

public interface FeedbackService {
    Long save(FeedbackRequest request, Authentication connectedUser);

    PageResponse<FeedbackResponse> findAllFeedbackByBook(Long bookId, int page, int size);

    Long updateFeedback(Long id, FeedbackRequest request, Authentication connectedUser);

    Long deleteFeedback(Long id, Authentication connectedUser);
}
