package admin.View;
import javax.swing.*;
import java.awt.*;
import java.net.URL; // Cần import

public class DashboardUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private final int ICON_WIDTH = 16;
    private final int ICON_HEIGHT = 16;

    public DashboardUI() {
        setTitle("Admin Dashboard UI");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
     
        JPanel sidebar = new GradientSidebar();
        sidebar.setPreferredSize(new Dimension(240, 700));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("ADMIN");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 0));
        sidebar.add(title);
        
        sidebar.add(createMenu("  Quản lý máy tính ", "computers", "/img/computer.png"));
        sidebar.add(createMenu("  Quản lý tài khoản", "accounts", "/img/account.png"));
        sidebar.add(createMenu("  Quản lý đơn hàng", "orders", "/img/order.png"));
        sidebar.add(createMenu("  Quản lí kho", "storages", "/img/storage.png"));
        sidebar.add(createMenu("  Thống kê", "statics", "/img/statics.png"));
        sidebar.add(createMenu("  Đăng xuất", "logout", "/img/logout.png"));

        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new Computer(this), "computers");
        mainPanel.add(new Account(), "accounts");
        mainPanel.add(new StorageView(), "storages");
        mainPanel.add(new Order(), "orders");
        mainPanel.add(new RevenueDashboard(), "statics");
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

   
    private JPanel createMenu(String text, String pageName, String inconPath) {
        JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(240, 45));
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(0, 0, 0, 0));
        ImageIcon icon = getScaledIcon(inconPath, ICON_WIDTH, ICON_HEIGHT);
        JLabel label = new JLabel(text, icon, JLabel.LEFT);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        panel.add(label, BorderLayout.CENTER);
  
        panel.addMouseListener(new java.awt.event.MouseAdapter() {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            cardLayout.show(mainPanel, pageName);
            System.out.println("Switched to page: " + pageName);
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            panel.setBackground(new Color(33, 37, 41));
            panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {

            panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
});

        return panel;
    }
    
    public ImageIcon getScaledIcon(String iconPath, int width, int height) {
        
        URL location = getClass().getResource(iconPath);
        
        if (location == null) {
            System.err.println("LỖI: Không tìm thấy file icon tại đường dẫn: " + iconPath + ". Sử dụng icon trống.");
            return new ImageIcon(); 
        }
        
        ImageIcon originalIcon = new ImageIcon(location);
        
        Image image = originalIcon.getImage();
        
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        
        return new ImageIcon(scaledImage);
    }
    
    
    class GradientSidebar extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(33, 37, 41),
                    0, getHeight(), new Color(52, 58, 64)
            );
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }


   
}