package com.prathameshmalavi.BookManagementSystem.feedback;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.PostMapping;

public record FeedbackRequest(
        @NotNull
        @Positive(message = "200")
        @Min(value = 0, message = "201")
        @Max(value = 5, message = "202")
        Double note,

        @NotNull(message = "203")
        @NotEmpty(message = "203")
        @NotBlank(message = "203")
        String comment,

        @NotNull(message = "204")
        Integer bookId
){
}
