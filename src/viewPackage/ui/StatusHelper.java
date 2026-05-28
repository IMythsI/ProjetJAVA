package viewPackage.ui;

import modelPackage.LineOrder;

import java.awt.*;
import java.util.ArrayList;

public final class StatusHelper {

    private StatusHelper() {
        // Utility class
    }

    /*
     * ============================================================
     * STATUS COLORS
     * ============================================================
     */

    public static Color getStatusColor(String status) {
        if (status == null || status.isBlank()) {
            return AppTheme.TEXT_SECONDARY;
        }

        return switch (status) {

            // Restaurant tables
            case "Available" -> AppTheme.SUCCESS;
            case "Reserved" -> AppTheme.WARNING;
            case "Occupied" -> AppTheme.DANGER;

            // Orders / line orders
            case "Pending" -> AppTheme.INFO;
            case "InPreparation" -> AppTheme.WARNING;
            case "Ready" -> AppTheme.SUCCESS;
            case "Served" -> AppTheme.TEXT_SECONDARY;

            // General
            case "Cancelled" -> AppTheme.DANGER;

            default -> AppTheme.TEXT_SECONDARY;
        };
    }

    /*
     * ============================================================
     * STATUS TRANSLATIONS
     * ============================================================
     */

    public static String getFrenchStatus(String status) {
        if (status == null || status.isBlank()) {
            return "Inconnu";
        }

        return switch (status) {

            // Restaurant tables
            case "Available" -> "Libre";
            case "Reserved" -> "Réservée";
            case "Occupied" -> "Occupée";

            // Orders / line orders
            case "Pending" -> "En attente";
            case "InPreparation" -> "En préparation";
            case "Ready" -> "Prête";
            case "Served" -> "Servie";

            // General
            case "Cancelled" -> "Annulée";

            default -> status;
        };
    }

    public static String getDisplayStatus(String status) {
        return getFrenchStatus(status);
    }

    /*
     * ============================================================
     * TEXT COLOR
     * ============================================================
     */

    public static Color getTextColorForStatus(String status) {
        Color backgroundColor = getStatusColor(status);

        int brightness = calculateBrightness(backgroundColor);

        if (brightness < 140) {
            return Color.WHITE;
        }

        return AppTheme.TEXT_PRIMARY;
    }

    private static int calculateBrightness(Color color) {
        return (color.getRed() * 299
                + color.getGreen() * 587
                + color.getBlue() * 114) / 1000;
    }

    /*
     * ============================================================
     * ORDER HELPERS
     * ============================================================
     */

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

    public static boolean isCancelled(String status) {
        return "Cancelled".equals(status);
    }

    public static boolean isAvailable(String status) {
        return "Available".equals(status);
    }

    public static boolean isReserved(String status) {
        return "Reserved".equals(status);
    }

    public static boolean isOccupied(String status) {
        return "Occupied".equals(status);
    }

    public static boolean isServed(String status) {
        return "Served".equals(status);
    }
}