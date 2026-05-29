package dataAccessPackage;

import exceptionPackage.ConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class SingletonConnection {

    private static final String DB_URL_ENV = "PROJETJAVA_DB_URL";
    private static final String DB_USER_ENV = "PROJETJAVA_DB_USER";
    private static final String DB_PASSWORD_ENV = "PROJETJAVA_DB_PASSWORD";

    private static final String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";

    private static Connection connection;

    private SingletonConnection() {
        // Utility class
    }

    public static synchronized Connection getInstance() throws ConnectionException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER_CLASS_NAME);

                connection = DriverManager.getConnection(
                        getRequiredEnvironmentValue(DB_URL_ENV),
                        getRequiredEnvironmentValue(DB_USER_ENV),
                        getRequiredEnvironmentValue(DB_PASSWORD_ENV)
                );
            }

            return connection;

        } catch (ClassNotFoundException exception) {
            throw new ConnectionException(
                    "Le driver MariaDB est introuvable.",
                    exception
            );

        } catch (SQLException exception) {
            throw new ConnectionException(
                    "Impossible de se connecter à la base de données.",
                    exception
            );
        }
    }

    public static synchronized void closeConnection() throws ConnectionException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }

            connection = null;

        } catch (SQLException exception) {
            throw new ConnectionException(
                    "Erreur lors de la fermeture de la connexion.",
                    exception
            );
        }
    }

    private static String getRequiredEnvironmentValue(String key) throws ConnectionException {
        String value = System.getenv(key);

        if (value == null || value.isBlank()) {
            throw new ConnectionException(
                    "La variable d'environnement " + key + " est manquante."
            );
        }

        return value;
    }
}