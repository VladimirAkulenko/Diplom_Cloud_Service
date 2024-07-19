package ru.netology.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.entities.FileEntity;
import ru.netology.exceptions.DeleteFileException;
import ru.netology.exceptions.FileUploadException;
import ru.netology.exceptions.RenameFileException;
import ru.netology.model.FileResponse;
import ru.netology.repository.FileRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public synchronized void uploadFile(MultipartFile file) {
        try {
            fileRepository.save(new FileEntity(file.getOriginalFilename(), file.getSize(), file.getContentType(), file.getBytes()));
        } catch (IOException e) {
            log.error("Error upload file: " + file.getOriginalFilename() + ". Please try again!");
            throw new FileUploadException("Error upload file!");
        }
    }

    public synchronized void deleteFile(String filename) {
        if (!fileRepository.existsById(filename)) {
            log.error("Error delete file!");
            throw new DeleteFileException("File " + filename + " not found");
        }
        fileRepository.deleteById(filename);
    }

    public byte[] downloadFile(String filename) {
        final FileEntity file = getFileByName(filename);
        return file.getFileContent();
    }

    public synchronized void editFileName(String fileName, String newFilename) {
        final FileEntity fileEntity = getFileByName(fileName);
        if (isNull(fileEntity)) {
            log.error("Error rename file: " + fileName + ". Please try again!");
            throw new RenameFileException("File is not exist!");
        }
        final FileEntity newFileEntity = new FileEntity(newFilename, fileEntity.getSize(), fileEntity.getType(), fileEntity.getFileContent());
        fileRepository.delete(fileEntity);
        fileRepository.save(newFileEntity);
        log.info("Successful rename file: " + fileName);
    }


    public List<FileResponse> getAllFiles(int limit) {
        final List<FileEntity> files = fileRepository.getFiles(limit);
        return files.stream()
                .map(file -> new FileResponse(file.getFileName(), file.getFileContent().length))
                .collect(Collectors.toList());
    }

    private FileEntity getFileByName(String filename) {
        return fileRepository.findById(filename).orElseThrow(() ->
                new RuntimeException("File " + filename + " not found"));
    }
}