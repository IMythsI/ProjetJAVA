package dataAccessPackage;

import exceptionPackage.ConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {
    private static Connection connection;

    private static final String URL = "jdbc:mariadb://localhost:3306/ProjetJAVA";
    private static final String USER = "projetjava";
    private static final String PASSWORD = "ML_Java";

    public static Connection getInstance() throws ConnectionException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            return connection;
        } catch (SQLException ex) {
            throw new ConnectionException("Erreur de connection à la base de données", ex);
        }
    }

}
