package com.example.bankcards.service;

import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.repository.CardStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class CardStatusService {
    @Autowired
    private CardStatusRepository cardStatusRepository;

    @Cacheable(sync = true, value = "cardStatusCache")
    public List<String> getAllCardStatuses() {
        return StreamSupport
                .stream(cardStatusRepository.findAll().spliterator(), false)
                .map(cardStatus -> cardStatus.getStatus())
                .toList();
    }

    public Optional<CardStatus> getCardStatusByName(String status) {
        return cardStatusRepository.findFirstByStatus(status);
    }

    @CacheEvict(value = "cardStatusCache")
    public CardStatus createCardStatus(CardStatus cardStatus) {
        return cardStatusRepository.save(cardStatus);
    }
}
