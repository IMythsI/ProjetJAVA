package businessPackage;

import dataAccessPackage.db.SearchDBAccess;
import dataAccessPackage.interfaces.SearchDataAccess;
import exceptionPackage.SearchException;
import modelPackage.BookingSearchResult;
import modelPackage.OrderSearchResult;
import modelPackage.ProductSearchResult;

import java.time.LocalDate;
import java.util.ArrayList;

public class SearchManager {

    private final SearchDataAccess searchDAO;

    public SearchManager() {
        searchDAO = new SearchDBAccess();
    }

    public ArrayList<String> getBookingCustomerNames() throws SearchException {
        return searchDAO.getBookingCustomerNames();
    }

    public ArrayList<BookingSearchResult> searchBookingsByCustomerAndDate(
            String customerName,
            LocalDate date
    ) throws SearchException {

        if (customerName == null || customerName.isBlank()) {
            throw new SearchException("Le client est obligatoire.");
        }

        if (date == null) {
            throw new SearchException("La date est obligatoire.");
        }

        return searchDAO.searchBookingsByCustomerAndDate(customerName, date);
    }

    public ArrayList<String> getWaiterNames() throws SearchException {
        return searchDAO.getWaiterNames();
    }

    public ArrayList<String> getOrderStatusLabels() throws SearchException {
        return searchDAO.getOrderStatusLabels();
    }

    public ArrayList<OrderSearchResult> searchOrdersByWaiterAndStatus(
            String waiterName,
            String statusLabel
    ) throws SearchException {

        if (waiterName == null || waiterName.isBlank()) {
            throw new SearchException("L'employé est obligatoire.");
        }

        if (statusLabel == null || statusLabel.isBlank()) {
            throw new SearchException("Le statut est obligatoire.");
        }

        return searchDAO.searchOrdersByWaiterAndStatus(waiterName, statusLabel);
    }

    public ArrayList<String> getProductTypes() throws SearchException {
        return searchDAO.getProductTypes();
    }

    public ArrayList<String> getAllergyLabels() throws SearchException {
        return searchDAO.getAllergyLabels();
    }

    public ArrayList<ProductSearchResult> searchProductsByTypeAndAllergy(
            String typeLabel,
            String allergyLabel
    ) throws SearchException {

        if (typeLabel == null || typeLabel.isBlank()) {
            throw new SearchException("Le type de produit est obligatoire.");
        }

        if (allergyLabel == null || allergyLabel.isBlank()) {
            throw new SearchException("L'allergène est obligatoire.");
        }

        return searchDAO.searchProductsByTypeAndAllergy(typeLabel, allergyLabel);
    }
}