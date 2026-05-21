package modelPackage;

import java.math.BigDecimal;

public class Product {
    private String productLabel;
    private BigDecimal price;
    private String description;
    private Type type;

    public Product(String productLabel, BigDecimal price, String description, Type type) {
        this.productLabel = productLabel;
        this.price = price;
        this.description = description;
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public Type getProductType() {
        return type;
    }
}
