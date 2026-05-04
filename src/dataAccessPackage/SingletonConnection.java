package dataAccessPackage;

import exceptionPackage.ConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {
    private static Connection connection;

    private static final String URL = "jdbc:mariadb://mysql-projetjava.alwaysdata.net:3306/projetjava_bd";
    private static final String USER = "projetjava";
    private static final String PASSWORD = "ML_Java";

    public static Connection getInstance() throws ConnectionException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.mariadb.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            return connection;
        } catch (ClassNotFoundException ex) {
            throw new ConnectionException("Driver MariaDB introuvable", ex);
        } catch (SQLException ex) {
            throw new ConnectionException("Erreur de connexion a la base de donnees", ex);
        }
    }
}
