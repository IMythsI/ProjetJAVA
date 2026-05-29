package viewPackage.Order;

import controllerPackage.ApplicationController;
import exceptionPackage.LineOrderException;
import modelPackage.LineOrder;
import modelPackage.Order;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OrderDetailPanel extends AppPage {

    private final ApplicationController controller;
    private final Order order;

    private JPanel linesCard;
    private JPanel linesPanel;
    private JLabel globalStatusLabel;

    public OrderDetailPanel(MainJFrame mainWindow, Order order) {
        super(mainWindow, true);

        this.controller = new ApplicationController();
        this.order = order;

        addCentered(
                createPageTitle("Détail de la commande"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle(createSubtitle()),
                1,
                new Insets(0, 0, 25, 0)
        );

        addCentered(
                createTopActionPanel(),
                2,
                new Insets(0, 0, 25, 0)
        );

        addCentered(
                createLinesCardWrapper(),
                3,
                new Insets(0, 0, 0, 0)
        );

        loadLineOrders();
    }

    private String createSubtitle() {
        if (order == null) {
            return "Commande inconnue";
        }

        if (order.getIsTakeAway()) {
            return "Commande à emporter n°" + order.getIdOrder();
        }

        if (order.getTable() == null) {
            return "Commande n°" + order.getIdOrder();
        }

        return "Commande n°"
                + order.getIdOrder()
                + " — Table "
                + order.getTable().getIdTable();
    }


    private JPanel createTopActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(
                FlowLayout.CENTER,
                AppTheme.COMPONENT_GAP_MEDIUM,
                0
        ));

        panel.setOpaque(false);

        globalStatusLabel = new JLabel("Statut global : chargement...");
        globalStatusLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        globalStatusLabel.setForeground(AppTheme.TEXT_SECONDARY);

        JButton refreshButton = ButtonFactory.createSecondaryButton(
                "Actualiser",
                this::loadLineOrders
        );

        JButton backToOrdersButton = ButtonFactory.createPrimaryButton(
                "Retour aux commandes",
                () -> mainWindow.showOrderCardsPanel()
        );

        backToOrdersButton.setPreferredSize(new Dimension(
                220,
                AppTheme.BUTTON_HEIGHT
        ));

        panel.add(globalStatusLabel);
        panel.add(refreshButton);
        panel.add(backToOrdersButton);

        return panel;
    }

    private JPanel createLinesCardWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        linesCard = createLinesCard();
        wrapper.add(linesCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeLinesCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createLinesCard() {
        JPanel card = CardFactory.createAdaptiveCard(
                AppTheme.TABLE_CARD_MAX_WIDTH,
                AppTheme.ORDER_DETAIL_CARD_HEIGHT
        );

        card.setLayout(new BorderLayout());

        linesPanel = new JPanel(new GridBagLayout());
        linesPanel.setOpaque(false);

        card.add(linesPanel, BorderLayout.CENTER);

        return card;
    }

    private void resizeLinesCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.TABLE_CARD_MAX_WIDTH;
        int minWidth = AppTheme.ORDER_DETAIL_CARD_MIN_WIDTH;
        int horizontalMargin = AppTheme.PAGE_HORIZONTAL_PADDING * 2;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        int currentHeight = linesCard.getPreferredSize().height;

        linesCard.setPreferredSize(new Dimension(newWidth, currentHeight));
        linesCard.setMinimumSize(new Dimension(
                minWidth,
                AppTheme.TABLE_MIN_HEIGHT
        ));
        linesCard.setMaximumSize(new Dimension(
                maxWidth,
                Integer.MAX_VALUE
        ));

        linesCard.revalidate();
        linesCard.repaint();
    }

    private void updateLinesCardHeight(int lineCount) {
        int safeLineCount = Math.max(lineCount, 1);

        int height = AppTheme.TABLE_BASE_HEIGHT
                + AppTheme.TABLE_HEADER_HEIGHT
                + safeLineCount * AppTheme.ORDER_DETAIL_ROW_HEIGHT
                + AppTheme.CARD_PADDING_TOP
                + AppTheme.CARD_PADDING_BOTTOM;

        height = Math.max(height, AppTheme.TABLE_MIN_HEIGHT);

        int currentWidth = linesCard.getPreferredSize().width;

        linesCard.setPreferredSize(new Dimension(currentWidth, height));
        linesCard.setMinimumSize(new Dimension(
                AppTheme.ORDER_DETAIL_CARD_MIN_WIDTH,
                AppTheme.TABLE_MIN_HEIGHT
        ));

        linesCard.revalidate();
        linesCard.repaint();
    }

    private JPanel createHeaderRow() {
        JPanel header = new JPanel(new GridBagLayout());

        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(
                0,
                AppTheme.TABLE_ROW_HORIZONTAL_PADDING,
                AppTheme.COMPONENT_GAP_MEDIUM,
                AppTheme.TABLE_ROW_HORIZONTAL_PADDING
        ));

        header.setPreferredSize(new Dimension(
                AppTheme.TABLE_CARD_MAX_WIDTH,
                AppTheme.TABLE_HEADER_HEIGHT
        ));

        addHeaderCell(
                header,
                "Produit",
                0,
                AppTheme.ORDER_DETAIL_PRODUCT_WEIGHT
        );

        addHeaderCell(
                header,
                "Quantité",
                1,
                AppTheme.ORDER_DETAIL_QUANTITY_WEIGHT
        );

        addHeaderCell(
                header,
                "Employé",
                2,
                AppTheme.ORDER_DETAIL_EMPLOYEE_WEIGHT
        );

        addHeaderCell(
                header,
                "Statut actuel",
                3,
                AppTheme.ORDER_DETAIL_CURRENT_STATUS_WEIGHT
        );

        addHeaderCell(
                header,
                "Nouveau statut",
                4,
                AppTheme.ORDER_DETAIL_NEW_STATUS_WEIGHT
        );

        addHeaderCell(
                header,
                "Action",
                5,
                AppTheme.ORDER_DETAIL_ACTION_WEIGHT
        );

        return header;
    }

    private void addHeaderCell(JPanel panel, String text, int column, double weight) {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = column;
        constraints.gridy = 0;
        constraints.weightx = weight;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(
                0,
                0,
                0,
                AppTheme.COMPONENT_GAP_MEDIUM
        );

        panel.add(createHeaderLabel(text), constraints);
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);

        label.setFont(AppTheme.TEXT_BOLD_FONT);
        label.setForeground(AppTheme.TEXT_SECONDARY);
        label.setHorizontalAlignment(SwingConstants.LEFT);

        return label;
    }

    private void loadLineOrders() {
        if (order == null || order.getIdOrder() == null) {
            showEmptyMessage("Commande invalide.");
            updateGlobalStatus(null);
            return;
        }

        LoadingHelper.runWithLoading(
                linesPanel,
                "Chargement des produits...",
                () -> controller.getLineOrdersByOrder(order.getIdOrder()),
                this::displayLineOrders,
                this::displayLoadingError
        );

        updateLinesCardHeight(1);
    }

    private void displayLineOrders(ArrayList<LineOrder> lines) {
        linesPanel.removeAll();

        addHeaderRow();

        if (lines == null || lines.isEmpty()) {
            addEmptyRow("Aucun produit dans cette commande.", 2);
            updateGlobalStatus(null);
            updateLinesCardHeight(1);
            refreshLinesPanel();
            return;
        }

        int row = 2;

        for (LineOrder line : lines) {
            addLineOrderRow(line, row);
            row += 2;
        }

        updateGlobalStatus(lines);
        updateLinesCardHeight(lines.size());

        refreshLinesPanel();
    }

    private void refreshLinesPanel() {
        linesPanel.revalidate();
        linesPanel.repaint();
        refreshPage();
    }

    private void addHeaderRow() {
        addHeaderCell("Produit", 0, AppTheme.ORDER_DETAIL_PRODUCT_WEIGHT);
        addHeaderCell("Quantité", 1, AppTheme.ORDER_DETAIL_QUANTITY_WEIGHT);
        addHeaderCell("Employé", 2, AppTheme.ORDER_DETAIL_EMPLOYEE_WEIGHT);
        addHeaderCell("Statut actuel", 3, AppTheme.ORDER_DETAIL_CURRENT_STATUS_WEIGHT);
        addHeaderCell("Nouveau statut", 4, AppTheme.ORDER_DETAIL_NEW_STATUS_WEIGHT);
        addHeaderCell("Action", 5, AppTheme.ORDER_DETAIL_ACTION_WEIGHT);

        addSeparatorRow(1);
    }

    private void addHeaderCell(String text, int column, double weight) {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = column;
        constraints.gridy = 0;
        constraints.weightx = weight;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(
                0,
                0,
                AppTheme.COMPONENT_GAP_MEDIUM,
                AppTheme.COMPONENT_GAP_MEDIUM
        );

        JLabel label = createHeaderLabel(text);

        linesPanel.add(label, constraints);
    }

    private void displayLoadingError(Exception exception) {
        LoadingHelper.showError(
                linesPanel,
                "Impossible de charger les produits de la commande."
        );

        updateLinesCardHeight(1);

        JOptionPane.showMessageDialog(
                this,
                getUsefulMessage(exception),
                "Erreur de chargement",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void addLineOrderRow(LineOrder line, int rowIndex) {
        JComboBox<StatusOption> statusComboBox = createStatusComboBox(line);

        JButton saveButton = ButtonFactory.createSmallIconButton(
                "💾",
                () -> updateLineStatus(line, statusComboBox)
        );

        addRowCell(
                createCellLabel(getProductText(line), AppTheme.TEXT_PRIMARY),
                0,
                rowIndex,
                AppTheme.ORDER_DETAIL_PRODUCT_WEIGHT
        );

        addRowCell(
                createCellLabel(String.valueOf(line.getQuantity()), AppTheme.TEXT_PRIMARY),
                1,
                rowIndex,
                AppTheme.ORDER_DETAIL_QUANTITY_WEIGHT
        );

        addRowCell(
                createCellLabel(getEmployeeText(line), AppTheme.TEXT_PRIMARY),
                2,
                rowIndex,
                AppTheme.ORDER_DETAIL_EMPLOYEE_WEIGHT
        );

        addRowCell(
                createCellLabel(
                        StatusHelper.getFrenchStatus(getStatusLabel(line)),
                        StatusHelper.getStatusColor(getStatusLabel(line))
                ),
                3,
                rowIndex,
                AppTheme.ORDER_DETAIL_CURRENT_STATUS_WEIGHT
        );

        addRowCell(
                statusComboBox,
                4,
                rowIndex,
                AppTheme.ORDER_DETAIL_NEW_STATUS_WEIGHT
        );

        addRowCell(
                TableFactory.createActionPanel(saveButton),
                5,
                rowIndex,
                AppTheme.ORDER_DETAIL_ACTION_WEIGHT
        );

        addSeparatorRow(rowIndex + 1);
    }

    private void addRowCell(Component component, int column, int row, double weight) {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = column;
        constraints.gridy = row;
        constraints.weightx = weight;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(
                AppTheme.TABLE_ROW_VERTICAL_PADDING,
                0,
                AppTheme.TABLE_ROW_VERTICAL_PADDING,
                AppTheme.COMPONENT_GAP_MEDIUM
        );

        JPanel cellWrapper = new JPanel(new BorderLayout());
        cellWrapper.setOpaque(false);
        cellWrapper.setPreferredSize(new Dimension(
                1,
                AppTheme.ORDER_DETAIL_ROW_HEIGHT
        ));

        cellWrapper.add(component, BorderLayout.CENTER);

        linesPanel.add(cellWrapper, constraints);
    }

    private void addSeparatorRow(int row) {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 6;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(
                0,
                0,
                0,
                AppTheme.COMPONENT_GAP_MEDIUM
        );

        linesPanel.add(TableFactory.createSeparator(), constraints);
    }

    private void addEmptyRow(String message, int row) {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 6;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(30, 0, 30, 0);

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(AppTheme.SUBTITLE_FONT);
        label.setForeground(AppTheme.TEXT_SECONDARY);

        linesPanel.add(label, constraints);
    }

    private JLabel createCellLabel(String text, Color color) {
        JLabel label = new JLabel(text == null ? "-" : text);

        label.setFont(AppTheme.TEXT_FONT);
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setVerticalAlignment(SwingConstants.CENTER);

        return label;
    }

    private JComboBox<StatusOption> createStatusComboBox(LineOrder line) {
        JComboBox<StatusOption> comboBox = FormFactory.createGenericComboBox();

        comboBox.addItem(new StatusOption("Pending", "En attente"));
        comboBox.addItem(new StatusOption("InPreparation", "En préparation"));
        comboBox.addItem(new StatusOption("Ready", "Prête"));
        comboBox.addItem(new StatusOption("Served", "Servie"));

        selectStatus(comboBox, getStatusLabel(line));

        comboBox.setFont(AppTheme.TEXT_FONT);
        comboBox.setPreferredSize(new Dimension(
                AppTheme.ORDER_DETAIL_STATUS_COMBO_WIDTH,
                AppTheme.FIELD_HEIGHT
        ));
        comboBox.setMinimumSize(new Dimension(
                AppTheme.ORDER_DETAIL_STATUS_COMBO_MIN_WIDTH,
                AppTheme.FIELD_HEIGHT
        ));
        comboBox.setMaximumSize(new Dimension(
                AppTheme.ORDER_DETAIL_STATUS_COMBO_MAX_WIDTH,
                AppTheme.FIELD_HEIGHT
        ));

        return comboBox;
    }

    private void selectStatus(JComboBox<StatusOption> comboBox, String statusLabel) {
        if (statusLabel == null) {
            return;
        }

        for (int index = 0; index < comboBox.getItemCount(); index++) {
            StatusOption option = comboBox.getItemAt(index);

            if (option.getDatabaseValue().equals(statusLabel)) {
                comboBox.setSelectedIndex(index);
                return;
            }
        }
    }

    private void updateLineStatus(LineOrder line, JComboBox<StatusOption> statusComboBox) {
        StatusOption selectedStatus = (StatusOption) statusComboBox.getSelectedItem();

        if (selectedStatus == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Le statut est obligatoire.",
                    "Erreur de validation",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        LoadingHelper.runWithLoading(
                linesPanel,
                "Modification du statut...",
                () -> {
                    controller.updateLineOrderStatus(
                            line.getIdLineOrder(),
                            selectedStatus.getDatabaseValue()
                    );

                    return null;
                },
                ignored -> displayUpdateSuccess(),
                this::displayUpdateError
        );

        updateLinesCardHeight(1);
    }

    private void displayUpdateSuccess() {
        JOptionPane.showMessageDialog(
                this,
                "Le statut du produit a bien été modifié.",
                "Modification réussie",
                JOptionPane.INFORMATION_MESSAGE
        );

        loadLineOrders();
    }

    private void displayUpdateError(Exception exception) {
        JOptionPane.showMessageDialog(
                this,
                getUsefulMessage(exception),
                "Erreur de modification",
                JOptionPane.ERROR_MESSAGE
        );

        loadLineOrders();
    }

    private void updateGlobalStatus(ArrayList<LineOrder> lines) {
        String status = calculateOrderStatus(lines);

        globalStatusLabel.setText(
                "Statut global : " + StatusHelper.getFrenchStatus(status)
        );
        globalStatusLabel.setForeground(StatusHelper.getStatusColor(status));
    }

    private String calculateOrderStatus(ArrayList<LineOrder> lines) {
        if (lines == null || lines.isEmpty()) {
            return "Pending";
        }

        boolean allServed = true;
        boolean allReadyOrServed = true;
        boolean hasPreparation = false;

        for (LineOrder line : lines) {
            String status = getStatusLabel(line);

            if (!"Served".equals(status)) {
                allServed = false;
            }

            if (!"Ready".equals(status) && !"Served".equals(status)) {
                allReadyOrServed = false;
            }

            if ("Pending".equals(status) || "InPreparation".equals(status)) {
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

    private void showEmptyMessage(String message) {
        LoadingHelper.showEmpty(linesPanel, message);
        updateLinesCardHeight(1);
        refreshPage();
    }

    private String getProductText(LineOrder line) {
        if (line == null || line.getProduct() == null) {
            return "-";
        }

        return line.getProduct().getProductLabel();
    }

    private String getEmployeeText(LineOrder line) {
        if (line == null || line.getEmployee() == null) {
            return "-";
        }

        return line.getEmployee().getFirstName()
                + " "
                + line.getEmployee().getLastName();
    }

    private String getStatusLabel(LineOrder line) {
        if (line == null || line.getStatus() == null) {
            return "Pending";
        }

        return line.getStatus().getStatusLabel();
    }

    private String getUsefulMessage(Exception exception) {
        Throwable cause = exception.getCause();

        if (cause != null && cause.getMessage() != null) {
            return cause.getMessage();
        }

        if (exception.getMessage() != null) {
            return exception.getMessage();
        }

        return "Une erreur inconnue est survenue.";
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