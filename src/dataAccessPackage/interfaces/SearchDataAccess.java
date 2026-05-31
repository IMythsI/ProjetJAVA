package dataAccessPackage.interfaces;

import exceptionPackage.SearchException;
import modelPackage.BookingSearchResult;
import modelPackage.OrderSearchResult;
import modelPackage.ProductSearchResult;

import java.time.LocalDate;
import java.util.ArrayList;

public interface SearchDataAccess {

    ArrayList<String> getBookingCustomerNames() throws SearchException;

    ArrayList<BookingSearchResult> searchBookingsByCustomerAndDate(String customerName, LocalDate date) throws SearchException;

    ArrayList<String> getWaiterNames() throws SearchException;

    ArrayList<String> getOrderStatusLabels() throws SearchException;

    ArrayList<OrderSearchResult> searchOrdersByWaiterAndStatus(String waiterName, String statusLabel) throws SearchException;

    ArrayList<String> getProductTypes() throws SearchException;

    ArrayList<String> getAllergyLabels() throws SearchException;

    ArrayList<ProductSearchResult> searchProductsByTypeAndAllergy(String typeLabel, String allergyLabel) throws SearchException;

    ArrayList<String> getBookingStatusLabels() throws SearchException;

    ArrayList<String> getTableStatusLabels() throws SearchException;

    ArrayList<String> getLineOrderStatusLabels() throws SearchException;
}