package dataAccessPackage.interfaces;

import exceptionPackage.BookingException;
import modelPackage.Book;

import java.time.LocalDate;
import java.util.ArrayList;

public interface BookingDataAccess {

    ArrayList<Book> getAllBookings() throws BookingException;

    ArrayList<Book> getBookingsByDate(LocalDate date) throws BookingException;

    void addBooking(Book booking) throws BookingException;

    void updateBooking(Book oldBooking, Book newBooking) throws BookingException;

    void deleteBooking(Book booking) throws BookingException;

    boolean isTableAlreadyBooked(Book booking) throws BookingException;

    boolean isTableAlreadyBookedForAnotherBooking(Book oldBooking, Book newBooking) throws BookingException;
}