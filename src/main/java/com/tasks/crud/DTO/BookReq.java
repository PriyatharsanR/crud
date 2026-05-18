package com.tasks.crud.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookReq(
    int id,

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    String title,

    @NotBlank(message = "Author cannot be blank")
    @Size(max = 100, message = "Author cannot exceed 100 characters")
    String author
) {}
