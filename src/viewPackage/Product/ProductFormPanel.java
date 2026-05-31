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

public class ProductFormPanel extends AppPage {

    private final ApplicationController controller;

    private JTextField nameField;
    private JTextField priceField;
    private JTextField descriptionField;
    private JComboBox<String> typeComboBox;


    public ProductFormPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(
                createPageTitle("Ajouter un produit"),
                0,
                new Insets(0,0,20,0)
        );

        addCentered(
                createForm(),
                1,
                new Insets(0,0,20,0)
        );
    }


    private JPanel createForm() {

        JPanel panel = new JPanel(new GridLayout(5,2,15,15));
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
                this::saveProduct
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

    private void saveProduct(){

        try {

            Product product = new Product(
                    nameField.getText(),
                    new BigDecimal(priceField.getText()),
                    descriptionField.getText(),
                    new Type(typeComboBox.getSelectedItem().toString())
            );


            controller.addProduct(product);


            JOptionPane.showMessageDialog(
                    this,
                    "Produit ajouté avec succès."
            );


            mainWindow.showProductManagementPanel();


        } catch(Exception exception){

            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
