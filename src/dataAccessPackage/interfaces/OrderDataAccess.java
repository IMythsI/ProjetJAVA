package dataAccessPackage.interfaces;

import exceptionPackage.OrderException;
import modelPackage.Order;
import modelPackage.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public interface OrderDataAccess {

    ArrayList<Order> getAllOrders() throws OrderException;

    ArrayList<Order> getOrdersByTable(Integer idTable) throws OrderException;

    BigDecimal getTotalAmountByTable(Integer idTable) throws OrderException;

    void addOrderWithLines(Order order, Map<Product, Integer> cart)
            throws OrderException;
}