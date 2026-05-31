package viewPackage.Product;

import controllerPackage.ApplicationController;
import exceptionPackage.SearchException;
import modelPackage.Product;
import modelPackage.Type;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class ProductEditPanel extends AppPage {

    private final ApplicationController controller;
    private final Product oldProduct;

    private JTextField nameField;
    private JTextField priceField;
    private JTextField descriptionField;
    private JComboBox<String> typeComboBox;

    public ProductEditPanel(MainJFrame mainWindow, Product product) {
        super(mainWindow, true);

        controller = new ApplicationController();
        oldProduct = product;

        addCentered(
                createPageTitle("Modifier un produit"),
                0,
                new Insets(0, 0, 20, 0)
        );

        addCentered(
                createForm(),
                1,
                new Insets(0, 0, 20, 0)
        );

        fillForm();
    }

    private JPanel createForm() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 15, 15));
        panel.setOpaque(false);

        nameField = new JTextField();
        priceField = new JTextField();
        descriptionField = new JTextField();

        typeComboBox = new JComboBox<>();
        loadProductTypes();

        panel.add(new JLabel("Nom :"));
        panel.add(nameField);

        panel.add(new JLabel("Prix :"));
        panel.add(priceField);

        panel.add(new JLabel("Description :"));
        panel.add(descriptionField);

        panel.add(new JLabel("Type :"));
        panel.add(typeComboBox);

        JButton saveButton = ButtonFactory.createPrimaryButton(
                "Enregistrer",
                this::updateProduct
        );

        panel.add(saveButton);

        return panel;
    }

    private void loadProductTypes() {
        try {
            typeComboBox.removeAllItems();

            for (String typeLabel : controller.getProductTypes()) {
                typeComboBox.addItem(typeLabel);
            }

        } catch (SearchException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur de chargement des types",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void fillForm() {
        nameField.setText(oldProduct.getProductLabel());

        if (oldProduct.getPrice() != null) {
            priceField.setText(oldProduct.getPrice().toString());
        }

        descriptionField.setText(
                oldProduct.getDescription() == null ? "" : oldProduct.getDescription()
        );

        if (oldProduct.getProductType() != null) {
            typeComboBox.setSelectedItem(oldProduct.getProductType().getTypeLabel());
        }
    }

    private void updateProduct() {
        try {
            Product newProduct = new Product(
                    nameField.getText(),
                    new BigDecimal(priceField.getText()),
                    descriptionField.getText(),
                    new Type(typeComboBox.getSelectedItem().toString())
            );

            controller.updateProduct(oldProduct, newProduct);

            JOptionPane.showMessageDialog(
                    this,
                    "Produit modifié avec succès."
            );

            mainWindow.showProductManagementPanel();

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
