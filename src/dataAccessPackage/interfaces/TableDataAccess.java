package dataAccessPackage.interfaces;

import exceptionPackage.*;
import modelPackage.*;

import java.util.ArrayList;

public interface TableDataAccess {
    ArrayList<Table> getAllTables() throws TableException;

    void updateTableStatus(Integer idTable, String statusLabel) throws TableException;
}
