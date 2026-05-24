package viewPackage.ui;

import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.awt.*;

public class LoadingHelper {

    public static void showLoading(JPanel panel, String message) {
        panel.removeAll();

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(AppTheme.TEXT_SECONDARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(30));
        panel.add(label);

        panel.revalidate();
        panel.repaint();
    }

    public static void showEmpty(JPanel panel, String message) {
        panel.removeAll();

        JPanel emptyPanel = new JPanel(new BorderLayout());
        emptyPanel.setOpaque(false);
        emptyPanel.setPreferredSize(new Dimension(900, 260));
        emptyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(AppTheme.SUBTITLE_FONT);
        label.setForeground(AppTheme.TEXT_SECONDARY);

        emptyPanel.add(label, BorderLayout.CENTER);

        panel.add(emptyPanel);

        panel.revalidate();
        panel.repaint();
    }
}