package user.View;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class HomeUser extends JFrame {


    private static final Color GRADIENT_START = new Color(135, 88, 255);
    // Màu kết thúc (Xanh đậm hơn)
    private static final Color GRADIENT_END = new Color(66, 95, 235);
    private static final Color BG_COLOR = new Color(255, 251, 236);


    private JLabel lblOnlineTime, lblRemainingTime;
    private JTabbedPane tabs;

    public HomeUser() {
        connect();
        setTitle("Home User");
        setSize(1200, 700); // Tăng chiều cao một chút cho thoáng
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR); // Đặt màu nền chính

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Giả sử bạn có class Login, nếu chưa có hãy comment lại dòng này
                 new Login().setVisible(true);
                dispose();
            }
        });
        
        // Tùy chỉnh một chút cho JTabbedPane trông hiện đại hơn (nếu dùng thư viện ngoài như FlatLaf sẽ đẹp hơn)
        UIManager.put("TabbedPane.selected", Color.WHITE);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0,0,0,0));
        
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.addTab("Trang chủ", createHomeTab());
        
        // Tab 1: Dịch vụ (Chỉ cần add một JPanel rỗng vì ta sẽ chuyển trang ngay khi click)


        // THÊM ĐOẠN NÀY: Bắt sự kiện chuyển tab
        tabs.addChangeListener(e -> {
            // Kiểm tra nếu tab được chọn là tab "Dịch vụ" (index = 1)
            if (tabs.getSelectedIndex() == 1) {
                // 1. Mở frame ProductListUI
                // (Giả sử class ProductListUI có hàm khởi tạo không tham số hoặc tương tự)
                try {
                    new ProductListUI().setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy class ProductListUI!");
                    ex.printStackTrace();
                }      
                // 2. Đóng frame HomeUser hiện tại
                this.dispose(); 
            }
        });

        add(tabs);

        startOnlineTimer();
    }

    // =================== TAB TRANG CHỦ ===================
    private JPanel createHomeTab() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        // ========== TOP TIME BOX (SỬ DỤNG GRADIENT PANEL) ==========
        // Sử dụng lớp ẩn danh để ghi đè phương thức paintComponent
        JPanel timeBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Tạo gradient từ trên xuống dưới
                GradientPaint gp = new GradientPaint(0, 0, GRADIENT_START, 0, getHeight(), GRADIENT_END);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        timeBox.setLayout(new BoxLayout(timeBox, BoxLayout.Y_AXIS));
        // Tăng padding để trông thoáng hơn
        timeBox.setBorder(BorderFactory.createEmptyBorder(25, 0, 30, 0));
        // Không cần setBackground vì đã tự vẽ

        lblOnlineTime = new JLabel("Thời gian online: 00:00:00");
        lblOnlineTime.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblOnlineTime.setForeground(Color.WHITE);
        lblOnlineTime.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblRemainingTime = new JLabel("Thời gian còn lại: 60 phút");
        lblRemainingTime.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblRemainingTime.setForeground(new Color(230, 230, 250)); // Màu trắng hơi tím nhạt
        lblRemainingTime.setAlignmentX(Component.CENTER_ALIGNMENT);

        timeBox.add(lblOnlineTime);
        timeBox.add(Box.createVerticalStrut(8));
        timeBox.add(lblRemainingTime);

        panel.add(timeBox, BorderLayout.NORTH);

        // ========== GRID DỊCH VỤ ==========
        // Sử dụng 5 cột như cũ
        JPanel grid = new JPanel(new GridLayout(0, 5, 20, 20));
        grid.setBackground(BG_COLOR); // Nền đồng bộ
        grid.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        for (int i = 1; i <= 18; i++) {
            grid.add(createGridServiceCard("Dịch vụ " + i));
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null); // Loại bỏ viền mặc định của ScrollPane
        scroll.getVerticalScrollBar().setUnitIncrement(16); // Cuộn mượt hơn

        panel.add(scroll, BorderLayout.CENTER);

        // ------------------- NÚT CHUYỂN TỚI DỊCH VỤ (SỬ DỤNG GRADIENT BUTTON) -------------------
        // Sử dụng class GradientButton tùy chỉnh ở bên dưới
        GradientButton btnGo = new GradientButton("Đi tới trang dịch vụ", 30);
        btnGo.setPreferredSize(new Dimension(400, 45)); // Nút to hơn một chút
        btnGo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        btnGo.addActionListener(e -> tabs.setSelectedIndex(1));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        buttonPanel.add(btnGo);
        // ========== BOTTOM CHAT ==========
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(BG_COLOR);
        
        // Tạo TitledBorder với màu sắc đồng bộ
        TitledBorder chatBorder = BorderFactory.createTitledBorder(
                new LineBorder(GRADIENT_END, 1, true), // Viền màu xanh đậm, bo tròn
                "Chat với máy chủ"
        );
        chatBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        chatBorder.setTitleColor(GRADIENT_END);
        chatPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 20, 20, 20), // Padding ngoài
                chatBorder // Viền trong
        ));

        JTextArea chatArea = new JTextArea(6, 20); // Tăng chiều cao chat
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); // Padding cho text area bên trong

        JTextField chatInput = new JTextField();
        chatInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatInput.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Nút Gửi cũng dùng Gradient
        GradientButton btnSend = new GradientButton("Gửi", 10);
        btnSend.setPreferredSize(new Dimension(80, 35));

        btnSend.addActionListener(e -> {
            if (!chatInput.getText().trim().isEmpty()) {
                chatArea.append("Bạn: " + chatInput.getText() + "\n");
                SocketClient.getInstance().send(chatInput.getText());
                chatArea.append("Server: Đã nhận (Auto-reply)!\n");
                chatInput.setText("");
                chatArea.setCaretPosition(chatArea.getDocument().getLength()); // Tự động cuộn xuống cuối
            }
        });

        JPanel bottomRow = new JPanel(new BorderLayout(10, 0)); // Gap giữa input và nút gửi
        bottomRow.setBackground(BG_COLOR);
        bottomRow.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        bottomRow.add(chatInput, BorderLayout.CENTER);
        bottomRow.add(btnSend, BorderLayout.EAST);

        chatPanel.add(chatScroll, BorderLayout.CENTER);
        chatPanel.add(bottomRow, BorderLayout.SOUTH);

        // ================= GOM HAI CÁI LẠI =================
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.setBackground(BG_COLOR);

        southPanel.add(buttonPanel, BorderLayout.NORTH);
        southPanel.add(chatPanel, BorderLayout.CENTER);

        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ===== CARD DỊCH VỤ CÓ ẢNH (ĐÃ CẬP NHẬT MÀU) =====
    private JPanel createGridServiceCard(String name) {

        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        // Tăng chiều cao một chút cho cân đối
        card.setPreferredSize(new Dimension(200, 250));
        card.setBackground(Color.WHITE);
        // Viền nhẹ nhàng hơn và có bóng đổ giả (bằng border phức hợp)
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 240), 1),
                BorderFactory.createMatteBorder(0,0,3,0, new Color(220,220,230)) // Giả bóng đổ ở đáy
        ));

        // Ảnh dịch vụ (Sử dụng ảnh placeholder nếu không có file thật)
        ImageIcon icon;
        try {
             icon = new ImageIcon("service.png"); // Đường dẫn ảnh thật của bạn
             if (icon.getIconWidth() == -1) throw new Exception("Image not found");
        } catch (Exception e) {
            // Tạo ảnh placeholder màu xám nếu không tìm thấy file
            java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(180, 130, java.awt.image.BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bi.createGraphics();
            g2.setPaint(new Color(240,240,245));
            g2.fillRect(0,0,180,130);
            g2.setColor(Color.GRAY);
            g2.drawString("Image not found", 40, 65);
            g2.dispose();
            icon = new ImageIcon(bi);
        }

        Image img = icon.getImage().getScaledInstance(180, 130, Image.SCALE_SMOOTH);
        JLabel lblImage = new JLabel(new ImageIcon(img));
        lblImage.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        // Header gradient (Sử dụng lại biến màu đã định nghĩa)
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sử dụng màu hằng số
                GradientPaint gp = new GradientPaint(
                        0, 0, GRADIENT_START,
                        getWidth(), getHeight(), GRADIENT_END
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(200, 45));

        JLabel lblTitle = new JLabel(name, SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setLayout(new BorderLayout());
        header.add(lblTitle);

        // ===== LABEL GIÁ (Đồng bộ màu text) =====
        JLabel lblPrice = new JLabel("Giá: 20.000đ", SwingConstants.CENTER);
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
        // Sử dụng màu xanh đậm của gradient cho giá tiền
        lblPrice.setForeground(GRADIENT_END);
        lblPrice.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        card.add(header, BorderLayout.NORTH);
        card.add(lblImage, BorderLayout.CENTER);
        card.add(lblPrice, BorderLayout.SOUTH);

        return card;
    }


    // ========== TIMER ==========
    private void startOnlineTimer() {
        long start = System.currentTimeMillis();
        Timer timer = new Timer(1000, e -> {
            long elapsed = (System.currentTimeMillis() - start) / 1000;
            long h = elapsed / 3600;
            long m = (elapsed % 3600) / 60;
            long s = elapsed % 60;
            lblOnlineTime.setText(String.format("Thời gian online: %02d:%02d:%02d", h, m, s));
        });
        timer.start();
    }

    // ===========================================================================
    // LỚP TÙY CHỈNH CHO NÚT BẤM CÓ MÀU GRADIENT
    // ===========================================================================
    private class GradientButton extends JButton {
        private Color startColor = GRADIENT_START;
        private Color endColor = GRADIENT_END;
        private int borderRadius = 20;

        public GradientButton(String text, int radius) {
            super(text);
            this.borderRadius = radius;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Hiệu ứng khi di chuột (Hover effect - sáng hơn một chút)
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    startColor = GRADIENT_START.brighter();
                    endColor = GRADIENT_END.brighter();
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    startColor = GRADIENT_START;
                    endColor = GRADIENT_END;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Tạo màu gradient ngang
            GradientPaint gp = new GradientPaint(0, 0, startColor, getWidth(), 0, endColor);
            g2.setPaint(gp);

            // Vẽ hình chữ nhật bo tròn làm nền
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), borderRadius, borderRadius));

            g2.dispose();

            // Vẽ text lên trên nền gradient
            super.paintComponent(g);
        }
    }
    public void connect(){
        try {
            SocketClient.getInstance().connect("127.0.0.1", 8080);
        } catch (IOException e) {
          
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new HomeUser().setVisible(true);
        });
    }
}