package viewPackage.ui;

import viewPackage.MainJFrame;

import javax.swing.*;
import java.awt.*;

public final class HeaderFactory {

    private HeaderFactory() {
        // Utility class
    }

    public static JPanel createHeader(MainJFrame mainWindow, boolean showBackButton) {
        JPanel headerPanel = new JPanel(new BorderLayout());

        headerPanel.setBackground(AppTheme.NAVBAR);
        headerPanel.setPreferredSize(new Dimension(0, AppTheme.HEADER_HEIGHT));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(
                0,
                AppTheme.HEADER_HORIZONTAL_PADDING,
                0,
                AppTheme.HEADER_HORIZONTAL_PADDING
        ));

        headerPanel.add(createLeftPanel(mainWindow, showBackButton), BorderLayout.WEST);
        headerPanel.add(createTitleLabel(), BorderLayout.CENTER);
        headerPanel.add(createRightPlaceholder(), BorderLayout.EAST);

        return headerPanel;
    }

    private static JPanel createLeftPanel(MainJFrame mainWindow, boolean showBackButton) {
        JPanel leftPanel = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                0,
                (AppTheme.HEADER_HEIGHT - AppTheme.BACK_BUTTON_SIZE.height) / 2
        ));

        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(160, AppTheme.HEADER_HEIGHT));

        if (showBackButton) {
            JButton backButton = ButtonFactory.createBackButton(mainWindow::goBack);
            leftPanel.add(backButton);
        }

        return leftPanel;
    }

    private static JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Gestion du restaurant");

        titleLabel.setFont(AppTheme.NAVBAR_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);

        return titleLabel;
    }

    private static JPanel createRightPlaceholder() {
        JPanel rightPanel = new JPanel();

        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(160, AppTheme.HEADER_HEIGHT));

        return rightPanel;
    }
}