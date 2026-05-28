package viewPackage.ui;

import javax.swing.*;
import java.awt.*;

public final class CardFactory {

    private CardFactory() {
        // Utility class
    }

    public static JPanel createCard(int width, int height) {
        JPanel card = new RoundedCardPanel(AppTheme.CARD_ARC);

        card.setLayout(new BorderLayout());
        card.setBackground(AppTheme.CARD);
        card.setBorder(BorderFactory.createEmptyBorder(
                AppTheme.CARD_PADDING_TOP,
                AppTheme.CARD_PADDING_LEFT,
                AppTheme.CARD_PADDING_BOTTOM,
                AppTheme.CARD_PADDING_RIGHT
        ));

        card.setPreferredSize(new Dimension(width, height));
        card.setMinimumSize(new Dimension(300, Math.min(height, 250)));

        return card;
    }

    public static JPanel createAdaptiveCard(int maxWidth, int minHeight) {
        JPanel card = new RoundedCardPanel(AppTheme.CARD_ARC);

        card.setLayout(new BorderLayout());
        card.setBackground(AppTheme.CARD);
        card.setBorder(BorderFactory.createEmptyBorder(
                AppTheme.CARD_PADDING_TOP,
                AppTheme.CARD_PADDING_LEFT,
                AppTheme.CARD_PADDING_BOTTOM,
                AppTheme.CARD_PADDING_RIGHT
        ));

        card.setPreferredSize(new Dimension(maxWidth, minHeight));
        card.setMinimumSize(new Dimension(300, minHeight));

        return card;
    }

    public static JButton createCardButton(String icon,
                                           String title,
                                           String description,
                                           Runnable action) {
        JButton card = new RoundedButton("", AppTheme.CARD_ARC);

        card.setLayout(new BorderLayout(
                AppTheme.COMPONENT_GAP_SMALL,
                AppTheme.COMPONENT_GAP_SMALL
        ));

        card.setBackground(AppTheme.CARD);
        card.setForeground(AppTheme.TEXT_PRIMARY);
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 16, 18));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(AppTheme.ROLE_CARD_SIZE);
        card.setMinimumSize(new Dimension(200, 120));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setPreferredSize(new Dimension(35, 35));
        iconLabel.setFont(AppTheme.EMOJI_FONT);
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

        if (action != null) {
            card.addActionListener(event -> action.run());
        }

        return card;
    }

    private static class RoundedCardPanel extends JPanel {

        private final int arc;

        public RoundedCardPanel(int arc) {
            this.arc = arc;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();

            graphics2D.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int inset = 2;
            int width = getWidth() - inset * 2;
            int height = getHeight() - inset * 2;

            graphics2D.setColor(getBackground());
            graphics2D.fillRoundRect(
                    inset,
                    inset,
                    width,
                    height,
                    arc,
                    arc
            );

            graphics2D.dispose();

            super.paintComponent(graphics);
        }

        @Override
        protected void paintBorder(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();

            graphics2D.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int inset = 2;
            int width = getWidth() - inset * 2;
            int height = getHeight() - inset * 2;

            graphics2D.setColor(AppTheme.BORDER);
            graphics2D.drawRoundRect(
                    inset,
                    inset,
                    width,
                    height,
                    arc,
                    arc
            );

            graphics2D.dispose();
        }
    }
}