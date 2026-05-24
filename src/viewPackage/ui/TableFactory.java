package viewPackage.ui;

import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.awt.*;

public class TableFactory {

    public static JPanel createTableCard(int width, int minHeight) {
        JPanel card = CardFactory.createAdaptiveCard(width, minHeight);
        card.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));
        return card;
    }

    public static JPanel createHeaderRow(String... columns) {
        JPanel header = new JPanel(new GridLayout(1, columns.length, 20, 0));

        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 25, 12, 25));
        header.setPreferredSize(new Dimension(800, 45));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        for (String column : columns) {
            JLabel label = new JLabel(column);
            label.setFont(AppTheme.BUTTON_FONT);
            label.setForeground(AppTheme.TEXT_SECONDARY);
            label.setHorizontalAlignment(SwingConstants.LEFT);
            header.add(label);
        }

        return header;
    }

    public static JPanel createDataRow(Component... components) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        container.setPreferredSize(new Dimension(850, 72));

        JPanel row = new JPanel(new GridLayout(1, components.length, 20, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        for (Component component : components) {
            row.add(component);
        }

        container.add(row, BorderLayout.CENTER);
        container.add(createSeparator(), BorderLayout.SOUTH);

        return container;
    }

    public static JLabel createCellLabel(String text, Color color) {
        JLabel label = new JLabel(text);

        label.setFont(AppTheme.BUTTON_FONT);
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.LEFT);

        return label;
    }

    public static JPanel createActionPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);

        for (JButton button : buttons) {
            panel.add(button);
        }

        return panel;
    }

    public static JSeparator createSeparator() {
        JSeparator separator = new JSeparator();

        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(new Color(235, 235, 235));

        return separator;
    }

    public static void updateAdaptiveTableCardSize(JPanel card, int rowCount) {
        int height = 90 + rowCount * 72;

        if (height < 420) {
            height = 420;
        }

        card.setPreferredSize(new Dimension(1050, height));
        card.setMinimumSize(new Dimension(850, 420));
        card.revalidate();
    }
}