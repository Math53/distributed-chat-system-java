import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer {

    static Vector<ClientHandler> clients = new Vector<>();

    public static void main(String[] args) {

        int port = 5050;

        try {

            ServerSocket serverSocket =
                    new ServerSocket(port);

            System.out.println(
                    "Chat Server started on port " + port);

            while (true) {

                Socket socket =
                        serverSocket.accept();

                System.out.println(
                        "New client connected: "
                                + socket);

                ClientHandler clientHandler =
                        new ClientHandler(socket);

                clients.add(clientHandler);

                Thread thread =
                        new Thread(clientHandler);

                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message,
                                 ClientHandler sender) {

        for (ClientHandler client : clients) {

            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public static void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}