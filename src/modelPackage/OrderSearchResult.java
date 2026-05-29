package modelPackage;

import java.time.LocalDate;
import java.time.LocalTime;

public class OrderSearchResult {

    private Integer orderId;
    private LocalDate orderDate;
    private Integer guestCount;
    private Boolean takeAway;
    private LocalTime pickUpTime;
    private Integer tableId;
    private String waiterName;
    private String statusLabel;
    private Double totalAmount;

    public OrderSearchResult(Integer orderId,
                             LocalDate orderDate,
                             Integer guestCount,
                             Boolean takeAway,
                             LocalTime pickUpTime,
                             Integer tableId,
                             String waiterName,
                             String statusLabel,
                             Double totalAmount) {
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

    public Integer getOrderId() {
        return orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public Boolean getTakeAway() {
        return takeAway;
    }

    public boolean isTakeAway() {
        return Boolean.TRUE.equals(takeAway);
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

    public Double getTotalAmount() {
        return totalAmount;
    }
}