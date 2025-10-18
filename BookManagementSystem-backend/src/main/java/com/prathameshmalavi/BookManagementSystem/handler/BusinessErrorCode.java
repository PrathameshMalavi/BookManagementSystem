package com.prathameshmalavi.BookManagementSystem.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;



public enum BusinessErrorCode {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, HttpStatus.BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, HttpStatus.BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, HttpStatus.FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, HttpStatus.FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, HttpStatus.FORBIDDEN, "Login and / or Password is incorrect"),
    ;

    @Getter
    private int code;

    @Getter
    private String description;

    @Getter
    private HttpStatus httpStatusCode;

    BusinessErrorCode(int code, HttpStatus httpStatusCode , String description) {
        this.code = code;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }
}
