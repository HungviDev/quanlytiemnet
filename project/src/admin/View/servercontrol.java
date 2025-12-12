package admin.View; 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JFrame; // Cần thiết
import javax.swing.SwingUtilities; // Cần thiết

public class servercontrol {
    
    private static final int PORT = 8080;
    private static final Map<String, PrintWriter> clientOutputs = new ConcurrentHashMap<>();
    
    // ⭐️ KHÔI PHỤC: startServer phải nhận JFrame owner
    public static void startServer(JFrame ownerFrame) { 
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(" Server Control đang lắng nghe tại cổng " + PORT + "...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client mới kết nối từ: " + clientSocket.getInetAddress().getHostName());
                
                // ⭐️ TRUYỀN OWNER FRAME VÀO HANDLER
                new ClientHandler(clientSocket, ownerFrame).start();
            }
        } catch (IOException e) {
            System.err.println("LỖI KHỞI TẠO SERVER: " + e.getMessage());
        }
    }

    public static boolean sendCommandToClient(String clientId, String command) {
        PrintWriter out = clientOutputs.get(clientId);
        if (out != null) {
            out.println(command);
            System.out.println("ĐÃ GỬI lệnh [" + command + "] tới Client " + clientId);
            return true;
        }
        System.err.println("LỖI: Client " + clientId + " không hoạt động hoặc không tìm thấy.");
        return false;
    }
    

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private String clientId = "UNKNOWN";
        private JFrame ownerFrame; 

        public ClientHandler(Socket socket, JFrame owner) {
            this.clientSocket = socket;
            this.ownerFrame = owner;
        }

        public void run() { 
            try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ) {
                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    
                    if (inputLine.startsWith("ID:")) {
                        clientId = clientSocket.getInetAddress().getHostAddress();
                        servercontrol.clientOutputs.put(clientId, out); 
                        System.out.println("-> Client " + clientId + " đã đăng ký và kết nối thành công.");
                        continue; 
                    }
                    
                    // ⭐️ LOGIC ĐÃ SỬA: Hiển thị JDialog
                    System.out.println("Nhận dữ liệu từ " + clientSocket.getInetAddress().getHostAddress() + ": " + inputLine);
                    
                    final String sourceId = clientId;
                    final String contentMessage = inputLine;
                    final JFrame frameOwner = this.ownerFrame; 
                    SwingUtilities.invokeLater(() -> {
                        NotificationForm dialog = new NotificationForm(
                            frameOwner, 
                            sourceId,   
                            contentMessage
                        );
                        dialog.setVisible(true);
                    });
                }
            } catch (IOException e) {
                System.err.println("Mất kết nối với Client " + clientId + ".");
            } finally {
                servercontrol.clientOutputs.remove(clientId);
                try { 
                    if (clientSocket != null && !clientSocket.isClosed()) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                }
                System.out.println("Đã đóng và XÓA kết nối với Client: " + clientId);
            }
        }
    }
}