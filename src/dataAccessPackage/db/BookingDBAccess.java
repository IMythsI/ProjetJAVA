package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.BookingDataAccess;
import exceptionPackage.*;
import modelPackage.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookingDBAccess implements BookingDataAccess {

    @Override
    public ArrayList<Book> getBookingsByDate(LocalDate date) throws BookingException {
        ArrayList<Book> bookings = new ArrayList<>();

        String sql = """
                SELECT b.bookDate,
                       b.bookHour,
                       b.idTable,
                       b.nameCustomer,
                       b.nbPerson,
                       b.comment,
                       b.telCustomer,
                       b.statusLabel,
                       t.nbSeats
                FROM Book b
                INNER JOIN RestaurantTable t
                        ON b.idTable = t.idTable
                WHERE b.bookDate = ?
                ORDER BY b.bookHour
                """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setDate(1, Date.valueOf(date));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(createBookFromResultSet(resultSet));
                }
            }

            return bookings;

        } catch (SQLException | ConnectionException exception) {
            throw new BookingException(
                    "Erreur lors du chargement des réservations.",
                    exception
            );
        }
    }

    private Book createBookFromResultSet(ResultSet resultSet) throws SQLException {
        Status status = new Status(
                resultSet.getString("statusLabel")
        );

        Table table = new Table(
                resultSet.getInt("idTable"),
                resultSet.getInt("nbSeats"),
                status
        );

        return new Book(
                resultSet.getDate("bookDate").toLocalDate(),
                resultSet.getTime("bookHour").toLocalTime(),
                table,
                resultSet.getString("nameCustomer"),
                resultSet.getInt("nbPerson"),
                resultSet.getString("comment"),
                resultSet.getString("telCustomer"),
                status
        );
    }
}