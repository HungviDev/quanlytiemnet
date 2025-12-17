package user.View;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CartUI extends JFrame {

    private JPanel cartItemsPanel;
    private JLabel lblTotalItems, lblTotalPrice, lblTotalQuantity;
    private JButton btnCheckout, btnBack;

    private static final Color GRADIENT_START = new Color(135, 88, 255);
    private static final Color GRADIENT_END = new Color(66, 95, 235);
    private static final Color BG_COLOR = new Color(245, 245, 245);
    private static final Color CARD_COLOR = Color.WHITE; // Màu trắng cho sạch
    private static final Color GRAY_BACK = new Color(220, 222, 225); // Xám cho nút Trở về

    private List<CartItem> cartList = new ArrayList<>();

    public CartUI() {
        setTitle("Giỏ hàng");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                new ProductListUI().setVisible(true);
                dispose();
            }
        });

        loadMockCartData();

        // Panel chính dùng BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(25, 25));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        add(mainPanel);

        // --- LEFT PANEL (65%) ---
        JPanel leftContainer = new JPanel(new BorderLayout());
        leftContainer.setOpaque(false);
        leftContainer.setPreferredSize(new Dimension(750, 0)); // Cố định chiều rộng 65%

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(BG_COLOR);

        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                " CHI TIẾT ĐƠN HÀNG ", 0, 0, new Font("Segoe UI", Font.BOLD, 16), GRADIENT_END));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        leftContainer.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(leftContainer, BorderLayout.WEST);

        // --- RIGHT PANEL (35%) ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(CARD_COLOR);
        rightPanel.setPreferredSize(new Dimension(380, 600));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(30, 25, 30, 25)));

        // Phần Summary (NORTH)
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("TỔNG KẾT ĐƠN HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(50, 50, 50));

        lblTotalItems = new JLabel("Số loại món: 0");
        lblTotalQuantity = new JLabel("Tổng số lượng: 0");
        lblTotalPrice = new JLabel( " 0 VNĐ");
   
        
        lblTotalItems.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblTotalQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblTotalPrice.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTotalPrice.setForeground(new Color(255, 69, 0));

        summaryPanel.add(lblTitle);
        summaryPanel.add(Box.createVerticalStrut(25));
        summaryPanel.add(lblTotalItems);
        summaryPanel.add(Box.createVerticalStrut(10));
        summaryPanel.add(lblTotalQuantity);
        summaryPanel.add(Box.createVerticalStrut(20));
        summaryPanel.add(new JSeparator());
        summaryPanel.add(Box.createVerticalStrut(20));
        summaryPanel.add(lblTotalPrice);

        rightPanel.add(summaryPanel, BorderLayout.NORTH);

        // Phần Buttons (SOUTH) - ĐÂY LÀ CHỖ KHẮC PHỤC LỖI
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 0, 15)); // Dùng GridLayout cho nút đều nhau
        btnPanel.setOpaque(false);

        btnCheckout = new GradientButton("ĐẶT MÓN NGAY", GRADIENT_START, GRADIENT_END, Color.WHITE);
        btnBack = new GradientButton("TRỞ VỀ", GRAY_BACK, new Color(180, 180, 180), new Color(60, 60, 60));

        btnCheckout.setPreferredSize(new Dimension(0, 50));
        btnBack.setPreferredSize(new Dimension(0, 50));

        btnCheckout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đặt món thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });
        btnBack.addActionListener(e -> {
            new ProductListUI().setVisible(true);
            dispose();
        });

        btnPanel.add(btnCheckout);
        btnPanel.add(btnBack);

        rightPanel.add(btnPanel, BorderLayout.SOUTH);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // --- BOTTOM PANEL (RULES) ---
        JPanel rulePanel = new JPanel(new BorderLayout());
        rulePanel.setBackground(new Color(235, 235, 235));
        rulePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblRule = new JLabel("Lưu ý: Vui lòng kiểm tra kỹ món ăn trước khi đặt. Dịch vụ phòng máy phục vụ 24/7.");
        lblRule.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        rulePanel.add(lblRule);
        mainPanel.add(rulePanel, BorderLayout.SOUTH);

        displayCartItems();
        updateSummary();
    }

    private void loadMockCartData() {
        cartList.add(new CartItem("Cơm rang dưa bò", 35000, 2));
        cartList.add(new CartItem("Trà sữa trân châu", 25000, 1));
        cartList.add(new CartItem("Mì gói trứng", 20000, 3));
    }

    private void displayCartItems() {
        cartItemsPanel.removeAll();
        for (CartItem item : cartList) {
            cartItemsPanel.add(new CartItemPanel(item));
            cartItemsPanel.add(Box.createVerticalStrut(10));
        }
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private void updateSummary() {
        long total = 0; int qty = 0;
        for (CartItem item : cartList) {
            total += (long) item.price * item.quantity;
            qty += item.quantity;
        }
        NumberFormat vn = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = vn.format(total);
        lblTotalItems.setText("Số loại món: " + cartList.size());
        lblTotalQuantity.setText("Tổng số lượng: " + qty);
        lblTotalPrice.setText(vn.format(total));
        lblTotalPrice.setText("Tổng tiền: " + formattedPrice);
    }

    // --- CUSTOM COMPONENTS ---

    class GradientButton extends JButton {
        private Color start, end;
        public GradientButton(String text, Color s, Color e, Color fg) {
            super(text);
            this.start = s; this.end = e;
            setForeground(fg);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, start, getWidth(), 0, end));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class CartItemPanel extends JPanel {
        public CartItemPanel(CartItem item) {
            setLayout(new BorderLayout(20, 0));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 235, 235)),
                new EmptyBorder(15, 20, 15, 20)));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            JLabel lblName = new JLabel(item.name);
            lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
            
            JLabel lblDetail = new JLabel("Số lượng: " + item.quantity + " x " + item.price + "đ");
            lblDetail.setForeground(Color.GRAY);

            JPanel info = new JPanel(new GridLayout(2, 1));
            info.setOpaque(false);
            info.add(lblName);
            info.add(lblDetail);

            JButton btnDel = new JButton("Xóa");
            btnDel.setForeground(Color.RED);
            btnDel.addActionListener(e -> {
                cartList.remove(item);
                displayCartItems();
                updateSummary();
            });

            add(info, BorderLayout.CENTER);
            add(btnDel, BorderLayout.EAST);
        }
    }

    private static class CartItem {
        String name; int price; int quantity;
        public CartItem(String n, int p, int q) { name = n; price = p; quantity = q; }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new CartUI().setVisible(true));
    }
}