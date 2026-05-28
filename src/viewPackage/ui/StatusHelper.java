package viewPackage.ui;

import modelPackage.LineOrder;

import java.awt.*;
import java.util.ArrayList;

public class StatusHelper {

    private StatusHelper() {
    }

    public static Color getStatusColor(String status) {
        if (status == null) {
            return AppTheme.TEXT_SECONDARY;
        }

        return switch (status) {

            // Tables
            case "Available" -> AppTheme.SUCCESS;
            case "Reserved" -> AppTheme.WARNING;
            case "Occupied" -> AppTheme.DANGER;

            // Bookings
            case "Booked" -> AppTheme.WARNING;
            case "Confirmed" -> AppTheme.SUCCESS;
            case "Cancelled" -> AppTheme.DANGER;

            // Orders / line orders
            case "Ready" -> AppTheme.SUCCESS;
            case "Served" -> AppTheme.TEXT_SECONDARY;
            case "InPreparation" -> AppTheme.WARNING;
            case "Pending" -> new Color(90, 140, 220);

            default -> AppTheme.TEXT_SECONDARY;
        };
    }

    public static String getFrenchStatus(String status) {
        if (status == null || status.isBlank()) {
            return "Inconnu";
        }

        return switch (status) {

            // Tables
            case "Available" -> "Libre";
            case "Reserved" -> "Réservée";
            case "Occupied" -> "Occupée";

            // Bookings
            case "Booked" -> "Réservée";
            case "Confirmed" -> "Confirmée";
            case "Cancelled" -> "Annulée";

            // Orders / line orders
            case "Ready" -> "Prête";
            case "Served" -> "Servie";
            case "InPreparation" -> "En préparation";
            case "Pending" -> "En attente";

            default -> status;
        };
    }


    public static String getDisplayStatus(String status) {
        return getFrenchStatus(status);
    }

    public static Color getTextColorForStatus(String status) {
        Color background = getStatusColor(status);

        int brightness = (background.getRed() * 299
                + background.getGreen() * 587
                + background.getBlue() * 114) / 1000;

        if (brightness < 140) {
            return Color.WHITE;
        }

        return AppTheme.TEXT_PRIMARY;
    }

    public static boolean isOrderFinished(ArrayList<LineOrder> lines) {
        if (lines == null || lines.isEmpty()) {
            return false;
        }

        for (LineOrder line : lines) {
            if (line == null || line.getStatus() == null) {
                return false;
            }

            String status = line.getStatus().getStatusLabel();

            if (!"Served".equals(status)) {
                return false;
            }
        }

        return true;
    }
}