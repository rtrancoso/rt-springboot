package br.com.rtrancoso.springboot.base.autoconfigure;

import br.com.rtrancoso.springboot.base.controller.model.ErrorDetail;
import br.com.rtrancoso.springboot.base.controller.model.ErrorResponse;
import br.com.rtrancoso.springboot.base.exception.BusinessException;
import br.com.rtrancoso.springboot.base.exception.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnWebApplication
public class RTAutoConfiguration {

    @Bean
    public WebMvcConfigurer corsMappings() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
            }
        };
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
        jackson2ObjectMapperBuilder.modules(new JavaTimeModule(), new ParameterNamesModule(), new Jdk8Module());
        jackson2ObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
        jackson2ObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        jackson2ObjectMapperBuilder.failOnUnknownProperties(false);
        jackson2ObjectMapperBuilder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return jackson2ObjectMapperBuilder;
    }

    @Slf4j
    @Configuration
    @RestControllerAdvice
    public static class RTResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

        private static final String ARGUMENT_NOT_VALID_MESSAGE = "There are one or more arguments not valid for the method called";
        private static final String UNEXPECTED_ERROR_MESSAGE = "Unexpected error, contact the system's administrator with code '%s'";
        private static final String BUSINESS_ERROR_MESSAGE = "Some business validations have not been met";

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
            return new ResponseEntity<>(ErrorResponse.builder()
                .message(ARGUMENT_NOT_VALID_MESSAGE)
                .errors(ex.getBindingResult().getFieldErrors().stream().map(fieldError -> ErrorDetail.builder()
                    .code("CONSTRAINT_" + fieldError.getField().toUpperCase())
                    .description(fieldError.getDefaultMessage())
                    .build()).collect(Collectors.toList()))
                .timestamp(LocalDateTime.now())
                .build(),
                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFound(Exception ex, WebRequest request) {
            return createErrorResponse(HttpStatus.NOT_FOUND, StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "ResourceNotFoundException");
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ARGUMENT_NOT_VALID_MESSAGE,
                ex.getConstraintViolations().stream().map(constraintViolation -> ErrorDetail.builder()
                    .code(constraintViolation.getMessageTemplate())
                    .description(constraintViolation.getMessage())
                    .build()).collect(Collectors.toList()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex, WebRequest request) {
            var tag = UUID.randomUUID();
            log.error("Unexpected error: {}", tag, ex);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, String.format(UNEXPECTED_ERROR_MESSAGE, tag));
        }

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, WebRequest request) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, BUSINESS_ERROR_MESSAGE,
                ex.getErrors().stream().map(businessError ->
                    ErrorDetail.builder()
                        .code(businessError.getCode())
                        .description(businessError.getDescription())
                        .build()).collect(Collectors.toList()));
        }

        private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus httpStatus, String errorMessage) {
            return createErrorResponse(httpStatus, errorMessage, null);
        }

        private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus httpStatus, String message, List<ErrorDetail> errors) {
            return new ResponseEntity<>(ErrorResponse.builder()
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build(), httpStatus);
        }

    }

}
