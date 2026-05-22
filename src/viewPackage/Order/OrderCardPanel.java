package viewPackage.Order;

import modelPackage.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OrderCardPanel extends JPanel {
    private Order order;
    private ArrayList<LineOrder> lineOrders;
    private boolean expanded;
    private JPanel detailPanel;
    private JButton detailButton;

    public OrderCardPanel(Order order, ArrayList<LineOrder> lineOrders) {
        this.order = order;
        this.lineOrders = lineOrders;
        this.expanded = false;

        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(250, 130));
        setMinimumSize(new Dimension(250, 130));
        setMaximumSize(new Dimension(250, Integer.MAX_VALUE));
        setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        putClientProperty("FlatLaf.style", "arc:15");

        setBackground(isFinished() ? new Color(220, 220, 220) : Color.WHITE);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(getOrderTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel typeLabel = new JLabel(order.getIsTakeAway() ? "À emporter" : "Sur table");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        typeLabel.setOpaque(true);
        typeLabel.putClientProperty("FlatLaf.style", "arc:15");
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        typeLabel.setPreferredSize(new Dimension(95, 28));
        typeLabel.setBackground(order.getIsTakeAway()
                ? new Color(0, 150, 200)
                : new Color(70, 180, 90));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(typeLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(5, 8));
        contentPanel.setOpaque(false);

        JPanel infoPanel = new JPanel(new GridLayout(1, 1));
        infoPanel.setOpaque(false);

        if (!order.getIsTakeAway()) {
            infoPanel.add(new JLabel("Personnes : " + order.getGuestCount()));
        } else {
            infoPanel.add(new JLabel("Heure de retrait : " + order.getPickUpTime()));
        }

        detailPanel = createDetailPanel();
        detailPanel.setVisible(false);

        contentPanel.add(infoPanel, BorderLayout.NORTH);
        contentPanel.add(detailPanel, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (LineOrder line : lineOrders) {
            JLabel label = new JLabel(line.getQuantity() + "x " + line.getProduct().getProductLabel() + " - " + line.getStatus().getStatusLabel());

            label.setFont(new Font("Arial", Font.PLAIN, 13));
            panel.add(label);
            panel.add(Box.createVerticalStrut(4));
        }

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout(10, 0));
        footerPanel.setOpaque(false);

        JLabel statusLabel = new JLabel(isFinished() ? "Terminée" : "En cours");
        statusLabel.setOpaque(true);
        statusLabel.putClientProperty("FlatLaf.style", "arc:15");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setPreferredSize(new Dimension(90, 28));
        statusLabel.setBackground(isFinished()
                ? new Color(120, 120, 120)
                : new Color(230, 150, 40));

        detailButton = new JButton("Détails");
        detailButton.putClientProperty("JButton.buttonType", "roundRect");
        detailButton.addActionListener(event -> toggleDetails());

        footerPanel.add(statusLabel, BorderLayout.WEST);
        footerPanel.add(detailButton, BorderLayout.EAST);

        return footerPanel;
    }

    private void toggleDetails() {
        expanded = !expanded;

        detailPanel.setVisible(expanded);
        detailButton.setText(expanded ? "Réduire" : "Détails");

        if (expanded) {
            setPreferredSize(new Dimension(250, (130 + 20 * lineOrders.size())));
        }else{
            setPreferredSize(new Dimension(250, 130));
        }

        revalidate();
        repaint();
    }

    private String getOrderTitle() {
        if (order.getIsTakeAway()) {
            return "Client : " + order.getNameCustomer();
        }

        return "Table " + order.getTable().getIdTable();
    }

    private boolean isFinished() {
        if (lineOrders == null || lineOrders.isEmpty()) {
            return false;
        }

        for (LineOrder line : lineOrders) {
            if (!line.getStatus().getStatusLabel().equals("Served")) {
                return false;
            }
        }

        return true;
    }
}