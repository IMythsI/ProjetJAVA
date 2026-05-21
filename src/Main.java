import com.formdev.flatlaf.*;
import viewPackage.MainJFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {

            UIManager.setLookAndFeel(new FlatLightLaf());

            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            UIManager.put("TextComponent.arc", 15);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            MainJFrame frame = new MainJFrame();
            frame.setVisible(true);
        });
    }
}
