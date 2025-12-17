package user.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Connection.DatabaseConnection;
import user.DAO.AccountDAO;
import user.Model.AccountModel;

public class Login extends JFrame {
    public static AccountModel accountModel = new AccountModel();
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private Connection connection;
    private AccountDAO accountDAO;

    public Login() {
        try {
            connection = DatabaseConnection.getConnection();
            accountDAO = new AccountDAO(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("Login UI");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ================= BACKGROUND GRADIENT =================
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(49, 88, 200),
                        getWidth(), getHeight(), new Color(194, 53, 191));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setLayout(new GridBagLayout());

        // ================= KHUNG LOGIN =================
        JPanel loginBox = new JPanel(new GridLayout(1, 2));
        loginBox.setPreferredSize(new Dimension(750, 330));
        loginBox.setBackground(Color.WHITE);
        loginBox.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));

        // ================= PANEL TRÁI (ẢNH) =================
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);

        ImageIcon rawImg = new ImageIcon(getClass().getResource("/img/banner.jpg"));
        Image scaledImg = rawImg.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel avatar = new JLabel(new ImageIcon(scaledImg));

        leftPanel.add(avatar);

        // ================= PANEL PHẢI (FORM) =================
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(20, 60, 20, 60));

        JLabel title = new JLabel("Chào mừng trở lại!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        ImageIcon userIcon = new ImageIcon(getClass().getResource("/img/user.png"));
        ImageIcon passIcon = new ImageIcon(getClass().getResource("/img/padlock.png"));

        // ===== TEXTFIELD THẬT =====
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        JPanel userBox = createInputField("  Tài khoản", userIcon, txtUsername, false);
        JPanel passBox = createInputField("  Mật khẩu", passIcon, txtPassword, true);

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setPreferredSize(new Dimension(200, 45));
        btnLogin.setMaximumSize(new Dimension(200, 45));
        btnLogin.setBackground(new Color(45, 171, 61));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new LineBorder(new Color(35, 150, 50), 1, true));

        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(65, 190, 80));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(45, 171, 61));
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()
                        || username.equals("Tài khoản")
                        || password.equals("Mật khẩu")) {

                    JOptionPane.showMessageDialog(
                            Login.this,
                            "Vui lòng nhập tài khoản và mật khẩu",
                            "Lỗi đăng nhập",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                System.out.println(txtUsername.getText().toString() + txtPassword.getText().toString());
                Boolean checkID;
                checkID = accountDAO.checkLogin(txtUsername.getText().toString(),
                        txtPassword.getText().toString());
                System.out.println(checkID);

                if (checkID) {
                    accountModel = accountDAO.getInfoByAccAndPass(txtUsername.getText().toString(),
                            txtPassword.getText().toString());
                    System.out.println(accountModel.toString());
                    new HomeUser().setVisible(true);
                    dispose();
                } else {
                    System.out.println("sai cach yeu");
                }

            }
        });

        JLabel forgot = new JLabel("Quên mật khẩu / Tạo tài khoản?");
        forgot.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        forgot.setForeground(Color.GRAY);
        forgot.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgot.setBorder(new EmptyBorder(10, 0, 0, 0));

        rightPanel.add(title);
        rightPanel.add(userBox);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        rightPanel.add(passBox);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(btnLogin);
        rightPanel.add(forgot);

        loginBox.add(leftPanel);
        loginBox.add(rightPanel);

        bgPanel.add(loginBox);
        add(bgPanel);

        setVisible(true);
    }

    // ================= INPUT FIELD =================
    private JPanel createInputField(String placeholder, ImageIcon icon,
            JTextField field, boolean isPassword) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new LineBorder(new Color(225, 225, 225), 1, true));
        panel.setMaximumSize(new Dimension(300, 40));

        Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaled));
        iconLabel.setBorder(new EmptyBorder(0, 10, 0, 10));

        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.setBorder(null);
        field.setBackground(new Color(245, 245, 245));

        if (isPassword && field instanceof JPasswordField) {
            ((JPasswordField) field).setEchoChar((char) 0);
        }

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    if (isPassword && field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar('●');
                    }
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                    if (isPassword && field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar((char) 0);
                    }
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
