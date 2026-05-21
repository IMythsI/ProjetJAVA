package viewPackage.Dashboard;

import controllerPackage.ApplicationController;
import exceptionPackage.TableException;
import lib.WrapLayout;
import modelPackage.Table;
import viewPackage.AbstractPanel;
import viewPackage.MainJFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WaiterDashboardPanel extends AbstractPanel {
    private ApplicationController controller;

    public WaiterDashboardPanel(MainJFrame mainWindow) {
        super(mainWindow);
        controller = new ApplicationController();

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton backButton = createBackButton();

        JLabel title = new JLabel("Espace serveur", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);

        return topPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 25, 0));

        mainPanel.add(createTablesPanel());
        mainPanel.add(createInfoPanel());

        return mainPanel;
    }

    private JPanel createTablesPanel() {

        JPanel wrapperPanel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                "Plan des tables",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel tablesPanel = new JPanel(
                new WrapLayout(
                        FlowLayout.LEFT,
                        15,
                        15
                )
        );

        try {

            ArrayList<Table> tables = controller.getAllTables();

            for (Table table : tables) {
                tablesPanel.add(createTableButton(table));
            }

        } catch (TableException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        JScrollPane scrollPane = new JScrollPane(tablesPanel);

        scrollPane.setBorder(null);

        wrapperPanel.add(title, BorderLayout.NORTH);
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);

        return wrapperPanel;
    }

    private JButton createTableButton(Table table) {
        JButton button = new JButton();
        String statusLabel = table.getStatus().getStatusLabel();
        button.setText(
                "<html><center>"
                        + "Table " + table.getIdTable()
                        + "<br>"
                        + table.getNbSeats() + " places"
                        + "</center></html>"
        );

        button.setPreferredSize(new Dimension(90, 70));





        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(getColorByStatus(statusLabel));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setToolTipText(
                "Table "
                        + table.getIdTable()
                        + " - "
                        + table.getNbSeats()
                        + " places"
                        + " - "
                        + statusLabel
        );
        button.addActionListener(event -> mainWindow.showTableDetailPanel(table));
        return button;
    }

    private Color getColorByStatus(String statusLabel) {
        return switch (statusLabel) {
            case "Available" -> new Color(70, 191, 70);
            case "Reserved" -> new Color(255, 167, 49);
            case "Occupied" -> new Color(230, 78, 78);
            default -> Color.LIGHT_GRAY;
        };
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel("Informations serveur", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));

        actionsPanel.add(createActionButton("Voir les commandes", () -> mainWindow.showOrderListPanel()));
        actionsPanel.add(Box.createVerticalStrut(15));

        actionsPanel.add(createActionButton("Voir les réservations", () -> mainWindow.showBookingListPanel()));
        actionsPanel.add(Box.createVerticalStrut(15));

        actionsPanel.add(createActionButton("Voir les allergies", () -> mainWindow.showAllergiesPanel()));
        actionsPanel.add(Box.createVerticalStrut(15));

        actionsPanel.add(createActionButton("Créer une commande à emporter", () -> mainWindow.showTakeAwayOrderFormPanel()));

        infoPanel.add(title, BorderLayout.NORTH);
        infoPanel.add(actionsPanel, BorderLayout.CENTER);

        return infoPanel;
    }

    private JButton createActionButton(String text, Runnable action) {
        JButton button = new JButton(text);

        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(event -> action.run());

        return button;
    }
}