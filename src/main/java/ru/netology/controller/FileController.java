package ru.netology.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.model.FileNameInRequest;
import ru.netology.service.AuthorizationService;
import ru.netology.service.FileService;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


@RestController
@RequestMapping("/file")
@AllArgsConstructor
@Validated
public class FileController {
    private final FileService fileService;
    private final AuthorizationService authorizationService;


    @PostMapping
    public void uploadFile(@RequestHeader("auth-token") @NotBlank String authToken, @RequestParam("file") MultipartFile file) {
        authorizationService.checkToken(authToken);
        fileService.uploadFile(file);
    }

    @DeleteMapping
    public void deleteFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String filename) {
        authorizationService.checkToken(authToken);
        fileService.deleteFile(filename);
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] downloadFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String filename) {
        authorizationService.checkToken(authToken);
        return fileService.downloadFile(filename);
    }

    @PutMapping
    public void editFileName(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String filename, @Valid @RequestBody FileNameInRequest newFilename) {
        authorizationService.checkToken(authToken);
        fileService.editFileName(filename, newFilename.getFilename());
    }
}