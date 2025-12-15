package interfaces;

public interface Transactable {
    boolean processTransaction(double amount, String type);
}
