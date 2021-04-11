package br.com.rtrancoso.springboot.base.controller.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class ErrorResponse {

    private String message;
    private List<ErrorDetail> errors;
    private LocalDateTime timestamp;

}
