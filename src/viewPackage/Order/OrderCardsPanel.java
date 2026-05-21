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

public class OrderCardsPanel extends AbstractPanel {
    private ApplicationController controller;

    private JCheckBox tableOrdersCheckBox;
    private JCheckBox takeAwayOrdersCheckBox;
    private JCheckBox finishedOrdersCheckBox;

    private JPanel cardsPanel;
    private ArrayList<Order> orders;

    public OrderCardsPanel(MainJFrame mainWindow) {
        super(mainWindow);

        controller = new ApplicationController();
        orders = new ArrayList<>();

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
        scrollPane.setBorder(null);

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));

        tableOrdersCheckBox = new JCheckBox("Commandes sur table", true);
        takeAwayOrdersCheckBox = new JCheckBox("Commandes à emporter", true);
        finishedOrdersCheckBox = new JCheckBox("Commandes terminées", false);

        tableOrdersCheckBox.addActionListener(event -> refreshCards());
        takeAwayOrdersCheckBox.addActionListener(event -> refreshCards());
        finishedOrdersCheckBox.addActionListener(event -> refreshCards());

        filterPanel.add(new JLabel("Afficher :"));
        filterPanel.add(tableOrdersCheckBox);
        filterPanel.add(takeAwayOrdersCheckBox);
        filterPanel.add(finishedOrdersCheckBox);

        return filterPanel;
    }

    private void loadOrders() {
        try {
            orders = controller.getAllOrders();
            refreshCards();

        } catch (OrderException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
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
            if (isFinished(o1) && !isFinished(o2)) {
                return 1;
            }

            if (!isFinished(o1) && isFinished(o2)) {
                return -1;
            }

            return o1.getIdOrder().compareTo(o2.getIdOrder());
        });

        for (Order order : filteredOrders) {
            cardsPanel.add(new OrderCardPanel(order));
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private boolean isFinished(Order order) {
        return order.getStatus() != null
                && order.getStatus().getStatusLabel().equals("Served");
    }
}