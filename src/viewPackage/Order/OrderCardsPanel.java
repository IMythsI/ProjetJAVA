package viewPackage.Order;

import controllerPackage.ApplicationController;
import exceptionPackage.*;
import modelPackage.*;
import viewPackage.AbstractPanel;
import viewPackage.MainJFrame;
import lib.WrapLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderCardsPanel extends AbstractPanel {
    private ApplicationController controller;

    private JCheckBox tableOrdersCheckBox;
    private JCheckBox takeAwayOrdersCheckBox;
    private JCheckBox finishedOrdersCheckBox;
    private JButton refreshButton;

    private JPanel cardsPanel;
    private ArrayList<Order> orders;
    private Map<Integer, ArrayList<LineOrder>> lineOrdersByOrder;

    public OrderCardsPanel(MainJFrame mainWindow) {
        super(mainWindow);

        controller = new ApplicationController();
        orders = new ArrayList<>();
        lineOrdersByOrder = new HashMap<>();

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);

        loadOrders();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(createBackButton(), BorderLayout.WEST);

        JLabel title = new JLabel("Commandes", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));

        topPanel.add(title, BorderLayout.CENTER);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));

        centerPanel.add(createFilterPanel(), BorderLayout.NORTH);

        cardsPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 18, 18));

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setBorder(null);

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));

        tableOrdersCheckBox = new JCheckBox("Commandes sur table", true);
        takeAwayOrdersCheckBox = new JCheckBox("Commandes à emporter", true);
        finishedOrdersCheckBox = new JCheckBox("Commandes terminées", false);

        refreshButton = new JButton("Refresh");

        tableOrdersCheckBox.addActionListener(event -> refreshCards());
        takeAwayOrdersCheckBox.addActionListener(event -> refreshCards());
        finishedOrdersCheckBox.addActionListener(event -> refreshCards());

        refreshButton.addActionListener(event -> {loadOrders();refreshCards();});

        filterPanel.add(new JLabel("Afficher :"));
        filterPanel.add(tableOrdersCheckBox);
        filterPanel.add(takeAwayOrdersCheckBox);
        filterPanel.add(finishedOrdersCheckBox);
        filterPanel.add(refreshButton);

        return filterPanel;
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

                    refreshCards();

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
        cardsPanel.removeAll();

        JLabel loadingLabel = new JLabel("Chargement des commandes...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 18));

        cardsPanel.add(loadingLabel);

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private void refreshCards() {
        cardsPanel.removeAll();

        boolean showTableOrders = tableOrdersCheckBox.isSelected();
        boolean showTakeAwayOrders = takeAwayOrdersCheckBox.isSelected();
        boolean showFinishedOrders = finishedOrdersCheckBox.isSelected();

        ArrayList<Order> filteredOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getIsTakeAway() && !showTakeAwayOrders) {
                continue;
            }

            if (!order.getIsTakeAway() && !showTableOrders) {
                continue;
            }

            if (isFinished(order) && !showFinishedOrders) {
                continue;
            }

            filteredOrders.add(order);
        }

        filteredOrders.sort((o1, o2) -> {
            boolean firstFinished = isFinished(o1);
            boolean secondFinished = isFinished(o2);

            if (firstFinished && !secondFinished) {
                return 1;
            }

            if (!firstFinished && secondFinished) {
                return -1;
            }

            return o1.getIdOrder().compareTo(o2.getIdOrder());
        });

        for (Order order : filteredOrders) {
            ArrayList<LineOrder> lines = lineOrdersByOrder.get(order.getIdOrder());

            if (lines == null) {
                lines = new ArrayList<>();
            }

            cardsPanel.add(new OrderCardPanel(order, lines));
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private boolean isFinished(Order order) {
        ArrayList<LineOrder> lines = lineOrdersByOrder.get(order.getIdOrder());

        if (lines == null || lines.isEmpty()) {
            return false;
        }

        for (LineOrder line : lines) {
            if (!line.getStatus().getStatusLabel().equals("Served")) {
                return false;
            }
        }

        return true;
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