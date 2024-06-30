package com.example.bookapi.controller;

import com.example.bookapi.dto.request.FeedbackRequest;
import com.example.bookapi.dto.response.FeedbackResponse;
import com.example.bookapi.dto.response.PageResponse;
import com.example.bookapi.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping()
    public ResponseEntity<Long> saveFeedback(
            @Valid @RequestBody FeedbackRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.save(request, connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbackByBook(
            @PathVariable("book-id") Long bookId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(feedbackService.findAllFeedbackByBook(bookId, page, size));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Long> updateFeedback(
            @PathVariable("id") Long id,
            @Valid @RequestBody FeedbackRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, request, connectedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteFeedback(
            @PathVariable("id") Long id,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.deleteFeedback(id, connectedUser));
    }
}
