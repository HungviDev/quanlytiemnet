package admin.View;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Order extends JPanel {

    private JTable orderTable;
    private JTable orderDetailTable;
    private JTextField txtSearch;
    private JLabel lblMaDon, lblSoMay, lblNgay, lblTrangThai, lblTongTien;
    private DefaultTableModel orderModel;
    private DefaultTableModel detailModel;
    private Map<String, Object[][]> sampleDetail = new HashMap<>();
    public Order() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel header = new JLabel("Quản lý đơn hàng");
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(new Color(40, 40, 40));
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        add(header, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(450, 700));
        leftPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitleLeft = new JLabel("Danh Sách Đơn Hàng");
        lblTitleLeft.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel searchPanel = new JPanel(new BorderLayout());
        txtSearch = new JTextField();
        JButton btnSearch = new JButton("Tìm");
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);

        orderModel = new DefaultTableModel(new Object[]{"Mã ĐH", "Số Máy", "Ngày", "Trạng Thái"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        orderTable = new JTable(orderModel);
        orderTable.setRowHeight(28);

        JScrollPane scrollOrders = new JScrollPane(orderTable);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));

        JButton btnAdd = createColorButton("Thêm", new Color(0, 122, 255));
        JButton btnEdit = createColorButton("Sửa", new Color(50, 205, 50));
        JButton btnDelete = createColorButton("Xóa", new Color(255, 60, 60));
        JButton btnPay = createColorButton("Thanh Toán", new Color(255, 140, 0));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnPay);

        leftPanel.add(lblTitleLeft, BorderLayout.NORTH);
        leftPanel.add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);
        leftPanel.add(scrollOrders, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        //---------------- RIGHT PANEL -------------------
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ====== TOP TITLE + INFO ======
        JPanel topInfoPanel = new JPanel();
        topInfoPanel.setLayout(new BoxLayout(topInfoPanel, BoxLayout.Y_AXIS));

        JLabel lblTitleRight = new JLabel("Chi Tiết Đơn Hàng");
        lblTitleRight.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitleRight.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        lblMaDon = createInfoLabel("Mã Đơn: ");
        lblSoMay = createInfoLabel("Số Máy: ");
        lblNgay = createInfoLabel("Ngày Tạo: ");
        lblTrangThai = createInfoLabel("Trạng Thái: ");

        infoPanel.add(lblMaDon);
        infoPanel.add(lblSoMay);
        infoPanel.add(lblNgay);
        infoPanel.add(lblTrangThai);

        topInfoPanel.add(lblTitleRight);
        topInfoPanel.add(infoPanel);

        // ====== CENTER: TABLE ======
        detailModel = new DefaultTableModel(
                new Object[]{"Tên SP", "Mã SP", "SL", "Giá", "Thành Tiền"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        orderDetailTable = new JTable(detailModel);
        orderDetailTable.setRowHeight(28);

        JScrollPane scrollDetails = new JScrollPane(orderDetailTable);

        // ====== BOTTOM: TOTAL + BUTTONS ======
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // TOTAL MONEY
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lblTongTien = new JLabel("Tổng tiền: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 17));
        totalPanel.add(lblTongTien);

        // BUTTON ROW
        JPanel detailButtonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        detailButtonPanel.setBorder(new EmptyBorder(10, 30, 0, 30));

        JButton btnAddDetail = createColorButton("Thêm", new Color(0, 122, 255));
        JButton btnEditDetail = createColorButton("Sửa", new Color(50, 205, 50));
        JButton btnDeleteDetail = createColorButton("Xóa", new Color(255, 60, 60));

        detailButtonPanel.add(btnAddDetail);
        detailButtonPanel.add(btnEditDetail);
        detailButtonPanel.add(btnDeleteDetail);

        bottomPanel.add(totalPanel, BorderLayout.NORTH);
        bottomPanel.add(detailButtonPanel, BorderLayout.SOUTH);

        // ===== ADD TO RIGHT PANEL =====
        rightPanel.add(topInfoPanel, BorderLayout.NORTH);
        rightPanel.add(scrollDetails, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.CENTER);

        //---------------- LOAD SAMPLE DATA ----------------
        loadSampleOrders();
        loadSampleDetails();

        //---------------- EVENTS ----------------
        btnSearch.addActionListener(e -> searchOrder());
        orderTable.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { loadOrderDetails(); } });

        btnAddDetail.addActionListener(e -> addDetail());
        btnEditDetail.addActionListener(e -> editDetail());
        btnDeleteDetail.addActionListener(e -> deleteDetail());

        // LEFT BUTTON EVENTS
        btnAdd.addActionListener(e -> addOrder());
        btnEdit.addActionListener(e -> editOrder());
        btnDelete.addActionListener(e -> deleteOrder());
        btnPay.addActionListener(e -> payOrder());
    }

    private JButton createColorButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }

    private JLabel createInfoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return lbl;
    }

    //------------------ SAMPLE DATA ------------------
    private void loadSampleOrders() {
        orderModel.addRow(new Object[]{"DH001", "Máy 01", "2025-01-12", "Đang xử lý"});
        orderModel.addRow(new Object[]{"DH002", "Máy 05", "2025-01-14", "Chưa thanh toán"});
        orderModel.addRow(new Object[]{"DH003", "Máy 02", "2025-01-16", "Đã thanh toán"});
        orderModel.addRow(new Object[]{"DH004", "Máy 03", "2025-01-18", "Đang xử lý"});
        orderModel.addRow(new Object[]{"DH005", "Máy 04", "2025-01-21", "Chưa thanh toán"});
    }

    private void loadSampleDetails() {
        sampleDetail.put("DH001", new Object[][]{
                {"Coca", "SP01", 2, 12000, 24000},
                {"Snack", "SP03", 1, 15000, 15000}
        });
        sampleDetail.put("DH002", new Object[][]{
                {"Sting", "SP02", 1, 10000, 10000},
                {"Mì ly", "SP05", 2, 14000, 28000}
        });
        sampleDetail.put("DH003", new Object[][]{
                {"Nước suối", "SP04", 3, 7000, 21000}
        });
        sampleDetail.put("DH004", new Object[][]{
                {"Redbull", "SP06", 1, 18000, 18000},
                {"Snack tôm", "SP08", 2, 12000, 24000}
        });
        sampleDetail.put("DH005", new Object[][]{
                {"Coca", "SP01", 1, 12000, 12000},
                {"Mì xúc xích", "SP10", 1, 20000, 20000}
        });
    }

    //------------------ ORDER DETAIL LOGIC ------------------
    private void loadOrderDetails() {
        int row = orderTable.getSelectedRow();
        if (row < 0) return;

        String maDon = orderModel.getValueAt(row, 0).toString();

        lblMaDon.setText("Mã Đơn: " + maDon);
        lblSoMay.setText("Số Máy: " + orderModel.getValueAt(row, 1));
        lblNgay.setText("Ngày Tạo: " + orderModel.getValueAt(row, 2));
        lblTrangThai.setText("Trạng Thái: " + orderModel.getValueAt(row, 3));

        detailModel.setRowCount(0);
        int total = 0;

        if (sampleDetail.containsKey(maDon)) {
            for (Object[] d : sampleDetail.get(maDon)) {
                detailModel.addRow(d);
                total += Integer.parseInt(d[4].toString());
            }
        }

        lblTongTien.setText("Tổng tiền: " + total + " VNĐ");
    }

    private void addDetail() {
        JTextField txtTen = new JTextField();
        JTextField txtMa = new JTextField();
        JTextField txtSL = new JTextField();
        JTextField txtGia = new JTextField();

        Object[] form = {
                "Tên SP:", txtTen,
                "Mã SP:", txtMa,
                "Số lượng:", txtSL,
                "Giá:", txtGia
        };

        int option = JOptionPane.showConfirmDialog(this, form, "THÊM SẢN PHẨM", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int sl = Integer.parseInt(txtSL.getText());
                int gia = Integer.parseInt(txtGia.getText());
                int thanhTien = sl * gia;

                detailModel.addRow(new Object[]{
                        txtTen.getText(),
                        txtMa.getText(),
                        sl,
                        gia,
                        thanhTien
                });

                updateTotal();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
            }
        }
    }

    private void editDetail() {
        int row = orderDetailTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn sản phẩm cần sửa!");
            return;
        }

        JTextField txtTen = new JTextField(detailModel.getValueAt(row, 0).toString());
        JTextField txtMa = new JTextField(detailModel.getValueAt(row, 1).toString());
        JTextField txtSL = new JTextField(detailModel.getValueAt(row, 2).toString());
        JTextField txtGia = new JTextField(detailModel.getValueAt(row, 3).toString());

        Object[] form = {
                "Tên SP:", txtTen,
                "Mã SP:", txtMa,
                "Số lượng:", txtSL,
                "Giá:", txtGia
        };

        int option = JOptionPane.showConfirmDialog(this, form, "SỬA SẢN PHẨM", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int sl = Integer.parseInt(txtSL.getText());
                int gia = Integer.parseInt(txtGia.getText());
                int thanhTien = sl * gia;

                detailModel.setValueAt(txtTen.getText(), row, 0);
                detailModel.setValueAt(txtMa.getText(), row, 1);
                detailModel.setValueAt(sl, row, 2);
                detailModel.setValueAt(gia, row, 3);
                detailModel.setValueAt(thanhTien, row, 4);

                updateTotal();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
            }
        }
    }

    private void deleteDetail() {
        int row = orderDetailTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn sản phẩm cần xóa!");
            return;
        }

        String maDon = orderModel.getValueAt(orderTable.getSelectedRow(), 0).toString();
        if (sampleDetail.containsKey(maDon)) {
            Object[][] old = sampleDetail.get(maDon);
            Object[][] newArr = new Object[old.length - 1][5];
            int idx = 0;
            for (int i = 0; i < old.length; i++) {
                if (i != row) newArr[idx++] = old[i];
            }
            sampleDetail.put(maDon, newArr);
        }

        detailModel.removeRow(row);
        updateTotal();
    }

    private void updateTotal() {
        int total = 0;
        for (int i = 0; i < detailModel.getRowCount(); i++) {
            total += Integer.parseInt(detailModel.getValueAt(i, 4).toString());
        }
        lblTongTien.setText("Tổng tiền: " + total + " VNĐ");
    }

    //------------------ LEFT BUTTON LOGIC ------------------
    private void addOrder() {
        JTextField txtMa = new JTextField("DH" + String.format("%03d", orderModel.getRowCount() + 1));
        JTextField txtSoMay = new JTextField();
        JTextField txtNgay = new JTextField();
        String[] trangThaiOptions = {"Đang xử lý", "Chưa thanh toán", "Đã thanh toán"};
        JComboBox<String> cmbTrangThai = new JComboBox<>(trangThaiOptions);

        Object[] form = {
                "Mã ĐH:", txtMa,
                "Số Máy:", txtSoMay,
                "Ngày Tạo (yyyy-MM-dd):", txtNgay,
                "Trạng Thái:", cmbTrangThai
        };

        int option = JOptionPane.showConfirmDialog(this, form, "THÊM ĐƠN HÀNG", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            orderModel.addRow(new Object[]{
                    txtMa.getText().trim(),
                    txtSoMay.getText().trim(),
                    txtNgay.getText().trim(),
                    cmbTrangThai.getSelectedItem().toString()
            });
            JOptionPane.showMessageDialog(this, "Đơn hàng mới đã được thêm!");
        }
    }

    private void editOrder() {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn đơn hàng cần sửa!");
            return;
        }

        String soMayCurrent = orderModel.getValueAt(row, 1).toString();
        String ngayCurrent = orderModel.getValueAt(row, 2).toString();
        String trangThaiCurrent = orderModel.getValueAt(row, 3).toString();

        JTextField txtSoMay = new JTextField(soMayCurrent);
        JTextField txtNgay = new JTextField(ngayCurrent);
        String[] trangThaiOptions = {"Đang xử lý", "Chưa thanh toán", "Đã thanh toán"};
        JComboBox<String> cmbTrangThai = new JComboBox<>(trangThaiOptions);
        cmbTrangThai.setSelectedItem(trangThaiCurrent);

        Object[] form = {
                "Số Máy:", txtSoMay,
                "Ngày Tạo (yyyy-MM-dd):", txtNgay,
                "Trạng Thái:", cmbTrangThai
        };

        int option = JOptionPane.showConfirmDialog(this, form, "SỬA ĐƠN HÀNG", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            orderModel.setValueAt(txtSoMay.getText().trim(), row, 1);
            orderModel.setValueAt(txtNgay.getText().trim(), row, 2);
            orderModel.setValueAt(cmbTrangThai.getSelectedItem().toString(), row, 3);
            if (orderTable.getSelectedRow() == row) {
                loadOrderDetails();
            }
            JOptionPane.showMessageDialog(this, "Đơn hàng đã được cập nhật!");
        }
    }

    private void deleteOrder() {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn đơn hàng để xóa!");
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Xóa đơn hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION) {
            String maDon = orderModel.getValueAt(row, 0).toString();
            sampleDetail.remove(maDon);
            orderModel.removeRow(row);
            detailModel.setRowCount(0);
            updateTotal();
        }
    }

    private void payOrder() {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn đơn hàng để thanh toán!");
            return;
        }

        orderModel.setValueAt("Đã thanh toán", row, 3);
        JOptionPane.showMessageDialog(this, "Đơn hàng đã được thanh toán!");
        loadOrderDetails();
    }

    private void searchOrder() {
        String key = txtSearch.getText().trim().toLowerCase();
        for (int i = 0; i < orderModel.getRowCount(); i++) {
            String maDon = orderModel.getValueAt(i, 0).toString().toLowerCase();
            if (maDon.contains(key)) {
                orderTable.setRowSelectionInterval(i, i);
                orderTable.scrollRectToVisible(orderTable.getCellRect(i, 0, true));
                loadOrderDetails();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Không tìm thấy đơn hàng!");
    }
}
