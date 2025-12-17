package user.View;

import java.awt.*;
import java.security.Provider.Service;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import Connection.DatabaseConnection;
import user.DAO.ServiceDAO;
import user.Model.ServiceModel;

public class ProductListUI extends JFrame {

    private JPanel panelProducts;
    private JComboBox<String> cbCategory;
    private JTextField txtSearch;
    private ServiceDAO serviceDAO;
    private Connection connection;

    private static final Color GRADIENT_START = new Color(135, 88, 255);
    private static final Color GRADIENT_END = new Color(66, 95, 235);
    private static final Color BG_COLOR = new Color(255, 251, 236);

    // 🔥 SỬA CÁC CLASS NỘI BỘ THÀNH STATIC

    static class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        // ... (phần còn lại của RoundedBorder giữ nguyên) ...
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 1, radius + 1, radius + 1, radius + 1);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    static class RoundedButton extends JButton {
        private int radius;
        // ... (phần còn lại của RoundedButton giữ nguyên) ...

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setOpaque(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Nếu là nút "MUA NGAY"
            if ("buy_now".equals(getName())) {
                GradientPaint gradient = new GradientPaint(
                        0, 0, GRADIENT_START,
                        getWidth(), 0, GRADIENT_END);
                g2.setPaint(gradient);
            } else {
                g2.setColor(getBackground());
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            // Text
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 3;

            g2.setColor(getForeground());
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }

    static class GradientPanel extends JPanel {
        private Color startColor;
        private Color endColor;

        public GradientPanel(Color start, Color end) {
            this.startColor = start;
            this.endColor = end;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            int w = getWidth();

            GradientPaint gp = new GradientPaint(
                    0, 0, startColor,
                    0, getHeight(), endColor);

            g2.setPaint(gp);
            g2.fillRect(0, 0, w, getHeight());

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Lớp Product (Nếu dùng)
    static class Product {
        // ...
    }

    public ProductListUI() {
        try {
            connection = DatabaseConnection.getConnection();
            serviceDAO = new ServiceDAO(connection);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi kết nối hoặc khởi tạo Database: " + e.getMessage(),
                    "Lỗi Hệ thống", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        setTitle("Dịch vụ");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Đảm bảo HomeUser tồn tại và có constructor không tham số
                // new HomeUser().setVisible(true);
                dispose();
            }
        });
        setLayout(new BorderLayout());

        Color bg = new Color(255, 251, 236);

        // ====================== TOP FILTER BAR ======================
        JPanel filterPanel = new GradientPanel(GRADIENT_START, GRADIENT_END);
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        // ... (Phần UI Bar giữ nguyên) ...

        // Style chung
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        // ===== Label =====
        JLabel lblCategory = new JLabel("Loại sản phẩm");
        lblCategory.setFont(labelFont);

        JLabel lblSort = new JLabel("Sắp xếp theo");
        lblSort.setFont(labelFont);

        // ===== ComboBox đẹp =====
        cbCategory = new JComboBox<>(new String[] { "Internet", "Drink", "Food", "Office", "Other"
        });
        cbCategory.addActionListener(e -> {
            // 1. Lấy danh mục đang được chọn
            String selectedCategory = (String) cbCategory.getSelectedItem();

            // 2. Gọi hàm tải lại danh sách sản phẩm theo danh mục
            filterProductsByCategory(selectedCategory);
        });
        Dimension cbSize = new Dimension(150, 32);
        cbCategory.setPreferredSize(cbSize);

        cbCategory.setFont(inputFont);

        // ===== Search box đẹp =====
        txtSearch = new JTextField(25);
        txtSearch.setPreferredSize(new Dimension(230, 32));
        txtSearch.setFont(inputFont);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // ===== Buttons =====
        RoundedButton btnClear = new RoundedButton("TÌM", 20);
        btnClear.setBackground(new Color(230, 230, 230));
        btnClear.setForeground(Color.BLACK);

        RoundedButton btnFilter = new RoundedButton("Đi tới giỏ hàng", 20);
        btnFilter.setIcon(new ImageIcon("/img/computer.png"));
        btnFilter.setBackground(new Color(51, 153, 255));
        btnFilter.setForeground(Color.WHITE);
        btnFilter.addActionListener(e -> {
            CartUI aCartUI = new CartUI();
            aCartUI.setVisible(true);
        });

        Dimension btnSize = new Dimension(110, 35);
        btnClear.setPreferredSize(btnSize);
        btnFilter.setPreferredSize(btnSize);

        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);
        btnClear.setFont(btnFont);
        btnFilter.setFont(btnFont);

        // ===== Add to panel =====
        filterPanel.add(lblCategory);
        filterPanel.add(cbCategory);
        filterPanel.add(lblSort);
        filterPanel.add(txtSearch);
        filterPanel.add(btnClear);
        filterPanel.add(btnFilter);

        add(filterPanel, BorderLayout.NORTH);

        // ====================== PRODUCT GRID ======================
        panelProducts = new JPanel(new GridLayout(0, 4, 30, 30));
        panelProducts.setBorder(new EmptyBorder(20, 40, 20, 40));
        panelProducts.setBackground(bg);

        // 🔥 Gọi hàm load sản phẩm
        loadProducts();

        JScrollPane scroll = new JScrollPane(panelProducts);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadProducts() {
        if (serviceDAO == null) {
            System.err.println("Lỗi: serviceDAO chưa được khởi tạo thành công.");
            return;
        }

        for (ServiceModel p : getFakeProducts()) {
            panelProducts.add(createProductCard(p));
        }
    }

    // ====================== TẢI DỮ LIỆU SẢN PHẨM ======================
    private ArrayList<ServiceModel> getFakeProducts() {
        // Không cần khai báo lại ArrayList, chỉ cần trả về kết quả từ DAO
        return serviceDAO.getAllServices();
    }

    // ====================== CARD UI ======================
    private JPanel createProductCard(ServiceModel p) {

        Color cardColor = new Color(245, 245, 245);
        JPanel card = new JPanel();
        card.setBackground(cardColor);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize((new Dimension(100, 240)));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)));

        // =============== PRICE ON TOP =================
        // 🔥 ĐÃ SỬA LỖI: %d -> %,.0f (định dạng số thực, làm tròn về 0 số thập phân, có
        // dấu phẩy phân cách hàng nghìn)
        JLabel lblPriceTop = new JLabel(String.format("%,.0f VNĐ", p.getPrice()), JLabel.CENTER);
        lblPriceTop.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPriceTop.setForeground(new Color(50, 50, 50));
        lblPriceTop.setAlignmentX(Component.CENTER_ALIGNMENT);

        // =============== IMAGE =================
        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(JLabel.CENTER);
        lblImg.setPreferredSize(new Dimension(100, 120));

        // =============== NAME =================
        JLabel lblName = new JLabel(p.getName(), JLabel.CENTER);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        // =============== QUANTITY & BUTTONS ===============
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        quantityPanel.setBackground(cardColor);

        RoundedButton btnMinus = new RoundedButton("-", 12);
        RoundedButton btnPlus = new RoundedButton("+", 12);

        btnMinus.setBackground(new Color(220, 220, 220));
        btnPlus.setBackground(new Color(220, 220, 220));
        btnMinus.setFont(new Font("Arial", Font.BOLD, 18));
        btnPlus.setFont(new Font("Arial", Font.BOLD, 18));

        btnMinus.setPreferredSize(new Dimension(35, 28));
        btnPlus.setPreferredSize(new Dimension(35, 28));

        JLabel lblQty = new JLabel("0");
        lblQty.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // ... (phần logic btnMinus/btnPlus giữ nguyên) ...
        btnMinus.addActionListener(e -> {
            int v = Integer.parseInt(lblQty.getText());
            if (v > 0)
                lblQty.setText(String.valueOf(v - 1));
        });

        btnPlus.addActionListener(e -> {
            int v = Integer.parseInt(lblQty.getText());
            lblQty.setText(String.valueOf(v + 1));
        });

        quantityPanel.add(btnMinus);
        quantityPanel.add(lblQty);
        quantityPanel.add(btnPlus);

        // =============== ADD BUTTON ===============
        RoundedButton btnBuyNow = new RoundedButton("MUA NGAY", 18);
        btnBuyNow.setBackground(new Color(46, 204, 113));
        btnBuyNow.setName("buy_now");
        btnBuyNow.setForeground(Color.WHITE);
        btnBuyNow.setPreferredSize(new Dimension(120, 40));
        btnBuyNow.setFocusPainted(false);
        btnBuyNow.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuyNow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btnBuyNow.addActionListener(e -> openProductDetail(p));

        // =============== CARD BUILD ===============
        card.add(lblPriceTop);
        card.add(Box.createVerticalStrut(5));
        card.add(lblImg);
        card.add(Box.createVerticalStrut(5));
        card.add(lblName);
        card.add(Box.createVerticalStrut(10));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBackground(cardColor);

        btnBuyNow.setPreferredSize(new Dimension(120, 40));
        btnBuyNow.setMaximumSize(new Dimension(120, 40));
        btnBuyNow.setMinimumSize(new Dimension(120, 40));

        quantityPanel.setMaximumSize(new Dimension(120, 40));
        quantityPanel.setMinimumSize(new Dimension(120, 40));

        bottomPanel.add(quantityPanel);
        bottomPanel.add(Box.createHorizontalStrut(5));
        bottomPanel.add(btnBuyNow);

        card.add(Box.createVerticalStrut(10));
        card.add(bottomPanel);

        return card;
    }

    private void openProductDetail(ServiceModel p) {

    }
    // Thêm phương thức này vào lớp ProductListUI

    private void filterProductsByCategory(String category) {
        if (serviceDAO == null) {
            System.err.println("Lỗi: serviceDAO chưa được khởi tạo.");
            return;
        }
        ArrayList<ServiceModel> filteredList;
        if (category.equals("Tất cả") || category.equals("Internet") || category.equals("Other")) {
            filteredList = serviceDAO.getAllServices();
        } else {
            // Tải danh sách theo danh mục cụ thể (ví dụ: Drink, Food)
            filteredList = serviceDAO.getServicesByCategory(category);
        }

        // 1. Xóa tất cả sản phẩm cũ trên panel
        panelProducts.removeAll();

        // 2. Thêm các sản phẩm mới đã lọc
        if (filteredList != null && !filteredList.isEmpty()) {
            for (ServiceModel p : filteredList) {
                panelProducts.add(createProductCard(p));
            }
        } else {
            // Hiển thị thông báo nếu không tìm thấy sản phẩm
            JLabel lblNoResult = new JLabel("Không tìm thấy dịch vụ nào thuộc danh mục " + category, JLabel.CENTER);
            lblNoResult.setFont(new Font("Segoe UI", Font.BOLD, 18));
            panelProducts.add(lblNoResult);
        }

        // 3. Cập nhật giao diện
        panelProducts.revalidate();
        panelProducts.repaint();
    }

    public static void main(String[] args) {
        // Khởi chạy trên Event Dispatch Thread (luôn là cách tốt nhất cho Swing)
        SwingUtilities.invokeLater(() -> new ProductListUI());
    }
}