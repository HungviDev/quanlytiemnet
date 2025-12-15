package admin.View;

import java.awt.*;
import javax.swing.*;

public class StorageForm extends JDialog {

    private JTextField tfName;
    private JTextField tfUnit;
    private JTextField tfPrice;
    private JTextArea taDescription;
    private JComboBox<String> cbCategory;

    private boolean saved = false;

    public StorageForm(Window owner, String title, boolean modal,
            String name, String unit, String price,
            String description, String category) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        initComponents(name, unit, price, description, category);
    }

    private void initComponents(String name, String unit, String price,
            String description, String category) {

        tfName = new JTextField(name, 20);
        tfUnit = new JTextField(unit, 20);
        tfPrice = new JTextField(price, 20);

        taDescription = new JTextArea(description, 3, 20);
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);

        cbCategory = new JComboBox<>(new String[] {
                "Internet", "Drink", "Food", "Office", "Other"
        });
        cbCategory.setSelectedItem(category);

        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        btnSave.addActionListener(e -> {
            if (tfName.getText().trim().isEmpty()) {
                msg("Tên dịch vụ không được để trống");
                return;
            }
            if (tfUnit.getText().trim().isEmpty()) {
                msg("Đơn vị không được để trống");
                return;
            }
            if (!tfPrice.getText().trim().matches("\\d+")) {
                msg("Giá phải là số nguyên dương");
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

        gc.gridx = 0;
        gc.gridy = 0;
        form.add(new JLabel("Tên dịch vụ:"), gc);
        gc.gridx = 1;
        form.add(tfName, gc);

        gc.gridx = 0;
        gc.gridy++;
        form.add(new JLabel("Đơn vị:"), gc);
        gc.gridx = 1;
        form.add(tfUnit, gc);

        gc.gridx = 0;
        gc.gridy++;
        form.add(new JLabel("Giá:"), gc);
        gc.gridx = 1;
        form.add(tfPrice, gc);

        gc.gridx = 0;
        gc.gridy++;
        form.add(new JLabel("Danh mục:"), gc);
        gc.gridx = 1;
        form.add(cbCategory, gc);

        gc.gridx = 0;
        gc.gridy++;
        form.add(new JLabel("Mô tả:"), gc);
        gc.gridx = 1;
        form.add(new JScrollPane(taDescription), gc);

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

    private void msg(String m) {
        JOptionPane.showMessageDialog(this, m, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    // ================= GETTER =================
    public boolean showDialog() {
        setVisible(true);
        return saved;
    }

    public String getName() {
        return tfName.getText().trim();
    }

    public String getQuantity_unit() {
        return tfUnit.getText().trim();
    }

    public long getPrice() {
        return Long.parseLong(tfPrice.getText().trim());
    }

    public String getDescription() {
        return taDescription.getText().trim();
    }

    public String getCategory() {
        return cbCategory.getSelectedItem().toString();
    }
}
