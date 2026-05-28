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

public class BookingValidationPanel extends AppPage {

    private ApplicationController controller;

    private JComboBox<Table> tableComboBox;
    private JSpinner numberOfPeopleSpinner;
    private JLabel resultLabel;

    public BookingValidationPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(createPageTitle("Validate booking capacity"), 0, new Insets(0, 0, 25, 0));
        addCentered(createValidationCard(), 1, new Insets(0, 0, 20, 0));

        loadTables();
    }

    private JPanel createValidationCard() {
        JPanel card = CardFactory.createCard(700, 360);
        card.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 35, 20, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tableComboBox = FormFactory.createGenericComboBox();
        numberOfPeopleSpinner = FormFactory.createNumberSpinner(1, 50, 2);

        FormFactory.addFormRow(formPanel, gbc, 0, "Table *", tableComboBox);
        FormFactory.addFormRow(formPanel, gbc, 1, "People *", numberOfPeopleSpinner);

        resultLabel = new JLabel("Select a table and a number of people.");
        resultLabel.setFont(AppTheme.SUBTITLE_FONT);
        resultLabel.setForeground(AppTheme.TEXT_SECONDARY);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createBottomPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 35, 25, 35));

        JButton validateButton = ButtonFactory.createPrimaryButton(
                "Validate",
                this::validateCapacity
        );

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(validateButton);

        bottomPanel.add(resultLabel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        return bottomPanel;
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

    private void validateCapacity() {
        try {
            Table selectedTable = (Table) tableComboBox.getSelectedItem();
            Integer numberOfPeople = (Integer) numberOfPeopleSpinner.getValue();

            Book temporaryBooking = new Book(
                    LocalDate.now(),
                    LocalTime.now(),
                    selectedTable,
                    "Temporary customer",
                    numberOfPeople,
                    null,
                    null,
                    new Status("Reserved")
            );

            controller.validateBookingCapacity(temporaryBooking);

            resultLabel.setText(
                    "Accepted: this table has enough seats for "
                            + numberOfPeople
                            + " people."
            );
            resultLabel.setForeground(AppTheme.SUCCESS);

            JOptionPane.showMessageDialog(
                    this,
                    "The booking capacity is valid.",
                    "Validation success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (BookingException exception) {
            resultLabel.setText(exception.getMessage());
            resultLabel.setForeground(AppTheme.DANGER);

            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Validation error",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}