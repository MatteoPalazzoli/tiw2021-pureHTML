package it.pala.demo.Exceptions;

public class WrongUserException extends Exception {

    public WrongUserException(){ super(); }

    public WrongUserException(String cause) { super(cause); }

}
