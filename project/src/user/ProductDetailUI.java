package user;


import java.awt.*;
import javax.swing.*;

public class ProductDetailUI extends JFrame {

    private JLabel lblImage, lblName, lblPrice, lblQuantity;
    private JTextArea txtDescription;

    private JButton btnBuy, btnBack;

      private static final Color GRADIENT_START = new Color(135, 88, 255);
    // Màu kết thúc (Xanh đậm hơn)
    private static final Color GRADIENT_END = new Color(66, 95, 235);

    public ProductDetailUI() {
        setTitle("Chi tiết sản phẩm");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Giả sử bạn có class Login, nếu chưa có hãy comment lại dòng này
                 new ProductListUI().setVisible(true);
                dispose();
            }
        });
        setLayout(new BorderLayout());

        Color bgColor   = new Color(245, 245, 245);
        Color cardColor = new Color(255, 251, 236);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel, BorderLayout.CENTER);

        // LEFT IMAGE
        JPanel leftPanel = new GradientPanel(GRADIENT_START, GRADIENT_END);
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(400, 500));

        leftPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(220, 220, 220)),
        BorderFactory.createEmptyBorder(30, 30, 30, 30)
));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(300, 320));
        lblImage.setOpaque(true);
        lblImage.setBackground(Color.WHITE);
        lblImage.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        leftPanel.add(lblImage);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // RIGHT PANEL
        JPanel rightPanel = new JPanel(new BorderLayout(15, 15));
        rightPanel.setBackground(cardColor);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(25, 45, 25, 40)
        ));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(cardColor);

        lblName = new JLabel("Tên sản phẩm");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 26));

        lblPrice = new JLabel("Đơn giá: 0 VNĐ");
        lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        lblQuantity = new JLabel("Số lượng: 0");
        lblQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        txtDescription = new JTextArea();
        txtDescription.setEditable(false);
        txtDescription.setBackground(cardColor);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtDescription.setBorder(BorderFactory.createTitledBorder("Mô tả sản phẩm"));

        infoPanel.add(lblName);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(lblPrice);
infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(lblQuantity);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(new JScrollPane(txtDescription));

        rightPanel.add(infoPanel, BorderLayout.CENTER);

        // BOTTOM BUTTONS
        JPanel btnPanel = new JPanel(new BorderLayout());
        btnPanel.setBackground(cardColor);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        btnBack = new GradientButton("TRỞ VỀ");
btnBack.setPreferredSize(new Dimension(140, 45));
        btnPanel.add(btnBack, BorderLayout.WEST);

        btnBack.addActionListener(e -> {
            new ProductListUI().setVisible(true);
            dispose();
        });

        
        btnBuy = new GradientButton("THANH TOÁN");
        btnBuy.setPreferredSize(new Dimension(140, 45));



        btnPanel.add(btnBuy, BorderLayout.EAST);

        rightPanel.add(btnPanel, BorderLayout.SOUTH);



        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // RULE PANEL
        JPanel rulePanel = new JPanel(new BorderLayout());
        rulePanel.setBackground(cardColor);
        rulePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JTextArea txtRules = new JTextArea();
        txtRules.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        txtRules.setEditable(false);
        txtRules.setLineWrap(true);
        txtRules.setWrapStyleWord(true);
        txtRules.setBackground(cardColor);
        txtRules.setText(
                " **QUY ĐỊNH SỬ DỤNG DỊCH VỤ PHÒNG MÁY**\n\n" +
                "- Không hút thuốc, không mang thức ăn có mùi mạnh.\n" +
                "- Tự bảo quản tài khoản game và tài sản cá nhân.\n" +
                "- Không tự ý tháo thiết bị.\n" +
                "- Giữ trật tự, tôn trọng người chơi khác.\n" +
                "- Báo nhân viên khi gặp sự cố kỹ thuật."
        );

        rulePanel.add(txtRules);
        add(rulePanel, BorderLayout.SOUTH);
    }
class GradientPanel extends JPanel {
    private Color start, end;

    public GradientPanel(Color start, Color end) {
        this.start = start;
        this.end = end;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gp = new GradientPaint(0, 0, start, getWidth(), 0, end);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.dispose();
        super.paintComponent(g);
    }
}

class GradientButton extends JButton {

    private Color start = ProductDetailUI.GRADIENT_START;
    private Color end   = ProductDetailUI.GRADIENT_END;
    private int radius = 20;

    public GradientButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gp = new GradientPaint(0, 0, start, getWidth(), 0, end);
        g2.setPaint(gp);

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();

        super.paintComponent(g);
    }
}

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    // RECEIVE PRODUCT DATA
    public void setProductData(String imgPath, String name, int price, int quantity, String desc) {
        try {
            ImageIcon icon = new ImageIcon(imgPath);
            Image scaled = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            lblImage.setText("No Image");
        }

        lblName.setText(name);
        lblPrice.setText("Đơn giá: " + price + " VNĐ");
        lblQuantity.setText("Số lượng: " + quantity);
        txtDescription.setText(desc);
    }
}