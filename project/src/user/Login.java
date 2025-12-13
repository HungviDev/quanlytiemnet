package user;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Login extends JFrame {

    public Login() {
        setTitle("Login UI");
        setSize(900, 500);
        setLocationRelativeTo(null);

        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(49, 88, 200),
                        getWidth(), getHeight(), new Color(194, 53, 191)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setLayout(new GridBagLayout());

        // --------------------- KHUNG LOGIN TRẮNG ---------------------
        JPanel loginBox = new JPanel();
        loginBox.setBackground(Color.WHITE);
        loginBox.setPreferredSize(new Dimension(750, 330));
        loginBox.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));
        loginBox.setLayout(new GridLayout(1, 2));

        // --------------------- PANEL AVATAR BÊN TRÁI -----------------
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);

        // avatar demo (dùng hình mặc định của Swing)
        JLabel avatar = new JLabel();
        ImageIcon rawImg = new ImageIcon(getClass().getResource("/img/banner.jpg"));
        Image scaledImg = rawImg.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);

        avatar.setIcon(new ImageIcon(scaledImg));

        leftPanel.add(avatar);

        // --------------------- PANEL FORM LOGIN BÊN PHẢI -------------
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(20, 60, 20, 60));

        JLabel title = new JLabel("Chào mừng trở lại!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Lấy icon từ thư mục img
        ImageIcon userIcon = new ImageIcon(getClass().getResource("/img/user.png"));
        ImageIcon passIcon  = new ImageIcon(getClass().getResource("/img/padlock.png"));

        // Truyền icon vào input field
        JPanel userBox = createInputField("  Tài khoản", userIcon);
        JPanel passBox  = createInputField(" Mật khẩu", passIcon);

        // Nút login
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setAlignmentX(CENTER_ALIGNMENT);
        btnLogin.setPreferredSize(new Dimension(200, 45));
        btnLogin.setMaximumSize(new Dimension(200, 45));
        btnLogin.setBackground(new Color(45, 171, 61));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new LineBorder(new Color(35, 150, 50), 1, true));
        
        //Vào trang HomeUser
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomeUser().setVisible(true); // mở frame mới
                dispose(); // đóng frame login
            }
        });

        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(65, 190, 80));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(45, 171, 61));
            }
        });

        JLabel forgot = new JLabel("Quên mật khẩu / Tạo tài khoản?");
        forgot.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        forgot.setForeground(Color.GRAY);
        forgot.setAlignmentX(CENTER_ALIGNMENT);
        forgot.setBorder(new EmptyBorder(10, 0, 0, 0));

        // add vào panel phải
        rightPanel.add(title);
        rightPanel.add(userBox);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        rightPanel.add(passBox);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(btnLogin);
        rightPanel.add(forgot);

        // add hai panel trái & phải vào khung trắng
        loginBox.add(leftPanel);
        loginBox.add(rightPanel);

        // add khung trắng vào bg gradient
        bgPanel.add(loginBox);

        add(bgPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createInputField(String placeholder, ImageIcon icon) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    panel.setBackground(new Color(245, 245, 245));
    panel.setBorder(new LineBorder(new Color(225, 225, 225), 1, true));
    panel.setMaximumSize(new Dimension(300, 40));

    // Resize icon 20x20
    Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    JLabel iconLabel = new JLabel(new ImageIcon(scaled));
    iconLabel.setBorder(new EmptyBorder(0, 10, 0, 10));

    JTextField field = new JTextField(placeholder);
    field.setForeground(Color.GRAY);
    field.setBorder(null);
    field.setBackground(new Color(245, 245, 245));

    field.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (field.getText().equals(placeholder)) {
                field.setText("");
                field.setForeground(Color.BLACK);
            }
        }

        public void focusLost(java.awt.event.FocusEvent evt) {
            if (field.getText().isEmpty()) {
                field.setText(placeholder);
                field.setForeground(Color.GRAY);
            }
        }
    });

    panel.add(iconLabel);
    panel.add(field);

    return panel;
    
}


    public static void main(String[] args) {
        new Login();
    }
}
