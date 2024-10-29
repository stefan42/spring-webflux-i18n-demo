package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
class DemoApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testHappyPathForPathVariable() {
        final Flux<String> result = webTestClient
                .get()
                .uri("/validate-path/abc")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class).getResponseBody();

        final String responseString = Mono.from(result).block();
        Assertions.assertEquals("Hello abc", responseString);
    }

    @Test
    public void testPathVariableValidationErrorForLocaleDE() {
        webTestClient
                .get()
                .uri("/validate-path/abcd")
                .header("Accept-Language", "de")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("""
                            {"validationErrorMessages" :  ["Größe muss zwischen 1 und 3 sein"]}
                        """);
    }

    @Test
    public void testPathVariableValidationErrorForLocaleEN() {
        webTestClient
                .get()
                .uri("/validate-path/abcd")
                .header("Accept-Language", "en")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("""
                            {"validationErrorMessages" :  ["size must be between 1 and 3"]}
                        """);
    }

    @Test
    public void testHappyPathForBody() {
        final Flux<String> result = webTestClient
                .post()
                .uri("/validate-body")
                .header("content-type", "application/json")
                .bodyValue("{\"field\":\"abc\"}")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class).getResponseBody();

        final String responseString = Mono.from(result).block();
        Assertions.assertEquals("Hello DemoDTO{field='abc'}", responseString);
    }

    @Test
    public void testBodyValidationErrorForLocaleDE() {
        webTestClient
                .post()
                .uri("/validate-body")
                .header("Content-Type", "application/json")
                .header("Accept-Language", "de")
                .bodyValue("{\"field\":\"abcd\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("""
                            {"validationErrorMessages" :  ["Größe muss zwischen 1 und 3 sein"]}
                        """);
    }

    @Test
    public void testBodyValidationErrorForLocaleEN() {
        webTestClient
                .post()
                .uri("/validate-body")
                .header("Content-Type", "application/json")
                .header("Accept-Language", "en")
                .bodyValue("{\"field\":\"abcd\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("""
                            {"validationErrorMessages" :  ["size must be between 1 and 3"]}
                        """);
    }
}
