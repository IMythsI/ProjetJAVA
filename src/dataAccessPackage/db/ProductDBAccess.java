package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.ProductDataAccess;
import exceptionPackage.ConnectionException;
import exceptionPackage.ProductException;
import modelPackage.Product;
import modelPackage.Type;

import java.sql.*;
import java.util.ArrayList;

public class ProductDBAccess implements ProductDataAccess {

    @Override
    public ArrayList<Product> getAllProducts() throws ProductException {
        ArrayList<Product> products = new ArrayList<>();

        String sql = """
                SELECT productLabel, price, description, productType
                FROM Product
                ORDER BY productType, productLabel
                """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Type type = new Type(resultSet.getString("productType"));

                Product product = new Product(
                        resultSet.getString("productLabel"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getString("description"),
                        type
                );

                products.add(product);
            }

            return products;

        } catch (SQLException | ConnectionException exception) {
            throw new ProductException("Erreur lors du chargement des produits.", exception);
        }
    }
}