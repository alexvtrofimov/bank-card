package com.example.bankcards.repository;

import com.example.bankcards.entity.CardStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardStatusRepository extends CrudRepository<CardStatus, Long> {
    Optional<CardStatus> findFirstByStatus(String status);
}
