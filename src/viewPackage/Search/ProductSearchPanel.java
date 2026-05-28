package viewPackage.Search;

import controllerPackage.ApplicationController;
import exceptionPackage.SearchException;
import modelPackage.ProductSearchResult;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ProductSearchPanel extends AppPage {

    private ApplicationController controller;

    private JComboBox<String> typeComboBox;
    private JComboBox<String> allergyComboBox;

    private DefaultTableModel tableModel;
    private JTable resultTable;

    public ProductSearchPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(createPageTitle("Search products"), 0, new Insets(0, 0, 25, 0));
        addCentered(createSearchCard(), 1, new Insets(0, 0, 20, 0));
        addCentered(createResultCard(), 2, new Insets(0, 0, 20, 0));

        loadProductTypes();
        loadAllergies();
    }

    private JPanel createSearchCard() {
        JPanel card = CardFactory.createCard(760, 230);
        card.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 15, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        typeComboBox = FormFactory.createComboBox();
        allergyComboBox = FormFactory.createComboBox();

        FormFactory.addFormRow(formPanel, gbc, 0, "Product type *", typeComboBox);
        FormFactory.addFormRow(formPanel, gbc, 1, "Allergy *", allergyComboBox);

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setOpaque(false);

        JButton searchButton = ButtonFactory.createPrimaryButton(
                "Search",
                this::searchProducts
        );

        buttonPanel.add(searchButton);

        return buttonPanel;
    }

    private JPanel createResultCard() {
        JPanel card = CardFactory.createCard(980, 380);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {
                "Product",
                "Type",
                "Ingredient",
                "Allergy",
                "Price",
                "Description"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultTable = new JTable(tableModel);
        resultTable.setRowHeight(34);
        resultTable.setFont(AppTheme.SUBTITLE_FONT);
        resultTable.getTableHeader().setFont(AppTheme.BUTTON_FONT);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private void loadProductTypes() {
        try {
            ArrayList<String> types = controller.getProductTypes();

            typeComboBox.removeAllItems();

            for (String type : types) {
                typeComboBox.addItem(type);
            }

        } catch (SearchException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Loading error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadAllergies() {
        try {
            ArrayList<String> allergies = controller.getAllergyLabels();

            allergyComboBox.removeAllItems();

            for (String allergy : allergies) {
                allergyComboBox.addItem(allergy);
            }

        } catch (SearchException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Loading error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void searchProducts() {
        try {
            String typeLabel = (String) typeComboBox.getSelectedItem();
            String allergyLabel = (String) allergyComboBox.getSelectedItem();

            ArrayList<ProductSearchResult> results =
                    controller.searchProductsByTypeAndAllergy(typeLabel, allergyLabel);

            refreshTable(results);

        } catch (SearchException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Search error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshTable(ArrayList<ProductSearchResult> results) {
        tableModel.setRowCount(0);

        for (ProductSearchResult result : results) {
            tableModel.addRow(new Object[]{
                    result.getProductLabel(),
                    result.getTypeLabel(),
                    result.getIngredientLabel(),
                    result.getAllergyLabel(),
                    String.format("%.2f €", result.getPrice()),
                    result.getDescription() == null ? "-" : result.getDescription()
            });
        }

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No product found for these criteria.",
                    "No result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}