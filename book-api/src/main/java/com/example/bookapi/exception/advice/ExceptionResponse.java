package com.example.bookapi.exception.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

    @JsonProperty("error_code")
    private Integer errorCode;
    @JsonProperty("error_description")
    private String errorDescription;
    private String error;
    @JsonProperty("validation_errors")
    private Set<String> validationErrors;

}
