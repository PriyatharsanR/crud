package com.tasks.crud.DTO;

import lombok.Builder;

@Builder
public record ApiRes<T>(
    String code,
    String title,
    String message,
    T data
) {
    public ApiRes(String code, String title, String message) {
        this(code, title, message, null);
    }
}
