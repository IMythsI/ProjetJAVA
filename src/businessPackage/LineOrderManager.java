package businessPackage;

import dataAccessPackage.db.LineOrderDBAccess;
import dataAccessPackage.interfaces.LineOrderDataAccess;
import exceptionPackage.LineOrderException;
import modelPackage.LineOrder;

import java.util.ArrayList;

public class LineOrderManager {
    private LineOrderDataAccess lineOrderDAO;

    public LineOrderManager() {
        lineOrderDAO = new LineOrderDBAccess();
    }

    public ArrayList<LineOrder> getLineOrdersByTable(Integer idTable) throws LineOrderException {
        return lineOrderDAO.getLineOrdersByTable(idTable);
    }
}