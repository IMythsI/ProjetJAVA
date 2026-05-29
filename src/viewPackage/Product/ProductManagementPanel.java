package viewPackage.Product;

import controllerPackage.ApplicationController;
import modelPackage.Product;
import viewPackage.MainJFrame;
import viewPackage.ui.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ProductManagementPanel extends AppPage {

    private final ApplicationController controller;

    private JPanel tableContainer;
    private JTable productTable;
    private ProductTableModel tableModel;

    public ProductManagementPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(createPageTitle("Gestion de la carte"), 0, new Insets(0, 0, 12, 0));
        addCentered(createPageSubtitle("Ajoutez, modifiez ou supprimez les produits du menu"), 1, new Insets(0, 0, 30, 0));
        addCentered(createActionPanel(), 2, new Insets(0, 0, 25, 0));
        addCentered(createTableCard(), 3, new Insets(0, 0, 0, 0));

        loadProducts();
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);

        JButton addButton = ButtonFactory.createPrimaryButton(
                "+ Ajouter un produit",
                () -> mainWindow.showProductFormPanel()
        );

        panel.add(addButton);
        return panel;
    }

    private JPanel createTableCard() {
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.TABLE_CARD_MAX_WIDTH, 500);
        card.setLayout(new BorderLayout());

        tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);

        card.add(tableContainer, BorderLayout.CENTER);

        return card;
    }

    private void loadProducts() {
        LoadingHelper.runWithLoading(
                tableContainer,
                "Chargement des produits...",
                controller::getAllProducts,
                this::displayProducts,
                this::displayLoadingError
        );
    }

    private void displayProducts(ArrayList<Product> products) {
        tableContainer.removeAll();

        tableModel = new ProductTableModel(products);
        productTable = new JTable(tableModel);

        productTable.setRowHeight(42);

        productTable.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        productTable.getColumnModel()
                .getColumn(4)
                .setCellRenderer(centerRenderer);

        productTable.getColumnModel()
                .getColumn(5)
                .setCellRenderer(centerRenderer);

        productTable.setShowGrid(false);
        productTable.setIntercellSpacing(new Dimension(0, 0));
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        productTable.getTableHeader().setFont(AppTheme.TEXT_BOLD_FONT);
        productTable.setAutoCreateRowSorter(true);
        productTable.getTableHeader().setReorderingAllowed(false);

        productTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(300);
        productTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(5).setPreferredWidth(110);

        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                int row = productTable.rowAtPoint(event.getPoint());
                int column = productTable.columnAtPoint(event.getPoint());

                if (row < 0 || column < 0) {
                    return;
                }

                int modelRow = productTable.convertRowIndexToModel(row);
                Product product = tableModel.getProductAt(modelRow);

                if (column == 4) {
                    mainWindow.showProductEditPanel(product);
                }

                if (column == 5) {
                    confirmAndDeleteProduct(product);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(null);

        tableContainer.add(scrollPane, BorderLayout.CENTER);
        refreshPage();
    }

    private void confirmAndDeleteProduct(Product product) {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment supprimer le produit \"" + product.getProductLabel() + "\" ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controller.deleteProduct(product);

            JOptionPane.showMessageDialog(
                    this,
                    "Le produit a bien été supprimé.",
                    "Suppression réussie",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadProducts();

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur de suppression",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void displayLoadingError(Exception exception) {
        LoadingHelper.showError(tableContainer, "Impossible de charger les produits.");

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de chargement",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private static class ProductTableModel extends AbstractTableModel {

        private final String[] columns = {
                "Produit",
                "Type",
                "Prix",
                "Description",
                "Modifier",
                "Supprimer"
        };

        private final ArrayList<Product> products;

        public ProductTableModel(ArrayList<Product> products) {
            this.products = products == null ? new ArrayList<>() : products;
        }

        @Override
        public int getRowCount() {
            return products.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Product product = products.get(rowIndex);

            return switch (columnIndex) {
                case 0 -> product.getProductLabel();
                case 1 -> product.getProductType().getTypeLabel();
                case 2 -> product.getPrice() == null ? "0,00 €" : product.getPrice().toString().replace(".", ",") + " €";
                case 3 -> product.getDescription();
                case 4 -> "✏️";
                case 5 -> "🗑️";
                default -> "";
            };
        }

        public Product getProductAt(int rowIndex) {
            return products.get(rowIndex);
        }
    }
}