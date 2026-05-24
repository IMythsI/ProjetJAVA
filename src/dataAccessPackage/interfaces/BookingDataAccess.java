package dataAccessPackage.interfaces;

import exceptionPackage.BookingException;
import modelPackage.Book;

import java.time.LocalDate;
import java.util.ArrayList;

public interface BookingDataAccess {
    ArrayList<Book> getBookingsByDate(LocalDate date) throws BookingException;
}