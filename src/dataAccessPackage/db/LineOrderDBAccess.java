package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.LineOrderDataAccess;
import exceptionPackage.ConnectionException;
import exceptionPackage.LineOrderException;
import modelPackage.Employee;
import modelPackage.JobType;
import modelPackage.LineOrder;
import modelPackage.Order;
import modelPackage.Product;
import modelPackage.Status;
import modelPackage.Table;
import modelPackage.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class LineOrderDBAccess implements LineOrderDataAccess {

    @Override
    public ArrayList<LineOrder> getLineOrdersByTable(Integer idTable)
            throws LineOrderException {

        ArrayList<LineOrder> lineOrders = new ArrayList<>();

        String sql = """
                SELECT lo.idLineOrder,
                       lo.quantity,
                       lo.statusLabel,
                       
                       p.productLabel,
                       p.price,
                       p.description,
                       p.productType,
                       
                       e.registrationNb,
                       e.lastName,
                       e.firstName,
                       e.jobLabel,
                       
                       co.idOrder,
                       co.comment,
                       co.guestCount,
                       co.orderDate,
                       co.isTakeAway,
                       co.pickUpTime,
                       co.nameCustomer,
                       co.telCustomer,
                       co.idTable,
                       
                       rt.nbSeats,
                       rt.statusLabel AS tableStatusLabel
                FROM LineOrder lo
                INNER JOIN Product p
                        ON lo.productLabel = p.productLabel
                INNER JOIN Employee e
                        ON lo.registrationNb = e.registrationNb
                INNER JOIN CustomerOrder co
                        ON lo.idOrder = co.idOrder
                LEFT JOIN RestaurantTable rt
                        ON co.idTable = rt.idTable
                WHERE co.idTable = ?
                ORDER BY co.idOrder, lo.idLineOrder
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, idTable);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        lineOrders.add(createLineOrderFromResultSet(resultSet));
                    }
                }
            }

            return lineOrders;

        } catch (SQLException | ConnectionException exception) {
            throw new LineOrderException(
                    "Erreur lors du chargement des lignes de commande de la table.",
                    exception
            );
        }
    }

    @Override
    public ArrayList<LineOrder> getLineOrdersByOrder(Integer idOrder)
            throws LineOrderException {

        ArrayList<LineOrder> lineOrders = new ArrayList<>();

        String sql = """
                SELECT lo.idLineOrder,
                       lo.quantity,
                       lo.statusLabel,
                       
                       p.productLabel,
                       p.price,
                       p.description,
                       p.productType,
                       
                       e.registrationNb,
                       e.lastName,
                       e.firstName,
                       e.jobLabel,
                       
                       co.idOrder,
                       co.comment,
                       co.guestCount,
                       co.orderDate,
                       co.isTakeAway,
                       co.pickUpTime,
                       co.nameCustomer,
                       co.telCustomer,
                       co.idTable,
                       
                       rt.nbSeats,
                       rt.statusLabel AS tableStatusLabel
                FROM LineOrder lo
                INNER JOIN Product p
                        ON lo.productLabel = p.productLabel
                INNER JOIN Employee e
                        ON lo.registrationNb = e.registrationNb
                INNER JOIN CustomerOrder co
                        ON lo.idOrder = co.idOrder
                LEFT JOIN RestaurantTable rt
                        ON co.idTable = rt.idTable
                WHERE co.idOrder = ?
                ORDER BY lo.idLineOrder
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, idOrder);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        lineOrders.add(createLineOrderFromResultSet(resultSet));
                    }
                }
            }

            return lineOrders;

        } catch (SQLException | ConnectionException exception) {
            throw new LineOrderException(
                    "Erreur lors du chargement des lignes de commande.",
                    exception
            );
        }
    }

    private LineOrder createLineOrderFromResultSet(ResultSet resultSet)
            throws SQLException {

        Product product = createProductFromResultSet(resultSet);
        Employee employee = createEmployeeFromResultSet(resultSet);
        Order order = createOrderFromResultSet(resultSet);

        Status lineStatus = new Status(
                resultSet.getString("statusLabel")
        );

        return new LineOrder(
                resultSet.getInt("idLineOrder"),
                resultSet.getInt("quantity"),
                product,
                employee,
                order,
                lineStatus
        );
    }

    private Product createProductFromResultSet(ResultSet resultSet)
            throws SQLException {

        Type productType = new Type(
                resultSet.getString("productType")
        );

        return new Product(
                resultSet.getString("productLabel"),
                resultSet.getBigDecimal("price"),
                resultSet.getString("description"),
                productType
        );
    }

    private Employee createEmployeeFromResultSet(ResultSet resultSet)
            throws SQLException {

        JobType jobType = new JobType(
                resultSet.getString("jobLabel")
        );

        return new Employee(
                resultSet.getInt("registrationNb"),
                resultSet.getString("lastName"),
                resultSet.getString("firstName"),
                jobType
        );
    }

    private Order createOrderFromResultSet(ResultSet resultSet)
            throws SQLException {

        Table table = createTableFromResultSet(resultSet);

        Time pickUpSqlTime = resultSet.getTime("pickUpTime");

        return new Order(
                resultSet.getInt("idOrder"),
                resultSet.getString("comment"),
                resultSet.getInt("guestCount"),
                resultSet.getDate("orderDate").toLocalDate(),
                resultSet.getBoolean("isTakeAway"),
                pickUpSqlTime == null ? null : pickUpSqlTime.toLocalTime(),
                resultSet.getString("nameCustomer"),
                resultSet.getString("telCustomer"),
                table
        );
    }

    private Table createTableFromResultSet(ResultSet resultSet)
            throws SQLException {

        Integer idTable = resultSet.getObject("idTable", Integer.class);

        if (idTable == null) {
            return null;
        }

        Status tableStatus = new Status(
                resultSet.getString("tableStatusLabel")
        );

        return new Table(
                idTable,
                resultSet.getInt("nbSeats"),
                tableStatus
        );
    }
}