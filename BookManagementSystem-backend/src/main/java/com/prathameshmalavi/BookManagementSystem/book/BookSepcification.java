package com.prathameshmalavi.BookManagementSystem.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSepcification {

    public static Specification<Book> withOwnerId(Integer ownerId){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner"), ownerId));
    }
}
