package user.View;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


 
public class ProductListUI extends JFrame {

    private JPanel panelProducts;
    private JComboBox<String> cbCategory, cbSort;
    private JTextField txtSearch;

     // =IMPORANT= ĐỊNH NGHĨA MÀU SẮC CHỦ ĐẠO (Gradient Xanh Tím)
    // Màu bắt đầu (Tím sáng hơn)
     private static final Color GRADIENT_START = new Color(135, 88, 255);
    // Màu kết thúc (Xanh đậm hơn)
    private static final Color GRADIENT_END = new Color(66, 95, 235);
    // Màu nền chính (Off-white để làm nổi bật nội dung)
    private static final Color BG_COLOR = new Color(255, 251, 236);

    public ProductListUI() {

        setTitle("Dịch vụ");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Giả sử bạn có class Login, nếu chưa có hãy comment lại dòng này
                 new HomeUser().setVisible(true);
                dispose();
            }
        });
        setLayout(new BorderLayout());

        Color bg = new Color(255, 251, 236);
        Color pr = new Color(245, 245, 245);
    

       class RoundedBorder implements Border {
    private int radius;

    RoundedBorder(int radius) {
        this.radius = radius;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(radius+1, radius+1, radius+1, radius+1);
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawRoundRect(x, y, width-1, height-1, radius, radius);
    }
}

       // ====================== TOP FILTER BAR ======================
JPanel filterPanel = new GradientPanel(GRADIENT_START, GRADIENT_END);
filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));


// Style chung
Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

// ===== Label =====
JLabel lblCategory = new JLabel("Loại sản phẩm");
lblCategory.setFont(labelFont);

JLabel lblSort = new JLabel("Sắp xếp theo");
lblSort.setFont(labelFont);

// ===== ComboBox đẹp =====
cbCategory = new JComboBox<>(new String[]{"Tất cả", "Nước uống", "Đồ ăn vặt", "Thẻ game"});
cbSort = new JComboBox<>(new String[]{"Tên A-Z", "Tên Z-A", "Giá tăng dần", "Giá giảm dần"});

Dimension cbSize = new Dimension(150, 32);
cbCategory.setPreferredSize(cbSize);
cbSort.setPreferredSize(cbSize);

cbCategory.setFont(inputFont);
cbSort.setFont(inputFont);

// ===== Search box đẹp =====
txtSearch = new JTextField(25);
txtSearch.setPreferredSize(new Dimension(230, 32));
txtSearch.setFont(inputFont);
txtSearch.setBorder(BorderFactory.createCompoundBorder(
        new RoundedBorder(10),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)

        
));

        
// ===== Buttons =====
RoundedButton btnClear = new RoundedButton("TÌM", 20);
btnClear.setBackground(new Color(230, 230, 230));
btnClear.setForeground(Color.BLACK);

