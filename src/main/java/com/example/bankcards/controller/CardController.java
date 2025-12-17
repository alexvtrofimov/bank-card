package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.PageParams;
import com.example.bankcards.dto.TransferBalanceDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.RequestBlockCard;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.BalanceException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.CardStatusService;
import com.example.bankcards.service.RequestBlockCardService;
import com.example.bankcards.service.UserService;
import com.example.bankcards.controller.validation.ValidCardStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardStatusService cardStatusService;

    @Autowired
    private UserService userService;

    @Autowired
    private Pattern serialLastDigitsPattern;

    @Autowired
    private RequestBlockCardService requestBlockCardService;

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity getAllCards(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "direction", required = false) String direction,
            @RequestParam(value = "field", required = false) String field
    ) {
        if (page != null || size != null) {
            PageParams pageParams = new PageParams(page, size, direction, field);
            Page<Card> pageCards = cardService.getAllCards(pageParams.getPageable());
            return ResponseEntity.ofNullable(pageCards);
        }
        return ResponseEntity.ofNullable(cardService.getAllCards());
    }

    @PostMapping("/add")
    @Secured("ROLE_ADMIN")
    public ResponseEntity addCard(
            @RequestParam(value = "username" , required = false) String username,
            @RequestParam(name = "user_id", required = false) Long id,
            @RequestBody @Validated CardDto cardDto
    ) {
        User user = null;
        if (username != null) {
            user = userService
                    .findUser(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
        }
        if (id != null) {
            user = userService
                    .findUser(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found!"));
        }
        if (user == null) {
            return ResponseEntity.badRequest().body("Parameter 'id' or 'username' not found");
        }
        Card card = cardService.createCard(user, cardDto);
        return ResponseEntity.ok(card);
    }

    @DeleteMapping("/delete")
    @Secured("ROLE_ADMIN")
    public ResponseEntity deleteCard(@RequestParam(name = "id") Long cardId) throws CardNotFoundException {
        Card card = cardService.getCard(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found", "request id: " + cardId));
        cardService.deleteCard(card);
        return ResponseEntity.ok("Card successfully deleted");
    }

    @PatchMapping("/status")
    @Secured("ROLE_ADMIN")
    public ResponseEntity updateStatusCard(
            @RequestParam(name = "id") Long cardId,
            @ValidCardStatus @RequestParam(name = "status") String newStatus
    ) throws CardNotFoundException {
        Card card = cardService.getCard(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found", "request id: " + cardId));
        CardStatus newCardStatus = cardStatusService.getCardStatusByName(newStatus).get();
        card.setStatus(newCardStatus);
        Card savedCard = cardService.saveCard(card);
        Optional<RequestBlockCard> optionalRequestBlockCard =
                requestBlockCardService.getCardRequestByCompleted(card, false);
        if (optionalRequestBlockCard.isPresent()) {
            User admin = userService.getCurrentUser();
            RequestBlockCard requestBlockCard = optionalRequestBlockCard.get();
            requestBlockCard.setAdmin(admin);
            requestBlockCard.setCompleted(true);
            requestBlockCardService.update(requestBlockCard);
        }
        return ResponseEntity.ok(savedCard);
    }

    @GetMapping("user")
    @Secured("ROLE_USER")
    public ResponseEntity getUserCards(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "serial", required = false) String lastSerial4Digits
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        User currentUser = userService.getCurrentUser();
        Page<Card> userCardsPage;
        if(lastSerial4Digits != null) {
            if (serialLastDigitsPattern.matcher(lastSerial4Digits).find()) {
                userCardsPage = cardService.getUserCards(currentUser, lastSerial4Digits, pageRequest);
            } else {
                throw new NumberFormatException("'" + lastSerial4Digits + "'" + " is not 4 digits");
            }
        } else {
            userCardsPage = cardService.getUserCards(currentUser, pageRequest);
        }
        return ResponseEntity.ok(userCardsPage);
    }

    @PatchMapping("transfer")
    @Secured("ROLE_USER")
    public ResponseEntity transferBalance(@RequestBody TransferBalanceDto transferBalanceDto)
            throws CardNotFoundException, BalanceException
    {
        User currentUser = userService.getCurrentUser();
        Card cardFrom = cardService.getUserCard(currentUser, transferBalanceDto.getFromCardId())
                .orElseThrow(() -> new CardNotFoundException("From card not found"));
        Card cardTo = cardService.getUserCard(currentUser, transferBalanceDto.getToCardId())
                .orElseThrow(() -> new CardNotFoundException("To card not found"));
        if (cardFrom.getBalance().compareTo(transferBalanceDto.getAmount()) < 0) {
            throw new BalanceException("Not enough balance on the From card", cardFrom.getBalance() + " < " + transferBalanceDto.getAmount());
        }
        cardService.transfer(cardFrom, cardTo, transferBalanceDto.getAmount());
        return ResponseEntity.ok("Transfer success");
    }

    @GetMapping("balance")
    @Secured("ROLE_USER")
    public ResponseEntity getCardBalance(
            @RequestParam(name = "id", required = false) Long cardId
    ) throws CardNotFoundException {
        User currentUser = userService.getCurrentUser();
        if (cardId != null) {
            Card card = cardService.getUserCard(currentUser, cardId)
                    .orElseThrow(() -> new CardNotFoundException("From card not found"));
            return ResponseEntity.ok(card.getBalance());
        }
        BigDecimal totalBalance = cardService.getUserCards(currentUser)
                .stream()
                .map(card -> card.getBalance())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return ResponseEntity.ok(totalBalance);
    }

    @PatchMapping("block-request")
    @Secured("ROLE_USER")
    public ResponseEntity sendRequestBlockCard(
            @RequestParam(name = "id") Long cardId
    ) throws CardNotFoundException {
        User currentUser = userService.getCurrentUser();
        Card card = cardService.getUserCard(currentUser, cardId)
                .orElseThrow(() -> new CardNotFoundException("From card not found"));

        if (requestBlockCardService.getCardRequestByCompleted(card, false).isPresent()) {
            return ResponseEntity.ok("Blocking request already exist!");
        }

        requestBlockCardService.createBlockRequest(card);

        return ResponseEntity.ok(String.format("The card (%s) blocking request has been sent successfully", card.getEncodedSerialNumber()));
    }

    @GetMapping("block-request")
    @Secured("ROLE_ADMIN")
    public ResponseEntity getRequestBlockCard(
            @RequestParam(name = "completed", required = false) Boolean completed
    ) {
        if (completed == null) {
            return ResponseEntity.ok(requestBlockCardService.getAllRequest());
        } else {
            return ResponseEntity.ok(requestBlockCardService.getAllRequestByCompleted(completed));
        }
    }
}
