package Exceptions;

public class InvalidAction extends RuntimeException {
    public InvalidAction(String message) {
        super(message);
    }
}
