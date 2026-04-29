package modelPackage;

public class Employee {
    private Integer registrationNb;
    private String lastName;
    private String firstName;
    private JobType jobType;

    public Employee(Integer registrationNb, String lastName, String firstName, JobType jobType) {
        this.registrationNb = registrationNb;
        this.lastName = lastName;
        this.firstName = firstName;
        this.jobType = jobType;
    }
}
