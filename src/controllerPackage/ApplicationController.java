package controllerPackage;

import businessPackage.*;
import exceptionPackage.*;
import modelPackage.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class ApplicationController {
    private OrderManager orderManager;
    private TableManager tableManager;
    private LineOrderManager lineOrderManager;
    private ProductManager productManager;
    private OrderValidationManager orderValidationManager;
    private BookingManager bookingManager;
    private SearchManager searchManager;

    public ApplicationController() {
        orderManager  = new OrderManager();
        tableManager = new TableManager();
        lineOrderManager = new LineOrderManager();
        productManager = new ProductManager();
        orderValidationManager = new OrderValidationManager();
        bookingManager = new BookingManager();
        searchManager = new SearchManager();
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

    public void validateOrder(Order order, Map<Product, Integer> cart) throws OrderException {
        orderValidationManager.validateOrder(order, cart);
    }

    public ArrayList<LineOrder> getLineOrdersByOrder(Integer idOrder) throws LineOrderException {
        return lineOrderManager.getLineOrdersByOrder(idOrder);
    }

    //BOOKING
    public ArrayList<Book> getBookingsByDate(LocalDate date) throws BookingException {
        return bookingManager.getBookingsByDate(date);
    }
    public ArrayList<Book> getAllBookings() throws BookingException {
        return bookingManager.getAllBookings();
    }

    public void addBooking(Book booking) throws BookingException {
        bookingManager.addBooking(booking);
    }

    public void updateBooking(Book oldBooking, Book newBooking) throws BookingException {
        bookingManager.updateBooking(oldBooking, newBooking);
    }

    public void deleteBooking(Book booking) throws BookingException {
        bookingManager.deleteBooking(booking);
    }

    //SEARCH
    public ArrayList<String> getBookingCustomerNames() throws SearchException {
        return searchManager.getBookingCustomerNames();
    }

    public ArrayList<BookingSearchResult> searchBookingsByCustomerAndDate(String customerName, LocalDate date) throws SearchException {
        return searchManager.searchBookingsByCustomerAndDate(customerName, date);
    }

    public ArrayList<String> getWaiterNames() throws SearchException {
        return searchManager.getWaiterNames();
    }

    public ArrayList<String> getOrderStatusLabels() throws SearchException {
        return searchManager.getOrderStatusLabels();
    }

    public ArrayList<OrderSearchResult> searchOrdersByWaiterAndStatus(String waiterName, String statusLabel) throws SearchException {
        return searchManager.searchOrdersByWaiterAndStatus(waiterName, statusLabel);
    }

    public ArrayList<String> getProductTypes() throws SearchException {
        return searchManager.getProductTypes();
    }

    public ArrayList<String> getAllergyLabels() throws SearchException {
        return searchManager.getAllergyLabels();
    }

    public ArrayList<ProductSearchResult> searchProductsByTypeAndAllergy(String typeLabel, String allergyLabel) throws SearchException {
        return searchManager.searchProductsByTypeAndAllergy(typeLabel, allergyLabel);
    }

    //Bussiness
    public void validateBookingCapacity(Book booking) throws BookingException {
        bookingManager.validateBookingCapacity(booking);
    }
}
