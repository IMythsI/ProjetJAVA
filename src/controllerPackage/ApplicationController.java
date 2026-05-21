package controllerPackage;

import businessPackage.*;
import exceptionPackage.*;
import modelPackage.*;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ApplicationController {
    private OrderManager orderManager;
    private TableManager tableManager;
    private LineOrderManager lineOrderManager;
    private ProductManager productManager;

    public ApplicationController() {
        orderManager  = new OrderManager();
        tableManager = new TableManager();
        lineOrderManager = new LineOrderManager();
        productManager = new ProductManager();
    }

    public ArrayList<Order> getAllOrders() throws OrderException {
        return orderManager.getAllOrders();
    }

    public void addOrder(Order order) throws OrderException {

    }

    public ArrayList<Table> getAllTables() throws TableException {
        return tableManager.getAllTables();
    }

    public void updateTableStatus(Integer idTable, String statusLabel) throws TableException {
        tableManager.updateTableStatus(idTable, statusLabel);
    }

    public ArrayList<Order> getOrdersByTable(Integer idTable) throws OrderException {
        return orderManager.getOrdersByTable(idTable);
    }

    public BigDecimal getTotalAmountByTable(Integer idTable) throws OrderException {
        return orderManager.getTotalAmountByTable(idTable);
    }

    public ArrayList<LineOrder> getLineOrdersByTable(Integer idTable) throws LineOrderException {
        return lineOrderManager.getLineOrdersByTable(idTable);
    }

    public ArrayList<Product> getAllProducts() throws ProductException {
        return productManager.getAllProducts();
    }
}
