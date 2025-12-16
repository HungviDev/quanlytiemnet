package admin.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Connection.DatabaseConnection;
import admin.DAO.computerDAO;
import admin.Model.computer;

import java.util.ArrayList;

public class Computer extends JPanel {
    public static Computer instance;
    private DashboardUI parentFrame;
    private final int ITEM_ICON_SIZE = 64;

    private JPanel content; // 🔥 PANEL CHỨA CÁC MÁY
    private ArrayList<computer> listcomputer = new ArrayList<>();

    private Connection connection;
    private computerDAO computerDAO;
    private servercontrol servercontrol = new servercontrol();

    public Computer(DashboardUI parentFrame) {
        instance = this;
        this.parentFrame = parentFrame;
        try {
            connection = DatabaseConnection.getConnection();
            computerDAO = new computerDAO(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Quản lý máy tính");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(createActionBar(), BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // ===== CONTENT =====
        content = new JPanel(new GridLayout(0, 4, 15, 15));
        content.setBackground(new Color(240, 243, 245));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Load lần đầu
        reloadComputerUI();
    }

    // ================= ACTION BAR =================
    private JPanel createActionBar() {
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionBar.setBackground(new Color(245, 247, 250));
        actionBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));

        JButton addButton = new JButton("➕ Thêm máy tính");
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.WHITE);

        JButton shutdownButton = new JButton("🔌 Khóa máy hàng loạt");
        shutdownButton.setBackground(new Color(220, 53, 69));
        shutdownButton.setForeground(Color.WHITE);

        actionBar.add(addButton);
        actionBar.add(shutdownButton);
        addButton.addActionListener(e -> {
            showAddComputerForm();
        });
        return actionBar;
    }

    // ================= RELOAD UI =================
    public void reloadComputerUI() {
        content.removeAll();
        listcomputer = computerDAO.getAllComputer();

        for (computer c : listcomputer) {
            content.add(createComputerItem(c));
        }
        content.revalidate();
        content.repaint();
    }

    // ================= COMPUTER ITEM =================
    private JPanel createComputerItem(computer computer) {

        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // ICON
        ImageIcon icon = parentFrame.getScaledIcon(
                "/img/computer.png",
                ITEM_ICON_SIZE,
                ITEM_ICON_SIZE);
        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);

        // INFO
        JLabel nameLabel = new JLabel(computer.getName(), JLabel.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel statusLabel = new JLabel(computer.getStatus(), JLabel.CENTER);
        statusLabel.setForeground(getStatusColor(computer.getStatus()));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(nameLabel);
        infoPanel.add(statusLabel);

        // ACTION
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBackground(Color.WHITE);

        JButton lockButton = new JButton(
                computer.getStatus().equals("Đã khóa") ? "Mở khóa" : "Khóa máy");

        lockButton.setBackground(new Color(23, 162, 184));
        lockButton.setForeground(Color.WHITE);

        lockButton.addActionListener(e -> {
            if (computer.getStatus().equals("Đã khóa")) {
                servercontrol.sendCommandToClient(computer.getIpadress(), "unlock");
            } else {
                servercontrol.sendCommandToClient(computer.getIpadress(), "lock");
            }
            reloadComputerUI();
        });

        JButton deleteButton = new JButton("Xóa");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);

        deleteButton.addActionListener((ActionEvent e) -> {
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Bạn có chắc muốn xóa máy này?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (computerDAO.deleteComputerByIp(computer.getIpadress())) {
                    reloadComputerUI(); // 🔥 FIX LỖI
                }
            }
        });

        actionPanel.add(lockButton);
        actionPanel.add(deleteButton);

        itemPanel.add(infoPanel, BorderLayout.NORTH);
        itemPanel.add(iconLabel, BorderLayout.CENTER);
        itemPanel.add(actionPanel, BorderLayout.SOUTH);

        return itemPanel;
    }

    // ================= STATUS COLOR =================
    private Color getStatusColor(String status) {
        status = status.toLowerCase();
        if (status.contains("hoạt động"))
            return new Color(40, 167, 69);
        if (status.contains("đã khóa"))
            return new Color(255, 193, 7);
        if (status.contains("rảnh"))
            return new Color(23, 162, 184);
        return Color.GRAY;
    }

    private void showAddComputerForm() {

        JTextField txtName = new JTextField();
        JTextField txtIp = new JTextField();

        Object[] form = {
                "Tên máy:", txtName,
                "IP Address:", txtIp
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                form,
                "Thêm máy tính",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {

            String name = txtName.getText().trim();
            String ip = txtIp.getText().trim();

            if (name.isEmpty() || ip.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không được để trống!");
                return;
            }

            computer c = new computer();
            c.setName(name);
            c.setIpadress(ip);
            c.setStatus("Rảnh"); // mặc định

            if (computerDAO.insertComputer(c)) {
                JOptionPane.showMessageDialog(this, "Thêm máy thành công!");
                reloadComputerUI(); // 🔥 reload
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        }
    }
}
