package ru.netology.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.model.FileResponse;
import ru.netology.service.AuthorizationService;
import ru.netology.service.FileService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
public class ListController {
    private final FileService fileService;
    private final AuthorizationService authorizationService;

    @GetMapping("/list")
    public List<FileResponse> getFileList(@RequestHeader("auth-token")
                                          @NotBlank String authToken, @Min(1) int limit) {
        authorizationService.checkToken(authToken);
        return fileService.getAllFiles(limit);
    }
}
