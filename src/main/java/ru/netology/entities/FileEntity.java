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

    @Id
    @Column(nullable = false, name = "file_name")
    private String fileName;

    private Long size;

    private String type;

    @Column(nullable = false, name = "file_content")
    private byte[] fileContent;

}