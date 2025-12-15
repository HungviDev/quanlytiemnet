package admin.View; // Giả sử DashboardUI và servercontrol đều ở đây

import java.awt.*;
import javax.swing.*;

public class AccountForm extends JDialog {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JTextField tfAmount;
    private JTextField tfCreated_at;

    private boolean saved = false;
    private static final int RATE = 10000;

    public AccountForm(Window owner, String title, boolean modal,
                       String username, String password, String created_at) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        initComponents(username, password, created_at);
    }

    private void initComponents(String username, String password, String created_at) {
        tfUsername = new JTextField(username, 20);
        pfPassword = new JPasswordField(password, 20);
        tfAmount = new JTextField("", 20);
        tfCreated_at = new JTextField(created_at, 20);
        tfCreated_at.setEditable(false);

        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        btnSave.addActionListener(e -> {
                if (tfUsername.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(AccountForm.this, "Tên tài khoản không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (tfAmount.getText().trim().isEmpty() || !tfAmount.getText().matches("\\d+")) {
                    JOptionPane.showMessageDialog(AccountForm.this, "Số tiền phải là số nguyên dương", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                saved = true;
                setVisible(false);
        });

        btnCancel.addActionListener(e -> {
            saved = false;
            setVisible(false);
        });

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;

        gc.gridx = 0; gc.gridy = 0; form.add(new JLabel("Tài khoản:"), gc);
        gc.gridx = 1; form.add(tfUsername, gc);

        gc.gridx = 0; gc.gridy++; form.add(new JLabel("Mật khẩu:"), gc);
        gc.gridx = 1; form.add(pfPassword, gc);

        gc.gridx = 0; gc.gridy++; form.add(new JLabel("Số tiền nạp (VND):"), gc);
        gc.gridx = 1; form.add(tfAmount, gc);
        tfAmount.setText("0");

        gc.gridx = 0; gc.gridy++; form.add(new JLabel("Thời gian tạo:"), gc);
        gc.gridx = 1; form.add(tfCreated_at, gc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(btnCancel);
        buttons.add(btnSave);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);

        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());
    }

    public boolean showDialog() {
        setVisible(true);
        return saved;
    }

    public String getUsername() { return tfUsername.getText().trim(); }
    public String getPassword() { return new String(pfPassword.getPassword()); }
    public long getHours() { 
        long amount = Long.parseLong(tfAmount.getText().trim());
        return amount / RATE;
    }
    public long getAmount() {
        try {
            
            return Long.parseLong(tfAmount.getText().trim());
        } catch(Exception e) { return 0; }
    }
    public String getCreated_at() { return tfCreated_at.getText().trim(); }
}
