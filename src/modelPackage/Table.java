package modelPackage;

public class Table {

    private Integer idTable;
    private Integer nbSeats;
    private Status status;

    public Table(Integer idTable, Integer nbSeats, Status status) {
        this.idTable = idTable;
        this.nbSeats = nbSeats;
        this.status = status;
    }

    public Integer getIdTable() {
        return idTable;
    }

    public Integer getNbSeats() {
        return nbSeats;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Table " + idTable + " (" + nbSeats + " places)";
    }
}