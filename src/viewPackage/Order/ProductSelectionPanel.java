package viewPackage.Order;

import controllerPackage.ApplicationController;
import exceptionPackage.*;
import lib.WrapLayout;
import modelPackage.*;
import viewPackage.AbstractPanel;
import viewPackage.MainJFrame;
import viewPackage.stylePackage.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProductSelectionPanel extends AbstractPanel {
    private ApplicationController controller;
    private Order order;
    private Table table;

    private JComboBox<String> typeComboBox;
    private ArrayList<Product> allProducts;
    private JPanel productButtonsPanel;

    private Map<Product, Integer> cart;
    private JPanel cartPanel;
    private JLabel totalLabel;

    private JPanel contentCardsPanel;
    private JPanel productsCard;
    private JPanel cartCard;

    public ProductSelectionPanel(MainJFrame mainWindow, Order order) {
        super(mainWindow);
        this.order = order;
        init();
    }

    public ProductSelectionPanel(MainJFrame mainWindow, Table table) {
        super(mainWindow);
        this.table = table;
        this.order = createTableOrder(table);
        init();
    }

    private void init() {
        controller = new ApplicationController();
        cart = new LinkedHashMap<>();

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createScrollableMainContent(), BorderLayout.CENTER);

        loadProducts();
        refreshCart();
    }

    private JScrollPane createScrollableMainContent() {
        JScrollPane scrollPane = new JScrollPane(createMainContent());

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

        JLabel title = new JLabel("Sélection des produits");
        title.setFont(AppTheme.TITLE_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel(getSubtitle());
        subtitle.setFont(AppTheme.SUBTITLE_FONT);
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 8, 0);
        mainPanel.add(title, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 35, 0);
        mainPanel.add(subtitle, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(createContentCards(), gbc);

        return mainPanel;
    }

    private String getSubtitle() {
        if (order.getIsTakeAway()) {
            return "Commande à emporter";
        }

        return "Commande pour la table " + table.getIdTable();
    }

    private JPanel createContentCards() {
        contentCardsPanel = new JPanel(new GridBagLayout());
        contentCardsPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;

        productsCard = createProductsCard();
        cartCard = createCartCard();

        gbc.gridx = 0;
        gbc.weightx = 0.70;
        gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, 0, 25);
        contentCardsPanel.add(productsCard, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.30;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentCardsPanel.add(cartCard, gbc);

        updateCardsSize();

        return contentCardsPanel;
    }

    private JPanel createProductsCard() {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(AppTheme.CARD);
        card.setBorder(BorderFactory.createEmptyBorder(22, 25, 22, 25));
        card.setMinimumSize(new Dimension(600, 430));
        card.putClientProperty("FlatLaf.style", "arc:20");

        JLabel title = new JLabel("Produits");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(AppTheme.TEXT_PRIMARY);

        typeComboBox = new JComboBox<>();
        typeComboBox.setPreferredSize(new Dimension(180, 38));
        typeComboBox.addActionListener(event -> refreshProductsBySelectedType());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(typeComboBox, BorderLayout.EAST);

        productButtonsPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 12, 12));
        productButtonsPanel.setOpaque(true);
        productButtonsPanel.setBackground(Color.WHITE);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(productButtonsPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createCartCard() {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(AppTheme.CARD);
        card.setBorder(BorderFactory.createEmptyBorder(22, 20, 22, 20));
        card.setMinimumSize(new Dimension(320, 430));
        card.putClientProperty("FlatLaf.style", "arc:20");

        JLabel title = new JLabel("Panier");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(AppTheme.TEXT_PRIMARY);

        cartPanel = new JPanel();
        cartPanel.setBackground(Color.WHITE);
        cartPanel.setOpaque(true);
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));

        card.add(title, BorderLayout.NORTH);
        card.add(cartPanel, BorderLayout.CENTER);
        card.add(createCartBottomPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCartBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 15));
        bottomPanel.setOpaque(false);

        totalLabel = new JLabel("Total : 0.00 €", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setOpaque(false);

        JButton validateButton = new JButton("Valider la commande");
        JButton cancelButton = new JButton("Annuler");

        stylePrimaryButton(validateButton);
        styleSecondaryButton(cancelButton);

        validateButton.addActionListener(event -> validateOrder());
        cancelButton.addActionListener(event -> mainWindow.goBack());

        buttonPanel.add(validateButton);
        buttonPanel.add(cancelButton);

        bottomPanel.add(totalLabel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        return bottomPanel;
    }

    private JButton createProductButton(Product product) {
        JButton button = new JButton();

        button.setText("<html><center>"
                + product.getProductLabel()
                + "<br><span style='color:#4678F0;'>"
                + product.getPrice()
                + " €</span></center></html>");

        button.setPreferredSize(new Dimension(150, 85));
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");

        button.addActionListener(event -> addProductToCart(product));

        return button;
    }

    private JButton createCartLine(Product product, Integer quantity) {
        BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        JButton lineButton = new JButton(
                product.getProductLabel()
                        + "   |   x" + quantity
                        + "   " + lineTotal + " €"
        );

        lineButton.setHorizontalAlignment(SwingConstants.LEFT);
        lineButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        lineButton.setPreferredSize(new Dimension(260, 42));
        lineButton.setFont(new Font("Arial", Font.PLAIN, 13));
        lineButton.setBackground(Color.WHITE);
        lineButton.setFocusPainted(false);
        lineButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lineButton.putClientProperty("JButton.buttonType", "roundRect");

        lineButton.addActionListener(event -> removeOneProduct(product));

        return lineButton;
    }

    private void stylePrimaryButton(JButton button) {
        button.setPreferredSize(new Dimension(240, 42));
        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(AppTheme.PRIMARY);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");
    }

    private void styleSecondaryButton(JButton button) {
        button.setPreferredSize(new Dimension(240, 42));
        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(AppTheme.TEXT_PRIMARY);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");
    }

    private void updateCardsSize() {
        if (productsCard == null || cartCard == null || contentCardsPanel == null) {
            return;
        }

        int height = calculateContentHeight();

        productsCard.setPreferredSize(new Dimension(600, height));
        cartCard.setPreferredSize(new Dimension(320, height));
        contentCardsPanel.setPreferredSize(new Dimension(950, height));

        productsCard.revalidate();
        cartCard.revalidate();
        contentCardsPanel.revalidate();
    }

    private int calculateContentHeight() {
        int selectedProductCount = countSelectedTypeProducts();
        int cartLineCount = cart == null ? 0 : cart.size();

        int productRows = (int) Math.ceil(selectedProductCount / 3.0);
        int productHeight = 150 + productRows * 105;

        int cartHeight = 180 + cartLineCount * 50;

        return Math.max(430, Math.max(productHeight, cartHeight));
    }

    private int countSelectedTypeProducts() {
        if (allProducts == null || typeComboBox == null || typeComboBox.getSelectedItem() == null) {
            return 0;
        }

        int count = 0;
        String selectedType = (String) typeComboBox.getSelectedItem();

        for (Product product : allProducts) {
            if (product.getProductType().getTypeLabel().equals(selectedType)) {
                count++;
            }
        }

        return count;
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

    private void loadProducts() {
        try {
            allProducts = controller.getAllProducts();

            typeComboBox.removeAllItems();

            for (String type : getProductTypes(allProducts)) {
                typeComboBox.addItem(type);
            }

            refreshProductsBySelectedType();

        } catch (ProductException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ArrayList<String> getProductTypes(ArrayList<Product> products) {
        ArrayList<String> types = new ArrayList<>();

        for (Product product : products) {
            String type = product.getProductType().getTypeLabel();

            if (!types.contains(type)) {
                types.add(type);
            }
        }

        return types;
    }

    private void refreshProductsBySelectedType() {
        if (allProducts == null || typeComboBox.getSelectedItem() == null) {
            return;
        }

        String selectedType = (String) typeComboBox.getSelectedItem();

        productButtonsPanel.removeAll();

        for (Product product : allProducts) {
            if (product.getProductType().getTypeLabel().equals(selectedType)) {
                productButtonsPanel.add(createProductButton(product));
            }
        }

        updateCardsSize();

        productButtonsPanel.revalidate();
        productButtonsPanel.repaint();

        revalidate();
        repaint();
    }

    private void addProductToCart(Product product) {
        Integer quantity = cart.get(product);

        if (quantity == null) {
            cart.put(product, 1);
        } else {
            cart.put(product, quantity + 1);
        }

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
        cartPanel.removeAll();

        if (cart.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucun produit sélectionné");
            emptyLabel.setForeground(AppTheme.TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            cartPanel.add(emptyLabel);
        }

        for (Product product : cart.keySet()) {
            cartPanel.add(createCartLine(product, cart.get(product)));
            cartPanel.add(Box.createVerticalStrut(8));
        }

        totalLabel.setText("Total : " + calculateTotal() + " €");

        updateCardsSize();

        cartPanel.revalidate();
        cartPanel.repaint();

        revalidate();
        repaint();
    }

    private BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (Product product : cart.keySet()) {
            BigDecimal lineTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(cart.get(product)));

            total = total.add(lineTotal);
        }

        return total;
    }

    private void validateOrder() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner au moins un produit.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            controller.validateOrder(order, cart);

            JOptionPane.showMessageDialog(
                    this,
                    "Commande validée avec succès.",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mainWindow.goBack();

        } catch (OrderException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}