package viewPackage.ui;

import viewPackage.MainJFrame;
import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.awt.*;

public class HeaderFactory {

    public static JPanel createHeader(MainJFrame mainWindow, boolean showBackButton) {
        JPanel header = new JPanel(new BorderLayout());

        header.setPreferredSize(new Dimension(0, 70));
        header.setBackground(AppTheme.NAVBAR);
        header.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 30));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 17));
        leftPanel.setOpaque(false);

        if (showBackButton) {
            leftPanel.add(ButtonFactory.createBackButton(() -> mainWindow.goBack()));
        }

        JLabel title = new JLabel("Restaurant Manager");
        title.setForeground(Color.WHITE);
        title.setFont(AppTheme.NAVBAR_FONT);

        leftPanel.add(title);
        header.add(leftPanel, BorderLayout.WEST);

        return header;
    }
}