import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 8080);
        System.out.println("Connection established");

        Scanner scanner = new Scanner(System.in);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        while (true) {
            System.out.print("Enter notification message: ");
            String message = scanner.nextLine();

            System.out.print("Enter time to send notification (in milliseconds): ");
            long time = Long.parseLong(scanner.nextLine());

            Notification notification = new Notification(message, time);
            out.writeObject(notification);

            System.out.println("Notification sent successfully.");

            Object response = in.readObject();
            if (response instanceof String) {
                System.out.println("Server response: " + response);
            }
        }


    }
}
