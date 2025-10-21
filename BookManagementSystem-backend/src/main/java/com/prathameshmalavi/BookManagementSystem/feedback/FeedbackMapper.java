package com.prathameshmalavi.BookManagementSystem.feedback;

import com.prathameshmalavi.BookManagementSystem.book.Book;

import java.util.Objects;

public class FeedbackMapper {


    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder()
                        .id(request.bookId())
                        .archived(false) //not required and has no impact
                        .shareable(false) //not required and has no impact
                        .build())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer userId) {
        boolean ownFeedback = Objects.equals(feedback.getCreatedBy(), userId);
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(ownFeedback)
                .build();
    }
}
