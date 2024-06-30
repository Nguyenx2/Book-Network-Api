package com.example.bookapi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookRequest {

    @NotBlank(message = "Title is mandatory")
    String title;
    @NotBlank(message = "Author name is mandatory")
    @JsonProperty("author_name")
    String authorName;
    @NotBlank(message = "Description is mandatory")
    String description;
    @NotNull(message = "Shareable is mandatory")
    boolean shareable;
}
