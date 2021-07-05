package it.pala.demo.Exceptions;

public class DuplicateCategoryException extends Exception{

    public DuplicateCategoryException() { super(); }

    public DuplicateCategoryException(String message){ super(message); }
}
