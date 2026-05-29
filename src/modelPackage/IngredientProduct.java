package modelPackage;

public class IngredientProduct {

    private Integer quantity;
    private Product product;
    private Ingredient ingredient;

    public IngredientProduct(Integer quantity, Product product, Ingredient ingredient) {
        this.quantity = quantity;
        this.product = product;
        this.ingredient = ingredient;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }
}