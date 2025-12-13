package admin.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import admin.Controller.computerDAO;
import admin.Model.computer;

import java.util.ArrayList;

public class Computer extends JPanel  {
    
    private DashboardUI parentFrame;
    private final int ITEM_ICON_SIZE = 64; // Kích thước lớn cho icon máy tính
    private ArrayList<computer> listcomputer = new ArrayList<>();
    private computerDAO computerDAO = new computerDAO();
    private servercontrol servercontrol = new servercontrol();

    public Computer(DashboardUI parentFrame) {
        this.parentFrame = parentFrame;
        
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); // Màu nền của trang (ngoài cùng)

        // 1. HEADER (Chứa Title và Action Bar)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 247, 250));
        
        JLabel title = new JLabel("Quản lý máy tính");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(40, 40, 40));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(createActionBar(), BorderLayout.CENTER); 
        
        add(headerPanel, BorderLayout.NORTH);

        // 2. CONTENT GRID (Chứa các item máy tính)
        JPanel content = new JPanel();
        // Màu nền xám xanh nhạt, tạo độ tương phản nhẹ với card trắng
        content.setBackground(new Color(240, 243, 245)); 
        content.setLayout(new GridLayout(0, 4, 15, 15)); // 0 dòng (tự động), 4 cột, khoảng cách 15px
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tạo cơ sở dữ liệu
        listcomputer = computerDAO.getAllComputer();

        for (computer computer : listcomputer) {
            content.add(createComputerItem(computer, "/img/computer.png", ITEM_ICON_SIZE));
        }
        // Bọc Content trong JScrollPane để cho phép cuộn khi cần
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        
        add(scrollPane, BorderLayout.CENTER);
    }
   
    private JPanel createActionBar() {
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionBar.setBackground(new Color(245, 247, 250));
        actionBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));

        // Nút Thêm Máy tính
        JButton addButton = new JButton("➕ Thêm máy tính");
        addButton.setBackground(new Color(40, 167, 69)); 
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        
        // Nút Tắt Máy Hàng Loạt
        JButton shutdownButton = new JButton("🔌 Khóa máy hàng loạt");
        shutdownButton.setBackground(new Color(220, 53, 69)); 
        shutdownButton.setForeground(Color.WHITE);
        shutdownButton.setFocusPainted(false);
        actionBar.add(addButton);
        actionBar.add(shutdownButton);
        
        return actionBar;
    }

    private JPanel createComputerItem(computer computer, String iconPath, int preferredWidth) { 
        JPanel itemPanel = new JPanel(new BorderLayout()); 
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setPreferredSize(new Dimension(preferredWidth, preferredWidth)); 

        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 225, 225), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10) 
        ));
        
        // Icon
        ImageIcon computerIcon = parentFrame.getScaledIcon(iconPath, ITEM_ICON_SIZE, ITEM_ICON_SIZE);
        JLabel iconLabel = new JLabel(computerIcon);
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // --- PANEL CHỨA TÊN VÀ TÌNH TRẠNG (NORTH) ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS)); // Sắp xếp dọc
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(0, 0, 5, 0)); 
        
        // Tên máy
        JLabel nameLabel = new JLabel(computer.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(50, 50, 50));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        // TÌNH TRẠNG MÁY
        JLabel statusLabel = new JLabel(computer.getStatus());
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(getStatusColor(computer.getStatus()));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        infoPanel.add(nameLabel);
        infoPanel.add(statusLabel);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(new EmptyBorder(5, 0, 0, 0)); 
        JButton lockButton;
        if (computer.getStatus().equals("Đã khóa")) {
            lockButton = new JButton(" Mở khóa");
            lockButton.setBackground(new Color(255, 193, 7)); // Vàng
        } else {
            lockButton = new JButton(" Khóa máy");
            lockButton.setBackground(new Color(23, 162, 184)); 
        }
        lockButton.setForeground(Color.WHITE);
        lockButton.setFocusPainted(false);
        lockButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        // xử lí gửi lệnh khóa
        lockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (computer.getStatus().equals("Đã khóa")) {
                   servercontrol.sendCommandToClient(computer.getIpadress(), "unlock");
                } else {
                    servercontrol.sendCommandToClient(computer.getIpadress(), "lock");
                } 
            }
        });
        // 
        JButton editButton = new JButton(" Sửa");
        editButton.setBackground(new Color(108, 117, 125)); // Xám
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font("Segoe UI", Font.PLAIN, 13)); 

        JButton deleteButton = new JButton(" Xóa");
        deleteButton.setBackground(new Color(220, 53, 69)); // Đỏ
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 13)); 

        actionPanel.add(lockButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        
        // Lắp ráp Panel Item
        itemPanel.add(infoPanel, BorderLayout.NORTH);
        itemPanel.add(iconLabel, BorderLayout.CENTER);
        itemPanel.add(actionPanel, BorderLayout.SOUTH);
        
        return itemPanel;
    }
    

    private Color getStatusColor(String status) {
        if (status.toLowerCase().contains("hoạt động")) {
            return new Color(40, 167, 69); // Xanh lá
        } else if (status.toLowerCase().contains("đã khóa")) {
            return new Color(255, 193, 7); // Vàng
        } else if (status.toLowerCase().contains("rảnh")) {
            return new Color(23, 162, 184); // Xanh dương
        } else {
            return Color.GRAY;
        }
    }


}