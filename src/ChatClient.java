import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ChatClient {

    private JFrame frame;

    private JTextArea chatArea;

    private JTextField inputField;

    private PrintWriter writer;

    public ChatClient(String serverAddress,
                      int serverPort) {

        try {

            Socket socket =
                    new Socket(serverAddress,
                            serverPort);

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));

            writer =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true);

            String username =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter username:");

            writer.println(username);

            frame = new JFrame(
                    "Distributed Chat System");

            frame.setSize(600, 500);

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE);

            frame.setLayout(new BorderLayout());

            chatArea = new JTextArea();

            chatArea.setEditable(false);

            chatArea.setFont(
                    new Font("Monospaced",
                            Font.PLAIN,
                            15));

            JScrollPane scrollPane =
                    new JScrollPane(chatArea);

            inputField = new JTextField();

            JButton sendButton =
                    new JButton("Send");

            sendButton.addActionListener(e -> {

                String message =
                        inputField.getText();

                writer.println(message);

                inputField.setText("");
            });

            JPanel bottomPanel =
                    new JPanel(
                            new BorderLayout());

            bottomPanel.add(
                    inputField,
                    BorderLayout.CENTER);

            bottomPanel.add(
                    sendButton,
                    BorderLayout.EAST);

            frame.add(
                    scrollPane,
                    BorderLayout.CENTER);

            frame.add(
                    bottomPanel,
                    BorderLayout.SOUTH);

            frame.setVisible(true);

            Thread receiveThread =
                    new Thread(() -> {

                        try {

                            String message;

                            while ((message =
                                    reader.readLine())
                                    != null) {

                                chatArea.append(
                                        message + "\n");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            receiveThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new ChatClient(
                        "localhost",
                        5050));
    }
}