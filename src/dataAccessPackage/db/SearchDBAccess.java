package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.SearchDataAccess;
import exceptionPackage.ConnectionException;
import exceptionPackage.SearchException;
import modelPackage.BookingSearchResult;
import modelPackage.OrderSearchResult;
import modelPackage.ProductSearchResult;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

public class SearchDBAccess implements SearchDataAccess {

    //SEARCH 1
    @Override
    public ArrayList<String> getBookingCustomerNames() throws SearchException {
        ArrayList<String> customerNames = new ArrayList<>();

        String sql = """
                SELECT DISTINCT nameCustomer
                FROM Book
                ORDER BY nameCustomer
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    customerNames.add(resultSet.getString("nameCustomer"));
                }
            }

            return customerNames;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Erreur lors du chargement des clients.",
                    exception
            );
        }
    }

    @Override
    public ArrayList<BookingSearchResult> searchBookingsByCustomerAndDate(
            String customerName,
            LocalDate date
    ) throws SearchException {

        ArrayList<BookingSearchResult> results = new ArrayList<>();

        String sql = """
                SELECT b.bookDate,
                       b.bookHour,
                       b.nameCustomer,
                       b.nbPerson,
                       t.idTable,
                       t.nbSeats,
                       s.statusLabel
                FROM Book b
                INNER JOIN RestaurantTable t
                        ON b.idTable = t.idTable
                INNER JOIN Status s
                        ON b.statusLabel = s.statusLabel
                WHERE b.nameCustomer = ?
                  AND b.bookDate = ?
                ORDER BY b.bookHour
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, customerName);
                statement.setDate(2, Date.valueOf(date));

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        results.add(createBookingSearchResult(resultSet));
                    }
                }
            }

            return results;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Erreur lors de la recherche des réservations.",
                    exception
            );
        }
    }

    private BookingSearchResult createBookingSearchResult(ResultSet resultSet)
            throws SQLException {

        return new BookingSearchResult(
                resultSet.getDate("bookDate").toLocalDate(),
                resultSet.getTime("bookHour").toLocalTime(),
                resultSet.getString("nameCustomer"),
                resultSet.getInt("idTable"),
                resultSet.getInt("nbSeats"),
                resultSet.getInt("nbPerson"),
                resultSet.getString("statusLabel")
        );
    }

    //SEARCH 2
    @Override
    public ArrayList<String> getWaiterNames() throws SearchException {
        ArrayList<String> employeeNames = new ArrayList<>();

        String sql = """
                SELECT CONCAT(firstName, ' ', lastName) AS employeeName
                FROM Employee
                ORDER BY firstName, lastName
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    employeeNames.add(resultSet.getString("employeeName"));
                }
            }

            return employeeNames;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Erreur lors du chargement des employés.",
                    exception
            );
        }
    }

    @Override
    public ArrayList<String> getOrderStatusLabels() throws SearchException {
        ArrayList<String> statuses = new ArrayList<>();

        String sql = """
                SELECT DISTINCT statusLabel
                FROM LineOrder
                ORDER BY statusLabel
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    statuses.add(resultSet.getString("statusLabel"));
                }
            }

            return statuses;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Erreur lors du chargement des statuts de commande.",
                    exception
            );
        }
    }

    @Override
    public ArrayList<OrderSearchResult> searchOrdersByWaiterAndStatus(
            String waiterName,
            String statusLabel
    ) throws SearchException {

        ArrayList<OrderSearchResult> results = new ArrayList<>();

        String sql = """
                SELECT co.idOrder,
                       co.orderDate,
                       co.guestCount,
                       co.isTakeAway,
                       co.pickUpTime,
                       co.idTable,
                       CONCAT(e.firstName, ' ', e.lastName) AS waiterName,
                       lo.statusLabel,
                       COALESCE(SUM(lo.quantity * p.price), 0) AS totalAmount
                FROM CustomerOrder co
                INNER JOIN LineOrder lo
                        ON co.idOrder = lo.idOrder
                INNER JOIN Employee e
                        ON lo.registrationNb = e.registrationNb
                INNER JOIN Status s
                        ON lo.statusLabel = s.statusLabel
                INNER JOIN Product p
                        ON lo.productLabel = p.productLabel
                WHERE CONCAT(e.firstName, ' ', e.lastName) = ?
                  AND lo.statusLabel = ?
                GROUP BY co.idOrder,
                         co.orderDate,
                         co.guestCount,
                         co.isTakeAway,
                         co.pickUpTime,
                         co.idTable,
                         e.firstName,
                         e.lastName,
                         lo.statusLabel
                ORDER BY co.orderDate DESC, co.idOrder DESC
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, waiterName);
                statement.setString(2, statusLabel);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        results.add(createOrderSearchResult(resultSet));
                    }
                }
            }

            return results;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Erreur lors de la recherche des commandes.",
                    exception
            );
        }
    }

    private OrderSearchResult createOrderSearchResult(ResultSet resultSet)
            throws SQLException {

        Time pickUpSqlTime = resultSet.getTime("pickUpTime");

        return new OrderSearchResult(
                resultSet.getInt("idOrder"),
                resultSet.getDate("orderDate").toLocalDate(),
                resultSet.getObject("guestCount", Integer.class),
                resultSet.getBoolean("isTakeAway"),
                pickUpSqlTime == null ? null : pickUpSqlTime.toLocalTime(),
                resultSet.getObject("idTable", Integer.class),
                resultSet.getString("waiterName"),
                resultSet.getString("statusLabel"),
                resultSet.getDouble("totalAmount")
        );
    }

    //SEARCH 3
    @Override
    public ArrayList<String> getProductTypes() throws SearchException {
        ArrayList<String> types = new ArrayList<>();

        String sql = """
                SELECT typeLabel
                FROM Type
                ORDER BY typeLabel
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    types.add(resultSet.getString("typeLabel"));
                }
            }

            return types;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Erreur lors du chargement des types de produits.",
                    exception
            );
        }
    }

    @Override
    public ArrayList<String> getAllergyLabels() throws SearchException {
        ArrayList<String> allergies = new ArrayList<>();

        String sql = """
                SELECT allergyLabel
                FROM Allergy
                ORDER BY allergyLabel
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    allergies.add(resultSet.getString("allergyLabel"));
                }
            }

            return allergies;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Erreur lors du chargement des allergènes.",
                    exception
            );
        }
    }

    @Override
    public ArrayList<ProductSearchResult> searchProductsByTypeAndAllergy(
            String typeLabel,
            String allergyLabel
    ) throws SearchException {

        ArrayList<ProductSearchResult> results = new ArrayList<>();

        String sql = """
                SELECT p.productLabel,
                       p.price,
                       p.description,
                       t.typeLabel,
                       i.ingredientLabel,
                       a.allergyLabel
                FROM Product p
                INNER JOIN Type t
                        ON p.productType = t.typeLabel
                INNER JOIN IngredientProduct ip
                        ON p.productLabel = ip.productLabel
                INNER JOIN Ingredient i
                        ON ip.ingredientLabel = i.ingredientLabel
                INNER JOIN ListAllergy la
                        ON i.ingredientLabel = la.ingredientLabel
                INNER JOIN Allergy a
                        ON la.allergyLabel = a.allergyLabel
                WHERE t.typeLabel = ?
                  AND a.allergyLabel = ?
                ORDER BY p.productLabel, i.ingredientLabel
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, typeLabel);
                statement.setString(2, allergyLabel);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        results.add(createProductSearchResult(resultSet));
                    }
                }
            }

            return results;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Erreur lors de la recherche des produits.",
                    exception
            );
        }
    }

    private ProductSearchResult createProductSearchResult(ResultSet resultSet)
            throws SQLException {

        return new ProductSearchResult(
                resultSet.getString("productLabel"),
                resultSet.getDouble("price"),
                resultSet.getString("description"),
                resultSet.getString("typeLabel"),
                resultSet.getString("ingredientLabel"),
                resultSet.getString("allergyLabel")
        );
    }
}