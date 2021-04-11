package br.com.rtrancoso.springboot.base.controller.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ErrorDetail {

    private String code;
    private String description;

}
