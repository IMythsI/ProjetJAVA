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

public class BookingFormPanel extends AppPage {

    private final ApplicationController controller;

    private JPanel formCard;

    private JComboBox<Integer> dayComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> hourComboBox;
    private JComboBox<Table> tableComboBox;
    private JTextField customerNameField;
    private JSpinner numberOfPeopleSpinner;
    private JTextField phoneField;
    private JTextArea commentArea;
    private JComboBox<StatusOption> statusComboBox;

    public BookingFormPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(
                createPageTitle("Nouvelle réservation"),
                0,
                new Insets(0, 0, 25, 0)
        );

        addCentered(
                createFormCardWrapper(),
                1,
                new Insets(0, 0, 20, 0)
        );

        loadTables();
    }

    private JPanel createFormCardWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        formCard = createFormCard();
        wrapper.add(formCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeFormCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createFormCard() {
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.FORM_CARD_MAX_WIDTH, 1000);
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

        createFormFields();

        FormFactory.addFormRow(formPanel, constraints, 0, "Date *", createDatePanel());
        FormFactory.addFormRow(formPanel, constraints, 1, "Heure *", hourComboBox);
        FormFactory.addFormRow(formPanel, constraints, 2, "Table *", tableComboBox);
        FormFactory.addFormRow(formPanel, constraints, 3, "Nom du client *", customerNameField);
        FormFactory.addFormRow(formPanel, constraints, 4, "Personnes *", numberOfPeopleSpinner);
        FormFactory.addFormRow(formPanel, constraints, 5, "Téléphone", phoneField);
        FormFactory.addFormRow(formPanel, constraints, 6, "Commentaire", FormFactory.createTextAreaScrollPane(commentArea));
        FormFactory.addFormRow(formPanel, constraints, 7, "Statut *", statusComboBox);

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private void createFormFields() {
        dayComboBox = FormFactory.createGenericComboBox();
        monthComboBox = FormFactory.createGenericComboBox();
        yearComboBox = FormFactory.createGenericComboBox();

        fillDateComboBoxes();

        hourComboBox = createHourComboBox();

        tableComboBox = FormFactory.createGenericComboBox();
        configureTableComboBoxRenderer();

        customerNameField = FormFactory.createTextField();
        numberOfPeopleSpinner = FormFactory.createNumberSpinner(1, 50, 2);
        phoneField = FormFactory.createTextField();
        commentArea = FormFactory.createTextArea();

        statusComboBox = FormFactory.createGenericComboBox();
        fillStatusComboBox();
    }

    private JPanel createDatePanel() {
        return FormFactory.createThreeColumnPanel(
                dayComboBox,
                monthComboBox,
                yearComboBox
        );
    }

    private void fillDateComboBoxes() {
        LocalDate today = LocalDate.now();

        for (int day = 1; day <= 31; day++) {
            dayComboBox.addItem(day);
        }

        for (int month = 1; month <= 12; month++) {
            monthComboBox.addItem(month);
        }

        for (int year = today.getYear(); year <= today.getYear() + 2; year++) {
            yearComboBox.addItem(year);
        }

        dayComboBox.setSelectedItem(today.getDayOfMonth());
        monthComboBox.setSelectedItem(today.getMonthValue());
        yearComboBox.setSelectedItem(today.getYear());
    }

    private JComboBox<String> createHourComboBox() {
        JComboBox<String> comboBox = FormFactory.createComboBox();

        for (int hour = 11; hour <= 22; hour++) {
            comboBox.addItem(String.format("%02d:00", hour));
            comboBox.addItem(String.format("%02d:15", hour));
            comboBox.addItem(String.format("%02d:30", hour));
            comboBox.addItem(String.format("%02d:45", hour));
        }

        comboBox.setSelectedItem("19:00");

        return comboBox;
    }

    private void fillStatusComboBox() {
        statusComboBox.addItem(new StatusOption("Reserved", "Réservée"));
        statusComboBox.addItem(new StatusOption("Pending", "En attente"));
        statusComboBox.addItem(new StatusOption("Cancelled", "Annulée"));
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

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(
                FlowLayout.RIGHT,
                AppTheme.COMPONENT_GAP_MEDIUM,
                AppTheme.COMPONENT_GAP_MEDIUM
        ));

        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 20));

        JButton cancelButton = ButtonFactory.createSecondaryButton(
                "Annuler",
                () -> mainWindow.goBack()
        );

        JButton saveButton = ButtonFactory.createPrimaryButton(
                "Enregistrer",
                this::saveBooking
        );

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        return buttonPanel;
    }

    private void resizeFormCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.FORM_CARD_MAX_WIDTH;
        int minWidth = 620;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        formCard.setPreferredSize(new Dimension(newWidth, 690));
        formCard.setMinimumSize(new Dimension(minWidth, 620));
        formCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        formCard.revalidate();
        formCard.repaint();
    }

    private void loadTables() {
        try {
            ArrayList<Table> tables = controller.getAllTables();

            tableComboBox.removeAllItems();

            for (Table table : tables) {
                tableComboBox.addItem(table);
            }

        } catch (TableException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur de chargement",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void saveBooking() {
        try {
            validateForm();

            Book booking = createBookingFromForm();

            controller.addBooking(booking);

            JOptionPane.showMessageDialog(
                    this,
                    "La réservation a bien été ajoutée.",
                    "Ajout réussi",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mainWindow.showBookingListPanel();

        } catch (BookingException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur réservation",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur de validation",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void validateForm() {
        getSelectedDate();

        if (hourComboBox.getSelectedItem() == null) {
            throw new IllegalArgumentException("L'heure est obligatoire.");
        }

        if (tableComboBox.getSelectedItem() == null) {
            throw new IllegalArgumentException("La table est obligatoire.");
        }

        String customerName = customerNameField.getText().trim();

        if (customerName.isEmpty()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire.");
        }

        Integer numberOfPeople = (Integer) numberOfPeopleSpinner.getValue();

        if (numberOfPeople == null || numberOfPeople <= 0) {
            throw new IllegalArgumentException("Le nombre de personnes doit être supérieur à 0.");
        }

        Table selectedTable = (Table) tableComboBox.getSelectedItem();

        if (selectedTable != null && numberOfPeople > selectedTable.getNbSeats()) {
            throw new IllegalArgumentException(
                    "Le nombre de personnes dépasse la capacité de la table."
            );
        }

        validatePhoneIfPresent();

        if (statusComboBox.getSelectedItem() == null) {
            throw new IllegalArgumentException("Le statut est obligatoire.");
        }
    }

    private void validatePhoneIfPresent() {
        String phone = phoneField.getText().trim();

        if (phone.isEmpty()) {
            return;
        }

        if (!phone.matches("[0-9+ ]{6,20}")) {
            throw new IllegalArgumentException(
                    "Le numéro de téléphone doit contenir uniquement des chiffres, des espaces ou le signe +."
            );
        }
    }

    private Book createBookingFromForm() {
        LocalDate bookDate = getSelectedDate();
        LocalTime bookHour = LocalTime.parse((String) hourComboBox.getSelectedItem());

        Table selectedTable = (Table) tableComboBox.getSelectedItem();

        String customerName = customerNameField.getText().trim();
        Integer numberOfPeople = (Integer) numberOfPeopleSpinner.getValue();

        String phone = getNullableText(phoneField.getText());
        String comment = getNullableText(commentArea.getText());

        StatusOption selectedStatus = (StatusOption) statusComboBox.getSelectedItem();

        Status status = new Status(selectedStatus.getDatabaseValue());

        return new Book(
                bookDate,
                bookHour,
                selectedTable,
                customerName,
                numberOfPeople,
                comment,
                phone,
                status
        );
    }

    private LocalDate getSelectedDate() {
        int day = (Integer) dayComboBox.getSelectedItem();
        int month = (Integer) monthComboBox.getSelectedItem();
        int year = (Integer) yearComboBox.getSelectedItem();

        try {
            return LocalDate.of(year, month, day);
        } catch (Exception exception) {
            throw new IllegalArgumentException("La date sélectionnée n'est pas valide.");
        }
    }

    private String getNullableText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        return text.trim();
    }

    private static class StatusOption {

        private final String databaseValue;
        private final String displayValue;

        public StatusOption(String databaseValue, String displayValue) {
            this.databaseValue = databaseValue;
            this.displayValue = displayValue;
        }

        public String getDatabaseValue() {
            return databaseValue;
        }

        @Override
        public String toString() {
            return displayValue;
        }
    }
}