package viewPackage.Search;

import controllerPackage.ApplicationController;
import modelPackage.ProductSearchResult;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ProductSearchPanel extends AppPage {

    private final ApplicationController controller;

    private JPanel searchCard;
    private JPanel resultCard;
    private JPanel resultContentPanel;

    private JComboBox<DisplayOption> typeComboBox;
    private JComboBox<DisplayOption> allergyComboBox;

    public ProductSearchPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(
                createPageTitle("Recherche de produits"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle("Rechercher les produits selon leur type et leurs allergènes"),
                1,
                new Insets(0, 0, 30, 0)
        );

        addCentered(
                createSearchCardWrapper(),
                2,
                new Insets(0, 0, 25, 0)
        );

        addCentered(
                createResultCardWrapper(),
                3,
                new Insets(0, 0, 0, 0)
        );

        loadSearchCriteria();
    }

    private JPanel createSearchCardWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        searchCard = createSearchCard();
        wrapper.add(searchCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeSearchCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createSearchCard() {
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.SEARCH_CARD_MAX_WIDTH, 250);
        card.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(
                AppTheme.CARD_PADDING_TOP,
                AppTheme.CARD_PADDING_LEFT,
                AppTheme.CARD_PADDING_BOTTOM,
                AppTheme.CARD_PADDING_RIGHT
        ));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        typeComboBox = FormFactory.createGenericComboBox();
        allergyComboBox = FormFactory.createGenericComboBox();

        FormFactory.addFormRow(formPanel, constraints, 0, "Type de produit *", typeComboBox);
        FormFactory.addFormRow(formPanel, constraints, 1, "Allergène *", allergyComboBox);

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(
                FlowLayout.RIGHT,
                AppTheme.COMPONENT_GAP_MEDIUM,
                AppTheme.COMPONENT_GAP_MEDIUM
        ));

        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 20));

        JButton searchButton = ButtonFactory.createPrimaryButton(
                "Rechercher",
                this::searchProducts
        );

        buttonPanel.add(searchButton);

        return buttonPanel;
    }

    private JPanel createResultCardWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        resultCard = createResultCard();
        wrapper.add(resultCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeResultCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createResultCard() {
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.TABLE_CARD_MAX_WIDTH, 400);
        card.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Résultats");
        titleLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        resultContentPanel = new JPanel(new BorderLayout());
        resultContentPanel.setOpaque(false);

        LoadingHelper.showEmpty(
                resultContentPanel,
                "Lancez une recherche pour afficher les résultats."
        );

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(resultContentPanel, BorderLayout.CENTER);

        return card;
    }

    private void resizeSearchCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.SEARCH_CARD_MAX_WIDTH;
        int minWidth = 620;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        searchCard.setPreferredSize(new Dimension(newWidth, 250));
        searchCard.setMinimumSize(new Dimension(minWidth, 230));
        searchCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        searchCard.revalidate();
        searchCard.repaint();
    }

    private void resizeResultCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.TABLE_CARD_MAX_WIDTH;
        int minWidth = 760;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        resultCard.setPreferredSize(new Dimension(newWidth, 400));
        resultCard.setMinimumSize(new Dimension(minWidth, 340));
        resultCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        resultCard.revalidate();
        resultCard.repaint();
    }

    private void loadSearchCriteria() {
        typeComboBox.setEnabled(false);
        allergyComboBox.setEnabled(false);

        typeComboBox.removeAllItems();
        allergyComboBox.removeAllItems();

        typeComboBox.addItem(new DisplayOption("", "Chargement..."));
        allergyComboBox.addItem(new DisplayOption("", "Chargement..."));

        LoadingHelper.runWithLoading(
                resultContentPanel,
                "Chargement des critères...",
                () -> {
                    ArrayList<String> types = controller.getProductTypes();
                    ArrayList<String> allergies = controller.getAllergyLabels();

                    return new SearchCriteria(types, allergies);
                },
                this::displaySearchCriteria,
                this::displayCriteriaLoadingError
        );
    }

    private void displaySearchCriteria(SearchCriteria criteria) {
        typeComboBox.removeAllItems();
        allergyComboBox.removeAllItems();

        for (String type : criteria.getTypes()) {
            typeComboBox.addItem(new DisplayOption(type, translateProductType(type)));
        }

        for (String allergy : criteria.getAllergies()) {
            allergyComboBox.addItem(new DisplayOption(allergy, translateAllergy(allergy)));
        }

        typeComboBox.setEnabled(true);
        allergyComboBox.setEnabled(true);

        LoadingHelper.showEmpty(
                resultContentPanel,
                "Lancez une recherche pour afficher les résultats."
        );
    }

    private void displayCriteriaLoadingError(Exception exception) {
        typeComboBox.removeAllItems();
        allergyComboBox.removeAllItems();

        typeComboBox.setEnabled(false);
        allergyComboBox.setEnabled(false);

        LoadingHelper.showError(
                resultContentPanel,
                "Impossible de charger les critères."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de chargement",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void searchProducts() {
        try {
            DisplayOption selectedType = (DisplayOption) typeComboBox.getSelectedItem();
            DisplayOption selectedAllergy = (DisplayOption) allergyComboBox.getSelectedItem();

            if (selectedType == null || selectedType.getDatabaseValue().isBlank()) {
                throw new IllegalArgumentException("Le type de produit est obligatoire.");
            }

            if (selectedAllergy == null || selectedAllergy.getDatabaseValue().isBlank()) {
                throw new IllegalArgumentException("L'allergène est obligatoire.");
            }

            LoadingHelper.runWithLoading(
                    resultContentPanel,
                    "Recherche des produits...",
                    () -> controller.searchProductsByTypeAndAllergy(
                            selectedType.getDatabaseValue(),
                            selectedAllergy.getDatabaseValue()
                    ),
                    this::displayResults,
                    this::displaySearchError
            );

        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur de validation",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void displayResults(ArrayList<ProductSearchResult> results) {
        resultContentPanel.removeAll();

        if (results == null || results.isEmpty()) {
            LoadingHelper.showEmpty(
                    resultContentPanel,
                    "Aucun produit trouvé pour ces critères."
            );
            return;
        }

        JTable table = createResultTable(results);
        JScrollPane scrollPane = new JScrollPane(table);

        resultContentPanel.add(scrollPane, BorderLayout.CENTER);
        resultContentPanel.revalidate();
        resultContentPanel.repaint();
    }

    private JTable createResultTable(ArrayList<ProductSearchResult> results) {
        String[] columns = {
                "Produit",
                "Type",
                "Ingrédient",
                "Allergène",
                "Prix",
                "Description"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (ProductSearchResult result : results) {
            tableModel.addRow(new Object[]{
                    result.getProductLabel(),
                    translateProductType(result.getTypeLabel()),
                    result.getIngredientLabel(),
                    translateAllergy(result.getAllergyLabel()),
                    String.format("%.2f €", result.getPrice()),
                    result.getDescription() == null ? "-" : result.getDescription()
            });
        }

        JTable table = new JTable(tableModel);

        table.setRowHeight(AppTheme.JTABLE_ROW_HEIGHT);
        table.setFont(AppTheme.TEXT_FONT);
        table.getTableHeader().setFont(AppTheme.TEXT_BOLD_FONT);
        table.getTableHeader().setPreferredSize(new Dimension(0, AppTheme.TABLE_HEADER_HEIGHT));

        return table;
    }

    private void displaySearchError(Exception exception) {
        LoadingHelper.showError(
                resultContentPanel,
                "Impossible d’effectuer la recherche."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de recherche",
                JOptionPane.ERROR_MESSAGE
        );
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

    private String translateAllergy(String allergy) {
        if (allergy == null) {
            return "-";
        }

        return switch (allergy) {
            case "Gluten" -> "Gluten";
            case "Lactose" -> "Lactose";
            case "Egg" -> "Œuf";
            case "Meat" -> "Viande";
            case "Citrus" -> "Agrumes";
            default -> allergy;
        };
    }

    private static class SearchCriteria {

        private final ArrayList<String> types;
        private final ArrayList<String> allergies;

        public SearchCriteria(ArrayList<String> types, ArrayList<String> allergies) {
            this.types = types;
            this.allergies = allergies;
        }

        public ArrayList<String> getTypes() {
            return types;
        }

        public ArrayList<String> getAllergies() {
            return allergies;
        }
    }

    private static class DisplayOption {

        private final String databaseValue;
        private final String displayValue;

        public DisplayOption(String databaseValue, String displayValue) {
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