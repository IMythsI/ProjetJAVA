package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.OrderDataAccess;
import exceptionPackage.ConnectionException;
import exceptionPackage.OrderException;
import modelPackage.Order;
import modelPackage.Status;
import modelPackage.Table;

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
    public void addOrder(Order order) throws OrderException {

    }

    @Override
    public void updateOrder(Order order) throws OrderException {

    }

    @Override
    public void deleteOrder(Integer idOrder) throws OrderException {

    }
}
