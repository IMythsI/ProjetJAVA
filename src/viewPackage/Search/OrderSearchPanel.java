package viewPackage.Search;

import controllerPackage.ApplicationController;
import modelPackage.OrderSearchResult;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class OrderSearchPanel extends AppPage {

    private final ApplicationController controller;

    private JPanel searchCard;
    private JPanel resultCard;
    private JPanel resultContentPanel;

    private JComboBox<String> waiterComboBox;
    private JComboBox<String> statusComboBox;

    public OrderSearchPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(
                createPageTitle("Recherche de commandes"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle("Rechercher les commandes par employé et par statut"),
                1,
                new Insets(0, 0, 30, 0)
        );

        addCentered(
                createSearchCardWrapper(),
                2,
                new Insets(0, 0, 25, 0)
        );

        addCentered(
                createResultCardWrapper(),
                3,
                new Insets(0, 0, 0, 0)
        );

        loadSearchCriteria();
    }

    private JPanel createSearchCardWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        searchCard = createSearchCard();
        wrapper.add(searchCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeSearchCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createSearchCard() {
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.SEARCH_CARD_MAX_WIDTH, 250);
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

        waiterComboBox = FormFactory.createComboBox();
        statusComboBox = FormFactory.createComboBox();

        FormFactory.addFormRow(formPanel, constraints, 0, "Employé *", waiterComboBox);
        FormFactory.addFormRow(formPanel, constraints, 1, "Statut *", statusComboBox);

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

        JButton searchButton = ButtonFactory.createPrimaryButton(
                "Rechercher",
                this::searchOrders
        );

        buttonPanel.add(searchButton);

        return buttonPanel;
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
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.TABLE_CARD_MAX_WIDTH, 390);
        card.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Résultats");
        titleLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        resultContentPanel = new JPanel(new BorderLayout());
        resultContentPanel.setOpaque(false);

        LoadingHelper.showEmpty(
                resultContentPanel,
                "Lancez une recherche pour afficher les résultats."
        );

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(resultContentPanel, BorderLayout.CENTER);

        return card;
    }

    private void resizeSearchCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.SEARCH_CARD_MAX_WIDTH;
        int minWidth = 620;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        searchCard.setPreferredSize(new Dimension(newWidth, 250));
        searchCard.setMinimumSize(new Dimension(minWidth, 230));
        searchCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        searchCard.revalidate();
        searchCard.repaint();
    }

    private void resizeResultCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.TABLE_CARD_MAX_WIDTH;
        int minWidth = 760;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        resultCard.setPreferredSize(new Dimension(newWidth, 390));
        resultCard.setMinimumSize(new Dimension(minWidth, 340));
        resultCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        resultCard.revalidate();
        resultCard.repaint();
    }

    private void loadSearchCriteria() {
        waiterComboBox.setEnabled(false);
        statusComboBox.setEnabled(false);

        waiterComboBox.removeAllItems();
        statusComboBox.removeAllItems();

        waiterComboBox.addItem("Chargement...");
        statusComboBox.addItem("Chargement...");

        LoadingHelper.runWithLoading(
                resultContentPanel,
                "Chargement des critères...",
                () -> {
                    ArrayList<String> waiters = controller.getWaiterNames();
                    ArrayList<String> statuses = controller.getOrderStatusLabels();

                    return new SearchCriteria(waiters, statuses);
                },
                this::displaySearchCriteria,
                this::displayCriteriaLoadingError
        );
    }

    private void displaySearchCriteria(SearchCriteria criteria) {
        waiterComboBox.removeAllItems();
        statusComboBox.removeAllItems();

        for (String waiter : criteria.getWaiters()) {
            waiterComboBox.addItem(waiter);
        }

        for (String status : criteria.getStatuses()) {
            statusComboBox.addItem(StatusHelper.getFrenchStatus(status));
        }

        waiterComboBox.setEnabled(true);
        statusComboBox.setEnabled(true);

        statusComboBox.putClientProperty("databaseStatuses", criteria.getStatuses());

        LoadingHelper.showEmpty(
                resultContentPanel,
                "Lancez une recherche pour afficher les résultats."
        );
    }

    private void displayCriteriaLoadingError(Exception exception) {
        waiterComboBox.removeAllItems();
        statusComboBox.removeAllItems();

        waiterComboBox.setEnabled(false);
        statusComboBox.setEnabled(false);

        LoadingHelper.showError(
                resultContentPanel,
                "Impossible de charger les critères."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de chargement",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void searchOrders() {
        try {
            String waiterName = (String) waiterComboBox.getSelectedItem();
            String statusLabel = getSelectedDatabaseStatus();

            if (waiterName == null || waiterName.isBlank() || waiterName.equals("Chargement...")) {
                throw new IllegalArgumentException("L'employé est obligatoire.");
            }

            if (statusLabel == null || statusLabel.isBlank()) {
                throw new IllegalArgumentException("Le statut est obligatoire.");
            }

            LoadingHelper.runWithLoading(
                    resultContentPanel,
                    "Recherche des commandes...",
                    () -> controller.searchOrdersByWaiterAndStatus(waiterName, statusLabel),
                    this::displayResults,
                    this::displaySearchError
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

    private String getSelectedDatabaseStatus() {
        Object property = statusComboBox.getClientProperty("databaseStatuses");

        if (!(property instanceof ArrayList<?> statuses)) {
            return null;
        }

        int selectedIndex = statusComboBox.getSelectedIndex();

        if (selectedIndex < 0 || selectedIndex >= statuses.size()) {
            return null;
        }

        return (String) statuses.get(selectedIndex);
    }

    private void displayResults(ArrayList<OrderSearchResult> results) {
        resultContentPanel.removeAll();

        if (results == null || results.isEmpty()) {
            LoadingHelper.showEmpty(
                    resultContentPanel,
                    "Aucune commande trouvée pour ces critères."
            );
            return;
        }

        JTable table = createResultTable(results);
        JScrollPane scrollPane = new JScrollPane(table);

        resultContentPanel.add(scrollPane, BorderLayout.CENTER);
        resultContentPanel.revalidate();
        resultContentPanel.repaint();
    }

    private JTable createResultTable(ArrayList<OrderSearchResult> results) {
        String[] columns = {
                "Commande",
                "Date",
                "Type",
                "Table",
                "Personnes",
                "Employé",
                "Statut",
                "Total"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (OrderSearchResult result : results) {
            tableModel.addRow(new Object[]{
                    result.getOrderId(),
                    DateHelper.formatShortDate(result.getOrderDate()),
                    result.isTakeAway() ? "À emporter" : "Sur place",
                    result.getTableId() == null ? "-" : "Table " + result.getTableId(),
                    result.getGuestCount() == null ? "-" : result.getGuestCount(),
                    result.getWaiterName(),
                    StatusHelper.getFrenchStatus(result.getStatusLabel()),
                    String.format("%.2f €", result.getTotalAmount())
            });
        }

        JTable table = new JTable(tableModel);

        table.setRowHeight(AppTheme.JTABLE_ROW_HEIGHT);
        table.setFont(AppTheme.TEXT_FONT);
        table.getTableHeader().setFont(AppTheme.TEXT_BOLD_FONT);
        table.getTableHeader().setPreferredSize(new Dimension(0, AppTheme.TABLE_HEADER_HEIGHT));

        return table;
    }

    private void displaySearchError(Exception exception) {
        LoadingHelper.showError(
                resultContentPanel,
                "Impossible d’effectuer la recherche."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de recherche",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private static class SearchCriteria {

        private final ArrayList<String> waiters;
        private final ArrayList<String> statuses;

        public SearchCriteria(ArrayList<String> waiters, ArrayList<String> statuses) {
            this.waiters = waiters;
            this.statuses = statuses;
        }

        public ArrayList<String> getWaiters() {
            return waiters;
        }

        public ArrayList<String> getStatuses() {
            return statuses;
        }
    }
}