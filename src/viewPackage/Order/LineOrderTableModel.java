package viewPackage.Order;

import modelPackage.LineOrder;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;

public class LineOrderTableModel extends AbstractTableModel {
    private final String[] columnNames = {
            "ID",
            "Produit",
            "Type",
            "Quantité",
            "Prix unitaire",
            "Total",
            "Employé",
            "Statut"
    };

    private ArrayList<LineOrder> lineOrders;

    public LineOrderTableModel(ArrayList<LineOrder> lineOrders) {
        this.lineOrders = lineOrders;
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
        LineOrder line = lineOrders.get(rowIndex);

        BigDecimal total = line.getProduct()
                .getPrice()
                .multiply(BigDecimal.valueOf(line.getQuantity()));

        return switch (columnIndex) {
            case 0 -> line.getIdLineOrder();
            case 1 -> line.getProduct().getProductLabel();
            case 2 -> line.getProduct().getProductType().getTypeLabel();
            case 3 -> line.getQuantity();
            case 4 -> line.getProduct().getPrice();
            case 5 -> total;
            case 6 -> line.getEmployee().getFirstName() + " " + line.getEmployee().getLastName();
            case 7 -> line.getStatus().getStatusLabel();
            default -> null;
        };
    }
}