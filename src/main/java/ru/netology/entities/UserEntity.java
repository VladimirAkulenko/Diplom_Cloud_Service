package ru.netology.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Id
    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;
}