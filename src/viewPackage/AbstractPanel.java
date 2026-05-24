package viewPackage;

import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractPanel extends JPanel {

    protected MainJFrame mainWindow;

    public AbstractPanel(MainJFrame mainWindow) {
        this.mainWindow = mainWindow;
    }

    protected JButton createBackButton() {
        return viewPackage.ui.ButtonFactory.createBackButton(
                () -> mainWindow.goBack()
        );
    }
}
