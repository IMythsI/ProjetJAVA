package modelPackage;

import java.time.LocalDate;
import java.time.LocalTime;

public class Book {

    private LocalDate bookDate;
    private LocalTime bookHour;
    private Table table;
    private String nameCustomer;
    private Integer nbPerson;
    private String comment;
    private String telCustomer;
    private Status status;

    public Book(LocalDate bookDate,
                LocalTime bookHour,
                Table table,
                String nameCustomer,
                Integer nbPerson,
                String comment,
                String telCustomer,
                Status status) {
        this.bookDate = bookDate;
        this.bookHour = bookHour;
        this.table = table;
        this.nameCustomer = nameCustomer;
        this.nbPerson = nbPerson;
        this.comment = comment;
        this.telCustomer = telCustomer;
        this.status = status;
    }

    public LocalDate getBookDate() {
        return bookDate;
    }

    public LocalTime getBookHour() {
        return bookHour;
    }

    public Table getTable() {
        return table;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public Integer getNbPerson() {
        return nbPerson;
    }

    public String getComment() {
        return comment;
    }

    public String getTelCustomer() {
        return telCustomer;
    }

    public Status getStatus() {
        return status;
    }
}