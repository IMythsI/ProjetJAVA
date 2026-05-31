package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.TableDataAccess;
import exceptionPackage.ConnectionException;
import exceptionPackage.TableException;
import modelPackage.Status;
import modelPackage.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableDBAccess implements TableDataAccess {

    @Override
    public ArrayList<Table> getAllTables() throws TableException {
        ArrayList<Table> tables = new ArrayList<>();

        String sql = """
                SELECT idTable,
                       nbSeats,
                       statusLabel
                FROM RestaurantTable
                ORDER BY idTable
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    tables.add(createTableFromResultSet(resultSet));
                }
            }

            return tables;

        } catch (SQLException | ConnectionException exception) {
            throw new TableException(
                    "Erreur lors du chargement des tables.",
                    exception
            );
        }
    }

    @Override
    public void updateTableStatus(Integer idTable, String statusLabel) throws TableException {
        String sql = """
                UPDATE RestaurantTable
                SET statusLabel = ?
                WHERE idTable = ?
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, statusLabel);
                statement.setInt(2, idTable);

                int updatedRows = statement.executeUpdate();

                if (updatedRows == 0) {
                    throw new TableException("Aucune table n'a été modifiée.");
                }
            }

        } catch (SQLException | ConnectionException exception) {
            throw new TableException(
                    "Erreur lors du changement de statut de la table.",
                    exception
            );
        }
    }

    private Table createTableFromResultSet(ResultSet resultSet) throws SQLException {
        Status status = new Status(
                resultSet.getString("statusLabel")
        );

        return new Table(
                resultSet.getInt("idTable"),
                resultSet.getInt("nbSeats"),
                status
        );
    }

    @Override
    public void addTable(Table table) throws TableException {
        String sql = """
            INSERT INTO RestaurantTable (
                idTable,
                nbSeats,
                statusLabel
            )
            VALUES (?, ?, ?)
            """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, table.getIdTable());
                statement.setInt(2, table.getNbSeats());
                statement.setString(3, table.getStatus().getStatusLabel());

                statement.executeUpdate();
            }

        } catch (SQLException | ConnectionException exception) {
            throw new TableException(
                    "Erreur lors de l'ajout de la table. Vérifiez que le numéro de table n'existe pas déjà.",
                    exception
            );
        }
    }

    @Override
    public void deleteTable(Integer idTable) throws TableException {
        String sql = """
            DELETE FROM RestaurantTable
            WHERE idTable = ?
            """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, idTable);

                int deletedRows = statement.executeUpdate();

                if (deletedRows == 0) {
                    throw new TableException("Aucune table n'a été supprimée.");
                }
            }

        } catch (SQLException | ConnectionException exception) {
            throw new TableException(
                    "Erreur lors de la suppression de la table.",
                    exception
            );
        }
    }

    @Override
    public boolean isTableUsed(Integer idTable) throws TableException {
        String sql = """
            SELECT
                (
                    SELECT COUNT(*)
                    FROM Book
                    WHERE idTable = ?
                )
                +
                (
                    SELECT COUNT(*)
                    FROM CustomerOrder
                    WHERE idTable = ?
                ) AS usageCount
            """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, idTable);
                statement.setInt(2, idTable);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("usageCount") > 0;
                    }
                }
            }

            return false;

        } catch (SQLException | ConnectionException exception) {
            throw new TableException(
                    "Erreur lors de la vérification de la table.",
                    exception
            );
        }
    }
}