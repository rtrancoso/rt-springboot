package br.com.rtrancoso.springboot.base.exception;

import br.com.rtrancoso.springboot.base.exception.model.Error;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends Exception {

    @Getter
    private final List<Error> errors = new ArrayList<>();

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Error error) {
        this.errors.add(error);
    }

    public BusinessException(List<? extends Error> businessErrors) {
        this.errors.addAll(businessErrors);
    }

    public void addBusinessError(Error error) {
        errors.add(error);
    }

    public String getBusinessErrorMessage() {
        if (errors.isEmpty()) {
            return getMessage();
        }

        var separator = " ; ";
        StringBuilder stringBuilder = new StringBuilder();
        for (Error error : errors) {
            stringBuilder.append(error.getDescription()).append(separator);
        }

        var lastSeparator = stringBuilder.lastIndexOf(separator);
        if (lastSeparator >= 0) {
            stringBuilder.setLength(lastSeparator);
        }

        return stringBuilder.toString();
    }

}
