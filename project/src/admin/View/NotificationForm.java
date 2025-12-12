package admin.View;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame; // Cần thiết
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class NotificationForm extends JDialog {

    private final Color HEADER_COLOR = new Color(52, 152, 219); 
    private final Color TEXT_COLOR = new Color(44, 62, 80); 
    private final Color BG_COLOR = new Color(240, 243, 245); 

    /**
     * Constructor tạo dialog thông báo tùy chỉnh
     * @param owner Khung cha (JFrame)
     * @param computerName Tên máy tính gửi thông báo
     * @param message Nội dung thông báo
     */
    public NotificationForm(JFrame owner, String computerName, String message) {
        super(owner, "Thông báo mới", true); 
        
        // Cấu hình chung cho Dialog
        setLayout(new BorderLayout(10, 10));
        setBackground(BG_COLOR);
        setSize(600, 350); 
        setLocationRelativeTo(owner); 
        setResizable(false);
        
        // 1. PHẦN HEADER (Tiêu đề và nguồn)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 10, 15));

        JLabel titleLabel = new JLabel("🔔 THÔNG BÁO MỚI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JLabel sourceLabel = new JLabel("Từ máy: " + computerName);
        sourceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sourceLabel.setForeground(new Color(220, 220, 220));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(sourceLabel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);

        // 2. PHẦN NỘI DUNG
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        contentPanel.setBackground(BG_COLOR);

        JTextArea messageArea = new JTextArea(message);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageArea.setForeground(TEXT_COLOR);
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setBackground(BG_COLOR);
        
        contentPanel.add(messageArea, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // 3. PHẦN FOOTER (Nút Đóng/Xác nhận)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        footerPanel.setBackground(BG_COLOR);

        JButton closeButton = new JButton("Đã rõ (Đóng)");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeButton.setBackground(HEADER_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        
        closeButton.addActionListener(e -> {
            dispose(); // Đóng dialog
        });

        footerPanel.add(closeButton);
        add(footerPanel, BorderLayout.SOUTH);
    }
}