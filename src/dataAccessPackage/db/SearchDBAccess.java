package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.SearchDataAccess;
import exceptionPackage.ConnectionException;
import exceptionPackage.SearchException;
import modelPackage.BookingSearchResult;
import modelPackage.OrderSearchResult;
import modelPackage.ProductSearchResult;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class SearchDBAccess implements SearchDataAccess {

    @Override
    public ArrayList<String> getBookingCustomerNames() throws SearchException {
        ArrayList<String> customerNames = new ArrayList<>();

        String sql = """
                SELECT DISTINCT nameCustomer
                FROM Book
                ORDER BY nameCustomer
                """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                customerNames.add(resultSet.getString("nameCustomer"));
            }

            return customerNames;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Error while loading booking customers.",
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

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, customerName);
            statement.setDate(2, Date.valueOf(date));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(new BookingSearchResult(
                            resultSet.getDate("bookDate").toLocalDate(),
                            resultSet.getTime("bookHour").toLocalTime(),
                            resultSet.getString("nameCustomer"),
                            resultSet.getInt("idTable"),
                            resultSet.getInt("nbSeats"),
                            resultSet.getInt("nbPerson"),
                            resultSet.getString("statusLabel")
                    ));
                }
            }

            return results;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Error while searching bookings.",
                    exception
            );
        }
    }

    @Override
    public ArrayList<String> getWaiterNames() throws SearchException {
        ArrayList<String> waiterNames = new ArrayList<>();

        String sql = """
            SELECT CONCAT(firstName, ' ', lastName) AS waiterName
            FROM Employee
            ORDER BY firstName, lastName
            """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                waiterNames.add(resultSet.getString("waiterName"));
            }

            return waiterNames;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Error while loading employees.",
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

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                statuses.add(resultSet.getString("statusLabel"));
            }

            return statuses;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Error while loading order statuses.",
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
                   SUM(lo.quantity * p.price) AS totalAmount
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
            ORDER BY co.orderDate DESC
            """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, waiterName);
            statement.setString(2, statusLabel);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Time pickUpSqlTime = resultSet.getTime("pickUpTime");

                    results.add(new OrderSearchResult(
                            resultSet.getInt("idOrder"),
                            resultSet.getDate("orderDate").toLocalDate(),
                            resultSet.getObject("guestCount", Integer.class),
                            resultSet.getBoolean("isTakeAway"),
                            pickUpSqlTime == null ? null : pickUpSqlTime.toLocalTime(),
                            resultSet.getObject("idTable", Integer.class),
                            resultSet.getString("waiterName"),
                            resultSet.getString("statusLabel"),
                            resultSet.getDouble("totalAmount")
                    ));
                }
            }

            return results;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Error while searching orders.",
                    exception
            );
        }
    }

    @Override
    public ArrayList<String> getProductTypes() throws SearchException {
        ArrayList<String> types = new ArrayList<>();

        String sql = """
            SELECT typeLabel
            FROM Type
            ORDER BY typeLabel
            """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                types.add(resultSet.getString("typeLabel"));
            }

            return types;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Error while loading product types.",
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

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                allergies.add(resultSet.getString("allergyLabel"));
            }

            return allergies;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Error while loading allergies.",
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

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, typeLabel);
            statement.setString(2, allergyLabel);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(new ProductSearchResult(
                            resultSet.getString("productLabel"),
                            resultSet.getDouble("price"),
                            resultSet.getString("description"),
                            resultSet.getString("typeLabel"),
                            resultSet.getString("ingredientLabel"),
                            resultSet.getString("allergyLabel")
                    ));
                }
            }

            return results;

        } catch (SQLException | ConnectionException exception) {
            throw new SearchException(
                    "Error while searching products by type and allergy.",
                    exception
            );
        }
    }
}