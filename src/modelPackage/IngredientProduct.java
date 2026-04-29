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
}
