package businessPackage;

import dataAccessPackage.db.BookingDBAccess;
import dataAccessPackage.interfaces.BookingDataAccess;
import exceptionPackage.BookingException;
import modelPackage.Book;

import java.time.LocalDate;
import java.util.ArrayList;

public class BookingManager {

    private final BookingDataAccess bookingDAO;

    public BookingManager() {
        bookingDAO = new BookingDBAccess();
    }

    public ArrayList<Book> getAllBookings() throws BookingException {
        return bookingDAO.getAllBookings();
    }

    public ArrayList<Book> getBookingsByDate(LocalDate date) throws BookingException {
        if (date == null) {
            throw new BookingException("La date est obligatoire.");
        }

        return bookingDAO.getBookingsByDate(date);
    }

    public void addBooking(Book booking) throws BookingException {
        validateBooking(booking);

        if (bookingDAO.isTableAlreadyBooked(booking)) {
            throw new BookingException(
                    "Cette table est déjà réservée à cette date et cette heure."
            );
        }

        bookingDAO.addBooking(booking);
    }

    public void updateBooking(Book oldBooking, Book newBooking) throws BookingException {
        if (oldBooking == null) {
            throw new BookingException("La réservation à modifier est invalide.");
        }

        validateBooking(newBooking);

        if (bookingDAO.isTableAlreadyBookedForAnotherBooking(oldBooking, newBooking)) {
            throw new BookingException(
                    "Cette table est déjà réservée à cette date et cette heure."
            );
        }

        bookingDAO.updateBooking(oldBooking, newBooking);
    }

    public void deleteBooking(Book booking) throws BookingException {
        if (booking == null) {
            throw new BookingException("La réservation à supprimer est invalide.");
        }

        bookingDAO.deleteBooking(booking);
    }

    private void validateBooking(Book booking) throws BookingException {
        if (booking == null) {
            throw new BookingException("La réservation est invalide.");
        }

        if (booking.getBookDate() == null) {
            throw new BookingException("La date de réservation est obligatoire.");
        }

        if (booking.getBookHour() == null) {
            throw new BookingException("L'heure de réservation est obligatoire.");
        }

        if (booking.getTable() == null) {
            throw new BookingException("La table est obligatoire.");
        }

        if (booking.getNameCustomer() == null || booking.getNameCustomer().isBlank()) {
            throw new BookingException("Le nom du client est obligatoire.");
        }

        if (booking.getNbPerson() == null || booking.getNbPerson() <= 0) {
            throw new BookingException("Le nombre de personnes doit être supérieur à 0.");
        }

        if (booking.getStatus() == null) {
            throw new BookingException("Le statut est obligatoire.");
        }

        if (
                booking.getStatus().getStatusLabel() == null
                        || booking.getStatus().getStatusLabel().isBlank()
        ) {
            throw new BookingException("Le statut est obligatoire.");
        }

        validateBookingCapacity(booking);
    }

    public void validateBookingCapacity(Book booking) throws BookingException {
        if (booking == null) {
            throw new BookingException("La réservation est invalide.");
        }

        if (booking.getTable() == null) {
            throw new BookingException("La table est obligatoire.");
        }

        if (booking.getNbPerson() == null || booking.getNbPerson() <= 0) {
            throw new BookingException("Le nombre de personnes doit être supérieur à 0.");
        }

        if (booking.getTable().getNbSeats() == null || booking.getTable().getNbSeats() <= 0) {
            throw new BookingException("La capacité de la table est invalide.");
        }

        if (booking.getNbPerson() > booking.getTable().getNbSeats()) {
            throw new BookingException(
                    "Le nombre de personnes dépasse la capacité de la table."
            );
        }
    }
}