package modelPackage;

public class Allergy {

    private String allergyLabel;

    public Allergy(String allergyLabel) {
        this.allergyLabel = allergyLabel;
    }

    public String getAllergyLabel() {
        return allergyLabel;
    }

    @Override
    public String toString() {
        return allergyLabel;
    }
}