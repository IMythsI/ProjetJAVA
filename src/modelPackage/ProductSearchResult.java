package modelPackage;

public class ProductSearchResult {

    private String productLabel;
    private double price;
    private String description;
    private String typeLabel;
    private String ingredientLabel;
    private String allergyLabel;

    public ProductSearchResult(String productLabel,
                               double price,
                               String description,
                               String typeLabel,
                               String ingredientLabel,
                               String allergyLabel) {
        this.productLabel = productLabel;
        this.price = price;
        this.description = description;
        this.typeLabel = typeLabel;
        this.ingredientLabel = ingredientLabel;
        this.allergyLabel = allergyLabel;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public String getIngredientLabel() {
        return ingredientLabel;
    }

    public String getAllergyLabel() {
        return allergyLabel;
    }
}