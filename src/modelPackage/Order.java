package modelPackage;

import java.time.LocalDate;
import java.time.LocalTime;

public class Order {
    private Integer idOrder;
    private String comment;
    private Integer guestCount;
    private LocalDate orderDate;
    private Boolean isTakeAway;
    private LocalTime pickupTime;
    private String nameCustomer;
    private String telCustomer;
    private Table table;

    public Order(Integer idOrder, String comment, Integer guestCount,
                 LocalDate orderDate, Boolean isTakeAway,
                 LocalTime pickUpTime, String nameCustomer,
                 String telCustomer, Table table) {
        this.idOrder = idOrder;
        this.comment = comment;
        this.guestCount = guestCount;
        this.orderDate = orderDate;
        this.isTakeAway = isTakeAway;
        this.pickupTime = pickUpTime;
        this.nameCustomer = nameCustomer;
        this.telCustomer = telCustomer;
        this.table = table;
    }

    @Override
    public String toString() {
        return "Order{" +
                "idOrder=" + idOrder +
                ", comment='" + comment + '\'' +
                ", guestCount=" + guestCount +
                ", orderDate=" + orderDate +
                ", isTakeAway=" + isTakeAway +
                ", pickupTime=" + pickupTime +
                ", nameCustomer='" + nameCustomer + '\'' +
                ", telCustomer='" + telCustomer + '\'' +
                ", table=" + table +
                '}';
    }
}
