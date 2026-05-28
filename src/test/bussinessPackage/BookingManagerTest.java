package test.bussinessPackage;

import businessPackage.BookingManager;
import exceptionPackage.BookingException;
import modelPackage.Book;
import modelPackage.Status;
import modelPackage.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingManagerTest {

    private BookingManager bookingManager;

    @BeforeEach
    public void setUp() {
        bookingManager = new BookingManager();
    }

    @Test
    public void validateBookingCapacity_shouldAcceptBookingWhenCapacityIsEnough()
            throws BookingException {

        Table table = new Table(
                1,
                4,
                new Status("Available")
        );

        Book booking = new Book(
                LocalDate.now(),
                LocalTime.of(19, 0),
                table,
                "Test Customer",
                4,
                null,
                null,
                new Status("Reserved")
        );

        assertDoesNotThrow(() -> bookingManager.validateBookingCapacity(booking));
    }

    @Test
    public void validateBookingCapacity_shouldRejectBookingWhenTooManyPeople() {
        Table table = new Table(
                1,
                2,
                new Status("Available")
        );

        Book booking = new Book(
                LocalDate.now(),
                LocalTime.of(19, 0),
                table,
                "Test Customer",
                3,
                null,
                null,
                new Status("Reserved")
        );

        BookingException exception = assertThrows(
                BookingException.class,
                () -> bookingManager.validateBookingCapacity(booking)
        );

        assertEquals(
                "Le nombre de personnes dépasse la capacité de la table.",
                exception.getMessage()
        );
    }

    @Test
    public void validateBookingCapacity_shouldRejectBookingWithoutTable() {
        Book booking = new Book(
                LocalDate.now(),
                LocalTime.of(19, 0),
                null,
                "Test Customer",
                2,
                null,
                null,
                new Status("Reserved")
        );

        BookingException exception = assertThrows(
                BookingException.class,
                () -> bookingManager.validateBookingCapacity(booking)
        );

        assertEquals(
                "La table est obligatoire.",
                exception.getMessage()
        );
    }

    @Test
    public void validateBookingCapacity_shouldRejectBookingWithInvalidPeopleNumber() {
        Table table = new Table(
                1,
                4,
                new Status("Available")
        );

        Book booking = new Book(
                LocalDate.now(),
                LocalTime.of(19, 0),
                table,
                "Test Customer",
                0,
                null,
                null,
                new Status("Reserved")
        );

        BookingException exception = assertThrows(
                BookingException.class,
                () -> bookingManager.validateBookingCapacity(booking)
        );

        assertEquals(
                "Le nombre de personnes doit être supérieur à 0.",
                exception.getMessage()
        );
    }
}