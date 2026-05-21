package viewPackage.Order;

import modelPackage.*;

import javax.swing.*;
import java.awt.*;

public class OrderCardPanel extends JPanel {
    private Order order;

    public OrderCardPanel(Order order) {
        this.order = order;

        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(270, 150));

        setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        putClientProperty(
                "FlatLaf.style",
                "arc:15"
        );

        if (isFinished()) {
            setBackground(new Color(220, 220, 220));
        } else {
            setBackground(Color.WHITE);
        }

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel(getOrderTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel typeLabel = new JLabel(order.getIsTakeAway() ? "À emporter": "Sur table");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        typeLabel.setOpaque(true);
        typeLabel.putClientProperty(
                "FlatLaf.style",
                "arc:15"
        );
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        typeLabel.setPreferredSize(new Dimension(95, 28));
        typeLabel.setBackground(order.getIsTakeAway() ? new Color(0, 150, 200) : new Color(70, 180, 90));
        headerPanel.add(titleLabel,BorderLayout.WEST);
        headerPanel.add(typeLabel,BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        contentPanel.setOpaque(false);

        contentPanel.add(new JLabel("Personnes : " + order.getGuestCount()));

        if (order.getIsTakeAway()) {
            contentPanel.add(new JLabel("Client : " + order.getNameCustomer()));
        } else {
            contentPanel.add(new JLabel("Table : " + order.getTable().getIdTable()));
        }

        return contentPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout(10, 0));
        footerPanel.setOpaque(false);
        JLabel statusLabel = new JLabel(isFinished() ? "Terminée" : "En cours");
        statusLabel.setOpaque(true);
        statusLabel.putClientProperty(
                "FlatLaf.style",
                "arc:15"
        );
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setPreferredSize(new Dimension(90, 28));
        if (isFinished()) {
            statusLabel.setBackground(new Color(120, 120, 120));
        } else {
            statusLabel.setBackground(new Color(230, 150, 40));
        }
        JButton detailButton = new JButton("Détails");
        detailButton.putClientProperty(
                "JButton.buttonType",
                "roundRect"
        );
        footerPanel.add(statusLabel,BorderLayout.WEST);
        footerPanel.add(detailButton,BorderLayout.EAST);
        return footerPanel;
    }

    private String getOrderTitle() {
        if (order.getIsTakeAway()) {
            return "Commande client";
        }

        return "Table " + order.getTable().getIdTable();
    }

    private boolean isFinished() {
        return order.getStatus() != null
                && order.getStatus().getStatusLabel().equals("Served");
    }
}