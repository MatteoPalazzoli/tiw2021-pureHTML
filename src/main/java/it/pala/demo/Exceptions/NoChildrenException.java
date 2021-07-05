package it.pala.demo.Exceptions;

public class NoChildrenException extends Exception{

    public NoChildrenException() {
    }

    public NoChildrenException(String message) {
        super(message);
    }
}
