package ru.netology.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entities.UserEntity;
import ru.netology.model.AuthorizationRequest;
import ru.netology.repository.TokenRepository;
import ru.netology.repository.UserRepository;

import java.util.Optional;
import static org.mockito.Mockito.when;

public class AuthorizationServiceTest {
    public static final String BEARER = "Bearer 000";
    public static final String UNKNOWN_AUTH_TOKEN = "afe1716dc4be4ecabe2926decea2cffe";
    public static final String EXISTING_USER = "vladimir";
    public static final String NOT_EXISTING_USER = "ivanov";
    public static final String CORRECT_PASSWORD = "password";

    public static final String INCORRECT_PASSWORD = "pass1";

    private final UserRepository userRepository = createUserRepositoryMock();
    private final TokenRepository tokenRepository = createTokenRepositoryMock();

    private UserRepository createUserRepositoryMock() {
        final UserRepository userRepository = Mockito.mock(UserRepository.class);
        when(userRepository.findById(EXISTING_USER)).thenReturn(Optional.of(new UserEntity(1, EXISTING_USER, CORRECT_PASSWORD)));
        when(userRepository.findById(NOT_EXISTING_USER)).thenReturn(Optional.empty());
        return userRepository;
    }

    private TokenRepository createTokenRepositoryMock() {
        final TokenRepository tokenRepository = Mockito.mock(TokenRepository.class);
        when(tokenRepository.existsById(BEARER.split(" ")[1].trim())).thenReturn(true);
        when(tokenRepository.existsById(UNKNOWN_AUTH_TOKEN)).thenReturn(false);
        return tokenRepository;
    }

    @Test
    void login() {
        final AuthorizationService authorizationService = new AuthorizationService(userRepository, tokenRepository);
        Assertions.assertDoesNotThrow(() -> authorizationService.login(new AuthorizationRequest(EXISTING_USER, CORRECT_PASSWORD)));
    }

    @Test
    void login_userNotFound() {
        final AuthorizationService authorizationService = new AuthorizationService(userRepository, tokenRepository);
        Assertions.assertThrows(RuntimeException.class, () -> authorizationService.login(new AuthorizationRequest(NOT_EXISTING_USER, CORRECT_PASSWORD)));
    }

    @Test
    void login_incorrectPassword() {
        final AuthorizationService authorizationService = new AuthorizationService(userRepository, tokenRepository);
        Assertions.assertThrows(RuntimeException.class, () -> authorizationService.login(new AuthorizationRequest(EXISTING_USER, INCORRECT_PASSWORD)));
    }

    @Test
    void logout() {
        final AuthorizationService authorizationService = new AuthorizationService(userRepository, tokenRepository);
        Assertions.assertDoesNotThrow(() -> authorizationService.logout(BEARER));
    }

    @Test
    void checkToken() {
        final AuthorizationService authorizationService = new AuthorizationService(userRepository, tokenRepository);
        Assertions.assertDoesNotThrow(() -> authorizationService.checkToken(BEARER));
    }

    @Test
    void checkToken_failed() {
        final AuthorizationService authorizationService = new AuthorizationService(userRepository, tokenRepository);
        Assertions.assertThrows(RuntimeException.class, () -> authorizationService.checkToken(UNKNOWN_AUTH_TOKEN));
    }
}
