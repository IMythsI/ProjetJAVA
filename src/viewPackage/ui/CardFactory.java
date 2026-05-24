package viewPackage.ui;

import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.awt.*;

public class CardFactory {

    public static JPanel createCard(int width, int height) {
        JPanel card = new JPanel(new BorderLayout());

        card.setBackground(AppTheme.CARD);
        card.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        card.setPreferredSize(new Dimension(width, height));
        card.putClientProperty("FlatLaf.style", "arc:20");

        return card;
    }

    public static JPanel createAdaptiveCard(int width, int minHeight) {
        JPanel card = new JPanel(new BorderLayout());

        card.setBackground(AppTheme.CARD);
        card.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        card.setPreferredSize(new Dimension(width, minHeight));
        card.setMinimumSize(new Dimension(width, minHeight));
        card.putClientProperty("FlatLaf.style", "arc:20");

        return card;
    }

    public static JButton createCardButton(String icon, String title, String description, Runnable action) {
        JButton card = new RoundedButton("", 20);

        card.setLayout(new BorderLayout(8, 8));
        card.setBackground(AppTheme.CARD);
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 16, 18));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setPreferredSize(new Dimension(35, 35));
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        iconLabel.setForeground(AppTheme.PRIMARY);
        iconLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconPanel.setOpaque(false);
        iconPanel.add(iconLabel);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(AppTheme.CARD_TITLE_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel descriptionLabel = new JLabel(description, SwingConstants.CENTER);
        descriptionLabel.setFont(AppTheme.CARD_DESCRIPTION_FONT);
        descriptionLabel.setForeground(AppTheme.TEXT_SECONDARY);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        centerPanel.setOpaque(false);
        centerPanel.add(titleLabel);
        centerPanel.add(descriptionLabel);

        card.add(iconPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);

        card.addActionListener(event -> action.run());

        return card;
    }
}