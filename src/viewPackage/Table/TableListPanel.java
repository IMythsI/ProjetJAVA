package viewPackage.Table;

import controllerPackage.ApplicationController;
import modelPackage.Table;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TableListPanel extends AppPage {

    private static final int TABLE_CARD_WIDTH = 155;
    private static final int TABLE_CARD_HEIGHT = 170;

    private final ApplicationController controller;

    private JPanel tablesCard;
    private JPanel tablesContentPanel;

    private int currentTableCount;

    public TableListPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(
                createPageTitle("Liste des tables"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle("Consultez l’état des tables du restaurant"),
                1,
                new Insets(0, 0, 30, 0)
        );

        addCentered(
                createTablesCardWrapper(),
                2,
                new Insets(0, 0, 25, 0)
        );

        addCentered(
                createLegendPanel(),
                3,
                new Insets(0, 0, 0, 0)
        );

        loadTables();
    }

    private JPanel createTablesCardWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        tablesCard = createTablesCard();
        wrapper.add(tablesCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeTablesCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createTablesCard() {
        JPanel card = CardFactory.createAdaptiveCard(
                AppTheme.TABLE_CARD_MAX_WIDTH,
                AppTheme.TABLE_MIN_HEIGHT
        );

        card.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Tables du restaurant");
        titleLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        tablesContentPanel = new JPanel(new BorderLayout());
        tablesContentPanel.setOpaque(false);

        LoadingHelper.showEmpty(
                tablesContentPanel,
                "Chargement des tables..."
        );

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(tablesContentPanel, BorderLayout.CENTER);

        return card;
    }

    private void resizeTablesCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.TABLE_CARD_MAX_WIDTH;
        int minWidth = 100;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        tablesCard.setPreferredSize(new Dimension(
                newWidth,
                calculateTablesCardHeight(currentTableCount, newWidth)
        ));

        tablesCard.setMinimumSize(new Dimension(
                minWidth,
                AppTheme.TABLE_MIN_HEIGHT
        ));

        tablesCard.setMaximumSize(new Dimension(
                maxWidth,
                Integer.MAX_VALUE
        ));

        tablesCard.revalidate();
        tablesCard.repaint();
    }

    private void loadTables() {
        LoadingHelper.runWithLoading(
                tablesContentPanel,
                "Chargement des tables...",
                controller::getAllTables,
                this::displayTables,
                this::displayLoadingError
        );
    }

    private void displayTables(ArrayList<Table> tables) {
        tablesContentPanel.removeAll();

        if (tables == null || tables.isEmpty()) {
            LoadingHelper.showEmpty(
                    tablesContentPanel,
                    "Aucune table à afficher."
            );
            return;
        }

        JPanel gridPanel = new JPanel(new lib.WrapLayout(
                FlowLayout.CENTER,
                AppTheme.COMPONENT_GAP_MEDIUM,
                AppTheme.COMPONENT_GAP_MEDIUM
        ));

        gridPanel.setOpaque(false);

        for (Table table : tables) {
            gridPanel.add(createTableCard(table));
        }

        currentTableCount = tables.size();
        updateTablesCardHeight(tables.size());

        tablesContentPanel.add(gridPanel, BorderLayout.CENTER);
        tablesContentPanel.revalidate();
        tablesContentPanel.repaint();

        refreshPage();
    }

    private void updateTablesCardHeight(int tableCount) {
        currentTableCount = tableCount;

        int currentWidth = tablesCard.getPreferredSize().width;

        tablesCard.setPreferredSize(new Dimension(
                currentWidth,
                calculateTablesCardHeight(tableCount, currentWidth)
        ));

        tablesCard.revalidate();
        tablesCard.repaint();
    }

    private int calculateTablesCardHeight(int tableCount, int cardWidth) {
        if (tableCount <= 0) {
            return AppTheme.TABLE_MIN_HEIGHT;
        }

        int availableWidth = cardWidth
                - AppTheme.CARD_PADDING_LEFT
                - AppTheme.CARD_PADDING_RIGHT
                - 40;

        int cardWidthWithGap = TABLE_CARD_WIDTH + AppTheme.COMPONENT_GAP_MEDIUM;

        int columns = Math.max(1, availableWidth / cardWidthWithGap);
        int rows = (int) Math.ceil((double) tableCount / columns);

        int titleHeight = 55;
        int verticalPadding = AppTheme.CARD_PADDING_TOP + AppTheme.CARD_PADDING_BOTTOM;
        int rowsHeight = rows * TABLE_CARD_HEIGHT;
        int gapsHeight = Math.max(0, rows - 1) * AppTheme.COMPONENT_GAP_MEDIUM;

        int totalHeight = titleHeight + verticalPadding + rowsHeight + gapsHeight + 30;

        return Math.max(totalHeight, AppTheme.TABLE_MIN_HEIGHT);
    }

    private void displayLoadingError(Exception exception) {
        LoadingHelper.showError(
                tablesContentPanel,
                "Impossible de charger les tables."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de chargement",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private JButton createTableCard(Table table) {
        JButton card = new RoundedButton("", AppTheme.CARD_ARC);

        card.setLayout(new BorderLayout(
                AppTheme.COMPONENT_GAP_SMALL,
                AppTheme.COMPONENT_GAP_SMALL
        ));

        card.setPreferredSize(new Dimension(TABLE_CARD_WIDTH, TABLE_CARD_HEIGHT));
        card.setMinimumSize(new Dimension(TABLE_CARD_WIDTH, TABLE_CARD_HEIGHT));
        card.setBackground(AppTheme.CARD);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createEmptyBorder(18, 16, 18, 16));

        String statusLabel = table.getStatus().getStatusLabel();

        JLabel iconLabel = new JLabel("🪑", SwingConstants.CENTER);
        iconLabel.setFont(AppTheme.EMOJI_FONT);
        iconLabel.setPreferredSize(AppTheme.SMALL_ICON_BUTTON_SIZE);
        iconLabel.setForeground(AppTheme.PRIMARY);

        JLabel tableLabel = new JLabel("Table " + table.getIdTable(), SwingConstants.CENTER);
        tableLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        tableLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel seatsLabel = new JLabel(table.getNbSeats() + " places", SwingConstants.CENTER);
        seatsLabel.setFont(AppTheme.TEXT_FONT);
        seatsLabel.setForeground(AppTheme.TEXT_SECONDARY);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        centerPanel.setOpaque(false);
        centerPanel.add(tableLabel);
        centerPanel.add(seatsLabel);

        JPanel statusPanel = createStatusPanel(statusLabel);

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(statusPanel, BorderLayout.SOUTH);

        card.addActionListener(event -> mainWindow.showTableDetailPanel(table));

        return card;
    }

    private JPanel createStatusPanel(String statusLabel) {
        JPanel panel = new JPanel(new FlowLayout(
                FlowLayout.CENTER,
                AppTheme.COMPONENT_GAP_SMALL,
                0
        ));

        panel.setOpaque(false);

        JLabel dotLabel = new JLabel("●");
        dotLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        dotLabel.setForeground(StatusHelper.getStatusColor(statusLabel));

        JLabel textLabel = new JLabel(StatusHelper.getFrenchStatus(statusLabel));
        textLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        textLabel.setForeground(AppTheme.TEXT_PRIMARY);

        panel.add(dotLabel);
        panel.add(textLabel);

        return panel;
    }

    private JPanel createLegendPanel() {
        JPanel legend = CardFactory.createAdaptiveCard(650, 70);

        legend.setLayout(new FlowLayout(
                FlowLayout.CENTER,
                AppTheme.COMPONENT_GAP_LARGE,
                AppTheme.COMPONENT_GAP_SMALL
        ));

        legend.add(createLegendItem("Libre", StatusHelper.getStatusColor("Available")));
        legend.add(createLegendItem("Réservée", StatusHelper.getStatusColor("Reserved")));
        legend.add(createLegendItem("Occupée", StatusHelper.getStatusColor("Occupied")));

        return legend;
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(
                FlowLayout.CENTER,
                AppTheme.COMPONENT_GAP_SMALL,
                0
        ));

        item.setOpaque(false);

        JLabel dotLabel = new JLabel("●");
        dotLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        dotLabel.setForeground(color);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(AppTheme.TEXT_FONT);
        textLabel.setForeground(AppTheme.TEXT_PRIMARY);

        item.add(dotLabel);
        item.add(textLabel);

        return item;
    }
}