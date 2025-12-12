package admin.View;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Account extends JPanel {

    // ====================== CONSTANTS ======================
    private static final Color BG_MAIN = new Color(245, 245, 245);
    private static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);

    private static final int ICON_WIDTH = 16;
    private static final int ICON_HEIGHT = 16;

    // ====================== COMPONENTS ======================
    private JTable accountTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public Account() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    // ====================== HEADER ======================
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setBackground(BG_MAIN);

        JLabel title = new JLabel("Quản Lý Tài Khoản Người Dùng");
        title.setFont(FONT_TITLE);
        title.setForeground(new Color(33, 33, 33));
        header.add(title, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.setBackground(BG_MAIN);

        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchField = new JTextField(20);
        searchField.setFont(FONT_NORMAL);
        searchField.setPreferredSize(new Dimension(200, 35));

        searchPanel.add(searchField);
        header.add(searchPanel, BorderLayout.EAST);

        return header;
    }

    // ====================== TABLE ======================
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        String[] cols = {"STT", "Tài khoản","Mật khẩu", "Thời gian còn lại",  "Thời gian tạo"};

        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        accountTable = new JTable(tableModel);
        accountTable.setFont(FONT_NORMAL);
        accountTable.setRowHeight(30);
        accountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeaderStyle(accountTable);

        panel.add(new JScrollPane(accountTable), BorderLayout.CENTER);
        return panel;
    }

    private void JTableHeaderStyle(JTable table) {
        table.getTableHeader().setFont(FONT_BOLD);
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
    }

    // ====================== BUTTON PANEL ======================
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(BG_MAIN);

        panel.add(createButton("THÊM MỚI", new Color(52, 152, 219), "/img/add.png", e -> handleAdd()));
        panel.add(createButton("CHỈNH SỬA", new Color(46, 204, 113), "/img/edit.png", e -> handleEdit()));
        panel.add(createButton("XÓA", new Color(231, 76, 60), "/img/delete.png", e -> handleDelete()));
        panel.add(createButton("LÀM MỚI", new Color(155, 89, 182), "/img/refresh.png", e -> handleRefresh()));

        return panel;
    }

    // Tạo button nhanh
    private JButton createButton(String text, Color color, String iconPath, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text, loadIcon(iconPath));
        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setIconTextGap(8);
        btn.setHorizontalTextPosition(SwingConstants.RIGHT);

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(color.darker()); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(color); }
        });

        btn.addActionListener(action);
        return btn;
    }

    // ====================== ICON LOADER ======================
    private ImageIcon loadIcon(String path) {
        try {
            var res = getClass().getResource(path);
            if (res == null) return null;

            BufferedImage img = ImageIO.read(res);
            Image scaled = img.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);

        } catch (IOException ex) {
            System.err.println("Không thể load icon: " + path);
            return null;
        }
    }

    // ====================== EVENT HANDLERS ======================
    private void handleAdd() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String now = LocalDateTime.now().format(fmt);

        AccountForm form = new AccountForm(SwingUtilities.getWindowAncestor(this), "Thêm tài khoản", true,
                "", "", "", now);

        if (form.showDialog()) {
            int stt = tableModel.getRowCount() + 1;
            tableModel.addRow(new Object[]{stt, form.getUsername(), form.getPassword(), form.getRemainingTime(), form.getCreatedAt()});
            JOptionPane.showMessageDialog(this, "Thêm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleEdit() {
        int row = accountTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = String.valueOf(tableModel.getValueAt(row, 1));
        String password = String.valueOf(tableModel.getValueAt(row, 2));
        String remaining = String.valueOf(tableModel.getValueAt(row, 3));
        String created = String.valueOf(tableModel.getValueAt(row, 4));

        AccountForm form = new AccountForm(SwingUtilities.getWindowAncestor(this), "Chỉnh sửa tài khoản", true,
                username, password, remaining, created);

        if (form.showDialog()) {
            tableModel.setValueAt(form.getUsername(), row, 1);
            tableModel.setValueAt(form.getPassword(), row, 2);
            tableModel.setValueAt(form.getRemainingTime(), row, 3);
            tableModel.setValueAt(form.getCreatedAt(), row, 4);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleDelete() {
        int row = accountTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản để xóa",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            tableModel.removeRow(row);
            JOptionPane.showMessageDialog(this, "Xóa thành công");
        }
    }

    private void handleRefresh() {
        tableModel.setRowCount(0);
        loadAccountsFromDatabase();
    }

    private void loadAccountsFromDatabase() {
        // TODO: Load database
    }
}
