package com.prathameshmalavi.BookManagementSystem.feedback;


import com.prathameshmalavi.BookManagementSystem.book.Book;
import com.prathameshmalavi.BookManagementSystem.common.BaseEntity;
import com.prathameshmalavi.BookManagementSystem.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Feedback extends BaseEntity {

    private Double note;  //starts 1 - 5

    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

}
