package br.com.rtrancoso.springboot.base.stream.exception;

public class EventProducerException extends RuntimeException {

    public EventProducerException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
