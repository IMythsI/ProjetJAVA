package modelPackage;

public class LineOrder {
    private Integer idLineOrder;
    private Integer quantity;
    private Product product;
    private Employee employee;
    private Order order;
    private Status status;

    public LineOrder(Integer idLineOrder, Integer quantity, Product product, Employee employee, Order order, Status status) {
        this.idLineOrder = idLineOrder;
        this.quantity = quantity;
        this.product = product;
        this.employee = employee;
        this.order = order;
        this.status = status;
    }
}
