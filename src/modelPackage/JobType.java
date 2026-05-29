package modelPackage;

public class JobType {

    private String jobLabel;

    public JobType(String jobLabel) {
        this.jobLabel = jobLabel;
    }

    public String getJobLabel() {
        return jobLabel;
    }

    @Override
    public String toString() {
        return jobLabel;
    }
}