package businessPackage;

import dataAccessPackage.db.TableDBAccess;
import dataAccessPackage.interfaces.TableDataAccess;
import exceptionPackage.TableException;
import modelPackage.Table;

import java.util.ArrayList;

public class TableManager {

    private TableDataAccess tableDAO;

    public TableManager() {
        tableDAO = new TableDBAccess();
    }

    public ArrayList<Table> getAllTables() throws TableException {
        return tableDAO.getAllTables();
    }

    public void updateTableStatus(Integer idTable, String statusLabel) throws TableException {
        tableDAO.updateTableStatus(idTable, statusLabel);
    }
}
