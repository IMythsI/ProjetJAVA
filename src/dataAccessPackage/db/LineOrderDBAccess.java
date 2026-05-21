package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.LineOrderDataAccess;
import exceptionPackage.ConnectionException;
import exceptionPackage.LineOrderException;
import modelPackage.*;

import java.sql.*;
import java.util.ArrayList;

public class LineOrderDBAccess implements LineOrderDataAccess {

    @Override
    public ArrayList<LineOrder> getLineOrdersByTable(Integer idTable) throws LineOrderException {
        ArrayList<LineOrder> lines = new ArrayList<>();

        String sql = """
                SELECT lo.idLineOrder,
                       lo.quantity,
                       p.productLabel,
                       p.price,
                       p.description,
                       t.typeLabel,
                       e.registrationNb,
                       e.lastName,
                       e.firstName,
                       jt.jobLabel,
                       co.idOrder,
                       s.statusLabel
                FROM LineOrder lo
                INNER JOIN Product p ON lo.productLabel = p.productLabel
                INNER JOIN Type t ON p.productType = t.typeLabel
                INNER JOIN Employee e ON lo.registrationNb = e.registrationNb
                INNER JOIN JobType jt ON e.jobLabel = jt.jobLabel
                INNER JOIN CustomerOrder co ON lo.idOrder = co.idOrder
                INNER JOIN Status s ON lo.statusLabel = s.statusLabel
                WHERE co.idTable = ?
                ORDER BY lo.idLineOrder
                """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, idTable);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    Type type = new Type(resultSet.getString("typeLabel"));

                    Product product = new Product(
                            resultSet.getString("productLabel"),
                            resultSet.getBigDecimal("price"),
                            resultSet.getString("description"),
                            type
                    );

                    JobType jobType = new JobType(resultSet.getString("jobLabel"));

                    Employee employee = new Employee(
                            resultSet.getInt("registrationNb"),
                            resultSet.getString("lastName"),
                            resultSet.getString("firstName"),
                            jobType
                    );

                    Order order = new Order(
                            resultSet.getInt("idOrder"),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );

                    Status status = new Status(resultSet.getString("statusLabel"));

                    LineOrder lineOrder = new LineOrder(
                            resultSet.getInt("idLineOrder"),
                            resultSet.getInt("quantity"),
                            product,
                            employee,
                            order,
                            status
                    );

                    lines.add(lineOrder);
                }
            }

            return lines;

        } catch (SQLException | ConnectionException exception) {
            throw new LineOrderException("Erreur lors du chargement des lignes de commande.", exception);
        }
    }
}