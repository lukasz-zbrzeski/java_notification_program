import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketServer {

    private static ArrayList<Notification> notificationQueue = new ArrayList<>();

    static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        Thread serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
                System.out.println("Server started. Waiting for clients...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket);

                    new Thread(() -> {
                        try {
                            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

                            while (true) {
                                Notification notification = (Notification) in.readObject();
                                System.out.println("Notification received: " + notification.getMessage());

                                Thread.sleep(1000);

                                notificationQueue.add(notification);
                                out.writeObject(notification.getMessage());
                            }
                        } catch (IOException | ClassNotFoundException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
    }
}