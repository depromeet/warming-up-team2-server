package com.depromeet.booboo.domain.category;

public class CategoryDuplicatedException extends RuntimeException {

    public CategoryDuplicatedException(String message) {
        super(message);
    }
}
