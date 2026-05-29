import com.formdev.flatlaf.FlatLightLaf;
import viewPackage.MainJFrame;
import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.FRENCH);

        setupLookAndFeel();

        SwingUtilities.invokeLater(() -> {
            MainJFrame frame = new MainJFrame();
            frame.setVisible(true);
        });
    }

    private static void setupLookAndFeel() {
        try {
            FlatLightLaf.setup();
            /*
            UIManager.put("defaultFont", AppTheme.TEXT_FONT);
            UIManager.put("Label.font", AppTheme.TEXT_FONT);
            UIManager.put("Button.font", AppTheme.BUTTON_FONT);
            UIManager.put("Table.font", AppTheme.TEXT_FONT);
            UIManager.put("TableHeader.font", AppTheme.TEXT_BOLD_FONT);
            UIManager.put("OptionPane.messageFont", AppTheme.TEXT_FONT);
            UIManager.put("OptionPane.buttonFont", AppTheme.BUTTON_FONT);
             */
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            UIManager.put("TextComponent.arc", 15);

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    null,
                    "Impossible de charger le thème graphique de l'application.",
                    "Erreur de démarrage",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}