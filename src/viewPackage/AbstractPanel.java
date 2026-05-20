package viewPackage;

import javax.swing.*;

public abstract class AbstractPanel extends JPanel {

    protected MainJFrame mainWindow;

    public AbstractPanel(MainJFrame mainWindow) {
        this.mainWindow = mainWindow;
    }

    protected JButton createBackButton() {

        JButton backButton = new JButton("← Retour");

        backButton.setFocusPainted(false);

        backButton.addActionListener(event -> mainWindow.goBack());

        return backButton;
    }
}
