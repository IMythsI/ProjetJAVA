package viewPackage.Order;

import controllerPackage.ApplicationController;
import exceptionPackage.OrderException;
import modelPackage.Order;
import modelPackage.Product;
import modelPackage.Table;
import modelPackage.Type;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProductSelectionPanel extends AppPage {

    private final ApplicationController controller;
    private final Order order;
    private final Table table;

    private JPanel contentPanel;
    private JPanel productsCard;
    private JPanel cartCard;
    private JPanel productButtonsPanel;
    private JPanel cartLinesPanel;

    private JComboBox<ProductTypeOption> typeComboBox;
    private JLabel totalLabel;
    private JButton validateButton;

    private ArrayList<Product> allProducts;
    private final Map<Product, Integer> cart;

    public ProductSelectionPanel(MainJFrame mainWindow, Order order) {
        super(mainWindow, true);

        this.controller = new ApplicationController();
        this.order = order;
        this.table = null;
        this.cart = new LinkedHashMap<>();

        buildPage();
    }

    public ProductSelectionPanel(MainJFrame mainWindow, Table table) {
        super(mainWindow, true);

        this.controller = new ApplicationController();
        this.table = table;
        this.order = createTableOrder(table);
        this.cart = new LinkedHashMap<>();

        buildPage();
    }

    private void buildPage() {
        addCentered(
                createPageTitle("Sélection des produits"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle(getSubtitle()),
                1,
                new Insets(0, 0, 30, 0)
        );

        addCentered(
                createContentWrapper(),
                2,
                new Insets(0, 0, 0, 0)
        );

        loadProducts();
        refreshCart();
    }

    private String getSubtitle() {
        if (order.getIsTakeAway()) {
            return "Commande à emporter";
        }

        return "Commande pour la table " + table.getIdTable();
    }

    private Order createTableOrder(Table table) {
        return new Order(
                null,
                "Commande table " + table.getIdTable(),
                table.getNbSeats(),
                LocalDate.now(),
                false,
                null,
                null,
                null,
                table
        );
    }

    private JPanel createContentWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        contentPanel = createContentPanel();
        wrapper.add(contentPanel);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeContentPanel(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        productsCard = createProductsCard();
        cartCard = createCartCard();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.NORTH;

        constraints.gridx = 0;
        constraints.weightx = 0.70;
        constraints.insets = new Insets(0, 0, 0, AppTheme.COMPONENT_GAP_LARGE);
        panel.add(productsCard, constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.30;
        constraints.insets = new Insets(0, 0, 0, 0);
        panel.add(cartCard, constraints);

        return panel;
    }

    private JPanel createProductsCard() {
        JPanel card = CardFactory.createAdaptiveCard(680, 500);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(
                AppTheme.CARD_PADDING_TOP,
                AppTheme.CARD_PADDING_LEFT,
                AppTheme.CARD_PADDING_BOTTOM,
                AppTheme.CARD_PADDING_RIGHT
        ));

        JLabel titleLabel = new JLabel("Produits");
        titleLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);

        typeComboBox = FormFactory.createGenericComboBox();
        typeComboBox.setPreferredSize(new Dimension(220, AppTheme.FIELD_HEIGHT));
        typeComboBox.addActionListener(event -> refreshProductsBySelectedType());

        JPanel topPanel = new JPanel(new BorderLayout(
                AppTheme.COMPONENT_GAP_MEDIUM,
                0
        ));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(typeComboBox, BorderLayout.EAST);

        productButtonsPanel = new JPanel(new lib.WrapLayout(
                FlowLayout.LEFT,
                AppTheme.COMPONENT_GAP_MEDIUM,
                AppTheme.COMPONENT_GAP_MEDIUM
        ));
        productButtonsPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(productButtonsPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private JPanel createCartCard() {
        JPanel card = CardFactory.createAdaptiveCard(360, 500);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(
                AppTheme.CARD_PADDING_TOP,
                AppTheme.CARD_PADDING_LEFT,
                AppTheme.CARD_PADDING_BOTTOM,
                AppTheme.CARD_PADDING_RIGHT
        ));

        JLabel titleLabel = new JLabel("Panier");
        titleLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        cartLinesPanel = new JPanel();
        cartLinesPanel.setOpaque(false);
        cartLinesPanel.setLayout(new BoxLayout(cartLinesPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(cartLinesPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        card.add(createCartBottomPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCartBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(
                0,
                AppTheme.COMPONENT_GAP_MEDIUM
        ));

        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(
                AppTheme.COMPONENT_GAP_MEDIUM,
                0,
                0,
                0
        ));

        totalLabel = new JLabel("Total : 0,00 €", SwingConstants.RIGHT);
        totalLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        totalLabel.setForeground(AppTheme.TEXT_PRIMARY);

        validateButton = ButtonFactory.createPrimaryButton(
                "Valider la commande",
                this::validateOrder
        );
        validateButton.setPreferredSize(new Dimension(220, AppTheme.BUTTON_HEIGHT));

        JButton cancelButton = ButtonFactory.createSecondaryButton(
                "Annuler",
                () -> mainWindow.goBack()
        );

        JPanel buttonPanel = new JPanel(new GridLayout(
                2,
                1,
                0,
                AppTheme.COMPONENT_GAP_SMALL
        ));
        buttonPanel.setOpaque(false);

        buttonPanel.add(validateButton);
        buttonPanel.add(cancelButton);

        bottomPanel.add(totalLabel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        return bottomPanel;
    }

    private void resizeContentPanel(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.TABLE_CARD_MAX_WIDTH;
        int minWidth = 820;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        int newHeight = calculateContentHeight();

        contentPanel.setPreferredSize(new Dimension(newWidth, newHeight));
        contentPanel.setMinimumSize(new Dimension(minWidth, 500));
        contentPanel.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        int cartWidth = 360;
        int productWidth = Math.max(460, newWidth - cartWidth - AppTheme.COMPONENT_GAP_LARGE);

        productsCard.setPreferredSize(new Dimension(productWidth, newHeight));
        cartCard.setPreferredSize(new Dimension(cartWidth, newHeight));

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private int calculateContentHeight() {
        int productCount = countSelectedTypeProducts();
        int cartLineCount = cart.size();

        int productRows = (int) Math.ceil(productCount / 3.0);
        int productHeight = 180 + productRows * 110;

        int cartHeight = 220 + cartLineCount * 55;

        return Math.max(500, Math.max(productHeight, cartHeight));
    }

    private int countSelectedTypeProducts() {
        if (allProducts == null || typeComboBox == null || typeComboBox.getSelectedItem() == null) {
            return 0;
        }

        ProductTypeOption selectedType = (ProductTypeOption) typeComboBox.getSelectedItem();

        int count = 0;

        for (Product product : allProducts) {
            if (
                    product.getProductType() != null
                            && product.getProductType().getTypeLabel().equals(selectedType.getDatabaseValue())
            ) {
                count++;
            }
        }

        return count;
    }

    private void loadProducts() {
        typeComboBox.setEnabled(false);
        typeComboBox.removeAllItems();

        LoadingHelper.runWithLoading(
                productButtonsPanel,
                "Chargement des produits...",
                controller::getAllProducts,
                this::displayProducts,
                this::displayProductLoadingError
        );
    }

    private void displayProducts(ArrayList<Product> products) {
        allProducts = products;

        typeComboBox.removeAllItems();

        for (String type : getProductTypes(products)) {
            typeComboBox.addItem(new ProductTypeOption(type, translateProductType(type)));
        }

        typeComboBox.setEnabled(true);

        refreshProductsBySelectedType();
    }

    private void displayProductLoadingError(Exception exception) {
        typeComboBox.setEnabled(false);

        LoadingHelper.showError(
                productButtonsPanel,
                "Impossible de charger les produits."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de chargement",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private ArrayList<String> getProductTypes(ArrayList<Product> products) {
        ArrayList<String> types = new ArrayList<>();

        if (products == null) {
            return types;
        }

        for (Product product : products) {
            Type type = product.getProductType();

            if (type != null && !types.contains(type.getTypeLabel())) {
                types.add(type.getTypeLabel());
            }
        }

        return types;
    }

    private void refreshProductsBySelectedType() {
        if (allProducts == null || typeComboBox.getSelectedItem() == null) {
            return;
        }

        ProductTypeOption selectedType = (ProductTypeOption) typeComboBox.getSelectedItem();

        productButtonsPanel.removeAll();

        for (Product product : allProducts) {
            if (product.getProductType().getTypeLabel().equals(selectedType.getDatabaseValue())) {
                productButtonsPanel.add(createProductButton(product));
            }
        }

        resizeContentPanel((JPanel) contentPanel.getParent());
        productButtonsPanel.revalidate();
        productButtonsPanel.repaint();
        refreshPage();
    }

    private JButton createProductButton(Product product) {
        JButton button = new RoundedButton("", AppTheme.SMALL_BUTTON_ARC);

        button.setLayout(new BorderLayout());
        button.setPreferredSize(AppTheme.PRODUCT_BUTTON_SIZE);
        button.setMinimumSize(AppTheme.PRODUCT_BUTTON_SIZE);
        button.setBackground(Color.WHITE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel(
                "<html><center>" + product.getProductLabel() + "</center></html>",
                SwingConstants.CENTER
        );
        nameLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        nameLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel priceLabel = new JLabel(formatPrice(product.getPrice()), SwingConstants.CENTER);
        priceLabel.setFont(AppTheme.TEXT_FONT);
        priceLabel.setForeground(AppTheme.PRIMARY);

        button.add(nameLabel, BorderLayout.CENTER);
        button.add(priceLabel, BorderLayout.SOUTH);

        button.addActionListener(event -> addProductToCart(product));

        return button;
    }

    private JButton createCartLine(Product product, Integer quantity) {
        BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        JButton button = new RoundedButton("", AppTheme.SMALL_BUTTON_ARC);

        button.setLayout(new BorderLayout());
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppTheme.CART_LINE_SIZE.height));
        button.setPreferredSize(AppTheme.CART_LINE_SIZE);
        button.setBackground(Color.WHITE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel productLabel = new JLabel(product.getProductLabel());
        productLabel.setFont(AppTheme.TEXT_FONT);
        productLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel quantityLabel = new JLabel("x" + quantity + " — " + formatPrice(lineTotal));
        quantityLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        quantityLabel.setForeground(AppTheme.PRIMARY);

        button.add(productLabel, BorderLayout.CENTER);
        button.add(quantityLabel, BorderLayout.EAST);

        button.addActionListener(event -> removeOneProduct(product));

        return button;
    }

    private void addProductToCart(Product product) {
        cart.put(product, cart.getOrDefault(product, 0) + 1);
        refreshCart();
    }

    private void removeOneProduct(Product product) {
        Integer quantity = cart.get(product);

        if (quantity == null) {
            return;
        }

        if (quantity <= 1) {
            cart.remove(product);
        } else {
            cart.put(product, quantity - 1);
        }

        refreshCart();
    }

    private void refreshCart() {
        cartLinesPanel.removeAll();

        if (cart.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucun produit sélectionné", SwingConstants.CENTER);
            emptyLabel.setFont(AppTheme.TEXT_FONT);
            emptyLabel.setForeground(AppTheme.TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            cartLinesPanel.add(emptyLabel);
        } else {
            for (Product product : cart.keySet()) {
                cartLinesPanel.add(createCartLine(product, cart.get(product)));
                cartLinesPanel.add(Box.createVerticalStrut(AppTheme.COMPONENT_GAP_SMALL));
            }
        }

        totalLabel.setText("Total : " + formatPrice(calculateTotal()));

        if (contentPanel != null && contentPanel.getParent() instanceof JPanel wrapper) {
            resizeContentPanel(wrapper);
        }

        cartLinesPanel.revalidate();
        cartLinesPanel.repaint();
        refreshPage();
    }

    private BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (Product product : cart.keySet()) {
            total = total.add(
                    product.getPrice().multiply(BigDecimal.valueOf(cart.get(product)))
            );
        }

        return total;
    }

    private void validateOrder() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner au moins un produit.",
                    "Erreur de validation",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        validateButton.setEnabled(false);

        LoadingHelper.runWithLoading(
                cartLinesPanel,
                "Validation de la commande...",
                () -> {
                    controller.validateOrder(order, cart);
                    return null;
                },
                ignored -> displayValidationSuccess(),
                this::displayValidationError
        );
    }

    private void displayValidationSuccess() {
        JOptionPane.showMessageDialog(
                this,
                "La commande a bien été validée.",
                "Commande validée",
                JOptionPane.INFORMATION_MESSAGE
        );

        mainWindow.goBack();
    }

    private void displayValidationError(Exception exception) {
        validateButton.setEnabled(true);

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de validation",
                JOptionPane.ERROR_MESSAGE
        );

        refreshCart();
    }

    private String formatPrice(BigDecimal price) {
        if (price == null) {
            return "0,00 €";
        }

        return String.format("%.2f €", price).replace(".", ",");
    }

    private String translateProductType(String type) {
        if (type == null) {
            return "-";
        }

        return switch (type) {
            case "Dish" -> "Plat";
            case "Drink" -> "Boisson";
            case "Dessert" -> "Dessert";
            case "Menu" -> "Menu";
            default -> type;
        };
    }

    private static class ProductTypeOption {

        private final String databaseValue;
        private final String displayValue;

        public ProductTypeOption(String databaseValue, String displayValue) {
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