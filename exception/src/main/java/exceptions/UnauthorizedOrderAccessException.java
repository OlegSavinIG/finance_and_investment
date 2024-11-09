package exceptions;

public class UnauthorizedOrderAccessException extends RuntimeException {
    public UnauthorizedOrderAccessException(Long userId, String orderId) {
        super("User with ID " + userId + " is not the owner of order ID " + orderId);
    }
}
