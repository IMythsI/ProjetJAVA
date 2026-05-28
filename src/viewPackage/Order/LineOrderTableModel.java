package viewPackage.Order;

import modelPackage.LineOrder;
import viewPackage.ui.StatusHelper;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;

public class LineOrderTableModel extends AbstractTableModel {

    private final String[] columnNames = {
            "N°",
            "Produit",
            "Type",
            "Quantité",
            "Prix unitaire",
            "Total",
            "Employé",
            "Statut"
    };

    private final ArrayList<LineOrder> lineOrders;

    public LineOrderTableModel(ArrayList<LineOrder> lineOrders) {
        this.lineOrders = lineOrders == null ? new ArrayList<>() : lineOrders;
    }

    @Override
    public int getRowCount() {
        return lineOrders.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LineOrder lineOrder = lineOrders.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> lineOrder.getIdLineOrder();
            case 1 -> getProductName(lineOrder);
            case 2 -> getProductType(lineOrder);
            case 3 -> lineOrder.getQuantity();
            case 4 -> formatPrice(getUnitPrice(lineOrder));
            case 5 -> formatPrice(calculateLineTotal(lineOrder));
            case 6 -> getEmployeeName(lineOrder);
            case 7 -> getStatusLabel(lineOrder);
            default -> "-";
        };
    }

    private String getProductName(LineOrder lineOrder) {
        if (lineOrder == null || lineOrder.getProduct() == null) {
            return "-";
        }

        return lineOrder.getProduct().getProductLabel();
    }

    private String getProductType(LineOrder lineOrder) {
        if (
                lineOrder == null
                        || lineOrder.getProduct() == null
                        || lineOrder.getProduct().getProductType() == null
        ) {
            return "-";
        }

        String typeLabel = lineOrder.getProduct()
                .getProductType()
                .getTypeLabel();

        return translateProductType(typeLabel);
    }

    private BigDecimal getUnitPrice(LineOrder lineOrder) {
        if (lineOrder == null || lineOrder.getProduct() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal price = lineOrder.getProduct().getPrice();

        return price == null ? BigDecimal.ZERO : price;
    }

    private BigDecimal calculateLineTotal(LineOrder lineOrder) {
        if (lineOrder == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal unitPrice = getUnitPrice(lineOrder);

        return unitPrice.multiply(BigDecimal.valueOf(lineOrder.getQuantity()));
    }

    private String getEmployeeName(LineOrder lineOrder) {
        if (lineOrder == null || lineOrder.getEmployee() == null) {
            return "-";
        }

        String firstName = lineOrder.getEmployee().getFirstName();
        String lastName = lineOrder.getEmployee().getLastName();

        return firstName + " " + lastName;
    }

    private String getStatusLabel(LineOrder lineOrder) {
        if (lineOrder == null || lineOrder.getStatus() == null) {
            return "-";
        }

        return StatusHelper.getFrenchStatus(
                lineOrder.getStatus().getStatusLabel()
        );
    }

    private String translateProductType(String typeLabel) {
        if (typeLabel == null) {
            return "-";
        }

        return switch (typeLabel) {
            case "Dish" -> "Plat";
            case "Drink" -> "Boisson";
            case "Dessert" -> "Dessert";
            case "Menu" -> "Menu";
            default -> typeLabel;
        };
    }

    private String formatPrice(BigDecimal price) {
        if (price == null) {
            return "0,00 €";
        }

        return String.format("%.2f €", price).replace(".", ",");
    }
}