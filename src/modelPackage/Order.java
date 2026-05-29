package modelPackage;

import java.time.LocalDate;
import java.time.LocalTime;

public class Order {

    private Integer idOrder;
    private String comment;
    private Integer guestCount;
    private LocalDate orderDate;
    private Boolean takeAway;
    private LocalTime pickUpTime;
    private String nameCustomer;
    private String telCustomer;
    private Table table;

    public Order(Integer idOrder,
                 String comment,
                 Integer guestCount,
                 LocalDate orderDate,
                 Boolean takeAway,
                 LocalTime pickUpTime,
                 String nameCustomer,
                 String telCustomer,
                 Table table) {
        this.idOrder = idOrder;
        this.comment = comment;
        this.guestCount = guestCount;
        this.orderDate = orderDate;
        this.takeAway = takeAway;
        this.pickUpTime = pickUpTime;
        this.nameCustomer = nameCustomer;
        this.telCustomer = telCustomer;
        this.table = table;
    }

    public Integer getIdOrder() {
        return idOrder;
    }

    public String getComment() {
        return comment;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Boolean getTakeAway() {
        return takeAway;
    }

    public boolean getIsTakeAway() {
        return Boolean.TRUE.equals(takeAway);
    }

    public LocalTime getPickUpTime() {
        return pickUpTime;
    }

    public LocalTime getPickupTime() {
        return pickUpTime;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public String getTelCustomer() {
        return telCustomer;
    }

    public Table getTable() {
        return table;
    }

    @Override
    public String toString() {
        return "Commande " + idOrder;
    }
}