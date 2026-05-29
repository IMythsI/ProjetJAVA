package modelPackage;

import java.math.BigDecimal;

public class Product {

    private String productLabel;
    private BigDecimal price;
    private String description;
    private Type productType;

    public Product(String productLabel, BigDecimal price, String description, Type productType) {
        this.productLabel = productLabel;
        this.price = price;
        this.description = description;
        this.productType = productType;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Type getProductType() {
        return productType;
    }

    @Override
    public String toString() {
        return productLabel;
    }
}