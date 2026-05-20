package viewPackage.Order;

import modelPackage.Order;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class OrderTableModel extends AbstractTableModel {
    private final String[] columnNames = {
            "ID",
            "Comment",
            "Guests",
            "Date",
            "Take away",
            "Pick up time",
            "Customer",
            "Phone",
            "Table"
    };

    private ArrayList<Order> orders;

    public OrderTableModel(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @Override
    public int getRowCount() {
        return orders.size();
    }
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Order order = orders.get(rowIndex);

        return switch (columnIndex){
            case 0 -> order.getIdOrder();
            case 1 -> order.getComment();
            case 2 -> order.getGuestCount();
            case 3 -> order.getOrderDate();
            case 4 -> order.getTakeAway();
            case 5 -> order.getPickupTime();
            case 6 -> order.getNameCustomer();
            case 7 -> order.getTelCustomer();
            case 8 -> order.getTable() == null ? null : order.getTable().getIdTable();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
