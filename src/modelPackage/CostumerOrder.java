package modelPackage;

import java.time.LocalDate;
import java.time.LocalTime;

public class CostumerOrder {
    private Integer idOrder;
    private String comment;
    private Integer guestCount;
    private LocalDate orderDate;
    private Boolean isTakeAway;
    private LocalTime pickupTime;
    private String nameCustomer;
    private String telCustomer;
    private Integer idTable;

    public CostumerOrder(Integer idOrder, String comment, Integer guestCount,
                         LocalDate orderDate, Boolean isTakeAway,
                         LocalTime pickUpTime, String nameCustomer,
                         String telCustomer, Integer idTable) {
        this.idOrder = idOrder;
        this.comment = comment;
        this.guestCount = guestCount;
        this.orderDate = orderDate;
        this.isTakeAway = isTakeAway;
        this.pickupTime = pickUpTime;
        this.nameCustomer = nameCustomer;
        this.telCustomer = telCustomer;
        this.idTable = idTable;
    }

}
