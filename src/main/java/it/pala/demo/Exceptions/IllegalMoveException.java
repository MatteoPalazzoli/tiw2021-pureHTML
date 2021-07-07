package it.pala.demo.Exceptions;

public class IllegalMoveException extends Exception {
    public IllegalMoveException() {
    }

    public IllegalMoveException(String message) {
        super(message);
    }

}
