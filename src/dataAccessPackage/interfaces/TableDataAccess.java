package dataAccessPackage.interfaces;

import exceptionPackage.TableException;
import modelPackage.Table;

import java.util.ArrayList;

public interface TableDataAccess {

    ArrayList<Table> getAllTables() throws TableException;

    void updateTableStatus(Integer idTable, String statusLabel) throws TableException;
}