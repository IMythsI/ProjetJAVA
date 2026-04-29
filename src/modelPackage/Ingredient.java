package modelPackage;

import java.util.ArrayList;
import java.util.List;

public class Ingredient {
    private String ingredientLabel;
    private List<Allergy> allergyList;

    public Ingredient(String ingredientLabel) {
        this.ingredientLabel = ingredientLabel;
        this.allergyList = new ArrayList<Allergy>();
    }
}
