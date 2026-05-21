package viewPackage.Table;

import controllerPackage.ApplicationController;
import exceptionPackage.*;
import modelPackage.*;
import viewPackage.AbstractPanel;
import viewPackage.MainJFrame;
import viewPackage.Order.*;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class TableDetailPanel extends AbstractPanel {
    private ApplicationController controller;
    private Table table;

    private JComboBox<String> statusComboBox;
    private JLabel totalLabel;
    private JPanel centerPanel;

    public TableDetailPanel(MainJFrame mainWindow, Table table) {
        super(mainWindow);

        this.table = table;
        controller = new ApplicationController();

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadTableLineOrdersIfOccupied();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(createBackButton(), BorderLayout.WEST);

        JLabel title = new JLabel("Table " + table.getIdTable(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));

        topPanel.add(title, BorderLayout.CENTER);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        centerPanel = new JPanel(new BorderLayout(15, 15));

        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        infoPanel.add(new JLabel("Capacité :"));
        infoPanel.add(new JLabel(table.getNbSeats() + " places"));

        infoPanel.add(new JLabel("Statut actuel :"));
        infoPanel.add(new JLabel(table.getStatus().getStatusLabel()));

        infoPanel.add(new JLabel("Changer statut :"));

        statusComboBox = new JComboBox<>(new String[]{
                "Available",
                "Reserved",
                "Occupied"
        });

        statusComboBox.setSelectedItem(table.getStatus().getStatusLabel());

        infoPanel.add(statusComboBox);

        centerPanel.add(infoPanel, BorderLayout.NORTH);

        return centerPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton saveStatusButton = new JButton("Changer le statut");
        JButton createOrderButton = new JButton("Créer commande");

        saveStatusButton.addActionListener(event -> updateStatus());
        createOrderButton.addActionListener(event ->
                mainWindow.showOrderFormTablePanel(table)
        );

        buttonPanel.add(createOrderButton);
        buttonPanel.add(saveStatusButton);

        return buttonPanel;
    }

    private void updateStatus() {
        try {
            String newStatus = (String) statusComboBox.getSelectedItem();

            controller.updateTableStatus(table.getIdTable(), newStatus);

            JOptionPane.showMessageDialog(
                    this,
                    "Statut modifié avec succès.",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mainWindow.showWaiterPanel();

        } catch (TableException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadTableLineOrdersIfOccupied() {
        if (!table.getStatus().getStatusLabel().equals("Occupied")) {
            JLabel message = new JLabel(
                    "Aucune ligne de commande : la table n'est pas occupée.",
                    SwingConstants.CENTER
            );

            centerPanel.add(message, BorderLayout.CENTER);
            return;
        }

        try {
            ArrayList<LineOrder> lineOrders =
                    controller.getLineOrdersByTable(table.getIdTable());

            JTable lineOrderTable = new JTable(new LineOrderTableModel(lineOrders));

            lineOrderTable.setRowHeight(28);
            lineOrderTable.setAutoCreateRowSorter(true);
            lineOrderTable.getTableHeader().setReorderingAllowed(false);

            BigDecimal total = calculateTotal(lineOrders);

            JLabel totalLabel = new JLabel("Total : " + total + " €", SwingConstants.RIGHT);
            totalLabel.setFont(new Font("Arial", Font.BOLD, 20));

            JPanel lineOrderPanel = new JPanel(new BorderLayout(10, 10));

            JLabel title = new JLabel("Produits commandés", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 20));

            lineOrderPanel.add(title, BorderLayout.NORTH);
            lineOrderPanel.add(new JScrollPane(lineOrderTable), BorderLayout.CENTER);
            lineOrderPanel.add(totalLabel, BorderLayout.SOUTH);

            centerPanel.add(lineOrderPanel, BorderLayout.CENTER);

        } catch (LineOrderException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private BigDecimal calculateTotal(ArrayList<LineOrder> lineOrders) {
        BigDecimal total = BigDecimal.ZERO;

        for (LineOrder line : lineOrders) {
            BigDecimal lineTotal = line.getProduct()
                    .getPrice()
                    .multiply(BigDecimal.valueOf(line.getQuantity()));

            total = total.add(lineTotal);
        }

        return total;
    }
}