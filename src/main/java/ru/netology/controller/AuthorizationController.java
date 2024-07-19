package ru.netology.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.model.AuthorizationRequest;
import ru.netology.model.AuthorizationResponse;
import ru.netology.service.AuthorizationService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@AllArgsConstructor
@Validated
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorizationResponse login(@Valid @RequestBody AuthorizationRequest authorizationRequest) {
        return authorizationService.login(authorizationRequest);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("auth-token") @NotBlank String authToken) {
        authorizationService.logout(authToken);
    }
}
