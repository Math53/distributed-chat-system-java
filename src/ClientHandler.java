import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;

    private BufferedReader reader;

    private PrintWriter writer;

    private String username;

    public ClientHandler(Socket socket) {

        this.socket = socket;

        try {

            reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));

            writer =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true);

            writer.println("Enter username:");

            username = reader.readLine();

            ChatServer.broadcast(
                    username + " joined the chat.",
                    this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        String message;

        try {

            while ((message = reader.readLine()) != null) {

                String fullMessage =
                        "[" + username + "]: "
                                + message;

                System.out.println(fullMessage);

                ChatServer.broadcast(
                        fullMessage,
                        this);
            }

        } catch (IOException e) {

            System.out.println(
                    username + " disconnected.");

        } finally {

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ChatServer.removeClient(this);

            ChatServer.broadcast(
                    username + " left the chat.",
                    this);
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}