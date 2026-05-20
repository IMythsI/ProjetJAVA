package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.OrderDataAccess;
import exceptionPackage.*;
import modelPackage.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class OrderDBAccess implements OrderDataAccess {

    @Override
    public ArrayList<Order> getAllOrders() throws OrderException {
        ArrayList<Order> orders = new ArrayList<>();

        String query = """
                SELECT co.idOrder, co.comment, co.guestCount, co.orderDate,
                       co.isTakeAway, co.pickUpTime, co.nameCustomer, co.telCustomer,
                       rt.idTable, rt.nbSeats, s.statusLabel
                FROM CustomerOrder co
                LEFT JOIN RestaurantTable rt ON co.idTable = rt.idTable
                LEFT JOIN Status s ON rt.statusLabel = s.statusLabel
                ORDER BY co.idOrder
                """;
        try (Connection connection = SingletonConnection.getInstance();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery();)
        {
            while (resultSet.next()) {
                Table table = null;

                Integer idTable = resultSet.getObject("idTable", Integer.class);

                if (idTable != null) {
                    Status status = new Status(resultSet.getString("statusLabel"));

                    table = new Table(
                            idTable,
                            resultSet.getInt("nbSeats"),
                            status
                    );
                }

                Time pickupTime = resultSet.getTime("pickUpTime");

                Order order = new Order(
                        resultSet.getInt("idOrder"),
                        resultSet.getString("comment"),
                        resultSet.getInt("guestCount"),
                        resultSet.getDate("orderDate").toLocalDate(),
                        resultSet.getBoolean("isTakeAway"),
                        pickupTime == null ? null : pickupTime.toLocalTime(),
                        resultSet.getString("nameCustomer"),
                        resultSet.getString("telCustomer"),
                        table
                );

                orders.add(order);
            }
            return orders;
        } catch (SQLException | ConnectionException exception) {
            throw new OrderException("Erreur lors du chargement des commandes", exception);
        }
    }

    @Override
    public ArrayList<Order> getOrdersByTable(Integer idTable) throws OrderException {
        ArrayList<Order> orders = new ArrayList<>();

        String sql = """
            SELECT idOrder, comment, guestCount, orderDate, isTakeAway,
                   pickUpTime, nameCustomer, telCustomer, idTable
            FROM CustomerOrder
            WHERE idTable = ?
            ORDER BY idOrder
            """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, idTable);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = new Order(
                            resultSet.getInt("idOrder"),
                            resultSet.getString("comment"),
                            resultSet.getInt("guestCount"),
                            resultSet.getDate("orderDate").toLocalDate(),
                            resultSet.getBoolean("isTakeAway"),
                            resultSet.getTime("pickUpTime") == null
                                    ? null
                                    : resultSet.getTime("pickUpTime").toLocalTime(),
                            resultSet.getString("nameCustomer"),
                            resultSet.getString("telCustomer"),
                            null
                    );

                    orders.add(order);
                }
            }

            return orders;

        } catch (SQLException | ConnectionException exception) {
            throw new OrderException("Erreur lors du chargement des commandes de la table.", exception);
        }
    }

    @Override
    public BigDecimal getTotalAmountByTable(Integer idTable) throws OrderException {
        String sql = """
            SELECT COALESCE(SUM(lo.quantity * p.price), 0) AS totalAmount
            FROM CustomerOrder co
            INNER JOIN LineOrder lo ON co.idOrder = lo.idOrder
            INNER JOIN Product p ON lo.productLabel = p.productLabel
            WHERE co.idTable = ?
            """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, idTable);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("totalAmount");
                }
            }

            return BigDecimal.ZERO;

        } catch (SQLException | ConnectionException exception) {
            throw new OrderException("Erreur lors du calcul du total de la table.", exception);
        }
    }

}
