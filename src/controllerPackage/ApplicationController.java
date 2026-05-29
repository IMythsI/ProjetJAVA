package controllerPackage;

import businessPackage.BookingManager;
import businessPackage.LineOrderManager;
import businessPackage.OrderManager;
import businessPackage.ProductManager;
import businessPackage.SearchManager;
import businessPackage.TableManager;

import exceptionPackage.BookingException;
import exceptionPackage.LineOrderException;
import exceptionPackage.OrderException;
import exceptionPackage.ProductException;
import exceptionPackage.SearchException;
import exceptionPackage.TableException;

import modelPackage.Book;
import modelPackage.BookingSearchResult;
import modelPackage.LineOrder;
import modelPackage.Order;
import modelPackage.OrderSearchResult;
import modelPackage.Product;
import modelPackage.ProductSearchResult;
import modelPackage.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class ApplicationController {

    private final BookingManager bookingManager;
    private final TableManager tableManager;
    private final ProductManager productManager;
    private final OrderManager orderManager;
    private final LineOrderManager lineOrderManager;
    private final SearchManager searchManager;

    public ApplicationController() {
        bookingManager = new BookingManager();
        tableManager = new TableManager();
        productManager = new ProductManager();
        orderManager = new OrderManager();
        lineOrderManager = new LineOrderManager();
        searchManager = new SearchManager();
    }

    //BOOKING
    public ArrayList<Book> getAllBookings() throws BookingException {
        return bookingManager.getAllBookings();
    }

    public ArrayList<Book> getBookingsByDate(LocalDate date) throws BookingException {
        return bookingManager.getBookingsByDate(date);
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

    public void validateBookingCapacity(Book booking) throws BookingException {
        bookingManager.validateBookingCapacity(booking);
    }

    //TABLE
    public ArrayList<Table> getAllTables() throws TableException {
        return tableManager.getAllTables();
    }

    public void updateTableStatus(Integer idTable, String statusLabel) throws TableException {
        tableManager.updateTableStatus(idTable, statusLabel);
    }

    //PRODUCT
    public ArrayList<Product> getAllProducts() throws ProductException {
        return productManager.getAllProducts();
    }

    //ORDER
    public ArrayList<Order> getAllOrders() throws OrderException {
        return orderManager.getAllOrders();
    }

    public ArrayList<Order> getOrdersByTable(Integer idTable) throws OrderException {
        return orderManager.getOrdersByTable(idTable);
    }

    public BigDecimal getTotalAmountByTable(Integer idTable) throws OrderException {
        return orderManager.getTotalAmountByTable(idTable);
    }

    public void validateOrder(Order order, Map<Product, Integer> cart) throws OrderException {
        orderManager.validateOrder(order, cart);
    }

    public void deleteOrder(Integer idOrder) throws OrderException {
        orderManager.deleteOrder(idOrder);
    }

    //LINE ORDER
    public ArrayList<LineOrder> getLineOrdersByTable(Integer idTable)
            throws LineOrderException {

        return lineOrderManager.getLineOrdersByTable(idTable);
    }

    public ArrayList<LineOrder> getLineOrdersByOrder(Integer idOrder)
            throws LineOrderException {

        return lineOrderManager.getLineOrdersByOrder(idOrder);
    }

    public void updateLineOrderStatus(Integer idLineOrder, String statusLabel) throws LineOrderException {
        lineOrderManager.updateLineOrderStatus(idLineOrder, statusLabel);
    }

    //SEARCH
    public ArrayList<String> getBookingCustomerNames() throws SearchException {
        return searchManager.getBookingCustomerNames();
    }

    public ArrayList<BookingSearchResult> searchBookingsByCustomerAndDate(
            String customerName,
            LocalDate date
    ) throws SearchException {

        return searchManager.searchBookingsByCustomerAndDate(customerName, date);
    }

    public ArrayList<String> getWaiterNames() throws SearchException {
        return searchManager.getWaiterNames();
    }

    public ArrayList<String> getOrderStatusLabels() throws SearchException {
        return searchManager.getOrderStatusLabels();
    }

    public ArrayList<OrderSearchResult> searchOrdersByWaiterAndStatus(
            String waiterName,
            String statusLabel
    ) throws SearchException {

        return searchManager.searchOrdersByWaiterAndStatus(waiterName, statusLabel);
    }

    public ArrayList<String> getProductTypes() throws SearchException {
        return searchManager.getProductTypes();
    }

    public ArrayList<String> getAllergyLabels() throws SearchException {
        return searchManager.getAllergyLabels();
    }

    public ArrayList<ProductSearchResult> searchProductsByTypeAndAllergy(
            String typeLabel,
            String allergyLabel
    ) throws SearchException {

        return searchManager.searchProductsByTypeAndAllergy(typeLabel, allergyLabel);
    }

    public void addProduct(Product product) throws ProductException {
        productManager.addProduct(product);
    }

    public void updateProduct(Product oldProduct, Product newProduct) throws ProductException {
        productManager.updateProduct(oldProduct, newProduct);
    }

    public void deleteProduct(Product product) throws ProductException {
        productManager.deleteProduct(product);
    }
}