package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.BookingDataAccess;
import exceptionPackage.BookingException;
import exceptionPackage.ConnectionException;
import modelPackage.Book;
import modelPackage.Status;
import modelPackage.Table;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookingDBAccess implements BookingDataAccess {

    @Override
    public ArrayList<Book> getAllBookings() throws BookingException {
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
                ORDER BY b.bookDate, b.bookHour
                """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                bookings.add(createBookFromResultSet(resultSet));
            }

            return bookings;

        } catch (SQLException | ConnectionException exception) {
            throw new BookingException(
                    "Erreur lors du chargement de toutes les réservations.",
                    exception
            );
        }
    }

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

    @Override
    public void addBooking(Book booking) throws BookingException {
        String sql = """
                INSERT INTO Book
                    (bookDate, bookHour, idTable, nameCustomer, nbPerson, comment, telCustomer, statusLabel)
                VALUES
                    (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            fillBookingValues(statement, booking, 1);
            statement.executeUpdate();

        } catch (SQLException | ConnectionException exception) {
            throw new BookingException(
                    "Erreur lors de l'ajout de la réservation.",
                    exception
            );
        }
    }

    @Override
    public void updateBooking(Book oldBooking, Book newBooking) throws BookingException {
        String sql = """
            UPDATE Book
            SET bookDate = ?,
                bookHour = ?,
                idTable = ?,
                nameCustomer = ?,
                nbPerson = ?,
                comment = ?,
                telCustomer = ?,
                statusLabel = ?
            WHERE bookDate = ?
              AND bookHour = ?
              AND idTable = ?
              AND nameCustomer = ?
            """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setDate(1, Date.valueOf(newBooking.getBookDate()));
            statement.setTime(2, Time.valueOf(newBooking.getBookHour()));
            statement.setInt(3, newBooking.getTable().getIdTable());
            statement.setString(4, newBooking.getNameCustomer());
            statement.setInt(5, newBooking.getNbPerson());

            if (newBooking.getComment() == null || newBooking.getComment().isBlank()) {
                statement.setNull(6, Types.VARCHAR);
            } else {
                statement.setString(6, newBooking.getComment());
            }

            if (newBooking.getTelCustomer() == null || newBooking.getTelCustomer().isBlank()) {
                statement.setNull(7, Types.VARCHAR);
            } else {
                statement.setString(7, newBooking.getTelCustomer());
            }

            statement.setString(8, newBooking.getStatus().getStatusLabel());

            statement.setDate(9, Date.valueOf(oldBooking.getBookDate()));
            statement.setTime(10, Time.valueOf(oldBooking.getBookHour()));
            statement.setInt(11, oldBooking.getTable().getIdTable());
            statement.setString(12, oldBooking.getNameCustomer());

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new BookingException("No booking has been updated.");
            }

        } catch (SQLException | ConnectionException exception) {
            throw new BookingException(
                    "Error while updating the booking.",
                    exception
            );
        }
    }

    @Override
    public void deleteBooking(Book booking) throws BookingException {
        String sql = """
                DELETE FROM Book
                WHERE bookDate = ?
                  AND bookHour = ?
                  AND idTable = ?
                  AND nameCustomer = ?
                """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setDate(1, Date.valueOf(booking.getBookDate()));
            statement.setTime(2, Time.valueOf(booking.getBookHour()));
            statement.setInt(3, booking.getTable().getIdTable());
            statement.setString(4, booking.getNameCustomer());

            int deletedRows = statement.executeUpdate();

            if (deletedRows == 0) {
                throw new BookingException("Aucune réservation n'a été supprimée.");
            }

        } catch (SQLException | ConnectionException exception) {
            throw new BookingException(
                    "Erreur lors de la suppression de la réservation.",
                    exception
            );
        }
    }

    private void fillBookingValues(PreparedStatement statement, Book booking, int startIndex)
            throws SQLException {

        statement.setDate(startIndex, Date.valueOf(booking.getBookDate()));
        statement.setTime(startIndex + 1, Time.valueOf(booking.getBookHour()));
        statement.setInt(startIndex + 2, booking.getTable().getIdTable());
        statement.setString(startIndex + 3, booking.getNameCustomer());
        statement.setInt(startIndex + 4, booking.getNbPerson());

        setNullableString(statement, startIndex + 5, booking.getComment());
        setNullableString(statement, startIndex + 6, booking.getTelCustomer());

        statement.setString(startIndex + 7, booking.getStatus().getStatusLabel());
    }

    private void setNullableString(PreparedStatement statement, int index, String value)
            throws SQLException {

        if (value == null || value.isBlank()) {
            statement.setNull(index, Types.VARCHAR);
        } else {
            statement.setString(index, value);
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

    @Override
    public boolean isTableAlreadyBooked(Book booking) throws BookingException {
        String sql = """
            SELECT COUNT(*) AS nbBookings
            FROM Book
            WHERE bookDate = ?
              AND bookHour = ?
              AND idTable = ?
              AND statusLabel <> 'Cancelled'
            """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setDate(1, Date.valueOf(booking.getBookDate()));
            statement.setTime(2, Time.valueOf(booking.getBookHour()));
            statement.setInt(3, booking.getTable().getIdTable());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("nbBookings") > 0;
                }
            }

            return false;

        } catch (SQLException | ConnectionException exception) {
            throw new BookingException(
                    "Erreur lors de la vérification de disponibilité de la table.",
                    exception
            );
        }
    }

    @Override
    public boolean isTableAlreadyBookedForAnotherBooking(Book oldBooking, Book newBooking)
            throws BookingException {

        String sql = """
            SELECT COUNT(*) AS nbBookings
            FROM Book
            WHERE bookDate = ?
              AND bookHour = ?
              AND idTable = ?
              AND statusLabel <> 'Cancelled'
              AND NOT (
                    bookDate = ?
                AND bookHour = ?
                AND idTable = ?
                AND nameCustomer = ?
              )
            """;

        try (
                Connection connection = SingletonConnection.getInstance();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setDate(1, Date.valueOf(newBooking.getBookDate()));
            statement.setTime(2, Time.valueOf(newBooking.getBookHour()));
            statement.setInt(3, newBooking.getTable().getIdTable());

            statement.setDate(4, Date.valueOf(oldBooking.getBookDate()));
            statement.setTime(5, Time.valueOf(oldBooking.getBookHour()));
            statement.setInt(6, oldBooking.getTable().getIdTable());
            statement.setString(7, oldBooking.getNameCustomer());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("nbBookings") > 0;
                }
            }

            return false;

        } catch (SQLException | ConnectionException exception) {
            throw new BookingException(
                    "Erreur lors de la vérification de disponibilité de la table.",
                    exception
            );
        }
    }
}