package modelPackage;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingSearchResult {

    private LocalDate bookDate;
    private LocalTime bookHour;
    private String customerName;
    private int tableId;
    private int nbSeats;
    private int nbPerson;
    private String statusLabel;

    public BookingSearchResult(LocalDate bookDate,
                               LocalTime bookHour,
                               String customerName,
                               int tableId,
                               int nbSeats,
                               int nbPerson,
                               String statusLabel) {
        this.bookDate = bookDate;
        this.bookHour = bookHour;
        this.customerName = customerName;
        this.tableId = tableId;
        this.nbSeats = nbSeats;
        this.nbPerson = nbPerson;
        this.statusLabel = statusLabel;
    }

    public LocalDate getBookDate() {
        return bookDate;
    }

    public LocalTime getBookHour() {
        return bookHour;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getTableId() {
        return tableId;
    }

    public int getNbSeats() {
        return nbSeats;
    }

    public int getNbPerson() {
        return nbPerson;
    }

    public String getStatusLabel() {
        return statusLabel;
    }
}