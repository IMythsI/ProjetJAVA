package modelPackage;

import java.time.LocalDate;
import java.time.LocalTime;

public class OrderSearchResult {

    private int orderId;
    private LocalDate orderDate;
    private Integer guestCount;
    private boolean takeAway;
    private LocalTime pickUpTime;
    private Integer tableId;
    private String waiterName;
    private String statusLabel;
    private double totalAmount;

    public OrderSearchResult(int orderId,
                             LocalDate orderDate,
                             Integer guestCount,
                             boolean takeAway,
                             LocalTime pickUpTime,
                             Integer tableId,
                             String waiterName,
                             String statusLabel,
                             double totalAmount) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.guestCount = guestCount;
        this.takeAway = takeAway;
        this.pickUpTime = pickUpTime;
        this.tableId = tableId;
        this.waiterName = waiterName;
        this.statusLabel = statusLabel;
        this.totalAmount = totalAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public boolean isTakeAway() {
        return takeAway;
    }

    public LocalTime getPickUpTime() {
        return pickUpTime;
    }

    public Integer getTableId() {
        return tableId;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}