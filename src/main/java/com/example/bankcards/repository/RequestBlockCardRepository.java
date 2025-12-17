package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.RequestBlockCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestBlockCardRepository extends JpaRepository<RequestBlockCard, Long> {
    List<RequestBlockCard> findAllByCompleted(boolean completed);

    Optional<RequestBlockCard> findFirstByCardAndCompleted(Card card, boolean completed);
}
