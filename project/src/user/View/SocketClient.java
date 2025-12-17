package user.View;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.*;
import java.net.Socket;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SocketClient {

    private static SocketClient instance;
    private JDialog lockDialog; 
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private SocketClient() {
    }

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

    // ================== NGHE SERVER ==================
    private void listenServer() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                System.out.println("Server: " + msg);
                handleCommand(msg.trim().toUpperCase());
            }
        } catch (IOException e) {
            System.err.println("❌ Mất kết nối server");
        }
    }

    // ================== XỬ LÝ LỆNH ==================
    private void handleCommand(String msg) {
        switch (msg) {
            case "LOCK":
                lockMachine();
                break;

            case "UNLOCK":
                unlockMachine();
                break;

            default:
                System.out.println("⚠️ Lệnh không xác định: " + msg);
        }
    }

    private void lockMachine() {
    if (lockDialog != null && lockDialog.isShowing()) return;

    SwingUtilities.invokeLater(() -> {
        // 1. Tạo nội dung thông báo
        JOptionPane optionPane = new JOptionPane(
                "🚫 Máy đã bị khóa bởi quản trị viên!",
                JOptionPane.ERROR_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                new Object[]{}, // Xóa nút bấm
                null
        );

        lockDialog = new JDialog((Frame) null, "LOCK", false); // false = non-modal
        lockDialog.setUndecorated(true); 
        
        lockDialog.setContentPane(optionPane);
        
        lockDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        lockDialog.setAlwaysOnTop(true);
        
        // Full screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        lockDialog.setSize(screenSize);
        lockDialog.setLocation(0, 0);

        // 5. Cuối cùng mới hiển thị
        lockDialog.setVisible(true);
    });
}

    private void unlockMachine() {
    SwingUtilities.invokeLater(() -> {
        // 1. Kiểm tra xem lockDialog có đang tồn tại không
        if (lockDialog != null) {
            // 🔥 Tắt màn hình khóa
            lockDialog.setVisible(false); // Ẩn trước cho nhanh
            lockDialog.dispose();         // Hủy hoàn toàn cửa sổ
            lockDialog = null;            
            System.out.println("Hệ thống: Đã đóng màn hình khóa.");
        }

        // 2. Sau đó mới hiện thông báo thành công
        JOptionPane.showMessageDialog(
                null,
                "✅ Máy đã được mở khóa bởi quản trị viên!",
                "THÔNG BÁO",
                JOptionPane.INFORMATION_MESSAGE);
    });
}

    public void disconnect() {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException ignored) {
        }
    }
}
