package viewPackage.ui;

import javax.swing.*;
import java.awt.*;

public final class TableFactory {

    private TableFactory() {
        // Utility class
    }

    /*
     * ============================================================
     * CARD
     * ============================================================
     */

    public static JPanel createTableCard(int width, int minHeight) {
        JPanel card = CardFactory.createAdaptiveCard(width, minHeight);

        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(
                AppTheme.CARD_PADDING_TOP,
                AppTheme.CARD_PADDING_LEFT,
                AppTheme.CARD_PADDING_BOTTOM,
                AppTheme.CARD_PADDING_RIGHT
        ));

        card.setPreferredSize(new Dimension(width, minHeight));
        card.setMinimumSize(new Dimension(300, minHeight));

        return card;
    }

    /*
     * ============================================================
     * HEADER ROW
     * ============================================================
     */

    public static JPanel createHeaderRow(String... columns) {
        JPanel header = new JPanel(new GridLayout(
                1,
                columns.length,
                AppTheme.COMPONENT_GAP_MEDIUM,
                0
        ));

        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(
                0,
                AppTheme.TABLE_ROW_HORIZONTAL_PADDING,
                AppTheme.COMPONENT_GAP_MEDIUM,
                AppTheme.TABLE_ROW_HORIZONTAL_PADDING
        ));

        header.setPreferredSize(new Dimension(
                AppTheme.TABLE_CARD_MAX_WIDTH,
                AppTheme.TABLE_HEADER_HEIGHT
        ));

        header.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                AppTheme.TABLE_HEADER_HEIGHT
        ));

        for (String column : columns) {
            JLabel label = createHeaderLabel(column);
            header.add(label);
        }

        return header;
    }

    private static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);

        label.setFont(AppTheme.TEXT_BOLD_FONT);
        label.setForeground(AppTheme.TEXT_SECONDARY);
        label.setHorizontalAlignment(SwingConstants.LEFT);

        return label;
    }

    /*
     * ============================================================
     * DATA ROW
     * ============================================================
     */

    public static JPanel createDataRow(Component... components) {
        JPanel container = new JPanel(new BorderLayout());

        container.setOpaque(false);
        container.setPreferredSize(new Dimension(
                AppTheme.TABLE_CARD_MAX_WIDTH,
                AppTheme.TABLE_ROW_HEIGHT
        ));

        container.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                AppTheme.TABLE_ROW_HEIGHT
        ));

        JPanel row = new JPanel(new GridLayout(
                1,
                components.length,
                AppTheme.COMPONENT_GAP_MEDIUM,
                0
        ));

        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(
                AppTheme.TABLE_ROW_VERTICAL_PADDING,
                AppTheme.TABLE_ROW_HORIZONTAL_PADDING,
                AppTheme.TABLE_ROW_VERTICAL_PADDING,
                AppTheme.TABLE_ROW_HORIZONTAL_PADDING
        ));

        for (Component component : components) {
            row.add(wrapCell(component));
        }

        container.add(row, BorderLayout.CENTER);
        container.add(createSeparator(), BorderLayout.SOUTH);

        return container;
    }

    private static JPanel wrapCell(Component component) {
        JPanel wrapper = new JPanel(new BorderLayout());

        wrapper.setOpaque(false);
        wrapper.add(component, BorderLayout.CENTER);

        return wrapper;
    }

    /*
     * ============================================================
     * CELL
     * ============================================================
     */

    public static JLabel createCellLabel(String text, Color color) {
        JLabel label = new JLabel(text == null ? "-" : text);

        label.setFont(AppTheme.TEXT_FONT);
        label.setForeground(color == null ? AppTheme.TEXT_PRIMARY : color);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setVerticalAlignment(SwingConstants.CENTER);

        return label;
    }

    public static JLabel createBoldCellLabel(String text, Color color) {
        JLabel label = createCellLabel(text, color);

        label.setFont(AppTheme.TEXT_BOLD_FONT);

        return label;
    }

    /*
     * ============================================================
     * ACTIONS
     * ============================================================
     */

    public static JPanel createActionPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                AppTheme.COMPONENT_GAP_SMALL,
                0
        ));

        panel.setOpaque(false);

        for (JButton button : buttons) {
            panel.add(button);
        }

        return panel;
    }

    /*
     * ============================================================
     * SEPARATOR
     * ============================================================
     */

    public static JSeparator createSeparator() {
        JSeparator separator = new JSeparator();

        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(AppTheme.SEPARATOR);
        separator.setBackground(AppTheme.SEPARATOR);

        return separator;
    }

    /*
     * ============================================================
     * ADAPTIVE SIZE
     * ============================================================
     */

    public static void updateAdaptiveTableCardSize(JPanel card, int rowCount) {
        int safeRowCount = Math.max(rowCount, 1);

        int height = AppTheme.TABLE_BASE_HEIGHT
                + safeRowCount * AppTheme.TABLE_ROW_HEIGHT;

        height = Math.max(height, AppTheme.TABLE_MIN_HEIGHT);

        card.setPreferredSize(new Dimension(
                AppTheme.TABLE_CARD_MAX_WIDTH,
                height
        ));

        card.setMinimumSize(new Dimension(
                300,
                AppTheme.TABLE_MIN_HEIGHT
        ));

        card.revalidate();
        card.repaint();
    }
}