package com.ajou_nice.with_pet.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ErrorCode {
    private HttpStatus status;
    private String message;
}
