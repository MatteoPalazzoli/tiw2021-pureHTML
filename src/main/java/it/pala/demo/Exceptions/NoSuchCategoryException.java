package it.pala.demo.Exceptions;

public class NoSuchCategoryException extends Exception{

    public NoSuchCategoryException() {
    }

    public NoSuchCategoryException(String message) {
        super(message);
    }
}
