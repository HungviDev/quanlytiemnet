package admin.View; 
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        
 
        final DashboardUI dashboard = new DashboardUI();
        

        new Thread(() -> {
   
            servercontrol.startServer(dashboard); 
        }).start();
        
     
        SwingUtilities.invokeLater(() -> {
            dashboard.setVisible(true);
        });
    }
}