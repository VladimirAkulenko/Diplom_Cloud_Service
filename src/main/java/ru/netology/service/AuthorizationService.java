package ru.netology.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.netology.entities.TokenEntity;
import ru.netology.entities.UserEntity;
import ru.netology.exceptions.AuthorizationException;
import ru.netology.exceptions.BadCredentialsException;
import ru.netology.model.AuthorizationRequest;
import ru.netology.model.AuthorizationResponse;
import ru.netology.repository.TokenRepository;
import ru.netology.repository.UserRepository;

import java.util.UUID;

@Slf4j
@Service
public class AuthorizationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;


    public AuthorizationService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public AuthorizationResponse login(AuthorizationRequest authorizationRequest) {
        final String loginFromRequest = authorizationRequest.getLogin();
        final UserEntity user = userRepository.findById(loginFromRequest).orElseThrow(() ->
                new BadCredentialsException("User with login " + loginFromRequest + " not found"));

        if (!user.getPassword().equals(authorizationRequest.getPassword())) {
            throw new BadCredentialsException("Incorrect password for user " + loginFromRequest);
        }
        final String authToken = UUID.randomUUID().toString().replace("-", "");
        tokenRepository.save(new TokenEntity(authToken));
        log.info("User " + loginFromRequest + " entered with token " + authToken);
        return new AuthorizationResponse(authToken);
    }

    public void logout(String authToken) {
        log.info("User logout");
        tokenRepository.deleteById(authToken.split(" ")[1].trim());
    }

    public void checkToken(String authToken) {
        if (!tokenRepository.existsById(authToken.split(" ")[1].trim())) {
            throw new AuthorizationException();
        }
    }
}