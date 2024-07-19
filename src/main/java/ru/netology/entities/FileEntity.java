package ru.netology.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    @Column(nullable = false, name = "file_name")
    private String fileName;

    private Long size;

    private String type;

    @Column(nullable = false, name = "file_content")
    private byte[] fileContent;

    public FileEntity(String fileName, long size, String type, byte[] fileContent) {
        this.fileName = fileName;
        this.size = size;
        this.type = type;
        this.fileContent = fileContent;
    }
}