package com.example.demo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class DemoController {

    @GetMapping(path = "/validate-path/{pathVariable}")
    public Mono<String> validatePathVariable(
            @PathVariable @Size(min = 1, max = 3) String pathVariable
    ) {
        return Mono.just("Hello " + pathVariable);
    }

    @PostMapping(path = "/validate-body")
    public Mono<String> validateBody(
            @RequestBody @Valid DemoDTO body
    ) {
        return Mono.just("Hello " + body.toString());
    }
}
