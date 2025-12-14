package user.View;


import java.io.*;
import java.net.Socket;

public class SocketClient {

    private static SocketClient instance;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private SocketClient() {}

    public static SocketClient getInstance() {
        if (instance == null) {
            instance = new SocketClient();
        }
        return instance;
    }

    public void connect(String ip, int port) throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(this::listenServer).start();
        }
    }

    public void send(String msg) {
        if (out != null) {
            out.println(msg);
        }
    }

    private void listenServer() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Server: " + line);
            }
        } catch (IOException e) {
            System.err.println("❌ Mất kết nối server");
        }
    }

    // ❌ Ngắt kết nối
    public void disconnect() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }
}
