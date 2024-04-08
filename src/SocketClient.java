import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {

    static final String HOST = "localhost";
    static final int SERVER_PORT = 8080;
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(HOST, SERVER_PORT);
        System.out.println("Connection established");

        Scanner scanner = new Scanner(System.in);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        Thread notificationThread = new Thread(() -> {
            try {
                while (true) {
                    Object response = in.readObject();
                    if (response instanceof Notification) {
                        System.out.println("Server response: " + ((Notification) response).getMessage());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        notificationThread.start();

        while (true) {
            System.out.print("Enter notification message: ");
            String message = scanner.nextLine();

            System.out.print("Enter time to send notification (in milliseconds): ");
            long time = Long.parseLong(scanner.nextLine());

            Notification notification = new Notification(message, time);
            out.writeObject(notification);

            System.out.println("Notification sent successfully.");
        }
    }
}
