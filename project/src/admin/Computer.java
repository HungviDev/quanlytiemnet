package admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Computer extends JPanel {
    //dựng layout ở đây nhé ko cần phải dựng cửa sổ window chỉ cần đi thẳng vào layout
    public Computer() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); 

        // ===================== HEADER =====================
        JLabel title = new JLabel("🖥️  Quản lý máy tính");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(40, 40, 40));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(title, BorderLayout.NORTH);

        // ===================== CONTENT =====================
        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setLayout(new GridBagLayout());
        GridBagConstraints  gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel label = new JLabel("Đây là trang quản lý máy tính");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(new Color(70, 70, 70));
        content.add(label, gbc);
        add(content, BorderLayout.CENTER);
    }

}
