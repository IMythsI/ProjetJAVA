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

    @Override
    public void addProduct(Product product) throws ProductException {
        String sql = """
            INSERT INTO Product (
                productLabel,
                price,
                description,
                productType
            )
            VALUES (?, ?, ?, ?)
            """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                fillProductStatement(statement, product);
                statement.executeUpdate();
            }

        } catch (SQLException | ConnectionException exception) {
            throw new ProductException(
                    "Erreur lors de l'ajout du produit.",
                    exception
            );
        }
    }

    @Override
    public void updateProduct(Product oldProduct, Product newProduct) throws ProductException {
        String sql = """
            UPDATE Product
            SET productLabel = ?,
                price = ?,
                description = ?,
                productType = ?
            WHERE productLabel = ?
            """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                fillProductStatement(statement, newProduct);
                statement.setString(5, oldProduct.getProductLabel());

                int updatedRows = statement.executeUpdate();

                if (updatedRows == 0) {
                    throw new ProductException("Aucun produit n'a été modifié.");
                }
            }

        } catch (SQLException | ConnectionException exception) {
            throw new ProductException(
                    "Erreur lors de la modification du produit.",
                    exception
            );
        }
    }

    @Override
    public void deleteProduct(Product product) throws ProductException {
        String sql = """
            DELETE FROM Product
            WHERE productLabel = ?
            """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, product.getProductLabel());

                int deletedRows = statement.executeUpdate();

                if (deletedRows == 0) {
                    throw new ProductException("Aucun produit n'a été supprimé.");
                }
            }

        } catch (SQLException | ConnectionException exception) {
            throw new ProductException(
                    "Erreur lors de la suppression du produit.",
                    exception
            );
        }
    }

    private void fillProductStatement(PreparedStatement statement, Product product)
            throws SQLException {

        statement.setString(1, product.getProductLabel());
        statement.setBigDecimal(2, product.getPrice());
        statement.setString(3, product.getDescription());
        statement.setString(4, product.getProductType().getTypeLabel());    }

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