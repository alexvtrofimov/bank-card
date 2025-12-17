package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.RequestBlockCard;
import com.example.bankcards.repository.RequestBlockCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestBlockCardService {

    @Autowired
    private RequestBlockCardRepository requestBlockCardRepository;

    public RequestBlockCard createBlockRequest(Card card) {
        return requestBlockCardRepository.save(new RequestBlockCard(card));
    }

    public List<RequestBlockCard> getAllRequest() {
        return requestBlockCardRepository.findAll();
    }

    public List<RequestBlockCard> getAllRequestByCompleted(boolean completed) {
        return requestBlockCardRepository.findAllByCompleted(completed);
    }

    public Optional<RequestBlockCard> getCardRequestByCompleted(Card card, boolean completed) {
        return requestBlockCardRepository.findFirstByCardAndCompleted(card, completed);
    }

    public RequestBlockCard update(RequestBlockCard requestBlockCard) {
        return requestBlockCardRepository.save(requestBlockCard);
    }
}
