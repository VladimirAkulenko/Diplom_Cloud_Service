package ru.netology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.entities.TokenEntity;

@Repository

public interface TokenRepository extends JpaRepository<TokenEntity, String> {
}