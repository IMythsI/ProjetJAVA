package dataAccessPackage.db;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.interfaces.BookingDataAccess;
import exceptionPackage.BookingException;
import exceptionPackage.ConnectionException;
import modelPackage.Book;
import modelPackage.Status;
import modelPackage.Table;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Time;
import java.sql.Types;
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
                       b.statusLabel AS bookStatusLabel,
                       t.nbSeats,
                       t.statusLabel AS tableStatusLabel
                FROM Book b
                INNER JOIN RestaurantTable t
                        ON b.idTable = t.idTable
                ORDER BY b.bookDate, b.bookHour
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()
            ) {
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
                       b.statusLabel AS bookStatusLabel,
                       t.nbSeats,
                       t.statusLabel AS tableStatusLabel
                FROM Book b
                INNER JOIN RestaurantTable t
                        ON b.idTable = t.idTable
                WHERE b.bookDate = ?
                ORDER BY b.bookHour
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, Date.valueOf(date));

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        bookings.add(createBookFromResultSet(resultSet));
                    }
                }
            }

            return bookings;

        } catch (SQLException | ConnectionException exception) {
            throw new BookingException(
                    "Erreur lors du chargement des réservations de la date sélectionnée.",
                    exception
            );
        }
    }

    @Override
    public void addBooking(Book booking) throws BookingException {
        String sql = """
                INSERT INTO Book (
                    bookDate,
                    bookHour,
                    idTable,
                    nameCustomer,
                    nbPerson,
                    comment,
                    telCustomer,
                    statusLabel
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                fillBookingValues(statement, booking, 1);
                statement.executeUpdate();
            }

        } catch (SQLIntegrityConstraintViolationException exception) {
            throw new BookingException(
                    "Impossible d’ajouter la réservation : cette table est peut-être déjà réservée à cette date et cette heure.",
                    exception
            );

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

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                fillBookingValues(statement, newBooking, 1);
                fillBookingPrimaryKey(statement, oldBooking, 9);

                int updatedRows = statement.executeUpdate();

                if (updatedRows == 0) {
                    throw new BookingException("Aucune réservation n'a été modifiée.");
                }
            }

        } catch (SQLIntegrityConstraintViolationException exception) {
            throw new BookingException(
                    "Impossible de modifier la réservation : cette table est peut-être déjà réservée à cette date et cette heure.",
                    exception
            );

        } catch (SQLException | ConnectionException exception) {
            throw new BookingException(
                    "Erreur lors de la modification de la réservation.",
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

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                fillBookingPrimaryKey(statement, booking, 1);

                int deletedRows = statement.executeUpdate();

                if (deletedRows == 0) {
                    throw new BookingException("Aucune réservation n'a été supprimée.");
                }
            }

        } catch (SQLException | ConnectionException exception) {
            throw new BookingException(
                    "Erreur lors de la suppression de la réservation.",
                    exception
            );
        }
    }

    @Override
    public boolean isTableAlreadyBooked(Book booking) throws BookingException {
        String sql = """
                SELECT COUNT(*) AS bookingCount
                FROM Book
                WHERE bookDate = ?
                  AND bookHour = ?
                  AND idTable = ?
                  AND statusLabel <> 'Cancelled'
                """;

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, Date.valueOf(booking.getBookDate()));
                statement.setTime(2, Time.valueOf(booking.getBookHour()));
                statement.setInt(3, booking.getTable().getIdTable());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("bookingCount") > 0;
                    }
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
                SELECT COUNT(*) AS bookingCount
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

        try {
            Connection connection = SingletonConnection.getInstance();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, Date.valueOf(newBooking.getBookDate()));
                statement.setTime(2, Time.valueOf(newBooking.getBookHour()));
                statement.setInt(3, newBooking.getTable().getIdTable());

                fillBookingPrimaryKey(statement, oldBooking, 4);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("bookingCount") > 0;
                    }
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

    private void fillBookingPrimaryKey(PreparedStatement statement, Book booking, int startIndex)
            throws SQLException {

        statement.setDate(startIndex, Date.valueOf(booking.getBookDate()));
        statement.setTime(startIndex + 1, Time.valueOf(booking.getBookHour()));
        statement.setInt(startIndex + 2, booking.getTable().getIdTable());
        statement.setString(startIndex + 3, booking.getNameCustomer());
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
        Status tableStatus = new Status(resultSet.getString("tableStatusLabel"));

        Table table = new Table(
                resultSet.getInt("idTable"),
                resultSet.getInt("nbSeats"),
                tableStatus
        );

        Status bookingStatus = new Status(resultSet.getString("bookStatusLabel"));

        return new Book(
                resultSet.getDate("bookDate").toLocalDate(),
                resultSet.getTime("bookHour").toLocalTime(),
                table,
                resultSet.getString("nameCustomer"),
                resultSet.getInt("nbPerson"),
                resultSet.getString("comment"),
                resultSet.getString("telCustomer"),
                bookingStatus
        );
    }
}