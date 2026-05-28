package viewPackage.ui;

import javax.swing.*;
import java.awt.*;

public final class ButtonFactory {

    private ButtonFactory() {
        // Utility class
    }

    public static JButton createPrimaryButton(String text, Runnable action) {
        JButton button = new RoundedButton(text, AppTheme.BUTTON_ARC);

        applyBaseButtonStyle(button, AppTheme.PRIMARY_BUTTON_SIZE);
        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(AppTheme.PRIMARY);

        addAction(button, action);

        return button;
    }

    public static JButton createSecondaryButton(String text, Runnable action) {
        JButton button = new RoundedButton(text, AppTheme.BUTTON_ARC);

        applyBaseButtonStyle(button, AppTheme.SECONDARY_BUTTON_SIZE);
        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(AppTheme.PRIMARY);
        button.setBackground(Color.WHITE);

        addAction(button, action);

        return button;
    }

    public static JButton createBackButton(Runnable action) {
        JButton button = new RoundedButton("← Retour", AppTheme.SMALL_BUTTON_ARC);

        applyBaseButtonStyle(button, AppTheme.BACK_BUTTON_SIZE);
        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(AppTheme.TEXT_PRIMARY);
        button.setBackground(Color.WHITE);

        addAction(button, action);

        return button;
    }

    public static JButton createSmallIconButton(String text, Runnable action) {
        JButton button = new RoundedButton(text, AppTheme.SMALL_BUTTON_ARC);

        applyBaseButtonStyle(button, AppTheme.SMALL_ICON_BUTTON_SIZE);
        button.setFont(AppTheme.SMALL_EMOJI_FONT);
        button.setForeground(AppTheme.PRIMARY);
        button.setBackground(Color.WHITE);

        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);

        addAction(button, action);

        return button;
    }

    private static void applyBaseButtonStyle(JButton button, Dimension preferredSize) {
        button.setPreferredSize(preferredSize);
        button.setMinimumSize(preferredSize);

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(0, 12, 0, 12));
    }

    private static void addAction(JButton button, Runnable action) {
        if (action != null) {
            button.addActionListener(event -> action.run());
        }
    }
}