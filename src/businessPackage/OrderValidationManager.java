package businessPackage;

import dataAccessPackage.SingletonConnection;
import exceptionPackage.ConnectionException;
import exceptionPackage.OrderException;
import modelPackage.Order;
import modelPackage.Product;

import java.sql.*;
import java.util.Map;

public class OrderValidationManager {

    public void validateOrder(Order order, Map<Product, Integer> cart) throws OrderException {
        String insertOrderSql = """
                INSERT INTO CustomerOrder
                (comment, guestCount, orderDate, isTakeAway, pickUpTime, nameCustomer, telCustomer, idTable)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String insertLineSql = """
                INSERT INTO LineOrder
                (quantity, productLabel, registrationNb, idOrder, statusLabel)
                VALUES (?, ?, ?, ?, ?)
                """;

        try {
            Connection connection = SingletonConnection.getInstance();
            connection.setAutoCommit(false);

            try (
                    PreparedStatement orderStatement = connection.prepareStatement(
                            insertOrderSql,
                            Statement.RETURN_GENERATED_KEYS
                    )
            ) {
                orderStatement.setString(1, order.getComment());
                orderStatement.setInt(2, order.getGuestCount());
                orderStatement.setDate(3, Date.valueOf(order.getOrderDate()));
                orderStatement.setBoolean(4, order.getIsTakeAway());

                if (order.getPickUpTime() == null) {
                    orderStatement.setNull(5, Types.TIME);
                } else {
                    orderStatement.setTime(5, Time.valueOf(order.getPickUpTime()));
                }

                orderStatement.setString(6, order.getNameCustomer());
                orderStatement.setString(7, order.getTelCustomer());

                if (order.getTable() == null) {
                    orderStatement.setNull(8, Types.INTEGER);
                } else {
                    orderStatement.setInt(8, order.getTable().getIdTable());
                }

                orderStatement.executeUpdate();

                int generatedOrderId;

                try (ResultSet generatedKeys = orderStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedOrderId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Aucun ID de commande généré.");
                    }
                }

                try (PreparedStatement lineStatement = connection.prepareStatement(insertLineSql)) {
                    for (Product product : cart.keySet()) {
                        lineStatement.setInt(1, cart.get(product));
                        lineStatement.setString(2, product.getProductLabel());

                        // Pour l’instant employé fixe. Plus tard : employé connecté.
                        lineStatement.setInt(3, 1001);

                        lineStatement.setInt(4, generatedOrderId);
                        lineStatement.setString(5, "Pending");

                        lineStatement.addBatch();
                    }

                    lineStatement.executeBatch();
                }

                connection.commit();

            } catch (SQLException exception) {
                connection.rollback();
                throw new OrderException("Erreur lors de la validation de la commande.", exception);

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException | ConnectionException exception) {
            throw new OrderException("Erreur lors de la validation de la commande.", exception);
        }
    }
}