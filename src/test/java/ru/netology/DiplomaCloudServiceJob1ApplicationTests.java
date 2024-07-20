package ru.netology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.netology.entities.FileEntity;
import ru.netology.entities.TokenEntity;
import ru.netology.entities.UserEntity;
import ru.netology.model.AuthorizationRequest;
import ru.netology.model.AuthorizationResponse;
import ru.netology.model.FileNameInRequest;
import ru.netology.repository.FileRepository;
import ru.netology.repository.TokenRepository;
import ru.netology.repository.UserRepository;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
class DiplomaCloudServiceJob1ApplicationTests {

    private static final String TEST_LOGIN = "vladimir";
    private static final String TEST_PASSWORD = "password";
    private static final String BEARER = "Bearer 000";
    private static final String AUTH_ENDPOINT = "/login";
    private static final String ENDPOINT_LOGOUT = "/logout";
    private static final String HEADER_NAME = "auth-token";
    private static final String TYPE = "text/plain";
    private static final long SIZE = 10;
    private static final byte[] CONTENT = new byte[]{102, 105, 108, 101, 32, 116, 101, 115, 116, 33};
    private static final String FILE_NAME = "testFile.txt";
    private static final String NEW_FILE_NAME = "test.txt";
    private static final String PATH_TO_TEST_FILE = "/file?filename=testFile.txt";

            @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        fileRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    public void testLogin() {
        userRepository.save(new UserEntity(1, TEST_LOGIN, TEST_PASSWORD));
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        final AuthorizationRequest operation = new AuthorizationRequest(TEST_LOGIN, TEST_PASSWORD);
        final HttpEntity<AuthorizationRequest> request = new HttpEntity<>(operation, headers);

        final ResponseEntity<AuthorizationResponse> result = this.restTemplate.postForEntity(AUTH_ENDPOINT, request, AuthorizationResponse.class);
        Assertions.assertNotNull(result.getBody());
        Assertions.assertNotNull(result.getBody().getAuthToken());
    }

    @Test
    public void testLogout() {
        final String authToken = BEARER;
        tokenRepository.save(new TokenEntity(authToken));

        final HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_NAME, authToken);
        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        this.restTemplate.postForEntity(ENDPOINT_LOGOUT, request, Void.class);
        Assertions.assertFalse(tokenRepository.existsById(authToken.split(" ")[1].trim()));
    }

    @Test
    public void testUploadFile() {
        final String authToken = BEARER;
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_NAME, authToken);

        final MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", new ClassPathResource(FILE_NAME));

        final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parts, headers);

        this.restTemplate.postForEntity(PATH_TO_TEST_FILE, request, Void.class);

        final Optional<FileEntity> fileInRepository = fileRepository.findById(FILE_NAME);
        Assertions.assertTrue(fileInRepository.isPresent());
        Assertions.assertEquals(new FileEntity(FILE_NAME, SIZE, TYPE, CONTENT), fileInRepository.get());
    }

    @Test
    public void testDeleteFile() {
        fileRepository.save(new FileEntity(FILE_NAME, SIZE, TYPE, CONTENT));

        final String authToken = BEARER;
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_NAME, authToken);

        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        this.restTemplate.exchange(PATH_TO_TEST_FILE, HttpMethod.DELETE, request, Void.class);

        Assertions.assertFalse(fileRepository.existsById(FILE_NAME));
    }

    @Test
    public void testGetFile() {
        fileRepository.save(new FileEntity(FILE_NAME, SIZE, TYPE, CONTENT));

        final String authToken = BEARER;
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_NAME, authToken);

        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        final ResponseEntity<byte[]> result = this.restTemplate.exchange(PATH_TO_TEST_FILE, HttpMethod.GET, request, byte[].class);

        Assertions.assertNotNull(result.getBody());
        Assertions.assertArrayEquals(CONTENT, result.getBody());
    }

    @Test
    public void testEditFile() {
        fileRepository.save(new FileEntity(FILE_NAME, SIZE, TYPE, CONTENT));

        final String authToken = BEARER;
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_NAME, authToken);

        final HttpEntity<FileNameInRequest> request =
                new HttpEntity<>(new FileNameInRequest(NEW_FILE_NAME), headers);

        this.restTemplate.exchange(PATH_TO_TEST_FILE, HttpMethod.PUT, request, Void.class);

        Assertions.assertFalse(fileRepository.existsById(FILE_NAME));
        final Optional<FileEntity> fileInRepository = fileRepository.findById(NEW_FILE_NAME);
        Assertions.assertTrue(fileInRepository.isPresent());
        Assertions.assertEquals(new FileEntity(NEW_FILE_NAME, SIZE, TYPE, CONTENT), fileInRepository.get());
    }

    @Test
    public void testGetFileList() {
        fileRepository.save(new FileEntity(FILE_NAME, SIZE, TYPE, CONTENT));

        final String authToken = BEARER;
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_NAME, authToken);

        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        final ResponseEntity<Object> result = this.restTemplate.exchange("/list?limit=15", HttpMethod.GET, request, Object.class);

        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals("[{filename=testFile.txt, size=10}]", result.getBody().toString());
    }

    @Test
    void contextLoads() {
    }

}
