package com.prathameshmalavi.BookManagementSystem.feedback;

import com.prathameshmalavi.BookManagementSystem.book.Book;
import com.prathameshmalavi.BookManagementSystem.book.BookRepository;
import com.prathameshmalavi.BookManagementSystem.book.BorrowedBookResponse;
import com.prathameshmalavi.BookManagementSystem.common.PageResponse;
import com.prathameshmalavi.BookManagementSystem.exception.OperationNotPermittedException;
import com.prathameshmalavi.BookManagementSystem.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    public Integer save(FeedbackRequest request, Authentication connectedUser) {

        Book book =  bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("No Book found with the id" + request.bookId()));

        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("The Requested book cannot be Rated since it is archived or not shareable");
        }

        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(book.getOwner().getId() , user.getId())){
            throw new OperationNotPermittedException("You cannot rate your own book");
        }

        Feedback feedback = feedbackMapper.toFeedback(request);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedback(Integer bookId, int page, int size, Authentication connectedUser) {

        Pageable pageable = PageRequest.of(page , size);
        User user = (User) connectedUser.getPrincipal();
        Page<Feedback> feedbackPage = feedbackRepository.findAllByBookId(bookId , pageable);

        List<FeedbackResponse> feedbackResponsesList = feedbackPage.stream()
                .map((feedback -> feedbackMapper.toFeedbackResponse(feedback, user.getId())))
                .toList();

        return  new PageResponse<FeedbackResponse>(
                feedbackResponsesList,
                feedbackPage.getNumber(),
                feedbackPage.getSize(),
                feedbackPage.getTotalElements(),
                feedbackPage.getTotalPages(),
                feedbackPage.isFirst(),
                feedbackPage.isLast()
        );
    }
}
