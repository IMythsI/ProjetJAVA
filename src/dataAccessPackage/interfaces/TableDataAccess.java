package dataAccessPackage.interfaces;

import exceptionPackage.TableException;
import modelPackage.Table;

import java.util.ArrayList;

public interface TableDataAccess {

    ArrayList<Table> getAllTables() throws TableException;

    void addTable(Table table) throws TableException;

    void updateTableStatus(Integer idTable, String statusLabel) throws TableException;

    void deleteTable(Integer idTable) throws TableException;

    boolean isTableUsed(Integer idTable) throws TableException;
}