package modelPackage;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingSearchResult {

    private LocalDate bookDate;
    private LocalTime bookHour;
    private String customerName;
    private Integer tableId;
    private Integer nbSeats;
    private Integer nbPerson;
    private String statusLabel;

    public BookingSearchResult(LocalDate bookDate,
                               LocalTime bookHour,
                               String customerName,
                               Integer tableId,
                               Integer nbSeats,
                               Integer nbPerson,
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

    public Integer getTableId() {
        return tableId;
    }

    public Integer getNbSeats() {
        return nbSeats;
    }

    public Integer getNbPerson() {
        return nbPerson;
    }

    public String getStatusLabel() {
        return statusLabel;
    }
}