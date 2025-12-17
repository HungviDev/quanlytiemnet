package user.View;

import javax.swing.*;
import java.awt.*;

public class LockScreen extends JFrame {

    public LockScreen() {
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JLabel label = new JLabel("🔒 MÁY ĐANG BỊ KHÓA", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 40));
        label.setForeground(Color.WHITE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);
        panel.add(label);

        setContentPane(panel);
    }
}
