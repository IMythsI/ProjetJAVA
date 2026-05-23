package viewPackage.Dashboard;

import viewPackage.AbstractPanel;
import viewPackage.CardFactory;
import viewPackage.MainJFrame;
import viewPackage.stylePackage.AppTheme;

import javax.swing.*;
import java.awt.*;

public class WaiterDashboardPanel extends AbstractPanel {
    private CardFactory cardFactory;

    public WaiterDashboardPanel(MainJFrame mainWindow) {
        super(mainWindow);
        cardFactory = new CardFactory();

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 70));
        header.setBackground(AppTheme.NAVBAR);
        header.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 30));

        JButton backButton = createBackButton();

        JLabel title = new JLabel("Restaurant Manager");
        title.setForeground(Color.WHITE);
        title.setFont(AppTheme.NAVBAR_FONT);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 17));
        leftPanel.setOpaque(false);
        leftPanel.add(backButton);
        leftPanel.add(title);

        header.add(leftPanel, BorderLayout.WEST);

        return header;
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(35, 20, 30, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("Dashboard Serveur");
        title.setFont(AppTheme.TITLE_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 45, 0);
        mainPanel.add(title, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 45, 0);
        mainPanel.add(createActionCardsPanel(), gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(createRecentOrdersPanel(), gbc);

        return mainPanel;
    }

    private JPanel createActionCardsPanel() {
        JPanel wrapper = new JPanel(new GridLayout(2, 2, 35, 35));
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(470, 270));

        wrapper.add(cardFactory.createRoleCard(
                "➕",
                "Nouvelle commande",
                "",
                () -> mainWindow.showTakeAwayOrderFormPanel()
        ));

        wrapper.add(cardFactory.createRoleCard(
                "\uD83D\uDDD3\uFE0F",
                "Réservations",
                "",
                () -> mainWindow.showBookingListPanel()
        ));

        wrapper.add(cardFactory.createRoleCard(
                "\uD83E\uDE91",
                "Tables",
                "",
                () -> mainWindow.showTableListPanel()
        ));

        wrapper.add(cardFactory.createRoleCard(
                "\uD83D\uDD52\uFE0F",
                "Commandes en cours",
                "",
                () -> mainWindow.showOrderCardsPanel()
        ));

        return wrapper;
    }

    private JPanel createRecentOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppTheme.CARD);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panel.setPreferredSize(new Dimension(470, 150));

        JLabel title = new JLabel("Commandes récentes");
        title.setFont(new Font("Arial", Font.BOLD, 15));
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JPanel listPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        listPanel.setOpaque(false);

        listPanel.add(createRecentOrderLine("Table 4", "Pizza + Coca", "En préparation", AppTheme.WARNING));
        listPanel.add(createRecentOrderLine("Table 2", "Burger + Frites", "Prête", AppTheme.SUCCESS));
        listPanel.add(createRecentOrderLine("Table 7", "Salade César", "Servie", AppTheme.TEXT_SECONDARY));

        panel.add(title, BorderLayout.NORTH);
        panel.add(listPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRecentOrderLine(String table, String products, String status, Color statusColor) {
        JPanel line = new JPanel(new GridLayout(1, 3));
        line.setOpaque(false);

        JLabel tableLabel = new JLabel(table);
        JLabel productsLabel = new JLabel(products);
        JLabel statusLabel = new JLabel(status);

        tableLabel.setFont(AppTheme.BUTTON_FONT);
        productsLabel.setFont(AppTheme.BUTTON_FONT);
        statusLabel.setFont(AppTheme.BUTTON_FONT);
        statusLabel.setForeground(statusColor);

        line.add(tableLabel);
        line.add(productsLabel);
        line.add(statusLabel);

        return line;
    }
}