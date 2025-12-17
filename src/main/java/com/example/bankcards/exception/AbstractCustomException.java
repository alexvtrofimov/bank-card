package com.example.bankcards.exception;

import lombok.Getter;

@Getter
public abstract class AbstractCustomException extends Exception {
    private String debugMessage;

    public AbstractCustomException(String message, String debugMessage) {
        super(message);
        this.debugMessage = debugMessage;
    }

    public AbstractCustomException(String message) {
        super(message);
    }
}
