package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.CardSerialNumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    @Autowired
    private DateTimeFormatter expiringDateFormatter;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardStatusService cardStatusService;

    public Optional<Card> getCard(Long id) {
        return cardRepository.findById(id);
    }

    public Optional<Card> getUserCard(User user, Long id) {
        return cardRepository.findByUserAndId(user, id);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Page<Card> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }

    public Page<Card> getUserCards(User user, Pageable pageable) {
        return cardRepository.findAllByUserOrderByExpirationDate(user, pageable);
    }

    public List<Card> getUserCards(User user) {
        return cardRepository.findAllByUser(user);
    }

    public Page<Card> getUserCards(User user, String last4digits, Pageable pageable) {
        return cardRepository.findAllByUserAndLast4Digits(user.getId(), last4digits, pageable);
    }

    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

    public void deleteCard(Card card) {
        cardRepository.delete(card);
    }

    public Card createCard(User user, CardDto cardDto) {
        CardStatus cardStatus = cardStatusService.getCardStatusByName(cardDto.getStatus()).get();
        YearMonth expirationMonthYear = YearMonth.parse(cardDto.getExpiration(), expiringDateFormatter);
        LocalDateTime expirationDate = LocalDateTime.of(
                expirationMonthYear.getYear(),
                expirationMonthYear.getMonth(),
                1,
                0,
                0,
                0
        );
        expirationDate = expirationDate.plusMonths(1);
        Card newCard = Card.builder()
                .user(user)
                .serialNumber(CardSerialNumberUtil.generate(4, 4))
                .status(cardStatus)
                .expirationDate(expirationDate)
                .balance(cardDto.getBalance())
                .build();

        return cardRepository.save(newCard);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transfer(Card cardFrom, Card cardTo, BigDecimal amount) {
        cardFrom.setBalance(cardFrom.getBalance().subtract(amount));
        cardTo.setBalance(cardTo.getBalance().add(amount));
        cardRepository.save(cardFrom);
        cardRepository.save(cardTo);
    }
}
