package admin.View;

import Connection.DatabaseConnection;
import admin.DAO.AccountDAO;
import admin.Model.AccountModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Account extends JPanel {

    private static final Color BG = new Color(245, 245, 245);
    private static final Font FN = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FB = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FT = new Font("Segoe UI", Font.BOLD, 20);

    private JTable table;
    private DefaultTableModel model;
    private java.util.List<AccountModel> cachedList = new ArrayList<>();
    private Timer timer;
    private static final int RATE = 10000; // 10000 VND = 1 hour
    private AccountDAO accountDAO;
    private Connection connection ;


    public Account() {
          try {
            connection = DatabaseConnection.getConnection();
            accountDAO = new AccountDAO(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }
        setLayout(new BorderLayout(10, 10));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(header(), BorderLayout.NORTH);
        add(tablePanel(), BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);

        startTimer();
    }

    // ================= HEADER =================
    private JPanel header() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG);

        JLabel title = new JLabel("Quản Lý Tài Khoản Người Dùng");
        title.setFont(FT);
        p.add(title, BorderLayout.WEST);

        return p;
    }

    // ================= TABLE =================
    private JPanel tablePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);

        String[] cols = {"STT", "Tài khoản", "Mật khẩu", "Thời gian còn lại", "Thời gian tạo"};
        model = new DefaultTableModel(cols, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    loadAccountTable(); // LOAD DATA
        
        table = new JTable(model);
        table.setFont(FN);
        table.setModel(model);
        table.setRowHeight(30);
        styleHeader();

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void styleHeader() {
        table.getTableHeader().setFont(FB);
        table.getTableHeader().setBackground(new Color(70,130,180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220,220,220));
    }

    // ================= BUTTONS =================
    private JPanel buttonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        p.setBackground(BG);

        p.add(btn("THÊM", new Color(52,152,219), "/img/add.png", e -> addAcc()));
        p.add(btn("SỬA", new Color(46,204,113), "/img/edit.png", e -> editAcc()));
        p.add(btn("XÓA", new Color(231,76,60), "/img/delete.png", e -> deleteAcc()));
        return p;
    }

    private JButton btn(String text, Color color, String icon, java.awt.event.ActionListener ac) {
        JButton b = new JButton(text, loadIcon(icon));
        b.setFont(FB);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(130, 40));
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.addActionListener(ac);

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(color.darker()); }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(color); }
        });
        return b;
    }

    // =============== ICON ===============
    private ImageIcon loadIcon(String path){
        try {
            var res = getClass().getResource(path);
            if(res == null) return null;
            BufferedImage img = ImageIO.read(res);
            return new ImageIcon(img.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        } catch(IOException e){ return null; }
    }

    // ================= EVENTS =================
    private void addAcc() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        AccountForm form = new AccountForm(SwingUtilities.getWindowAncestor(this), "Thêm tài khoản", true,
                "", "", now);
        if(form.showDialog()){
            long amount = form.getAmount(); // VND
            try (Connection conn = DatabaseConnection.getConnection()){
                AccountDAO dao = new AccountDAO(conn);
                AccountModel a = new AccountModel(form.getUsername(), form.getPassword(), amount, form.getCreated_at());
                if(dao.insertAccount(a)){
                    refreshAcc();
                } else {
                    msg("Lỗi khi lưu vào database.");
                }
            } catch(Exception e){ e.printStackTrace(); msg("Kết nối DB thất bại."); }
        }
    }

    private void editAcc() {
        int r = table.getSelectedRow();
        if(r < 0){
            msg("Vui lòng chọn tài khoản!");
            return;
        }

        AccountModel selected = cachedList.get(r);
        int userId = selected.getUserId();
        String oldUsername = selected.getUsername();
        AccountForm form = new AccountForm(
            SwingUtilities.getWindowAncestor(this),
            "Chỉnh sửa tài khoản",
            true,
            oldUsername,
            model.getValueAt(r,2).toString(),
            model.getValueAt(r,4).toString()
        );

        if(form.showDialog()){
            String newUsername = form.getUsername();
            String password = form.getPassword();
            long addedAmount = form.getAmount();
            try (Connection conn = DatabaseConnection.getConnection()){
                    AccountDAO dao = new AccountDAO(conn);
                    boolean okRename = true;
                    if(!oldUsername.equals(newUsername)){
                        okRename = dao.updateUsername(userId, newUsername);
                    }
                    boolean ok1 = dao.updatePassword(userId, password);
                    boolean ok2 = dao.addBalance(userId, addedAmount);
                    if(okRename && (ok1 || ok2)){
                        // refresh row data from DB
                        refreshAcc();
                    } else {
                        msg("Cập nhật thất bại.");
                    }
            } catch(Exception e){ e.printStackTrace(); msg("Kết nối DB thất bại."); }
        }
    }

    private void deleteAcc(){
        int r = table.getSelectedRow();
        if(r < 0){ msg("Vui lòng chọn tài khoản!"); return; }

        if(JOptionPane.showConfirmDialog(this,"Xóa tài khoản?","Xác nhận",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            AccountModel selected = cachedList.get(r);
            int userId = selected.getUserId();
            try (Connection conn = DatabaseConnection.getConnection()){
                AccountDAO dao = new AccountDAO(conn);
                if(dao.deleteById(userId)){
                    refreshAcc();
                } else {
                    msg("Xóa thất bại trên DB.");
                }
            } catch(Exception e){ e.printStackTrace(); msg("Kết nối DB thất bại."); }
        }
    }

    private void refreshAcc() {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()){
            AccountDAO dao = new AccountDAO(conn);
            java.util.ArrayList<AccountModel> list = dao.getAllAccounts();
            cachedList = list;
            for(int i = 0; i < list.size(); i++){
                AccountModel a = list.get(i);
                long sec = (a.getBalance() / RATE) * 3600;
                model.addRow(new Object[]{
                    i + 1,
                    a.getUsername(),
                    a.getPassword(),
                    format(sec),
                    a.getCreatedAt()
                });
            }
        } catch(Exception e){ e.printStackTrace(); msg("Không thể tải dữ liệu từ DB."); }
    }

    private void msg(String m){ JOptionPane.showMessageDialog(this, m); }
    private void startTimer() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override public void run(){ countdown(); }
        },1000,1000);
    }

    private void countdown() {
    SwingUtilities.invokeLater(() -> {
        for (int i = 0; i < model.getRowCount(); i++) {
            int userId = Integer.parseInt(model.getValueAt(i, 0).toString());
            if (!accountDAO.checkStatusByUserId(userId)) {
                continue; //
            }
            long sec = toSec(model.getValueAt(i, 3).toString());

            if (sec > 0) {
                model.setValueAt(format(sec - 1), i, 3);
            } else {
                model.setValueAt("Hết giờ", i, 3);
            }
        }
    });
}



    // ================= UTIL =================
    private long toSec(String t) {
        try {
            String[] p = t.split(":");
            return Long.parseLong(p[0]) * 3600 +
                   Long.parseLong(p[1]) * 60 +
                   Long.parseLong(p[2]);
        } catch(Exception e){ return 0; }
    }

    private String format(long sec){
        long h = sec / 3600;
        long m = (sec % 3600) / 60;
        long s = sec % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    public void stopTimer(){
        if(timer != null) timer.cancel();
    }
    public void loadAccountTable() {
        refreshAcc();
}
}
