package viewPackage.ui;

import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.awt.*;

public class ButtonFactory {

    public static JButton createPrimaryButton(String text, Runnable action) {
        JButton button = new JButton(text);

        button.setPreferredSize(new Dimension(190, 42));
        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(AppTheme.PRIMARY);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");

        button.addActionListener(event -> action.run());

        return button;
    }

    public static JButton createSecondaryButton(String text, Runnable action) {
        JButton button = new JButton(text);

        button.setPreferredSize(new Dimension(150, 42));
        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(AppTheme.PRIMARY);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");

        button.addActionListener(event -> action.run());

        return button;
    }

    public static JButton createBackButton(Runnable action) {
        JButton button = new JButton("← Retour");

        button.setPreferredSize(new Dimension(110, 36));
        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(AppTheme.TEXT_PRIMARY);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");

        button.addActionListener(event -> action.run());

        return button;
    }

    public static JButton createSmallIconButton(String text, Runnable action) {
        JButton button = new JButton(text);

        button.setPreferredSize(new Dimension(46, 34));
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setForeground(AppTheme.PRIMARY);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");

        button.addActionListener(event -> action.run());

        return button;
    }
}