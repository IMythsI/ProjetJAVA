package dataAccessPackage.interfaces;

import exceptionPackage.OrderException;
import modelPackage.Order;

import java.util.ArrayList;

public interface OrderDataAccess {
    ArrayList<Order> getAllOrders() throws OrderException;

    void addOrder(Order order) throws OrderException;
    void updateOrder(Order order) throws OrderException;
    void deleteOrder(Integer idOrder) throws OrderException;
}
