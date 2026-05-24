package viewPackage.Table;

import controllerPackage.ApplicationController;
import exceptionPackage.TableException;
import modelPackage.Table;
import viewPackage.AbstractPanel;
import viewPackage.CardFactory;
import viewPackage.MainJFrame;
import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TableListPanel extends AbstractPanel {
    private ApplicationController controller;
    private JPanel tablesPanel;
    private ArrayList<Table> loadedTables;

    public TableListPanel(MainJFrame mainWindow) {
        super(mainWindow);
        loadedTables = new ArrayList<>();

        controller = new ApplicationController();

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createScrollableMainContent(), BorderLayout.CENTER);

        loadTables();
    }

    private JScrollPane createScrollableMainContent() {
        JPanel content = createMainContent();

        JScrollPane scrollPane = new JScrollPane(content);

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

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(35, 30, 35, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("Liste des tables");
        title.setFont(AppTheme.TITLE_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("État des tables en temps réel");
        subtitle.setFont(AppTheme.SUBTITLE_FONT);
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);

        tablesPanel = new JPanel();
        tablesPanel.setOpaque(false);
        tablesPanel.setPreferredSize(new Dimension(850, 450));

        tablesPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                refreshTablesGrid();
            }
        });

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 8, 0);
        mainPanel.add(title, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 45, 0);
        mainPanel.add(subtitle, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 35, 0);
        mainPanel.add(tablesPanel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(createLegendPanel(), gbc);

        return mainPanel;
    }

    private void loadTables() {
        try {
            loadedTables = controller.getAllTables();
            refreshTablesGrid();

        } catch (TableException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshTablesGrid() {
        if (tablesPanel == null || loadedTables == null) {
            return;
        }

        tablesPanel.removeAll();

        int cardWidth = 130;
        int cardHeight = 160;
        int gap = 25;

        int availableWidth = tablesPanel.getWidth();

        if (availableWidth <= 0) {
            availableWidth = 850;
        }

        int columns = Math.max(1, availableWidth / (cardWidth + gap));
        int rows = (int) Math.ceil((double) loadedTables.size() / columns);

        tablesPanel.setLayout(new GridLayout(rows, columns, gap, gap));

        tablesPanel.setPreferredSize(
                new Dimension(
                        850,
                        rows * cardHeight + Math.max(0, rows - 1) * gap
                )
        );

        for (Table table : loadedTables) {
            tablesPanel.add(createTableCard(table));
        }

        tablesPanel.revalidate();
        tablesPanel.repaint();
    }

    private JButton createTableCard(Table table) {
        JButton card = new CardFactory.RoundedButton(25);

        String statusLabel = table.getStatus().getStatusLabel();

        card.setLayout(new BorderLayout(5, 8));

        card.setPreferredSize(new Dimension(130, 160));
        card.setMinimumSize(new Dimension(130, 160));
        card.setMaximumSize(new Dimension(130, 160));

        card.setBackground(AppTheme.CARD);

        card.setContentAreaFilled(false);
        card.setOpaque(false);

        card.setFocusPainted(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JLabel icon = new JLabel("\uD83E\uDE91", SwingConstants.CENTER);
        icon.setPreferredSize(new Dimension(35, 35));
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        icon.setForeground(AppTheme.PRIMARY);

        JLabel tableLabel = new JLabel("Table " + table.getIdTable(), SwingConstants.CENTER);
        tableLabel.setFont(AppTheme.BUTTON_FONT);
        tableLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel seatsLabel = new JLabel(table.getNbSeats() + " places", SwingConstants.CENTER);
        seatsLabel.setFont(AppTheme.SUBTITLE_FONT);
        seatsLabel.setForeground(AppTheme.TEXT_SECONDARY);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        statusPanel.setOpaque(false);

        JLabel dot = new JLabel("\uD83D\uDD18");
        dot.setForeground(getColorByStatus(statusLabel));

        JLabel statusText = new JLabel(getFrenchStatus(statusLabel));
        statusText.setFont(AppTheme.BUTTON_FONT);
        statusText.setForeground(AppTheme.TEXT_PRIMARY);

        statusPanel.add(dot);
        statusPanel.add(statusText);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        centerPanel.setOpaque(false);
        centerPanel.add(tableLabel);
        centerPanel.add(seatsLabel);

        card.add(icon, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(statusPanel, BorderLayout.SOUTH);

        card.addActionListener(event -> mainWindow.showTableDetailPanel(table));

        return card;
    }

    private JPanel createLegendPanel() {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 0));

        legend.setBackground(AppTheme.CARD);
        legend.setPreferredSize(new Dimension(620, 50));
        legend.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        legend.putClientProperty("FlatLaf.style", "arc:18");

        legend.add(createLegendItem("Libre", new Color(70, 191, 70)));
        legend.add(createLegendItem("Occupée", new Color(230, 78, 78)));
        legend.add(createLegendItem("Réservée", new Color(255, 167, 49)));
        legend.add(createLegendItem("Hors service", Color.LIGHT_GRAY));

        return legend;
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 13));
        item.setOpaque(false);

        JLabel dot = new JLabel("●");
        dot.setForeground(color);
        dot.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel label = new JLabel(text);
        label.setFont(AppTheme.BUTTON_FONT);
        label.setForeground(AppTheme.TEXT_PRIMARY);

        item.add(dot);
        item.add(label);

        return item;
    }

    private Color getColorByStatus(String statusLabel) {
        return switch (statusLabel) {
            case "Available" -> new Color(70, 191, 70);
            case "Reserved" -> new Color(255, 167, 49);
            case "Occupied" -> new Color(230, 78, 78);
            default -> Color.LIGHT_GRAY;
        };
    }

    private String getFrenchStatus(String statusLabel) {
        return switch (statusLabel) {
            case "Available" -> "Libre";
            case "Reserved" -> "Réservée";
            case "Occupied" -> "Occupée";
            default -> "Hors service";
        };
    }
}