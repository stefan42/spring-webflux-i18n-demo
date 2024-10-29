package com.example.demo;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected Mono<ResponseEntity<Object>> handleHandlerMethodValidationException(
            final HandlerMethodValidationException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final ServerWebExchange exchange
    ) {
        final List<String> validationErrorMessages = ex.getAllValidationResults()
                .stream()
                .map(r -> r.getResolvableErrors()
                        .stream()
                        .map(MessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining()))
                .toList();

        final ProblemDetail result = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getReason());
        result.setProperties(Map.of("validationErrorMessages", validationErrorMessages));

        return Mono.just(ResponseEntity.badRequest().body(result));
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(
            final WebExchangeBindException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final ServerWebExchange exchange
    ) {
        final List<String> validationErrorMessages = ex.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        final ProblemDetail result = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getReason());
        result.setProperties(Map.of("validationErrorMessages", validationErrorMessages));

        return Mono.just(ResponseEntity.badRequest().body(result));
    }
}
