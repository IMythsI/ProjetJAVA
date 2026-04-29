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

    public Book(LocalDate bookDate,LocalTime bookHour,Table table,String nameCustomer,Integer nbPerson,String comment,String telCustomer) {
        this.bookDate = bookDate;
        this.bookHour = bookHour;
        this.table = table;
        this.nameCustomer = nameCustomer;
        this.nbPerson = nbPerson;
        this.comment = comment;
        this.telCustomer = telCustomer;
    }
}
