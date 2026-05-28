package viewPackage.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public final class LoadingHelper {

    private LoadingHelper() {
        // Utility class
    }

    /*
     * ============================================================
     * FUNCTIONAL INTERFACES
     * ============================================================
     */

    @FunctionalInterface
    public interface BackgroundTask<T> {
        T execute() throws Exception;
    }

    /*
     * ============================================================
     * BACKGROUND EXECUTION
     * ============================================================
     */

    public static <T> void runWithLoading(JPanel targetPanel,
                                          String loadingMessage,
                                          BackgroundTask<T> backgroundTask,
                                          Consumer<T> onSuccess,
                                          Consumer<Exception> onError) {
        showLoading(targetPanel, loadingMessage);

        SwingWorker<T, Void> worker = new SwingWorker<>() {

            @Override
            protected T doInBackground() throws Exception {
                return backgroundTask.execute();
            }

            @Override
            protected void done() {
                try {
                    T result = get();

                    if (onSuccess != null) {
                        onSuccess.accept(result);
                    }

                } catch (Exception exception) {
                    Exception realException = extractRealException(exception);

                    if (onError != null) {
                        onError.accept(realException);
                    } else {
                        showError(targetPanel, "Une erreur est survenue lors du chargement.");
                    }
                }
            }
        };

        worker.execute();
    }

    private static Exception extractRealException(Exception exception) {
        Throwable cause = exception.getCause();

        if (cause instanceof Exception causeException) {
            return causeException;
        }

        return exception;
    }

    /*
     * ============================================================
     * STATES
     * ============================================================
     */

    public static void showLoading(JPanel panel, String message) {
        panel.removeAll();

        JPanel loadingPanel = createStatePanel(
                message == null ? "Chargement en cours..." : message,
                AppTheme.TEXT_SECONDARY,
                true
        );

        panel.add(loadingPanel);

        refresh(panel);
    }

    public static void showEmpty(JPanel panel, String message) {
        panel.removeAll();

        JPanel emptyPanel = createStatePanel(
                message == null ? "Aucune donnée à afficher." : message,
                AppTheme.TEXT_SECONDARY,
                false
        );

        panel.add(emptyPanel);

        refresh(panel);
    }

    public static void showError(JPanel panel, String message) {
        panel.removeAll();

        JPanel errorPanel = createStatePanel(
                message == null ? "Une erreur est survenue." : message,
                AppTheme.DANGER,
                false
        );

        panel.add(errorPanel);

        refresh(panel);
    }

    public static <T> void showListOrEmpty(JPanel panel,
                                           List<T> data,
                                           String emptyMessage,
                                           Consumer<T> rowBuilder) {
        panel.removeAll();

        if (data == null || data.isEmpty()) {
            showEmpty(panel, emptyMessage);
            return;
        }

        for (T item : data) {
            rowBuilder.accept(item);
        }

        refresh(panel);
    }

    /*
     * ============================================================
     * UI HELPERS
     * ============================================================
     */

    private static JPanel createStatePanel(String message, Color color, boolean withSpinner) {
        JPanel panel = new JPanel(new GridBagLayout());

        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(
                AppTheme.TABLE_CARD_MAX_WIDTH,
                AppTheme.TABLE_MIN_HEIGHT
        ));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(8, 8, 8, 8);

        if (withSpinner) {
            JProgressBar progressBar = new JProgressBar();

            progressBar.setIndeterminate(true);
            progressBar.setPreferredSize(new Dimension(220, 18));

            panel.add(progressBar, constraints);

            constraints.gridy = 1;
        }

        JLabel label = new JLabel(message, SwingConstants.CENTER);

        label.setFont(AppTheme.SUBTITLE_FONT);
        label.setForeground(color);

        panel.add(label, constraints);

        return panel;
    }

    private static void refresh(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }
}