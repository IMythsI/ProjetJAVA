import viewPackage.MainJFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception exception) {
                System.out.println("Look and feel non chargé");
            }

            MainJFrame window = new MainJFrame();
            window.setVisible(true);
        });
    }
}
