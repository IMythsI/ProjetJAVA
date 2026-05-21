package viewPackage.Order;

import controllerPackage.ApplicationController;
import exceptionPackage.*;
import lib.WrapLayout;
import modelPackage.*;
import viewPackage.AbstractPanel;
import viewPackage.MainJFrame;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProductSelectionPanel extends AbstractPanel {
    private ApplicationController controller;
    private Order order;

    private JComboBox<String> typeComboBox;
    private ArrayList<Product> allProducts;
    private JPanel productButtonsPanel;

    private Map<Product, Integer> cart;
    private JPanel productsPanel;
    private JPanel cartPanel;
    private JLabel totalLabel;

    public ProductSelectionPanel(MainJFrame mainWindow, Order order) {
        super(mainWindow);

        this.order = order;
        controller = new ApplicationController();
        cart = new LinkedHashMap<>();

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        loadProducts();
        refreshCart();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(createBackButton(), BorderLayout.WEST);

        JLabel title = new JLabel("Sélection des produits", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));

        topPanel.add(title, BorderLayout.CENTER);

        return topPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));

        typeComboBox = new JComboBox<>();
        typeComboBox.setPreferredSize(new Dimension(100, 40));
        typeComboBox.addActionListener(event -> refreshProductsBySelectedType());

        leftPanel.add(typeComboBox, BorderLayout.NORTH);

        productButtonsPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 12, 12));

        JScrollPane productsScrollPane = new JScrollPane(productButtonsPanel);
        productsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        productsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        productsScrollPane.setBorder(BorderFactory.createTitledBorder("Produits"));

        leftPanel.add(productsScrollPane, BorderLayout.CENTER);

        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));

        JScrollPane cartScrollPane = new JScrollPane(cartPanel);
        cartScrollPane.setPreferredSize(new Dimension(330, 0));
        cartScrollPane.setBorder(BorderFactory.createTitledBorder("Panier"));

        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(cartScrollPane, BorderLayout.EAST);

        return mainPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Total : 0.00 €", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton cancelButton = new JButton("Annuler");
        JButton validateButton = new JButton("Valider la commande");

        cancelButton.addActionListener(event -> mainWindow.goBack());
        validateButton.addActionListener(event -> validateOrder());

        buttonPanel.add(cancelButton);
        buttonPanel.add(validateButton);

        bottomPanel.add(totalLabel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        return bottomPanel;
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
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
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

        productButtonsPanel.revalidate();
        productButtonsPanel.repaint();
    }

    private JButton createProductButton(Product product) {
        JButton button = new JButton();

        button.setText("<html><center>"
                + product.getProductLabel()
                + "<br>"
                + product.getPrice() + " €"
                + "</center></html>");

        button.setPreferredSize(new Dimension(150, 75));
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(event -> addProductToCart(product));

        return button;
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

        for (Product product : cart.keySet()) {
            cartPanel.add(createCartLine(product, cart.get(product)));
            cartPanel.add(Box.createVerticalStrut(8));
        }

        totalLabel.setText("Total : " + calculateTotal() + " €");

        cartPanel.revalidate();
        cartPanel.repaint();
    }

    private JButton createCartLine(Product product, Integer quantity) {
        BigDecimal lineTotal =
                product.getPrice().multiply(BigDecimal.valueOf(quantity));

        String text = String.format(
                "%-10s   x%-3d   |   %s",
                lineTotal + " €",
                quantity,
                product.getProductLabel()
        );

        JButton lineButton = new JButton(text);

        lineButton.setHorizontalAlignment(SwingConstants.LEFT);
        lineButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        lineButton.setPreferredSize(new Dimension(300, 42));
        lineButton.setFont(new Font("Arial", Font.PLAIN, 14));
        lineButton.setFocusPainted(false);

        lineButton.addActionListener(event -> removeOneProduct(product));

        return lineButton;
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

        JOptionPane.showMessageDialog(
                this,
                "La sélection est prête. Prochaine étape : enregistrer Order + LineOrder en base.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}