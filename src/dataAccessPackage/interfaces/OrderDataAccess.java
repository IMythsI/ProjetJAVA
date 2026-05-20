package dataAccessPackage.interfaces;

import exceptionPackage.OrderException;
import modelPackage.Order;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface OrderDataAccess {
    ArrayList<Order> getAllOrders() throws OrderException;

    ArrayList<Order> getOrdersByTable(Integer idTable) throws OrderException;

    BigDecimal getTotalAmountByTable(Integer idTable) throws OrderException;
}
