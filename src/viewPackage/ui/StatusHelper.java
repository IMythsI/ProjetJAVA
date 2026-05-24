package viewPackage.ui;

import modelPackage.LineOrder;
import viewPackage.ui.AppTheme;

import java.awt.*;
import java.util.ArrayList;

public class StatusHelper {

    public static Color getStatusColor(String status) {
        return switch (status) {
            case "Available" -> new Color(70, 191, 70);
            case "Reserved" -> new Color(255, 167, 49);
            case "Occupied" -> new Color(230, 78, 78);

            case "Ready" -> AppTheme.SUCCESS;
            case "Served" -> AppTheme.TEXT_SECONDARY;
            case "InPreparation", "Pending" -> AppTheme.WARNING;
            case "Cancelled" -> AppTheme.DANGER;

            default -> AppTheme.TEXT_SECONDARY;
        };
    }

    public static String getFrenchStatus(String status) {
        return switch (status) {
            case "Available" -> "Libre";
            case "Reserved" -> "Réservée";
            case "Occupied" -> "Occupée";

            case "Ready" -> "Prête";
            case "Served" -> "Servie";
            case "InPreparation" -> "En préparation";
            case "Pending" -> "En attente";
            case "Cancelled" -> "Annulée";

            default -> status;
        };
    }

    public static boolean isOrderFinished(ArrayList<LineOrder> lines) {
        if (lines == null || lines.isEmpty()) {
            return false;
        }

        for (LineOrder line : lines) {
            if (!line.getStatus().getStatusLabel().equals("Served")) {
                return false;
            }
        }

        return true;
    }
}