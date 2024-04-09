import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {

    static final String HOST = "localhost";
    static final int SERVER_PORT = 8080;
    public static void main(String[] args) throws IOException {
        Socket socket;
        try {
            socket = new Socket(HOST, SERVER_PORT);
        } catch (ConnectException e) {
            System.out.println("Server is not started.");
            return;
        }
        System.out.println("Connection established");

        Scanner scanner = new Scanner(System.in);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        Thread notificationThread = new Thread(() -> {
            try {
                while (true) {
                    Object response = in.readObject();
                    if (response instanceof Notification) {
                        System.out.println("\nServer response: " + ((Notification) response).getMessage() + ", time: " + ((Notification) response).getSendTime());
                    }
                    System.out.print("Enter notification message: ");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        });
        notificationThread.start();

        while (true) {
            System.out.print("Enter notification message: ");
            String message = scanner.nextLine();

            System.out.print("Enter time to send notification (in milliseconds): ");
            long time;
            try {
                time = Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("The given value is not a number!");
                continue;
            }
            if (time < 0) {
                System.out.println("The given number must not be negative!");
                continue;
            }

            Notification notification = new Notification(message, time);
            out.writeObject(notification);

            System.out.println("Notification sent successfully.");
        }
    }
}
