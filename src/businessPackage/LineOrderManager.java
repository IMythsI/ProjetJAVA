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

    public void updateLineOrderStatus(Integer idLineOrder, String statusLabel) throws LineOrderException {
        if (idLineOrder == null || idLineOrder <= 0) {
            throw new LineOrderException("La ligne de commande sélectionnée est invalide.");
        }

        if (statusLabel == null || statusLabel.isBlank()) {
            throw new LineOrderException("Le statut est obligatoire.");
        }

        if (!isAllowedLineOrderStatus(statusLabel)) {
            throw new LineOrderException("Le statut sélectionné n'est pas valide.");
        }

        lineOrderDAO.updateLineOrderStatus(idLineOrder, statusLabel);
    }

    private boolean isAllowedLineOrderStatus(String statusLabel) {
        return switch (statusLabel) {
            case "Pending", "InPreparation", "Ready", "Served" -> true;
            default -> false;
        };
    }
}