package admin.View;

import Connection.DatabaseConnection;
import admin.DAO.computerDAO;
import admin.DAO.storageDAO;
import admin.Model.computer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;

public class StorageView extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private ArrayList<admin.Model.Storage> cachedList = new ArrayList<>();
    private storageDAO dao;

    private static final Color BG = new Color(245, 245, 245);
    private static final Font FN = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FB = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FT = new Font("Segoe UI", Font.BOLD, 20);

    public StorageView() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            dao = new storageDAO(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout(10, 10));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(header(), BorderLayout.NORTH);
        add(tablePanel(), BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);

        loadData();
    }

    // ================= HEADER =================
    private JPanel header() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG);

        JLabel title = new JLabel("QUẢN LÝ DỊCH VỤ");
        title.setFont(FT);
        p.add(title, BorderLayout.WEST);

        return p;
    }

    // ================= TABLE =================
    private JPanel tablePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);

        String[] cols = {
                "ID",
                "Tên dịch vụ",
                "Đơn vị",
                "Giá",
                "Mô tả",
                "Danh mục"
        };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(FN);
        table.setRowHeight(30);
        styleTable();

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void styleTable() {
        table.getTableHeader().setFont(FB);
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
    }

    // ================= BUTTON =================
    private JPanel buttonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        p.setBackground(BG);

        p.add(btn("THÊM", new Color(52, 152, 219), e -> addItem()));
        p.add(btn("SỬA", new Color(46, 204, 113), e -> editItem()));
        p.add(btn("XÓA", new Color(231, 76, 60), e -> deleteItem()));

        return p;
    }

    private JButton btn(String text, Color color, java.awt.event.ActionListener ac) {
        JButton b = new JButton(text);
        b.setFont(FB);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(120, 40));
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.addActionListener(ac);
        return b;
    }

    // ================= CRUD =================
    private void addItem() {
        StorageForm form = new StorageForm(
                SwingUtilities.getWindowAncestor(this),
                "Thêm dịch vụ",
                true,
                "", "", "0", "", "Other");

        if (form.showDialog()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                storageDAO dao = new storageDAO(conn);

                admin.Model.Storage s = new admin.Model.Storage(
                        null,
                        form.getName(),
                        form.getQuantity_unit(),
                        form.getPrice(),
                        form.getDescription(),
                        form.getCategory());

                if (dao.insertStorage(s)) {
                    loadData();
                } else {
                    msg("Thêm dịch vụ thất bại!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg("Kết nối DB thất bại!");
            }
        }
    }

    private void editItem() {
        int r = table.getSelectedRow();
        if (r < 0) {
            msg("Vui lòng chọn dịch vụ!");
            return;
        }

        admin.Model.Storage old = cachedList.get(r);

        StorageForm form = new StorageForm(
                SwingUtilities.getWindowAncestor(this),
                "Chỉnh sửa dịch vụ",
                true,
                old.getName(),
                old.getQuantity_unit(),
                String.valueOf(old.getPrice()),
                old.getDescription(),
                old.getCategory());

        if (form.showDialog()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                storageDAO dao = new storageDAO(conn);

                admin.Model.Storage updated = new admin.Model.Storage(
                        old.getService_id(),
                        form.getName(),
                        form.getQuantity_unit(),
                        form.getPrice(),
                        form.getDescription(),
                        form.getCategory());

                if (dao.updateStorage(updated)) {
                    loadData();
                } else {
                    msg("Cập nhật thất bại!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg("Kết nối DB thất bại!");
            }
        }
    }

    private void deleteItem() {
        int r = table.getSelectedRow();
        if (r < 0) {
            msg("Vui lòng chọn dịch vụ!");
            return;
        }

        if (JOptionPane.showConfirmDialog(
                this,
                "Xóa dịch vụ này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            String id = cachedList.get(r).getService_id();
            if (dao.deleteStorageById(id)) {
                loadData();
            } else {
                msg("Xóa thất bại!");
            }
        }
    }

    // ================= LOAD DATA =================
    private void loadData() {
        model.setRowCount(0);
        cachedList = dao.getAllStorage();

        for (admin.Model.Storage c : cachedList) {
            model.addRow(new Object[] {
                    c.getService_id(),
                    c.getName(),
                    c.getQuantity_unit(),
                    c.getPrice(),
                    c.getDescription(),
                    c.getCategory()
            });
        }
    }

    private void msg(String m) {
        JOptionPane.showMessageDialog(this, m);
    }
}
