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

    @Override
    public String toString() {
        return "Table{" +
                "idTabel=" + idTabel +
                ", nbSeats=" + nbSeats +
                ", status=" + status +
                '}';
    }

    public Integer getIdTabel() {
        return idTabel;
    }

    public Integer getNbSeats() {
        return nbSeats;
    }

    public Status getStatus() {
        return status;
    }
}
