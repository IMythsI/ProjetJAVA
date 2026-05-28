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
import java.util.Date;

public class BookingFormPanel extends AppPage {

    private ApplicationController controller;

    JComboBox<Integer> dayComboBox;
    JComboBox<Integer> monthComboBox;
    JComboBox<Integer> yearComboBox;
    private JComboBox<String> hourComboBox;
    private JComboBox<Table> tableComboBox;
    private JTextField customerNameField;
    private JSpinner numberOfPeopleSpinner;
    private JTextField phoneField;
    private JTextArea commentArea;
    private JComboBox<String> statusComboBox;

    public BookingFormPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(createPageTitle("New booking"), 0, new Insets(0, 0, 25, 0));
        addCentered(createFormCard(), 1, new Insets(0, 0, 20, 0));

        loadTables();
    }

    private JPanel createFormCard() {
        JPanel card = CardFactory.createCard(700, 680);
        card.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        dayComboBox = FormFactory.createGenericComboBox();
        monthComboBox = FormFactory.createGenericComboBox();
        yearComboBox = FormFactory.createGenericComboBox();
        fillDateComboBoxes();
        hourComboBox = createHourComboBox();
        tableComboBox = FormFactory.createGenericComboBox();
        customerNameField = FormFactory.createTextField();
        numberOfPeopleSpinner = FormFactory.createNumberSpinner(1, 20, 2);
        phoneField = FormFactory.createTextField();
        commentArea = FormFactory.createTextArea();
        statusComboBox = FormFactory.createComboBox();

        statusComboBox.addItem("Booked");
        statusComboBox.addItem("Confirmed");
        statusComboBox.addItem("Cancelled");

        JScrollPane commentScrollPane = new JScrollPane(commentArea);
        commentScrollPane.setPreferredSize(new Dimension(FormFactory.FIELD_WIDTH, 130));
        commentScrollPane.setMinimumSize(new Dimension(FormFactory.FIELD_WIDTH, 130));
        commentScrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));

        FormFactory.addFormRow(formPanel, gbc, 0, "Date *", createDatePanel());
        FormFactory.addFormRow(formPanel, gbc, 1, "Hour *", hourComboBox);
        FormFactory.addFormRow(formPanel, gbc, 2, "Table *", tableComboBox);
        FormFactory.addFormRow(formPanel, gbc, 3, "Customer name *", customerNameField);
        FormFactory.addFormRow(formPanel, gbc, 4, "People *", numberOfPeopleSpinner);
        FormFactory.addFormRow(formPanel, gbc, 5, "Phone", phoneField);
        FormFactory.addFormRow(formPanel, gbc, 6, "Comment", commentScrollPane);
        FormFactory.addFormRow(formPanel, gbc, 7, "Status *", statusComboBox);

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        buttonPanel.setOpaque(false);

        JButton cancelButton = ButtonFactory.createSecondaryButton(
                "Cancel",
                () -> mainWindow.goBack()
        );

        JButton saveButton = ButtonFactory.createPrimaryButton(
                "Save booking",
                this::saveBooking
        );

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        return buttonPanel;
    }

    private JPanel createDatePanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 0));
        panel.setOpaque(false);

        panel.add(dayComboBox);
        panel.add(monthComboBox);
        panel.add(yearComboBox);

        return panel;
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
                    "Table loading error",
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
                    "Booking successfully added.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mainWindow.showBookingListPanel();

        } catch (BookingException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Booking error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Validation error",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void validateForm() {
        if (customerNameField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required.");
        }

        if (tableComboBox.getSelectedItem() == null) {
            throw new IllegalArgumentException("A table must be selected.");
        }

        int numberOfPeople = (Integer) numberOfPeopleSpinner.getValue();

        if (numberOfPeople <= 0) {
            throw new IllegalArgumentException("Number of people must be greater than 0.");
        }

        Table selectedTable = (Table) tableComboBox.getSelectedItem();

        if (selectedTable != null && numberOfPeople > selectedTable.getNbSeats()) {
            throw new IllegalArgumentException(
                    "Number of people cannot exceed the table capacity."
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

        Status status = new Status((String) statusComboBox.getSelectedItem());

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
            throw new IllegalArgumentException("The selected date is not valid.");
        }
    }

    private String getNullableText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        return text.trim();
    }
}