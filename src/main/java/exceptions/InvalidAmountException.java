package exceptions;
//
public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(double min) {
        super("Value must be at least " + min + ".");
    }
    public InvalidAmountException(String message) {
        super(message);
    }
}
