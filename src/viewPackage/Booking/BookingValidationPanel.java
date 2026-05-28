package viewPackage.Booking;

import controllerPackage.ApplicationController;
import exceptionPackage.BookingException;
import exceptionPackage.TableException;
import modelPackage.Book;
import modelPackage.Status;
import modelPackage.Table;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BookingValidationPanel extends AppPage {

    private final ApplicationController controller;

    private JPanel validationCard;
    private JComboBox<Table> tableComboBox;
    private JSpinner numberOfPeopleSpinner;
    private JLabel resultLabel;
    private JButton validateButton;

    public BookingValidationPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(
                createPageTitle("Validation de réservation"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle("Vérifiez si une table peut accueillir un nombre de personnes"),
                1,
                new Insets(0, 0, 30, 0)
        );

        addCentered(
                createValidationCardWrapper(),
                2,
                new Insets(0, 0, 10, 0)
        );

        loadTables();
    }

    private JPanel createValidationCardWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        validationCard = createValidationCard();
        wrapper.add(validationCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeValidationCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createValidationCard() {
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.FORM_CARD_MAX_WIDTH, 360);
        card.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(
                AppTheme.CARD_PADDING_TOP,
                AppTheme.CARD_PADDING_LEFT,
                AppTheme.CARD_PADDING_BOTTOM,
                AppTheme.CARD_PADDING_RIGHT
        ));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        tableComboBox = FormFactory.createGenericComboBox();
        configureTableComboBoxRenderer();

        numberOfPeopleSpinner = FormFactory.createNumberSpinner(1, 50, 2);

        FormFactory.addFormRow(formPanel, constraints, 0, "Table *", tableComboBox);
        FormFactory.addFormRow(formPanel, constraints, 1, "Personnes *", numberOfPeopleSpinner);

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createBottomPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(
                0,
                AppTheme.CARD_PADDING_LEFT,
                AppTheme.COMPONENT_GAP_LARGE,
                AppTheme.CARD_PADDING_RIGHT
        ));

        resultLabel = new JLabel("Sélectionnez une table et un nombre de personnes.");
        resultLabel.setFont(AppTheme.TEXT_FONT);
        resultLabel.setForeground(AppTheme.TEXT_SECONDARY);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        validateButton = ButtonFactory.createPrimaryButton(
                "Valider",
                this::validateCapacity
        );

        JPanel buttonPanel = new JPanel(new FlowLayout(
                FlowLayout.RIGHT,
                0,
                AppTheme.COMPONENT_GAP_MEDIUM
        ));

        buttonPanel.setOpaque(false);
        buttonPanel.add(validateButton);

        bottomPanel.add(resultLabel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        return bottomPanel;
    }

    private void configureTableComboBoxRenderer() {
        tableComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                                                          Object value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Table table) {
                    setText("Table " + table.getIdTable() + " (" + table.getNbSeats() + " places)");
                }

                return this;
            }
        });
    }

    private void resizeValidationCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.FORM_CARD_MAX_WIDTH;
        int minWidth = 560;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        validationCard.setPreferredSize(new Dimension(newWidth, 360));
        validationCard.setMinimumSize(new Dimension(minWidth, 320));
        validationCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        validationCard.revalidate();
        validationCard.repaint();
    }

    private void loadTables() {
        tableComboBox.setEnabled(false);
        validateButton.setEnabled(false);
        resultLabel.setText("Chargement des tables...");

        SwingWorker<ArrayList<Table>, Void> worker = new SwingWorker<>() {
            @Override
            protected ArrayList<Table> doInBackground() throws TableException {
                return controller.getAllTables();
            }

            @Override
            protected void done() {
                try {
                    ArrayList<Table> tables = get();
                    fillTableComboBox(tables);

                    tableComboBox.setEnabled(true);
                    validateButton.setEnabled(true);
                    resultLabel.setText("Sélectionnez une table et un nombre de personnes.");
                    resultLabel.setForeground(AppTheme.TEXT_SECONDARY);

                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    displayTableLoadingError(exception);

                } catch (ExecutionException exception) {
                    displayTableLoadingError(exception);
                }
            }
        };

        worker.execute();
    }

    private void fillTableComboBox(ArrayList<Table> tables) {
        tableComboBox.removeAllItems();

        for (Table table : tables) {
            tableComboBox.addItem(table);
        }
    }

    private void displayTableLoadingError(Exception exception) {
        resultLabel.setText("Impossible de charger les tables.");
        resultLabel.setForeground(AppTheme.DANGER);

        JOptionPane.showMessageDialog(
                this,
                getUsefulErrorMessage(exception),
                "Erreur de chargement",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void validateCapacity() {
        try {
            Book booking = createTemporaryBooking();

            controller.validateBookingCapacity(booking);

            Table table = booking.getTable();

            resultLabel.setText(
                    "Réservation possible : la table "
                            + table.getIdTable()
                            + " possède assez de places."
            );
            resultLabel.setForeground(AppTheme.SUCCESS);

        } catch (BookingException exception) {
            resultLabel.setText(exception.getMessage());
            resultLabel.setForeground(AppTheme.DANGER);

        } catch (IllegalArgumentException exception) {
            resultLabel.setText(exception.getMessage());
            resultLabel.setForeground(AppTheme.WARNING);
        }
    }

    private Book createTemporaryBooking() {
        Table selectedTable = (Table) tableComboBox.getSelectedItem();

        if (selectedTable == null) {
            throw new IllegalArgumentException("La table est obligatoire.");
        }

        Integer numberOfPeople = (Integer) numberOfPeopleSpinner.getValue();

        if (numberOfPeople == null || numberOfPeople <= 0) {
            throw new IllegalArgumentException("Le nombre de personnes doit être supérieur à 0.");
        }

        return new Book(
                LocalDate.now(),
                LocalTime.now(),
                selectedTable,
                "Client temporaire",
                numberOfPeople,
                null,
                null,
                new Status("Reserved")
        );
    }

    private String getUsefulErrorMessage(Exception exception) {
        Throwable cause = exception.getCause();

        if (cause != null && cause.getMessage() != null) {
            return cause.getMessage();
        }

        if (exception.getMessage() != null) {
            return exception.getMessage();
        }

        return "Une erreur inconnue est survenue.";
    }
}