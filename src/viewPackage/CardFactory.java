package viewPackage;

import viewPackage.stylePackage.AppTheme;

import javax.swing.*;
import java.awt.*;

public class CardFactory {

    public JButton createRoleCard(String icon, String title, String description, Runnable action) {
        JButton card = new RoundedButton(20);

        card.setLayout(new BorderLayout(8, 8));
        card.setBackground(AppTheme.CARD);
        card.setFocusPainted(false);

        card.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        18,
                        16,
                        18
                )
        );

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);

        iconLabel.setFont(new Font("Segoe UI Emoji",Font.PLAIN,22));

        iconLabel.setForeground(AppTheme.PRIMARY);

        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));

        iconPanel.setOpaque(false);

        iconPanel.add(iconLabel);

        JLabel titleLabel = new JLabel(
                title,
                SwingConstants.CENTER
        );

        titleLabel.setFont(AppTheme.CARD_TITLE_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel descriptionLabel = new JLabel(
                description,
                SwingConstants.CENTER
        );

        descriptionLabel.setFont(AppTheme.CARD_DESCRIPTION_FONT);
        descriptionLabel.setForeground(AppTheme.TEXT_SECONDARY);

        JPanel centerPanel = new JPanel(
                new GridLayout(
                        2,
                        1,
                        0,
                        8
                )
        );

        centerPanel.setOpaque(false);

        centerPanel.add(titleLabel);
        centerPanel.add(descriptionLabel);

        card.add(iconPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);

        card.addActionListener(event -> action.run());

        return card;
    }

    public JButton createSecondaryButton(String text, Runnable action) {
        JButton button = new JButton(text);

        button.setPreferredSize(AppTheme.SECONDARY_BUTTON_SIZE);
        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(AppTheme.PRIMARY);
        button.setBackground(AppTheme.CARD);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.putClientProperty(
                "FlatLaf.style",
                "arc:18;" +
                        "borderWidth:1;" +
                        "borderColor:#AFC6FF;" +
                        "focusWidth:0;" +
                        "innerFocusWidth:0"
        );

        button.addActionListener(event -> action.run());

        return button;
    }

    private static class RoundedButton extends JButton {
        private final int arc;

        private boolean hovered;

        public RoundedButton(int arc) {
            this.arc = arc;

            hovered = false;

            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent event) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent event) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            // Hover background
            if (hovered) {

                g2.setColor(new Color(245, 248, 255));

            } else {

                g2.setColor(getBackground());
            }

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    arc,
                    arc
            );

            g2.setColor(new Color(70, 120, 240, 60));

            g2.setStroke(new BasicStroke(1));

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    arc,
                    arc
            );

            g2.dispose();

            super.paintComponent(graphics);
        }
    }
}