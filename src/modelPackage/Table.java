package modelPackage;

public class Table {
    private Integer idTabel;
    private Integer nbSeats;
    private Status status;

    public Table(Integer idTabel, Integer nbSeats, Status status) {
        this.idTabel = idTabel;
        this.nbSeats = nbSeats;
        this.status = status;
    }
}