RoundedButton btnFilter = new RoundedButton("TRỞ VỀ", 20);
btnFilter.setBackground(new Color(51, 153, 255));
btnFilter.setForeground(Color.WHITE);
btnFilter.addActionListener(e -> {
    new HomeUser().setVisible(true);  
    dispose();                        
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
filterPanel.add(cbSort);

filterPanel.add(txtSearch);

filterPanel.add(btnClear);
filterPanel.add(btnFilter);


add(filterPanel, BorderLayout.NORTH);



        // ====================== PRODUCT GRID ======================
        panelProducts = new JPanel(new GridLayout(0, 4, 30, 30));
        panelProducts.setBorder(new EmptyBorder(20,40,20,40));
        panelProducts.setBackground(bg);
       

        for (Product p : getFakeProducts()) {
            panelProducts.add(createProductCard(p));
        }

        JScrollPane scroll = new JScrollPane(panelProducts);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

class RoundedButton extends JButton {
    private int radius;

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
        getWidth(), 0, GRADIENT_END
);

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

class GradientPanel extends JPanel {
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
        int h = getHeight();

  
        GradientPaint gp = new GradientPaint(
    0, 0, startColor,
    0, getHeight(), endColor
);

        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);

        g2.dispose();
        super.paintComponent(g);
    }
}



    // ====================== FAKE PRODUCTS ======================
    private ArrayList<Product> getFakeProducts() {

        ArrayList<Product> list = new ArrayList<>();
        list.add(new Product("Coca", 15000, "img/coca.png"));
        list.add(new Product("Pepsi", 15000, "img/pepsi.png"));
        list.add(new Product("7up", 15000, "img/7up.png"));
        list.add(new Product("Nước suối", 10000, "img/water.png"));
        list.add(new Product("Sting", 15000, "img/sting.png"));
        list.add(new Product("Redbull", 20000, "img/redbull.png"));
        list.add(new Product("Snack Oishi", 8000, "img/snack.png"));
        list.add(new Product("Thẻ Garena 100k", 100000, "img/garena.png"));
        list.add(new Product("Mì Hảo Hảo", 6000, "img/haohao.png"));
        list.add(new Product("Bánh ChocoPie", 12000, "img/chocopie.png"));
        list.add(new Product("Trà Xanh Không Độ", 10000, "img/khongdo.png"));
        list.add(new Product("Cafe Lon Highlands", 25000, "img/highlands.png"));

        return list;
    }
// ====================== CARD UI ======================
    private JPanel createProductCard(Product p) {

        Color cardColor = new Color(245, 245, 245);
        JPanel card = new JPanel();
        card.setBackground(cardColor);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize((new Dimension(100, 240)));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                new EmptyBorder(10,10,10,10)
        ));

        // =============== PRICE ON TOP =================
        JLabel lblPriceTop = new JLabel(String.format("%,d VNĐ", p.price), JLabel.CENTER);
        lblPriceTop.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPriceTop.setForeground(new Color(50,50,50));
        lblPriceTop.setAlignmentX(Component.CENTER_ALIGNMENT);

        // =============== IMAGE =================
        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(JLabel.CENTER);
        lblImg.setPreferredSize(new Dimension(100,120));

        try {
            ImageIcon icon = new ImageIcon(p.imagePath);
            Image scaled = icon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            lblImg.setText("No Image");
        }

        // =============== NAME =================
        JLabel lblName = new JLabel(p.name, JLabel.CENTER);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        // =============== QUANTITY ===============
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        quantityPanel.setBackground(cardColor);

        RoundedButton btnMinus = new RoundedButton("-", 12);
        RoundedButton btnPlus  = new RoundedButton("+", 12);

        btnMinus.setBackground(new Color(220, 220, 220));
        btnPlus.setBackground(new Color(220, 220, 220));
        btnMinus.setFont(new Font("Arial", Font.BOLD, 18));
        btnPlus.setFont(new Font("Arial", Font.BOLD, 18));

        btnMinus.setPreferredSize(new Dimension(35, 28));
        btnPlus.setPreferredSize(new Dimension(35, 28));

        JLabel lblQty = new JLabel("0");
        lblQty.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        

        btnMinus.addActionListener(e -> {
            int v = Integer.parseInt(lblQty.getText());
            if (v > 0) lblQty.setText(String.valueOf(v - 1));
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
bottomPanel.add(Box.createHorizontalStrut(5)); // khoảng cách giữa 2 phần
bottomPanel.add(btnBuyNow);

card.add(Box.createVerticalStrut(10));
card.add(bottomPanel);




        return card;
    }

    private void openProductDetail(Product p) {
    ProductDetailUI detail = new ProductDetailUI();
    detail.setProductData(p.imagePath, p.name, p.price, 1, "Mô tả sản phẩm chưa có.");
    detail.setVisible(true);

    this.dispose(); 
}

    // ====================== PRODUCT MODEL ======================
    class Product {
        String name;
        int price;
        String imagePath;

        Product(String name, int price, String imagePath) {
            this.name = name;
            this.price = price;
            this.imagePath = imagePath;
        }
    }

    public static void main(String[] args) {
        new ProductListUI();
    }
}