package businessPackage;

import dataAccessPackage.db.TableDBAccess;
import dataAccessPackage.interfaces.TableDataAccess;
import exceptionPackage.TableException;
import modelPackage.Table;

import java.util.ArrayList;

public class TableManager {

    private final TableDataAccess tableDAO;

    public TableManager() {
        tableDAO = new TableDBAccess();
    }

    public ArrayList<Table> getAllTables() throws TableException {
        return tableDAO.getAllTables();
    }

    public void updateTableStatus(Integer idTable, String statusLabel) throws TableException {
        validateTableId(idTable);
        validateTableStatus(statusLabel);

        tableDAO.updateTableStatus(idTable, statusLabel);
    }

    private void validateTableId(Integer idTable) throws TableException {
        if (idTable == null || idTable <= 0) {
            throw new TableException("La table sélectionnée est invalide.");
        }
    }

    private void validateTableStatus(String statusLabel) throws TableException {
        if (statusLabel == null || statusLabel.isBlank()) {
            throw new TableException("Le statut de la table est obligatoire.");
        }

        if (!isAllowedTableStatus(statusLabel)) {
            throw new TableException("Le statut de la table n'est pas valide.");
        }
    }

    private boolean isAllowedTableStatus(String statusLabel) {
        return switch (statusLabel) {
            case "Available", "Reserved", "Occupied" -> true;
            default -> false;
        };
    }
}