package businessPackage;

import dataAccessPackage.db.OrderDBAccess;
import exceptionPackage.OrderException;
import modelPackage.Order;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OrderManager {
    private OrderDBAccess orderDAO;

    public OrderManager() {
        orderDAO = new OrderDBAccess();
    }

    public ArrayList<Order> getAllOrders() throws OrderException {
        return orderDAO.getAllOrders();
    }

    public ArrayList<Order> getOrdersByTable(Integer idTable) throws OrderException {
        return orderDAO.getOrdersByTable(idTable);
    }

    public BigDecimal getTotalAmountByTable(Integer idTable) throws OrderException {
        return orderDAO.getTotalAmountByTable(idTable);
    }

}
