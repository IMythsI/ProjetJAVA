package viewPackage.Order;

import controllerPackage.ApplicationController;
import exceptionPackage.OrderException;
import modelPackage.Order;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.ArrayList;

public class OrderListPanel extends AppPage {

    private final ApplicationController controller;

    private JPanel resultCard;
    private JPanel resultContentPanel;
    private JTable orderTable;
    private ArrayList<Order> currentOrders;

    public OrderListPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();
        currentOrders = new ArrayList<>();

        addCentered(
                createPageTitle("Gestion des commandes"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle("Tableau de toutes les commandes"),
                1,
                new Insets(0, 0, 30, 0)
        );

        addCentered(
                createActionPanel(),
                2,
                new Insets(0, 0, 25, 0)
        );

        addCentered(
                createResultCardWrapper(),
                3,
                new Insets(0, 0, 0, 0)
        );

        loadOrders();
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(
                FlowLayout.CENTER,
                AppTheme.COMPONENT_GAP_MEDIUM,
                0
        ));

        panel.setOpaque(false);

        JButton addButton = ButtonFactory.createPrimaryButton(
                "Ajouter",
                () -> mainWindow.showOrderFormPanel()
        );

        JButton editButton = ButtonFactory.createSecondaryButton(
                "Modifier",
                this::editSelectedOrder
        );

        JButton deleteButton = ButtonFactory.createDangerButton(
                "Supprimer",
                this::deleteSelectedOrders
        );

        JButton refreshButton = ButtonFactory.createSecondaryButton(
                "Actualiser",
                this::loadOrders
        );

        JButton currentOrdersButton = ButtonFactory.createSecondaryButton(
                "Commandes en cours",
                () -> mainWindow.showOrderCardsPanel()
        );

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(refreshButton);
        panel.add(currentOrdersButton);

        return panel;
    }

    private JPanel createResultCardWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        resultCard = createResultCard();
        wrapper.add(resultCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeResultCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createResultCard() {
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.TABLE_CARD_MAX_WIDTH, 430);
        card.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Commandes enregistrées");

        titleLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        resultContentPanel = new JPanel(new BorderLayout());
        resultContentPanel.setOpaque(false);

        LoadingHelper.showEmpty(
                resultContentPanel,
                "Chargement des commandes..."
        );

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(resultContentPanel, BorderLayout.CENTER);

        return card;
    }

    private void resizeResultCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.TABLE_CARD_MAX_WIDTH;
        int minWidth = 820;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        resultCard.setPreferredSize(new Dimension(newWidth, 430));
        resultCard.setMinimumSize(new Dimension(minWidth, 360));
        resultCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        resultCard.revalidate();
        resultCard.repaint();
    }

    private void loadOrders() {
        LoadingHelper.runWithLoading(
                resultContentPanel,
                "Chargement des commandes...",
                controller::getAllOrders,
                this::displayOrders,
                this::displayLoadingError
        );
    }

    private void displayOrders(ArrayList<Order> orders) {
        resultContentPanel.removeAll();
        currentOrders = orders == null ? new ArrayList<>() : orders;

        if (currentOrders.isEmpty()) {
            LoadingHelper.showEmpty(
                    resultContentPanel,
                    "Aucune commande à afficher."
            );
            return;
        }

        orderTable = createOrderTable(currentOrders);
        JScrollPane scrollPane = new JScrollPane(orderTable);

        resultContentPanel.add(scrollPane, BorderLayout.CENTER);
        resultContentPanel.revalidate();
        resultContentPanel.repaint();
    }

    private JTable createOrderTable(ArrayList<Order> orders) {
        String[] columns = {
                "N°",
                "Type",
                "Date",
                "Heure retrait",
                "Client",
                "Téléphone",
                "Table",
                "Personnes",
                "Commentaire"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Order order : orders) {
            tableModel.addRow(new Object[]{
                    order.getIdOrder(),
                    order.getIsTakeAway() ? "À emporter" : "Sur place",
                    DateHelper.formatShortDate(order.getOrderDate()),
                    DateHelper.formatTime(order.getPickUpTime()),
                    order.getNameCustomer() == null ? "-" : order.getNameCustomer(),
                    order.getTelCustomer() == null ? "-" : order.getTelCustomer(),
                    order.getTable() == null ? "-" : "Table " + order.getTable().getIdTable(),
                    order.getGuestCount(),
                    order.getComment() == null ? "-" : order.getComment()
            });
        }

        JTable table = new JTable(tableModel);

        table.setRowHeight(AppTheme.JTABLE_ROW_HEIGHT);
        table.setFont(AppTheme.TEXT_FONT);
        table.getTableHeader().setFont(AppTheme.TEXT_BOLD_FONT);
        table.getTableHeader().setPreferredSize(new Dimension(0, AppTheme.TABLE_HEADER_HEIGHT));
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        return table;
    }

    private void editSelectedOrder() {
        Order selectedOrder = getSingleSelectedOrder();

        if (selectedOrder == null) {
            return;
        }

        mainWindow.showOrderEditPanel(selectedOrder);
    }

    private Order getSingleSelectedOrder() {
        if (orderTable == null || orderTable.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionnez une commande à modifier.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        if (orderTable.getSelectedRowCount() > 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionnez une seule commande pour la modification.",
                    "Sélection multiple",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        int viewRow = orderTable.getSelectedRow();
        int modelRow = orderTable.convertRowIndexToModel(viewRow);

        return currentOrders.get(modelRow);
    }

    private void deleteSelectedOrders() {
        ArrayList<Integer> selectedOrderIds = getSelectedOrderIds();

        if (selectedOrderIds.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionnez au moins une commande à supprimer.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment supprimer " + selectedOrderIds.size() + " commande(s) ?\n" +
                        "Les lignes de commande liées seront également supprimées.",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controller.deleteOrders(selectedOrderIds);

            JOptionPane.showMessageDialog(
                    this,
                    "La suppression a bien été effectuée.",
                    "Suppression réussie",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadOrders();

        } catch (OrderException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur de suppression",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private ArrayList<Integer> getSelectedOrderIds() {
        ArrayList<Integer> ids = new ArrayList<>();

        if (orderTable == null) {
            return ids;
        }

        int[] selectedRows = orderTable.getSelectedRows();

        for (int viewRow : selectedRows) {
            int modelRow = orderTable.convertRowIndexToModel(viewRow);
            Order order = currentOrders.get(modelRow);

            ids.add(order.getIdOrder());
        }

        return ids;
    }

    private void displayLoadingError(Exception exception) {
        LoadingHelper.showError(
                resultContentPanel,
                "Impossible de charger les commandes."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de chargement",
                JOptionPane.ERROR_MESSAGE
        );
    }
}