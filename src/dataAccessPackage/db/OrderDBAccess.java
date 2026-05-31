package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.OrderDataAccess;
import exceptionPackage.ConnectionException;
import exceptionPackage.OrderException;
import modelPackage.Order;
import modelPackage.Product;
import modelPackage.Status;
import modelPackage.Table;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Map;

public class OrderDBAccess implements OrderDataAccess {

    @Override
    public ArrayList<Order> getAllOrders() throws OrderException {
        ArrayList<Order> orders = new ArrayList<>();

        String sql = """
                SELECT co.idOrder,
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
                FROM CustomerOrder co
                LEFT JOIN RestaurantTable rt
                       ON co.idTable = rt.idTable
                ORDER BY co.orderDate DESC, co.idOrder DESC
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    orders.add(createOrderFromResultSet(resultSet));
                }
            }

            return orders;

        } catch (SQLException | ConnectionException exception) {
            throw new OrderException(
                    "Erreur lors du chargement des commandes.",
                    exception
            );
        }
    }

    @Override
    public ArrayList<Order> getOrdersByTable(Integer idTable) throws OrderException {
        ArrayList<Order> orders = new ArrayList<>();

        String sql = """
                SELECT co.idOrder,
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
                FROM CustomerOrder co
                INNER JOIN RestaurantTable rt
                        ON co.idTable = rt.idTable
                WHERE co.idTable = ?
                ORDER BY co.orderDate DESC, co.idOrder DESC
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, idTable);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        orders.add(createOrderFromResultSet(resultSet));
                    }
                }
            }

            return orders;

        } catch (SQLException | ConnectionException exception) {
            throw new OrderException(
                    "Erreur lors du chargement des commandes de la table.",
                    exception
            );
        }
    }

    @Override
    public BigDecimal getTotalAmountByTable(Integer idTable) throws OrderException {
        String sql = """
                SELECT COALESCE(SUM(lo.quantity * p.price), 0) AS totalAmount
                FROM CustomerOrder co
                INNER JOIN LineOrder lo
                        ON co.idOrder = lo.idOrder
                INNER JOIN Product p
                        ON lo.productLabel = p.productLabel
                WHERE co.idTable = ?
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, idTable);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBigDecimal("totalAmount");
                    }
                }
            }

            return BigDecimal.ZERO;

        } catch (SQLException | ConnectionException exception) {
            throw new OrderException(
                    "Erreur lors du calcul du total de la table.",
                    exception
            );
        }
    }

    @Override
    public void addOrder(Order order) throws OrderException {
        String sql = """
            INSERT INTO CustomerOrder (
                comment,
                guestCount,
                orderDate,
                isTakeAway,
                pickUpTime,
                nameCustomer,
                telCustomer,
                idTable
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                fillOrderStatement(statement, order);
                statement.executeUpdate();
            }

        } catch (SQLException | ConnectionException exception) {
            throw new OrderException(
                    "Erreur lors de l'ajout de la commande.",
                    exception
            );
        }
    }

    @Override
    public void updateOrder(Order order) throws OrderException {
        String sql = """
            UPDATE CustomerOrder
            SET comment = ?,
                guestCount = ?,
                orderDate = ?,
                isTakeAway = ?,
                pickUpTime = ?,
                nameCustomer = ?,
                telCustomer = ?,
                idTable = ?
            WHERE idOrder = ?
            """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                fillOrderStatement(statement, order);
                statement.setInt(9, order.getIdOrder());

                int updatedRows = statement.executeUpdate();

                if (updatedRows == 0) {
                    throw new OrderException("Aucune commande n'a été modifiée.");
                }
            }

        } catch (OrderException exception) {
            throw exception;

        } catch (SQLException | ConnectionException exception) {
            throw new OrderException(
                    "Erreur lors de la modification de la commande.",
                    exception
            );
        }
    }

    @Override
    public void addOrderWithLines(Order order, Map<Product, Integer> cart)
            throws OrderException {

        String orderSql = """
                INSERT INTO CustomerOrder (
                    comment,
                    guestCount,
                    orderDate,
                    isTakeAway,
                    pickUpTime,
                    nameCustomer,
                    telCustomer,
                    idTable
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String lineOrderSql = """
                INSERT INTO LineOrder (
                    quantity,
                    productLabel,
                    registrationNb,
                    idOrder,
                    statusLabel
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection connection = null;
        boolean previousAutoCommit = true;

        try {
            connection = SingletonConnection.getInstance();

            previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            Integer generatedOrderId;

            try (PreparedStatement orderStatement = connection.prepareStatement(
                    orderSql,
                    Statement.RETURN_GENERATED_KEYS
            )) {
                fillOrderStatement(orderStatement, order);
                orderStatement.executeUpdate();

                generatedOrderId = getGeneratedOrderId(orderStatement);
            }

            try (PreparedStatement lineStatement = connection.prepareStatement(lineOrderSql)) {
                for (Product product : cart.keySet()) {
                    Integer quantity = cart.get(product);
                    Integer employeeRegistrationNb = findEmployeeRegistrationForProduct(connection, product);

                    lineStatement.setInt(1, quantity);
                    lineStatement.setString(2, product.getProductLabel());
                    lineStatement.setInt(3, employeeRegistrationNb);
                    lineStatement.setInt(4, generatedOrderId);
                    lineStatement.setString(5, "Pending");

                    lineStatement.addBatch();
                }

                lineStatement.executeBatch();
            }

            connection.commit();

        } catch (OrderException exception) {
            rollback(connection);
            throw exception;

        } catch (SQLException | ConnectionException exception) {
            rollback(connection);

            throw new OrderException(
                    "Erreur lors de l'enregistrement de la commande.",
                    exception
            );

        } finally {
            restoreAutoCommit(connection, previousAutoCommit);
        }
    }

    private void fillOrderStatement(PreparedStatement statement, Order order)
            throws SQLException {

        setNullableString(statement, 1, order.getComment());

        statement.setInt(2, order.getGuestCount());
        statement.setDate(3, Date.valueOf(order.getOrderDate()));
        statement.setBoolean(4, order.getIsTakeAway());

        if (order.getPickUpTime() == null) {
            statement.setNull(5, Types.TIME);
        } else {
            statement.setTime(5, Time.valueOf(order.getPickUpTime()));
        }

        setNullableString(statement, 6, order.getNameCustomer());
        setNullableString(statement, 7, order.getTelCustomer());

        if (order.getTable() == null) {
            statement.setNull(8, Types.INTEGER);
        } else {
            statement.setInt(8, order.getTable().getIdTable());
        }
    }

    private Integer getGeneratedOrderId(PreparedStatement statement)
            throws SQLException, OrderException {

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }

        throw new OrderException("Impossible de récupérer le numéro de la commande créée.");
    }

    private Integer findEmployeeRegistrationForProduct(Connection connection, Product product)
            throws SQLException, OrderException {

        String preferredJob = getPreferredJobForProduct(product);

        Integer registrationNb = findEmployeeByJob(connection, preferredJob);

        if (registrationNb != null) {
            return registrationNb;
        }

        registrationNb = findAnyEmployee(connection);

        if (registrationNb != null) {
            return registrationNb;
        }

        throw new OrderException("Aucun employé disponible pour traiter la commande.");
    }

    private String getPreferredJobForProduct(Product product) {
        if (product == null || product.getProductType() == null) {
            return "Waiter";
        }

        String typeLabel = product.getProductType().getTypeLabel();

        return switch (typeLabel) {
            case "Dish", "Dessert", "Menu" -> "Cook";
            case "Drink" -> "Waiter";
            default -> "Waiter";
        };
    }

    private Integer findEmployeeByJob(Connection connection, String jobLabel)
            throws SQLException {

        String sql = """
                SELECT registrationNb
                FROM Employee
                WHERE jobLabel = ?
                ORDER BY registrationNb
                LIMIT 1
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, jobLabel);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("registrationNb");
                }
            }
        }

        return null;
    }

    private Integer findAnyEmployee(Connection connection)
            throws SQLException {

        String sql = """
                SELECT registrationNb
                FROM Employee
                ORDER BY registrationNb
                LIMIT 1
                """;

        try (
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                return resultSet.getInt("registrationNb");
            }
        }

        return null;
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

    private void setNullableString(PreparedStatement statement, int index, String value)
            throws SQLException {

        if (value == null || value.isBlank()) {
            statement.setNull(index, Types.VARCHAR);
        } else {
            statement.setString(index, value);
        }
    }

    private void rollback(Connection connection) {
        if (connection == null) {
            return;
        }

        try {
            connection.rollback();
        } catch (SQLException ignored) {
        }
    }

    private void restoreAutoCommit(Connection connection, boolean previousAutoCommit) {
        if (connection == null) {
            return;
        }

        try {
            connection.setAutoCommit(previousAutoCommit);
        } catch (SQLException ignored) {
        }
    }

    @Override
    public void deleteOrder(Integer idOrder) throws OrderException {
        String deleteLineOrdersSql = """
            DELETE FROM LineOrder
            WHERE idOrder = ?
            """;

        String deleteOrderSql = """
            DELETE FROM CustomerOrder
            WHERE idOrder = ?
            """;

        Connection connection = null;
        boolean previousAutoCommit = true;

        try {
            connection = SingletonConnection.getInstance();

            previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try (PreparedStatement lineStatement = connection.prepareStatement(deleteLineOrdersSql)) {
                lineStatement.setInt(1, idOrder);
                lineStatement.executeUpdate();
            }

            try (PreparedStatement orderStatement = connection.prepareStatement(deleteOrderSql)) {
                orderStatement.setInt(1, idOrder);

                int deletedRows = orderStatement.executeUpdate();

                if (deletedRows == 0) {
                    throw new OrderException("Aucune commande n'a été supprimée.");
                }
            }

            connection.commit();

        } catch (OrderException exception) {
            rollback(connection);
            throw exception;

        } catch (SQLException | ConnectionException exception) {
            rollback(connection);

            throw new OrderException(
                    "Erreur lors de la suppression de la commande.",
                    exception
            );

        } finally {
            restoreAutoCommit(connection, previousAutoCommit);
        }
    }

    @Override
    public void deleteOrders(ArrayList<Integer> orderIds) throws OrderException {
        if (orderIds == null || orderIds.isEmpty()) {
            throw new OrderException("Aucune commande n'a été supprimée.");
        }

        String deleteLineOrdersSql = """
            DELETE FROM LineOrder
            WHERE idOrder = ?
            """;

        String deleteOrderSql = """
            DELETE FROM CustomerOrder
            WHERE idOrder = ?
            """;

        Connection connection = null;
        boolean previousAutoCommit = true;

        try {
            connection = SingletonConnection.getInstance();

            previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try (PreparedStatement lineStatement = connection.prepareStatement(deleteLineOrdersSql)) {
                for (Integer idOrder : orderIds) {
                    lineStatement.setInt(1, idOrder);
                    lineStatement.addBatch();
                }

                lineStatement.executeBatch();
            }

            int deletedRows = 0;

            try (PreparedStatement orderStatement = connection.prepareStatement(deleteOrderSql)) {
                for (Integer idOrder : orderIds) {
                    orderStatement.setInt(1, idOrder);
                    deletedRows += orderStatement.executeUpdate();
                }
            }

            if (deletedRows == 0) {
                throw new OrderException("Aucune commande n'a été supprimée.");
            }

            connection.commit();

        } catch (OrderException exception) {
            rollback(connection);
            throw exception;

        } catch (SQLException | ConnectionException exception) {
            rollback(connection);

            throw new OrderException(
                    "Erreur lors de la suppression des commandes.",
                    exception
            );

        } finally {
            restoreAutoCommit(connection, previousAutoCommit);
        }
    }
}