package viewPackage.Order;

import controllerPackage.ApplicationController;
import exceptionPackage.OrderException;
import modelPackage.Order;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OrderListPanel extends JPanel {
    private ApplicationController controller;

    public OrderListPanel() {
        controller = new ApplicationController();

        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Liste des commandes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton refreshButton = new JButton("Actualiser");
        JButton addButton = new JButton("Ajouter");
        JButton deleteButton = new JButton("Supprimer");

        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadOrder();
    }

    private void loadOrder() {
        try {
            ArrayList<Order> orders = controller.getAllOrders();

            JTable ordersTable = new JTable(new OrderTableModel(orders));

            ordersTable.setRowHeight(28);
            ordersTable.setShowGrid(true);
            ordersTable.setAutoCreateRowSorter(true);
            ordersTable.getTableHeader().setReorderingAllowed(false);
            ordersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
            ordersTable.setFont(new Font("Arial", Font.PLAIN, 13));

            removeExistingTable();
            add(new JScrollPane(ordersTable), BorderLayout.CENTER);

            revalidate();
            repaint();

        } catch (OrderException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void removeExistingTable() {
        Component centerComponent = ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER);

        if (centerComponent != null) {
            remove(centerComponent);
        }
    }
}
