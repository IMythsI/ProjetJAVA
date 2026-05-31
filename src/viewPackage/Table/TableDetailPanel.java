package viewPackage.Table;

import controllerPackage.ApplicationController;
import exceptionPackage.LineOrderException;
import exceptionPackage.TableException;
import modelPackage.LineOrder;
import modelPackage.Table;
import viewPackage.MainJFrame;
import viewPackage.Order.LineOrderTableModel;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class TableDetailPanel extends AppPage {

    private final ApplicationController controller;
    private final Table table;

    private JPanel infoCard;
    private JPanel ordersCard;
    private JPanel ordersContentPanel;

    private JComboBox<StatusOption> statusComboBox;

    public TableDetailPanel(MainJFrame mainWindow, Table table) {
        super(mainWindow, true);

        this.controller = new ApplicationController();
        this.table = table;

        addCentered(
                createPageTitle("Table " + table.getIdTable()),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle("Détails et gestion de la table"),
                1,
                new Insets(0, 0, 30, 0)
        );

        addCentered(
                createInfoCardWrapper(),
                2,
                new Insets(0, 0, 25, 0)
        );

        addCentered(
                createOrdersCardWrapper(),
                3,
                new Insets(0, 0, 0, 0)
        );

        loadTableLineOrdersIfOccupied();
    }

    private JPanel createInfoCardWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        infoCard = createInfoCard();
        wrapper.add(infoCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeInfoCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createInfoCard() {
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.FORM_CARD_MAX_WIDTH, 300);
        card.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(
                AppTheme.CARD_PADDING_TOP,
                AppTheme.CARD_PADDING_LEFT,
                AppTheme.CARD_PADDING_BOTTOM,
                AppTheme.CARD_PADDING_RIGHT
        ));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        statusComboBox = FormFactory.createGenericComboBox();
        fillStatusComboBox();

        FormFactory.addFormRow(
                formPanel,
                constraints,
                0,
                "Capacité",
                createReadOnlyLabel(table.getNbSeats() + " places")
        );

        FormFactory.addFormRow(
                formPanel,
                constraints,
                1,
                "Statut actuel",
                createReadOnlyStatusLabel(table.getStatus().getStatusLabel())
        );

        FormFactory.addFormRow(
                formPanel,
                constraints,
                2,
                "Nouveau statut",
                statusComboBox
        );

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(
                FlowLayout.RIGHT,
                AppTheme.COMPONENT_GAP_MEDIUM,
                AppTheme.COMPONENT_GAP_MEDIUM
        ));

        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 20));

        JButton deleteTableButton = ButtonFactory.createSecondaryButton(
                "Supprimer",
                this::confirmAndDeleteTable
        );

        JButton createOrderButton = ButtonFactory.createSecondaryButton(
                "Créer une commande",
                () -> mainWindow.showProductSelectionPanel(table)
        );

        JButton saveStatusButton = ButtonFactory.createPrimaryButton(
                "Changer le statut",
                this::updateStatus
        );

        deleteTableButton.setForeground(AppTheme.DANGER);

        deleteTableButton.setPreferredSize(new Dimension(150, AppTheme.BUTTON_HEIGHT));
        createOrderButton.setPreferredSize(new Dimension(210, AppTheme.BUTTON_HEIGHT));
        saveStatusButton.setPreferredSize(new Dimension(210, AppTheme.BUTTON_HEIGHT));

        buttonPanel.add(deleteTableButton);
        buttonPanel.add(createOrderButton);
        buttonPanel.add(saveStatusButton);

        return buttonPanel;
    }

    private void resizeInfoCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.FORM_CARD_MAX_WIDTH;
        int minWidth = 560;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        infoCard.setPreferredSize(new Dimension(newWidth, 300));
        infoCard.setMinimumSize(new Dimension(minWidth, 270));
        infoCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        infoCard.revalidate();
        infoCard.repaint();
    }

    private JPanel createOrdersCardWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        ordersCard = createOrdersCard();
        wrapper.add(ordersCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeOrdersCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createOrdersCard() {
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.TABLE_CARD_MAX_WIDTH, 420);
        card.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Produits commandés");
        titleLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        ordersContentPanel = new JPanel(new BorderLayout());
        ordersContentPanel.setOpaque(false);

        LoadingHelper.showEmpty(
                ordersContentPanel,
                "Aucune ligne de commande à afficher."
        );

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(ordersContentPanel, BorderLayout.CENTER);

        return card;
    }

    private void resizeOrdersCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.TABLE_CARD_MAX_WIDTH;
        int minWidth = 760;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        ordersCard.setPreferredSize(new Dimension(newWidth, 420));
        ordersCard.setMinimumSize(new Dimension(minWidth, 360));
        ordersCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        ordersCard.revalidate();
        ordersCard.repaint();
    }

    private JLabel createReadOnlyLabel(String text) {
        JLabel label = new JLabel(text);

        label.setFont(AppTheme.TEXT_FONT);
        label.setForeground(AppTheme.TEXT_PRIMARY);
        label.setPreferredSize(AppTheme.FIELD_SIZE);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        return label;
    }

    private JLabel createReadOnlyStatusLabel(String statusLabel) {
        JLabel label = createReadOnlyLabel(StatusHelper.getFrenchStatus(statusLabel));

        label.setForeground(StatusHelper.getStatusColor(statusLabel));

        return label;
    }

    private void fillStatusComboBox() {
        statusComboBox.addItem(new StatusOption("Available", "Libre"));
        statusComboBox.addItem(new StatusOption("Reserved", "Réservée"));
        statusComboBox.addItem(new StatusOption("Occupied", "Occupée"));

        selectStatus(table.getStatus().getStatusLabel());
    }

    private void selectStatus(String statusLabel) {
        if (statusLabel == null) {
            return;
        }

        for (int i = 0; i < statusComboBox.getItemCount(); i++) {
            StatusOption option = statusComboBox.getItemAt(i);

            if (option.getDatabaseValue().equals(statusLabel)) {
                statusComboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private void updateStatus() {
        try {
            StatusOption selectedStatus = (StatusOption) statusComboBox.getSelectedItem();

            if (selectedStatus == null || selectedStatus.getDatabaseValue().isBlank()) {
                throw new IllegalArgumentException("Le statut est obligatoire.");
            }

            controller.updateTableStatus(
                    table.getIdTable(),
                    selectedStatus.getDatabaseValue()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Le statut de la table a bien été modifié.",
                    "Modification réussie",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mainWindow.showTableListPanel();

        } catch (TableException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur de modification",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur de validation",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void confirmAndDeleteTable() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment supprimer la table "
                        + table.getIdTable()
                        + " ?\nLa suppression sera refusée si la table est liée à une réservation ou une commande.",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        deleteTable();
    }

    private void deleteTable() {
        LoadingHelper.runWithLoading(
                ordersContentPanel,
                "Suppression de la table...",
                () -> {
                    controller.deleteTable(table.getIdTable());
                    return null;
                },
                ignored -> {
                    JOptionPane.showMessageDialog(
                            this,
                            "La table a bien été supprimée.",
                            "Suppression réussie",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    mainWindow.showTableListPanel();
                },
                this::displayDeleteError
        );
    }

    private void displayDeleteError(Exception exception) {
        LoadingHelper.showError(
                ordersContentPanel,
                "Impossible de supprimer la table."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de suppression",
                JOptionPane.ERROR_MESSAGE
        );

        loadTableLineOrdersIfOccupied();
    }

    private void loadTableLineOrdersIfOccupied() {
        String statusLabel = table.getStatus().getStatusLabel();

        if (!"Occupied".equals(statusLabel)) {
            LoadingHelper.showEmpty(
                    ordersContentPanel,
                    "Aucune ligne de commande : la table n’est pas occupée."
            );
            return;
        }

        LoadingHelper.runWithLoading(
                ordersContentPanel,
                "Chargement des produits commandés...",
                () -> controller.getLineOrdersByTable(table.getIdTable()),
                this::displayLineOrders,
                this::displayLineOrderLoadingError
        );
    }

    private void displayLineOrders(ArrayList<LineOrder> lineOrders) {
        ordersContentPanel.removeAll();

        if (lineOrders == null || lineOrders.isEmpty()) {
            LoadingHelper.showEmpty(
                    ordersContentPanel,
                    "Aucun produit commandé pour cette table."
            );
            return;
        }

        JTable lineOrderTable = new JTable(new LineOrderTableModel(lineOrders));

        lineOrderTable.setRowHeight(AppTheme.JTABLE_ROW_HEIGHT);
        lineOrderTable.setFont(AppTheme.TEXT_FONT);
        lineOrderTable.getTableHeader().setFont(AppTheme.TEXT_BOLD_FONT);
        lineOrderTable.getTableHeader().setPreferredSize(new Dimension(0, AppTheme.TABLE_HEADER_HEIGHT));
        lineOrderTable.setAutoCreateRowSorter(true);
        lineOrderTable.getTableHeader().setReorderingAllowed(false);

        JLabel totalLabel = new JLabel(
                "Total : " + formatPrice(calculateTotal(lineOrders)),
                SwingConstants.RIGHT
        );
        totalLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        totalLabel.setForeground(AppTheme.TEXT_PRIMARY);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.add(new JScrollPane(lineOrderTable), BorderLayout.CENTER);
        tablePanel.add(totalLabel, BorderLayout.SOUTH);

        ordersContentPanel.add(tablePanel, BorderLayout.CENTER);
        ordersContentPanel.revalidate();
        ordersContentPanel.repaint();

        refreshPage();
    }

    private void displayLineOrderLoadingError(Exception exception) {
        LoadingHelper.showError(
                ordersContentPanel,
                "Impossible de charger les produits commandés."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de chargement",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private BigDecimal calculateTotal(ArrayList<LineOrder> lineOrders) {
        BigDecimal total = BigDecimal.ZERO;

        for (LineOrder line : lineOrders) {
            if (line == null || line.getProduct() == null || line.getProduct().getPrice() == null) {
                continue;
            }

            BigDecimal lineTotal = line.getProduct()
                    .getPrice()
                    .multiply(BigDecimal.valueOf(line.getQuantity()));

            total = total.add(lineTotal);
        }

        return total;
    }

    private String formatPrice(BigDecimal price) {
        if (price == null) {
            return "0,00 €";
        }

        return String.format("%.2f €", price).replace(".", ",");
    }

    private static class StatusOption {

        private final String databaseValue;
        private final String displayValue;

        public StatusOption(String databaseValue, String displayValue) {
            this.databaseValue = databaseValue;
            this.displayValue = displayValue;
        }

        public String getDatabaseValue() {
            return databaseValue;
        }

        @Override
        public String toString() {
            return displayValue;
        }
    }
}