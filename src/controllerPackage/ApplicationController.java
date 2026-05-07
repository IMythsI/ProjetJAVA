package controllerPackage;

import businessPackage.OrderManager;
import exceptionPackage.OrderException;
import modelPackage.Order;

import java.util.ArrayList;

public class ApplicationController {
    private OrderManager orderManager;

    public ApplicationController() {
        orderManager  = new OrderManager();
    }
    public ArrayList<Order> getAllOrders() throws OrderException {
        return orderManager.getAllOrders();
    }
}
