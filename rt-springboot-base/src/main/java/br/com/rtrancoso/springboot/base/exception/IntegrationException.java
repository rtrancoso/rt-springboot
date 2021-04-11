package br.com.rtrancoso.springboot.base.exception;

import br.com.rtrancoso.springboot.base.exception.model.Error;
import lombok.Getter;

@Getter
public class IntegrationException extends RuntimeException {

    private final Error error;
    private final Integer status;
    private final String url;

    public IntegrationException(final Error error, final Integer status, final String url) {
        super(error.getDescription());
        this.error = error;
        this.status = status;
        this.url = url;
    }

    public IntegrationException(final Throwable cause, final Error error, final Integer status, final String url) {
        super(error.getDescription(), cause);
        this.error = error;
        this.status = status;
        this.url = url;
    }

}
