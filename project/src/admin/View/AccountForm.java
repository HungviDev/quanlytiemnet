package admin.View;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AccountForm extends JDialog {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JTextField tfRemaining;
    private JTextField tfCreatedAt;

    private boolean saved = false;

    public AccountForm(Window owner, String title, boolean modal,
                       String username, String password, String remaining, String createdAt) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        initComponents(username, password, remaining, createdAt);
    }

    private void initComponents(String username, String password, String remaining, String createdAt) {
        tfUsername = new JTextField(username, 20);
        pfPassword = new JPasswordField(password, 20);
        tfRemaining = new JTextField(remaining, 20);
        tfCreatedAt = new JTextField(createdAt, 20);
        tfCreatedAt.setEditable(false);

        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tfUsername.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(AccountForm.this, "Tên tài khoản không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                saved = true;
                setVisible(false);
            }
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

        gc.gridx = 0; gc.gridy++; form.add(new JLabel("Thời gian :"), gc);
        gc.gridx = 1; form.add(tfRemaining, gc);

        gc.gridx = 0; gc.gridy++; form.add(new JLabel("Thời gian tạo:"), gc);
        gc.gridx = 1; form.add(tfCreatedAt, gc);

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
    public String getRemainingTime() { return tfRemaining.getText().trim(); }
    public String getCreatedAt() { return tfCreatedAt.getText().trim(); }
}
