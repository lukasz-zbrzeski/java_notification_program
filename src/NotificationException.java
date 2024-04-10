public class NotificationException extends Exception {
    private String message;

    public NotificationException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
