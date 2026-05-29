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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookingManagerTest {

    private BookingManager bookingManager;

    @BeforeEach
    public void setUp() {
        bookingManager = new BookingManager();
    }

    @Test
    public void validateBookingCapacity_shouldAcceptBookingWhenCapacityIsEnough() {
        Book booking = createBooking(4, 4);

        assertDoesNotThrow(() -> bookingManager.validateBookingCapacity(booking));
    }

    @Test
    public void validateBookingCapacity_shouldAcceptBookingWhenPeopleAreLessThanCapacity() {
        Book booking = createBooking(2, 4);

        assertDoesNotThrow(() -> bookingManager.validateBookingCapacity(booking));
    }

    @Test
    public void validateBookingCapacity_shouldRejectBookingWhenTooManyPeople() {
        Book booking = createBooking(5, 4);

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
    public void validateBookingCapacity_shouldRejectNullBooking() {
        BookingException exception = assertThrows(
                BookingException.class,
                () -> bookingManager.validateBookingCapacity(null)
        );

        assertEquals(
                "La réservation est invalide.",
                exception.getMessage()
        );
    }

    @Test
    public void validateBookingCapacity_shouldRejectBookingWithoutTable() {
        Book booking = new Book(
                LocalDate.now(),
                LocalTime.of(19, 0),
                null,
                "Client test",
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
    public void validateBookingCapacity_shouldRejectBookingWithZeroPeople() {
        Book booking = createBooking(0, 4);

        BookingException exception = assertThrows(
                BookingException.class,
                () -> bookingManager.validateBookingCapacity(booking)
        );

        assertEquals(
                "Le nombre de personnes doit être supérieur à 0.",
                exception.getMessage()
        );
    }

    @Test
    public void validateBookingCapacity_shouldRejectBookingWithNegativePeople() {
        Book booking = createBooking(-1, 4);

        BookingException exception = assertThrows(
                BookingException.class,
                () -> bookingManager.validateBookingCapacity(booking)
        );

        assertEquals(
                "Le nombre de personnes doit être supérieur à 0.",
                exception.getMessage()
        );
    }

    @Test
    public void validateBookingCapacity_shouldRejectTableWithInvalidCapacity() {
        Book booking = createBooking(2, 0);

        BookingException exception = assertThrows(
                BookingException.class,
                () -> bookingManager.validateBookingCapacity(booking)
        );

        assertEquals(
                "La capacité de la table est invalide.",
                exception.getMessage()
        );
    }

    private Book createBooking(Integer numberOfPeople, Integer tableCapacity) {
        Table table = new Table(
                1,
                tableCapacity,
                new Status("Available")
        );

        return new Book(
                LocalDate.now(),
                LocalTime.of(19, 0),
                table,
                "Client test",
                numberOfPeople,
                null,
                null,
                new Status("Reserved")
        );
    }
}