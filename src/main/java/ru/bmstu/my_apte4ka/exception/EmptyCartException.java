package ru.bmstu.my_apte4ka.exception;


public class EmptyCartException extends Exception{
    public EmptyCartException(String message) {
        super(message);
    }
}
