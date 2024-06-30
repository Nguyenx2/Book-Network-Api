package com.example.bookapi.service.impl;

import com.example.bookapi.dto.request.FeedbackRequest;
import com.example.bookapi.dto.response.FeedbackResponse;
import com.example.bookapi.dto.response.PageResponse;
import com.example.bookapi.entity.Book;
import com.example.bookapi.entity.Feedback;
import com.example.bookapi.entity.User;
import com.example.bookapi.exception.OperationNotPermittedException;
import com.example.bookapi.repository.BookRepository;
import com.example.bookapi.repository.FeedbackRepository;
import com.example.bookapi.service.FeedbackService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public Long save(FeedbackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find book with id = " + request.getBookId()));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("Cannot give feedback for this book");
        }
        User user = (User) connectedUser.getPrincipal();
        if (book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("Cannot give feedback for your own book");
        }
        return feedbackRepository.save(Feedback.builder()
                .rate(request.getRate())
                .comment(request.getComment())
                .book(book)
                .user(user)
                .build()).getId();
    }

    @Override
    public PageResponse<FeedbackResponse> findAllFeedbackByBook(Long bookId, int page, int size) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find book with id = " + bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("Cannot get feedback for this book");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(FeedbackResponse::fromFeedback)
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }

    @Override
    @Transactional
    public Long updateFeedback(Long id, FeedbackRequest request, Authentication connectedUser) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find feedback with id = " + id));
        User user = (User) connectedUser.getPrincipal();
        if (!feedback.getUser().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("Cannot update feedback from another user");
        }
        if (feedback.getRate() != null) {
            feedback.setRate(request.getRate());
        }
        if (feedback.getComment() != null) {
            feedback.setComment(request.getComment());
        }
        return feedbackRepository.save(feedback).getId();
    }

    @Override
    @Transactional
    public Long deleteFeedback(Long id, Authentication connectedUser) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find feedback with id = " + id));
        User user = (User) connectedUser.getPrincipal();
        if (!feedback.getUser().getId().equals(user.getId()) &&
                !user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new OperationNotPermittedException("Cannot delete feedback from another user");
        }
        feedbackRepository.delete(feedback);
        return id;
    }
}
