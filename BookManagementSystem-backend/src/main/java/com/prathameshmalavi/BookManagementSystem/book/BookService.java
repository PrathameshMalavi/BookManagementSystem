package com.prathameshmalavi.BookManagementSystem.book;


import com.prathameshmalavi.BookManagementSystem.common.PageResponse;
import com.prathameshmalavi.BookManagementSystem.exception.OperationNotPermittedException;
import com.prathameshmalavi.BookManagementSystem.file.FileStorageService;
import com.prathameshmalavi.BookManagementSystem.history.BookTransactionHistory;
import com.prathameshmalavi.BookManagementSystem.history.BookTransactionHistoryRepository;
import com.prathameshmalavi.BookManagementSystem.user.User;
import jakarta.mail.internet.InternetHeaders;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;





    public Integer save(BookRequest request, Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);

        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(book -> bookMapper.toBookResponse(book))
                .orElseThrow(() -> new EntityNotFoundException("No Book found with the id" + bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> booksPage = bookRepository.findAllDisplayableBooks(pageable , user.getId());

        List<BookResponse> bookResponseList  = booksPage.stream()
                .map((book) ->bookMapper.toBookResponse(book))
                .collect(Collectors.toList());

        return new PageResponse<BookResponse>(
                bookResponseList,
                booksPage.getNumber(),
                booksPage.getSize(),
                booksPage.getTotalElements(),
                booksPage.getTotalPages(),
                booksPage.isFirst(),
                booksPage.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> booksPage = bookRepository.findAll(BookSepcification.withOwnerId(user.getId()), pageable);

        List<BookResponse> bookResponseList  = booksPage.stream()
                .map((book) ->bookMapper.toBookResponse(book))
                .collect(Collectors.toList());

        return new PageResponse<BookResponse>(
                bookResponseList,
                booksPage.getNumber(),
                booksPage.getSize(),
                booksPage.getTotalElements(),
                booksPage.getTotalPages(),
                booksPage.isFirst(),
                booksPage.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooksPage = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable , user.getId());

        List<BorrowedBookResponse> bookResponseList  = allBorrowedBooksPage.stream()
                .map((bookTransactionHistory) ->bookMapper.toBorrowedBookResponse(bookTransactionHistory))
                .collect(Collectors.toList());


        return new PageResponse<BorrowedBookResponse>(
                bookResponseList,
                allBorrowedBooksPage.getNumber(),
                allBorrowedBooksPage.getSize(),
                allBorrowedBooksPage.getTotalElements(),
                allBorrowedBooksPage.getTotalPages(),
                allBorrowedBooksPage.isFirst(),
                allBorrowedBooksPage.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allReturnedBooksPage = bookTransactionHistoryRepository.findAllReturnedBooks(pageable , user.getId());

        List<BorrowedBookResponse> bookResponseList  = allReturnedBooksPage.stream()
                .map((bookTransactionHistory) ->bookMapper.toBorrowedBookResponse(bookTransactionHistory))
                .collect(Collectors.toList());


        return new PageResponse<BorrowedBookResponse>(
                bookResponseList,
                allReturnedBooksPage.getNumber(),
                allReturnedBooksPage.getSize(),
                allReturnedBooksPage.getTotalElements(),
                allReturnedBooksPage.getTotalPages(),
                allReturnedBooksPage.isFirst(),
                allReturnedBooksPage.isLast()
        );
    }


    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("No Book Found with the " + bookId));

        User user = (User) connectedUser.getPrincipal();

        if(!Objects.equals(book.getOwner().getId() , user.getId())){
            throw new OperationNotPermittedException("You cannot update books shareable status");
        }

        book.setShareable(!book.isShareable());
        bookRepository.save(book);

        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("No Book Found with the " + bookId));

        User user = (User) connectedUser.getPrincipal();

        if(!Objects.equals(book.getOwner().getId() , user.getId())){
            throw new OperationNotPermittedException("You cannot update books archived status");
        }

        book.setShareable(!book.isArchived());
        bookRepository.save(book);

        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("No Book Found with the " + bookId));

        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("The Requested book cannot be borrowed since it is archived or not shareable");
        }

        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(book.getOwner().getId() , user.getId())){
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }
        
        final  boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId , user.getId());

        if(isAlreadyBorrowed){
            throw new OperationNotPermittedException("Requested book is already borrowed");
        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();

        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBorrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("No Book Found with the " + bookId));

        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("The Requested book cannot be borrowed since it is archived or not shareable");
        }

        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(book.getOwner().getId() , user.getId())){
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId , user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));

        bookTransactionHistory.setReturned(true);

        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }


    public Integer approveReturnBorrowBook(Integer bookId, Authentication connectedUser) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("No Book Found with the " + bookId));

        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("The Requested book cannot be borrowed since it is archived or not shareable");
        }

        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(book.getOwner().getId() , user.getId())){
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId , book.getOwner().getId())
                .orElseThrow(() -> new OperationNotPermittedException("The Book is not returned yet"));

        bookTransactionHistory.setReturnApproved(true);

        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Integer bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("No Book Found with the " + bookId));
        User user = (User) connectedUser.getPrincipal();

        var bookCover  = fileStorageService.saveFile(file, book, user.getId());
        book.setBookCover(bookCover);

        bookRepository.save(book);
    }
}
