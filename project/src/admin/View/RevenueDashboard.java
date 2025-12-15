package admin.View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
public class RevenueDashboard extends JPanel {
    private static final Color CARD = new Color(178, 222, 224);
    private static final Font TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font VALUE = new Font("Segoe UI", Font.BOLD, 26);

    public RevenueDashboard() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ===== CARDS =====
        content.add(card("💰 Tổng doanh thu bán ra", "226,585,000đ"));
        content.add(Box.createVerticalStrut(15));
        content.add(card("💼 Tổng kinh phí nhập hàng", "410,599,000đ"));
        content.add(Box.createVerticalStrut(15));
        content.add(card("📈 Tổng lợi nhuận", "-184,014,000đ"));

        content.add(Box.createVerticalStrut(30));

        // ===== CHART =====
        content.add(chartPanel());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    // ================= CARD =================
    private JPanel card(String title, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        p.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lbTitle = new JLabel(title);
        lbTitle.setFont(TITLE);

        JLabel lbValue = new JLabel(value);
        lbValue.setFont(VALUE);

        p.add(lbTitle, BorderLayout.NORTH);
        p.add(lbValue, BorderLayout.CENTER);

        return p;
    }

    // ================= CHART =================
    private JPanel chartPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Giả lập dữ liệu doanh thu theo tháng
        dataset.addValue(30, "Doanh thu", "T1");
        dataset.addValue(45, "Doanh thu", "T2");
        dataset.addValue(55, "Doanh thu", "T3");
        dataset.addValue(40, "Doanh thu", "T4");
        dataset.addValue(70, "Doanh thu", "T5");
        dataset.addValue(90, "Doanh thu", "T6");

        JFreeChart chart = ChartFactory.createLineChart(
                "Doanh thu theo tháng",
                "Tháng",
                "Triệu VND",
                dataset);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(800, 350));

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Color.WHITE);
        wrap.add(panel, BorderLayout.CENTER);

        return wrap;
    }

    // ================= TEST =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("THỐNG KÊ DOANH THU");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1000, 700);
            f.setLocationRelativeTo(null);
            f.setContentPane(new RevenueDashboard());
            f.setVisible(true);
        });
    }
}
