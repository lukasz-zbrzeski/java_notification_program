import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketServer {

    private static ArrayList<Notification> notificationQueue = new ArrayList<>();
    private static ArrayList<ObjectOutputStream> clientOutputStreams = new ArrayList<>();

    static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        Thread serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
                System.out.println("Server started. Waiting for clients...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket);

                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                    clientOutputStreams.add(out);

                    new Thread(() -> {
                        try {
                            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                            while (true) {
                                Notification notification = (Notification) in.readObject();
                                System.out.println("Notification received: " + notification.getMessage() + ", time: " + notification.getSendTime());

                                Thread.sleep(notification.getSendTime());

                                synchronized (notificationQueue) {
                                    notificationQueue.add(notification);
                                    for (ObjectOutputStream clientOut : clientOutputStreams) {
                                        clientOut.writeObject(notification);
                                        clientOut.flush();
                                    }
                                }
                            }
                        } catch (IOException | ClassNotFoundException | InterruptedException e) {
                            System.out.println("Client disconnected.");
                        }
                    }).start();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
        serverThread.start();
    }
}