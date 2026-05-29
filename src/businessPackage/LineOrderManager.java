package businessPackage;

import dataAccessPackage.db.LineOrderDBAccess;
import dataAccessPackage.interfaces.LineOrderDataAccess;
import exceptionPackage.LineOrderException;
import modelPackage.LineOrder;

import java.util.ArrayList;

public class LineOrderManager {

    private final LineOrderDataAccess lineOrderDAO;

    public LineOrderManager() {
        lineOrderDAO = new LineOrderDBAccess();
    }

    public ArrayList<LineOrder> getLineOrdersByTable(Integer idTable)
            throws LineOrderException {

        validateId(idTable, "La table sélectionnée est invalide.");

        return lineOrderDAO.getLineOrdersByTable(idTable);
    }

    public ArrayList<LineOrder> getLineOrdersByOrder(Integer idOrder)
            throws LineOrderException {

        validateId(idOrder, "La commande sélectionnée est invalide.");

        return lineOrderDAO.getLineOrdersByOrder(idOrder);
    }

    private void validateId(Integer id, String message) throws LineOrderException {
        if (id == null || id <= 0) {
            throw new LineOrderException(message);
        }
    }
}