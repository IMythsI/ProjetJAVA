package businessPackage;

import dataAccessPackage.db.OrderDBAccess;
import exceptionPackage.OrderException;
import modelPackage.Order;

import java.util.ArrayList;

public class OrderManager {
    private OrderDBAccess orderDAO;

    public OrderManager() {
        orderDAO = new OrderDBAccess();
    }

    public ArrayList<Order> getAllOrders() throws OrderException {
        return orderDAO.getAllOrders();
    }

}
