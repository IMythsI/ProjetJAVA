package modelPackage;

import java.util.ArrayList;
import java.util.List;

public class Ingredient {

    private String ingredientLabel;
    private List<Allergy> allergies;

    public Ingredient(String ingredientLabel) {
        this.ingredientLabel = ingredientLabel;
        this.allergies = new ArrayList<>();
    }

    public Ingredient(String ingredientLabel, List<Allergy> allergies) {
        this.ingredientLabel = ingredientLabel;
        this.allergies = allergies == null ? new ArrayList<>() : allergies;
    }

    public String getIngredientLabel() {
        return ingredientLabel;
    }

    public List<Allergy> getAllergies() {
        return allergies;
    }

    @Override
    public String toString() {
        return ingredientLabel;
    }
}