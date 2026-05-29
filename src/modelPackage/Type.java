package modelPackage;

public class Type {

    private String typeLabel;

    public Type(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    @Override
    public String toString() {
        return typeLabel;
    }
}