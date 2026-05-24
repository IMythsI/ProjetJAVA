package viewPackage.Order;

import controllerPackage.ApplicationController;
import modelPackage.*;
import viewPackage.AbstractPanel;
import viewPackage.MainJFrame;
import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderCardsPanel extends AbstractPanel {
    private ApplicationController controller;

    private JButton allButton;
    private JButton preparationButton;
    private JButton readyButton;
    private JButton servedButton;
    private JButton refreshButton;

    private JPanel ordersListPanel;
    private JPanel ordersCard;

    private ArrayList<Order> orders;
    private Map<Integer, ArrayList<LineOrder>> lineOrdersByOrder;

    private String selectedFilter = "Toutes";

    public OrderCardsPanel(MainJFrame mainWindow) {
        super(mainWindow);

        controller = new ApplicationController();
        orders = new ArrayList<>();
        lineOrdersByOrder = new HashMap<>();

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createScrollableMainPanel(), BorderLayout.CENTER);

        loadOrders();
    }

    private JScrollPane createScrollableMainPanel() {
        JScrollPane scrollPane = new JScrollPane(createMainPanel());

        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(25);
        scrollPane.getViewport().setBackground(AppTheme.BACKGROUND);

        return scrollPane;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());

        header.setPreferredSize(new Dimension(0, 70));
        header.setBackground(AppTheme.NAVBAR);
        header.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 30));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 17));
        leftPanel.setOpaque(false);

        JButton backButton = createBackButton();

        JLabel title = new JLabel("Restaurant Manager");
        title.setForeground(Color.WHITE);
        title.setFont(AppTheme.NAVBAR_FONT);

        leftPanel.add(backButton);
        leftPanel.add(title);

        header.add(leftPanel, BorderLayout.WEST);

        return header;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());

        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(35, 30, 35, 30));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("Commandes en cours");
        title.setFont(AppTheme.TITLE_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Suivi des commandes en temps réel");
        subtitle.setFont(AppTheme.SUBTITLE_FONT);
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 8, 0);
        mainPanel.add(title, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 35, 0);
        mainPanel.add(subtitle, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(createFilterPanel(), gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(createOrdersCard(), gbc);

        return mainPanel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 55, 0));

        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(900, 45));

        allButton = createFilterButton("Toutes");
        preparationButton = createFilterButton("En préparation");
        readyButton = createFilterButton("Prêtes");
        servedButton = createFilterButton("Servies");

        refreshButton = new JButton("Actualiser");
        refreshButton.setFont(AppTheme.BUTTON_FONT);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.putClientProperty("JButton.buttonType", "roundRect");
        refreshButton.addActionListener(event -> loadOrders());

        panel.add(allButton);
        panel.add(preparationButton);
        panel.add(readyButton);
        panel.add(servedButton);
        panel.add(refreshButton);

        updateFilterButtons();

        return panel;
    }

    private JButton createFilterButton(String text) {
        JButton button = new JButton(text);

        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(AppTheme.TEXT_SECONDARY);
        button.setBackground(AppTheme.BACKGROUND);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(event -> {
            selectedFilter = text;
            updateFilterButtons();
            refreshOrdersList();
        });

        return button;
    }

    private void updateFilterButtons() {
        styleFilterButton(allButton, selectedFilter.equals("Toutes"));
        styleFilterButton(preparationButton, selectedFilter.equals("En préparation"));
        styleFilterButton(readyButton, selectedFilter.equals("Prêtes"));
        styleFilterButton(servedButton, selectedFilter.equals("Servies"));
    }

    private void styleFilterButton(JButton button, boolean selected) {
        if (selected) {
            button.setForeground(AppTheme.PRIMARY);
            button.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, AppTheme.PRIMARY));
        } else {
            button.setForeground(AppTheme.TEXT_SECONDARY);
            button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        }
    }

    private JPanel createOrdersCard() {
        ordersCard = new JPanel(new BorderLayout());

        ordersCard.setBackground(AppTheme.CARD);
        ordersCard.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));
        ordersCard.putClientProperty("FlatLaf.style", "arc:20");

        ordersListPanel = new JPanel();
        ordersListPanel.setBackground(Color.WHITE);
        ordersListPanel.setLayout(new BoxLayout(ordersListPanel, BoxLayout.Y_AXIS));

        ordersCard.add(ordersListPanel, BorderLayout.CENTER);

        updateOrdersCardSize(0);

        return ordersCard;
    }

    private void updateOrdersCardSize(int displayedCount) {
        int height = 80 + displayedCount * 72;

        if (height < 420) {
            height = 420;
        }

        ordersCard.setPreferredSize(new Dimension(850, height));
        ordersCard.setMinimumSize(new Dimension(800, 420));
    }

    private void loadOrders() {
        showLoading();

        SwingWorker<OrderCardsData, Void> worker = new SwingWorker<>() {
            @Override
            protected OrderCardsData doInBackground() throws Exception {
                ArrayList<Order> loadedOrders = controller.getAllOrders();
                Map<Integer, ArrayList<LineOrder>> loadedLines = new HashMap<>();

                for (Order order : loadedOrders) {
                    loadedLines.put(
                            order.getIdOrder(),
                            controller.getLineOrdersByOrder(order.getIdOrder())
                    );
                }

                return new OrderCardsData(loadedOrders, loadedLines);
            }

            @Override
            protected void done() {
                try {
                    OrderCardsData data = get();

                    orders = data.orders;
                    lineOrdersByOrder = data.lineOrdersByOrder;

                    refreshOrdersList();

                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(
                            OrderCardsPanel.this,
                            "Erreur lors du chargement des commandes.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void showLoading() {
        if (ordersListPanel == null) {
            return;
        }

        ordersListPanel.removeAll();

        JLabel loadingLabel = new JLabel("Chargement des commandes...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ordersListPanel.add(Box.createVerticalStrut(30));
        ordersListPanel.add(loadingLabel);

        ordersListPanel.revalidate();
        ordersListPanel.repaint();
    }

    private void refreshOrdersList() {
        ordersListPanel.removeAll();

        int displayedCount = 0;

        for (Order order : orders) {
            if (mustDisplayOrder(order)) {
                ordersListPanel.add(createOrderRow(order));
                displayedCount++;
            }
        }

        updateOrdersCardSize(displayedCount);

        ordersListPanel.revalidate();
        ordersListPanel.repaint();

        ordersCard.revalidate();
        ordersCard.repaint();

        revalidate();
        repaint();
    }

    private boolean mustDisplayOrder(Order order) {
        String status = getOrderStatus(order);

        return switch (selectedFilter) {
            case "En préparation" -> status.equals("Pending") || status.equals("InPreparation");
            case "Prêtes" -> status.equals("Ready");
            case "Servies" -> status.equals("Served");
            default -> true;
        };
    }

    private JPanel createOrderRow(Order order) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        container.setPreferredSize(new Dimension(850, 72));

        JPanel row = new JPanel(new GridLayout(1, 5, 20, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        JLabel originLabel = createRowLabel(getOrderOrigin(order), AppTheme.TEXT_PRIMARY);
        JLabel productsLabel = createRowLabel(getProductsSummary(order), AppTheme.TEXT_PRIMARY);
        JLabel timeLabel = createRowLabel(getOrderTime(order), AppTheme.TEXT_PRIMARY);
        JLabel statusLabel = createRowLabel(
                getFrenchStatus(getOrderStatus(order)),
                getStatusColor(getOrderStatus(order))
        );

        JPanel detailPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        detailPanel.setOpaque(false);

        JButton detailButton = new JButton("👁");
        detailButton.setPreferredSize(new Dimension(55, 32));
        detailButton.setFocusPainted(false);
        detailButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailButton.putClientProperty("JButton.buttonType", "roundRect");
        detailButton.addActionListener(event -> showOrderDetails(order));

        detailPanel.add(detailButton);

        row.add(originLabel);
        row.add(productsLabel);
        row.add(timeLabel);
        row.add(statusLabel);
        row.add(detailPanel);

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(235, 235, 235));

        container.add(row, BorderLayout.CENTER);
        container.add(separator, BorderLayout.SOUTH);

        return container;
    }

    private JLabel createRowLabel(String text, Color color) {
        JLabel label = new JLabel(text);

        label.setFont(AppTheme.BUTTON_FONT);
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.LEFT);

        return label;
    }

    private String getOrderOrigin(Order order) {
        if (order.getIsTakeAway()) {
            return "À emporter";
        }

        return "Table " + order.getTable().getIdTable();
    }

    private String getOrderTime(Order order) {
        if (order.getIsTakeAway() && order.getPickUpTime() != null) {
            return order.getPickUpTime().toString();
        }

        return "";
    }

    private String getProductsSummary(Order order) {
        ArrayList<LineOrder> lines = lineOrdersByOrder.get(order.getIdOrder());

        if (lines == null || lines.isEmpty()) {
            return "Aucun produit";
        }

        StringBuilder summary = new StringBuilder();

        for (int i = 0; i < lines.size() && i < 2; i++) {
            if (i > 0) {
                summary.append(" + ");
            }

            summary.append(lines.get(i).getProduct().getProductLabel());
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
        boolean allReady = true;
        boolean hasPreparation = false;

        for (LineOrder line : lines) {
            String status = line.getStatus().getStatusLabel();

            if (!status.equals("Served")) {
                allServed = false;
            }

            if (!status.equals("Ready") && !status.equals("Served")) {
                allReady = false;
            }

            if (status.equals("InPreparation") || status.equals("Pending")) {
                hasPreparation = true;
            }
        }

        if (allServed) {
            return "Served";
        }

        if (allReady) {
            return "Ready";
        }

        if (hasPreparation) {
            return "InPreparation";
        }

        return "Pending";
    }

    private String getFrenchStatus(String status) {
        return switch (status) {
            case "Ready" -> "Prête";
            case "Served" -> "Servie";
            case "InPreparation" -> "En préparation";
            case "Pending" -> "En attente";
            default -> status;
        };
    }

    private Color getStatusColor(String status) {
        return switch (status) {
            case "Ready" -> AppTheme.SUCCESS;
            case "Served" -> AppTheme.TEXT_SECONDARY;
            case "InPreparation", "Pending" -> AppTheme.WARNING;
            default -> AppTheme.TEXT_PRIMARY;
        };
    }

    private void showOrderDetails(Order order) {
        ArrayList<LineOrder> lines = lineOrdersByOrder.get(order.getIdOrder());

        if (lines == null || lines.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun produit dans cette commande.");
            return;
        }

        StringBuilder message = new StringBuilder();

        for (LineOrder line : lines) {
            message.append(line.getQuantity())
                    .append("x ")
                    .append(line.getProduct().getProductLabel())
                    .append(" - ")
                    .append(getFrenchStatus(line.getStatus().getStatusLabel()))
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
        private ArrayList<Order> orders;
        private Map<Integer, ArrayList<LineOrder>> lineOrdersByOrder;

        public OrderCardsData(ArrayList<Order> orders,
                              Map<Integer, ArrayList<LineOrder>> lineOrdersByOrder) {
            this.orders = orders;
            this.lineOrdersByOrder = lineOrdersByOrder;
        }
    }
}