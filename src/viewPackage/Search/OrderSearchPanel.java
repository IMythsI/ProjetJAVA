package viewPackage.Search;

import controllerPackage.ApplicationController;
import exceptionPackage.SearchException;
import modelPackage.OrderSearchResult;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class OrderSearchPanel extends AppPage {

    private ApplicationController controller;

    private JComboBox<String> waiterComboBox;
    private JComboBox<String> statusComboBox;

    private DefaultTableModel tableModel;
    private JTable resultTable;

    public OrderSearchPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(createPageTitle("Search orders"), 0, new Insets(0, 0, 25, 0));
        addCentered(createSearchCard(), 1, new Insets(0, 0, 20, 0));
        addCentered(createResultCard(), 2, new Insets(0, 0, 20, 0));

        loadWaiters();
        loadStatuses();
    }

    private JPanel createSearchCard() {
        JPanel card = CardFactory.createCard(760, 230);
        card.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 15, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        waiterComboBox = FormFactory.createComboBox();
        statusComboBox = FormFactory.createComboBox();

        FormFactory.addFormRow(formPanel, gbc, 0, "Waiter *", waiterComboBox);
        FormFactory.addFormRow(formPanel, gbc, 1, "Status *", statusComboBox);

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setOpaque(false);

        JButton searchButton = ButtonFactory.createPrimaryButton(
                "Search",
                this::searchOrders
        );

        buttonPanel.add(searchButton);

        return buttonPanel;
    }

    private JPanel createResultCard() {
        JPanel card = CardFactory.createCard(950, 360);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {
                "Order",
                "Date",
                "Type",
                "Table",
                "Guests",
                "Waiter",
                "Status",
                "Total"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultTable = new JTable(tableModel);
        resultTable.setRowHeight(34);
        resultTable.setFont(AppTheme.SUBTITLE_FONT);
        resultTable.getTableHeader().setFont(AppTheme.BUTTON_FONT);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private void loadWaiters() {
        try {
            ArrayList<String> waiters = controller.getWaiterNames();

            waiterComboBox.removeAllItems();

            for (String waiter : waiters) {
                waiterComboBox.addItem(waiter);
            }

        } catch (SearchException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Loading error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadStatuses() {
        try {
            ArrayList<String> statuses = controller.getOrderStatusLabels();

            statusComboBox.removeAllItems();

            for (String status : statuses) {
                statusComboBox.addItem(status);
            }

        } catch (SearchException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Loading error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void searchOrders() {
        try {
            String waiterName = (String) waiterComboBox.getSelectedItem();
            String status = (String) statusComboBox.getSelectedItem();

            ArrayList<OrderSearchResult> results =
                    controller.searchOrdersByWaiterAndStatus(waiterName, status);

            refreshTable(results);

        } catch (SearchException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Search error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshTable(ArrayList<OrderSearchResult> results) {
        tableModel.setRowCount(0);

        for (OrderSearchResult result : results) {
            String orderType = result.isTakeAway() ? "Take away" : "On site";

            String tableText = result.getTableId() == null
                    ? "-"
                    : "Table " + result.getTableId();

            String guestText = result.getGuestCount() == null
                    ? "-"
                    : String.valueOf(result.getGuestCount());

            tableModel.addRow(new Object[]{
                    result.getOrderId(),
                    result.getOrderDate(),
                    orderType,
                    tableText,
                    guestText,
                    result.getWaiterName(),
                    StatusHelper.getFrenchStatus(result.getStatusLabel()),
                    String.format("%.2f €", result.getTotalAmount())
            });
        }

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No order found for these criteria.",
                    "No result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}