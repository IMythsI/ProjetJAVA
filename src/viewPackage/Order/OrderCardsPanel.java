package viewPackage.Order;

import controllerPackage.ApplicationController;
import modelPackage.LineOrder;
import modelPackage.Order;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderCardsPanel extends AppPage {

    private static final String FILTER_ALL = "Toutes";
    private static final String FILTER_PREPARATION = "En préparation";
    private static final String FILTER_READY = "Prêtes";
    private static final String FILTER_SERVED = "Servies";

    private final ApplicationController controller;

    private JButton allButton;
    private JButton preparationButton;
    private JButton readyButton;
    private JButton servedButton;

    private JPanel wrapper;

    private JPanel ordersCard;
    private JPanel ordersRowsPanel;

    private ArrayList<Order> orders;
    private Map<Integer, ArrayList<LineOrder>> lineOrdersByOrder;

    private String selectedFilter;

    public OrderCardsPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();
        orders = new ArrayList<>();
        lineOrdersByOrder = new HashMap<>();
        selectedFilter = FILTER_ALL;

        addCentered(
                createPageTitle("Commandes en cours"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle("Suivi des commandes du restaurant"),
                1,
                new Insets(0, 0, 30, 0)
        );

        addCentered(
                createFilterPanel(),
                2,
                new Insets(0, 0, 25, 0)
        );

        addCentered(
                createOrdersCardWrapper(),
                3,
                new Insets(0, 0, 0, 0)
        );

        loadOrders();
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(
                FlowLayout.CENTER,
                AppTheme.COMPONENT_GAP_MEDIUM,
                AppTheme.COMPONENT_GAP_SMALL
        ));

        panel.setOpaque(false);

        allButton = createFilterButton(FILTER_ALL);
        preparationButton = createFilterButton(FILTER_PREPARATION);
        readyButton = createFilterButton(FILTER_READY);
        servedButton = createFilterButton(FILTER_SERVED);

        JButton refreshButton = ButtonFactory.createSecondaryButton(
                "Actualiser",
                this::loadOrders
        );

        refreshButton.setPreferredSize(new Dimension(150, AppTheme.BUTTON_HEIGHT));

        panel.add(allButton);
        panel.add(preparationButton);
        panel.add(readyButton);
        panel.add(servedButton);
        panel.add(refreshButton);

        updateFilterButtons();

        return panel;
    }

    private JButton createFilterButton(String text) {
        JButton button = new RoundedButton(text, AppTheme.SMALL_BUTTON_ARC);

        button.setPreferredSize(new Dimension(155, AppTheme.BUTTON_HEIGHT));
        button.setMinimumSize(new Dimension(130, AppTheme.BUTTON_HEIGHT));

        button.setFont(AppTheme.BUTTON_FONT);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addActionListener(event -> {
            selectedFilter = text;
            updateFilterButtons();
            refreshOrdersList();
        });

        return button;
    }

    private void updateFilterButtons() {
        styleFilterButton(allButton, selectedFilter.equals(FILTER_ALL));
        styleFilterButton(preparationButton, selectedFilter.equals(FILTER_PREPARATION));
        styleFilterButton(readyButton, selectedFilter.equals(FILTER_READY));
        styleFilterButton(servedButton, selectedFilter.equals(FILTER_SERVED));
    }

    private void styleFilterButton(JButton button, boolean selected) {
        if (selected) {
            button.setForeground(Color.WHITE);
            button.setBackground(AppTheme.PRIMARY);
        } else {
            button.setForeground(AppTheme.TEXT_SECONDARY);
            button.setBackground(Color.WHITE);
        }
    }

    private JPanel createOrdersCardWrapper() {
        wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
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
        JPanel card = TableFactory.createTableCard(
                AppTheme.TABLE_CARD_MAX_WIDTH,
                AppTheme.TABLE_MIN_HEIGHT
        );

        card.add(
                TableFactory.createHeaderRow(
                        "Origine",
                        "Produits",
                        "Date",
                        "Heure",
                        "Statut",
                        "Actions"
                ),
                BorderLayout.NORTH
        );

        ordersRowsPanel = new JPanel();
        ordersRowsPanel.setOpaque(false);
        ordersRowsPanel.setLayout(new BoxLayout(ordersRowsPanel, BoxLayout.Y_AXIS));

        card.add(ordersRowsPanel, BorderLayout.CENTER);

        return card;
    }

    private void resizeOrdersCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.TABLE_CARD_MAX_WIDTH;
        int minWidth = 780;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        int currentHeight = ordersCard.getPreferredSize().height;

        ordersCard.setPreferredSize(new Dimension(newWidth, currentHeight));
        ordersCard.setMinimumSize(new Dimension(minWidth, AppTheme.TABLE_MIN_HEIGHT));
        ordersCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        ordersCard.revalidate();
        ordersCard.repaint();
    }

    private void loadOrders() {
        LoadingHelper.runWithLoading(
                ordersRowsPanel,
                "Chargement des commandes...",
                this::loadOrderCardsData,
                this::displayOrderCardsData,
                this::displayLoadingError
        );
    }

    private OrderCardsData loadOrderCardsData() throws Exception {
        ArrayList<Order> loadedOrders = controller.getAllOrders();
        Map<Integer, ArrayList<LineOrder>> loadedLineOrders = new HashMap<>();

        for (Order order : loadedOrders) {
            loadedLineOrders.put(
                    order.getIdOrder(),
                    controller.getLineOrdersByOrder(order.getIdOrder())
            );
        }

        return new OrderCardsData(loadedOrders, loadedLineOrders);
    }

    private void displayOrderCardsData(OrderCardsData data) {
        orders = data.getOrders();
        lineOrdersByOrder = data.getLineOrdersByOrder();

        refreshOrdersList();
    }

    private void displayLoadingError(Exception exception) {
        LoadingHelper.showError(
                ordersRowsPanel,
                "Impossible de charger les commandes."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de chargement",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void refreshOrdersList() {
        ordersRowsPanel.removeAll();

        int displayedCount = 0;

        for (Order order : orders) {
            if (mustDisplayOrder(order)) {
                ordersRowsPanel.add(createOrderRow(order));
                displayedCount++;
            }
        }

        if (displayedCount == 0) {
            LoadingHelper.showEmpty(
                    ordersRowsPanel,
                    "Aucune commande à afficher."
            );
        } else {
            TableFactory.updateAdaptiveTableCardSize(ordersCard, displayedCount);
        }

        ordersRowsPanel.revalidate();
        ordersRowsPanel.repaint();

        refreshPage();
        resizeOrdersCard(wrapper);
    }

    private boolean mustDisplayOrder(Order order) {
        String status = getOrderStatus(order);

        return switch (selectedFilter) {
            case FILTER_PREPARATION -> status.equals("Pending") || status.equals("InPreparation");
            case FILTER_READY -> status.equals("Ready");
            case FILTER_SERVED -> status.equals("Served");
            default -> true;
        };
    }

    private JPanel createOrderRow(Order order) {
        JButton detailButton = ButtonFactory.createSmallIconButton(
                "👁",
                () -> showOrderDetails(order)
        );

        return TableFactory.createDataRow(
                TableFactory.createCellLabel(
                        getOrderOrigin(order),
                        AppTheme.TEXT_PRIMARY
                ),
                TableFactory.createCellLabel(
                        getProductsSummary(order),
                        AppTheme.TEXT_PRIMARY
                ),
                TableFactory.createCellLabel(
                        DateHelper.formatShortDate(order.getOrderDate()),
                        AppTheme.TEXT_PRIMARY
                ),
                TableFactory.createCellLabel(
                        getOrderTime(order),
                        AppTheme.TEXT_PRIMARY
                ),
                TableFactory.createCellLabel(
                        StatusHelper.getFrenchStatus(getOrderStatus(order)),
                        StatusHelper.getStatusColor(getOrderStatus(order))
                ),
                TableFactory.createActionPanel(detailButton)
        );
    }

    private String getOrderOrigin(Order order) {
        if (order.getIsTakeAway()) {
            return "À emporter";
        }

        if (order.getTable() == null) {
            return "-";
        }

        return "Table " + order.getTable().getIdTable();
    }

    private String getOrderTime(Order order) {
        if (order.getIsTakeAway()) {
            return DateHelper.formatTime(order.getPickUpTime());
        }

        return "-";
    }

    private String getProductsSummary(Order order) {
        ArrayList<LineOrder> lines = lineOrdersByOrder.get(order.getIdOrder());

        if (lines == null || lines.isEmpty()) {
            return "Aucun produit";
        }

        StringBuilder summary = new StringBuilder();

        for (int index = 0; index < lines.size() && index < 2; index++) {
            if (index > 0) {
                summary.append(" + ");
            }

            LineOrder line = lines.get(index);

            summary.append(line.getQuantity())
                    .append("x ")
                    .append(line.getProduct().getProductLabel());
        }

        if (lines.size() > 2) {
            summary.append(" + ...");
        }

        return summary.toString();
    }

    private String getOrderStatus(Order order) {
        ArrayList<LineOrder> lines = lineOrdersByOrder.get(order.getIdOrder());

        if (lines == null || lines.isEmpty()) {
            return "Pending";
        }

        boolean allServed = true;
        boolean allReadyOrServed = true;
        boolean hasPreparation = false;

        for (LineOrder line : lines) {
            if (line == null || line.getStatus() == null) {
                allServed = false;
                allReadyOrServed = false;
                hasPreparation = true;
                continue;
            }

            String status = line.getStatus().getStatusLabel();

            if (!"Served".equals(status)) {
                allServed = false;
            }

            if (!"Ready".equals(status) && !"Served".equals(status)) {
                allReadyOrServed = false;
            }

            if ("InPreparation".equals(status) || "Pending".equals(status)) {
                hasPreparation = true;
            }
        }

        if (allServed) {
            return "Served";
        }

        if (allReadyOrServed) {
            return "Ready";
        }

        if (hasPreparation) {
            return "InPreparation";
        }

        return "Pending";
    }

    private void showOrderDetails(Order order) {
        ArrayList<LineOrder> lines = lineOrdersByOrder.get(order.getIdOrder());

        if (lines == null || lines.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Aucun produit dans cette commande.",
                    "Détails de la commande",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        StringBuilder message = new StringBuilder();

        for (LineOrder line : lines) {
            message.append(line.getQuantity())
                    .append("x ")
                    .append(line.getProduct().getProductLabel())
                    .append(" - ")
                    .append(StatusHelper.getFrenchStatus(line.getStatus().getStatusLabel()))
                    .append("\n");
        }

        JOptionPane.showMessageDialog(
                this,
                message.toString(),
                getOrderOrigin(order),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private static class OrderCardsData {

        private final ArrayList<Order> orders;
        private final Map<Integer, ArrayList<LineOrder>> lineOrdersByOrder;

        public OrderCardsData(ArrayList<Order> orders,
                              Map<Integer, ArrayList<LineOrder>> lineOrdersByOrder) {
            this.orders = orders;
            this.lineOrdersByOrder = lineOrdersByOrder;
        }

        public ArrayList<Order> getOrders() {
            return orders;
        }

        public Map<Integer, ArrayList<LineOrder>> getLineOrdersByOrder() {
            return lineOrdersByOrder;
        }
    }
}