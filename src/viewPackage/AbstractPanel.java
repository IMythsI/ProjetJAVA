package viewPackage;

import viewPackage.stylePackage.AppTheme;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractPanel extends JPanel {

    protected MainJFrame mainWindow;

    public AbstractPanel(MainJFrame mainWindow) {
        this.mainWindow = mainWindow;
    }

    protected JButton createBackButton() {
        JButton backButton = new JButton("← Retour");

        backButton.setPreferredSize(new Dimension(110, 36));
        backButton.setFont(new Font("Arial", Font.BOLD, 13));
        backButton.setForeground(AppTheme.TEXT_PRIMARY);
        backButton.setBackground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.putClientProperty("JButton.buttonType", "roundRect");

        backButton.addActionListener(event -> mainWindow.goBack());

        return backButton;
    }
}
