package ru.practicum.shareit.exception;

public class DublicateEmailException extends RuntimeException {
    public DublicateEmailException(String mes) {
        super(mes);
    }
}
