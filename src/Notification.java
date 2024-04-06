import java.io.Serializable;

public class Notification implements Serializable {
    private String message;
    private long sendTime;

    public Notification(String message, long sendTime) {
        this.message = message;
        this.sendTime = sendTime;
    }

    public String getMessage() {
        return message;
    }

    public long getSendTime() {
        return sendTime;
    }
}
