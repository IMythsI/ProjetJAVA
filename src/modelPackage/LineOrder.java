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

    public Product getProduct() {
        return product;
    }

    public int  getQuantity() {
        return quantity;
    }

    public int getIdLineOrder() {
        return idLineOrder;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Status getStatus() {
        return status;
    }
}
