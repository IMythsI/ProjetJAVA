package businessPackage;

import dataAccessPackage.db.BookingDBAccess;
import dataAccessPackage.interfaces.BookingDataAccess;
import exceptionPackage.BookingException;
import modelPackage.Book;

import java.time.LocalDate;
import java.util.ArrayList;

public class BookingManager {
    private BookingDataAccess bookingDAO;

    public BookingManager() {
        bookingDAO = new BookingDBAccess();
    }

    public ArrayList<Book> getBookingsByDate(LocalDate date) throws BookingException {
        return bookingDAO.getBookingsByDate(date);
    }
}