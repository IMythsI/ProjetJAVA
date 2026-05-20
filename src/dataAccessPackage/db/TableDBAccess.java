package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.TableDataAccess;
import exceptionPackage.*;
import modelPackage.*;

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
        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Status status = new Status(
                        resultSet.getString("statusLabel")
                );
                Table table = new Table(
                        resultSet.getInt("idTable"),
                        resultSet.getInt("nbSeats"),
                        status
                );
                tables.add(table);
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

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, statusLabel);
            statement.setInt(2, idTable);

            statement.executeUpdate();

        } catch (SQLException | ConnectionException exception) {
            throw new TableException("Erreur lors du changement de statut de la table.", exception);
        }
    }
}
