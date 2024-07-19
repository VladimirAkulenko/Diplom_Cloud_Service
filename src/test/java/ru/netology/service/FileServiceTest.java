package ru.netology.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entities.FileEntity;
import ru.netology.model.FileResponse;
import ru.netology.repository.FileRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class FileServiceTest {
    public static final String EXISTING_FILE = "existingFile";
    public static final String NOT_EXISTING_FILE = "notExistingFile";

    private final FileEntity existingFileEntity = new FileEntity(EXISTING_FILE, 64, "pdf/plain", new byte[]{6, 4, 6});

    private final FileRepository fileRepository = createFileRepositoryMock();
    private final FileService fileService = new FileService(fileRepository);

    private FileRepository createFileRepositoryMock() {
        final FileRepository fileRepository = Mockito.mock(FileRepository.class);

        when(fileRepository.findById(EXISTING_FILE)).thenReturn(Optional.of(existingFileEntity));
        when(fileRepository.findById(NOT_EXISTING_FILE)).thenReturn(Optional.empty());

        when(fileRepository.existsById(EXISTING_FILE)).thenReturn(true);
        when(fileRepository.existsById(NOT_EXISTING_FILE)).thenReturn(false);

        when(fileRepository.getFiles(1)).thenReturn(List.of(existingFileEntity));

        return fileRepository;
    }

    @Test
    void getFile() {
        final byte[] expectedFile = new byte[]{0, 1, 2};
        final byte[] file = fileService.downloadFile(EXISTING_FILE);
        Assertions.assertArrayEquals(expectedFile, file);
    }

    @Test
    void getFile_failed() {
        Assertions.assertThrows(RuntimeException.class, () -> fileService.downloadFile(NOT_EXISTING_FILE));
    }

    @Test
    void deleteFile() {
        Assertions.assertDoesNotThrow(() -> fileService.deleteFile(EXISTING_FILE));
    }

    @Test
    void deleteFile_failed() {
        Assertions.assertThrows(RuntimeException.class, () -> fileService.deleteFile(NOT_EXISTING_FILE));
    }

    @Test
    void editFileName() {
        Assertions.assertDoesNotThrow(() -> fileService.editFileName(EXISTING_FILE, NOT_EXISTING_FILE));
    }

    @Test
    void editFileName_failed() {
        Assertions.assertThrows(RuntimeException.class, () -> fileService.editFileName(NOT_EXISTING_FILE, EXISTING_FILE));
    }

    @Test
    void getFileList() {
        final List<FileResponse> expectedFileList = List.of(new FileResponse(EXISTING_FILE, 3));
        final List<FileResponse> fileList = fileService.getAllFiles(1);
        Assertions.assertEquals(expectedFileList, fileList);
    }
}
