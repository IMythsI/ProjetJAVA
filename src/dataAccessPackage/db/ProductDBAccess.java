package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.ProductDataAccess;
import exceptionPackage.ConnectionException;
import exceptionPackage.ProductException;
import modelPackage.Product;
import modelPackage.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDBAccess implements ProductDataAccess {

    @Override
    public ArrayList<Product> getAllProducts() throws ProductException {
        ArrayList<Product> products = new ArrayList<>();

        String sql = """
                SELECT p.productLabel,
                       p.price,
                       p.description,
                       p.productType
                FROM Product p
                INNER JOIN Type t
                        ON p.productType = t.typeLabel
                ORDER BY p.productType, p.productLabel
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    products.add(createProductFromResultSet(resultSet));
                }
            }

            return products;

        } catch (SQLException | ConnectionException exception) {
            throw new ProductException(
                    "Erreur lors du chargement des produits.",
                    exception
            );
        }
    }

    private Product createProductFromResultSet(ResultSet resultSet) throws SQLException {
        Type type = new Type(
                resultSet.getString("productType")
        );

        return new Product(
                resultSet.getString("productLabel"),
                resultSet.getBigDecimal("price"),
                resultSet.getString("description"),
                type
        );
    }
}