package modelPackage;

public class Status {
    private String statuslabel;

    public Status(String statuslabel) {
        this.statuslabel = statuslabel;
    }

    @Override
    public String toString() {
        return "Status{" +
                "statuslabel='" + statuslabel + '\'' +
                '}';
    }
}
