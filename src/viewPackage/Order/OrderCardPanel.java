package viewPackage.Order;

import modelPackage.LineOrder;
import modelPackage.Order;
import viewPackage.ui.AppTheme;
import viewPackage.ui.ButtonFactory;
import viewPackage.ui.StatusHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OrderCardPanel extends JPanel {
    private Order order;
    private ArrayList<LineOrder> lineOrders;

    private boolean expanded;
    private JPanel detailPanel;
    private JButton detailButton;

    private static final int CARD_WIDTH = 250;
    private static final int CLOSED_HEIGHT = 130;
    private static final int LINE_HEIGHT = 22;

    public OrderCardPanel(Order order, ArrayList<LineOrder> lineOrders) {
        this.order = order;
        this.lineOrders = lineOrders;
        this.expanded = false;

        setLayout(new BorderLayout(10, 10));

        setPreferredSize(new Dimension(CARD_WIDTH, CLOSED_HEIGHT));
        setMinimumSize(new Dimension(CARD_WIDTH, CLOSED_HEIGHT));
        setMaximumSize(new Dimension(CARD_WIDTH, Integer.MAX_VALUE));

        setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        setBackground(isFinished() ? new Color(220, 220, 220) : AppTheme.CARD);

        putClientProperty("FlatLaf.style", "arc:15");

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(8, 0));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(getOrderTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel typeLabel = createBadge(
                order.getIsTakeAway() ? "À emporter" : "Sur table",
                order.getIsTakeAway()
                        ? new Color(0, 150, 200)
                        : AppTheme.SUCCESS
        );

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(typeLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(5, 8));
        contentPanel.setOpaque(false);

        JLabel infoLabel;

        if (order.getIsTakeAway()) {
            infoLabel = new JLabel("Heure de retrait : " + order.getPickUpTime());
        } else {
            infoLabel = new JLabel("Personnes : " + order.getGuestCount());
        }

        infoLabel.setFont(AppTheme.BUTTON_FONT);
        infoLabel.setForeground(AppTheme.TEXT_SECONDARY);

        detailPanel = createDetailPanel();
        detailPanel.setVisible(false);

        contentPanel.add(infoLabel, BorderLayout.NORTH);
        contentPanel.add(detailPanel, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (lineOrders == null || lineOrders.isEmpty()) {
            JLabel emptyLabel = createDetailLabel("Aucun produit");
            panel.add(emptyLabel);
            return panel;
        }

        for (LineOrder line : lineOrders) {
            String text =
                    line.getQuantity()
                            + "x "
                            + line.getProduct().getProductLabel()
                            + " - "
                            + StatusHelper.getFrenchStatus(
                            line.getStatus().getStatusLabel()
                    );

            panel.add(createDetailLabel(text));
            panel.add(Box.createVerticalStrut(4));
        }

        return panel;
    }

    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);

        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(AppTheme.TEXT_PRIMARY);

        return label;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout(10, 0));
        footerPanel.setOpaque(false);

        JLabel statusLabel = createBadge(
                isFinished() ? "Terminée" : "En cours",
                isFinished() ? AppTheme.TEXT_SECONDARY : AppTheme.WARNING
        );

        detailButton = ButtonFactory.createSecondaryButton(
                "Détails",
                this::toggleDetails
        );

        detailButton.setPreferredSize(new Dimension(90, 28));

        footerPanel.add(statusLabel, BorderLayout.WEST);
        footerPanel.add(detailButton, BorderLayout.EAST);

        return footerPanel;
    }

    private JLabel createBadge(String text, Color backgroundColor) {
        JLabel badge = new JLabel(text, SwingConstants.CENTER);

        badge.setOpaque(true);
        badge.setFont(new Font("Arial", Font.BOLD, 12));
        badge.setForeground(Color.WHITE);
        badge.setBackground(backgroundColor);
        badge.setPreferredSize(new Dimension(95, 28));

        badge.putClientProperty("FlatLaf.style", "arc:15");

        return badge;
    }

    private void toggleDetails() {
        expanded = !expanded;

        detailPanel.setVisible(expanded);
        detailButton.setText(expanded ? "Réduire" : "Détails");

        setPreferredSize(
                new Dimension(
                        CARD_WIDTH,
                        calculateCardHeight()
                )
        );

        revalidate();
        repaint();

        Container parent = getParent();

        if (parent != null) {
            parent.revalidate();
            parent.repaint();
        }
    }

    private int calculateCardHeight() {
        if (!expanded) {
            return CLOSED_HEIGHT;
        }

        int lineCount = lineOrders == null ? 1 : Math.max(1, lineOrders.size());

        return CLOSED_HEIGHT + lineCount * LINE_HEIGHT;
    }

    private String getOrderTitle() {
        if (order.getIsTakeAway()) {
            return "Client : " + order.getNameCustomer();
        }

        return "Table " + order.getTable().getIdTable();
    }

    private boolean isFinished() {
        return StatusHelper.isOrderFinished(lineOrders);
    }
}