package modelPackage;

public class Status {

    private String statusLabel;

    public Status(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    @Override
    public String toString() {
        return statusLabel;
    }
}