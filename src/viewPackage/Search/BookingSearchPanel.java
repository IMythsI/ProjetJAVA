package viewPackage.Search;

import controllerPackage.ApplicationController;
import exceptionPackage.SearchException;
import modelPackage.BookingSearchResult;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookingSearchPanel extends AppPage {

    private ApplicationController controller;

    private JComboBox<String> customerComboBox;
    private JComboBox<Integer> dayComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> yearComboBox;

    private DefaultTableModel tableModel;
    private JTable resultTable;

    public BookingSearchPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(createPageTitle("Search bookings"), 0, new Insets(0, 0, 25, 0));
        addCentered(createSearchCard(), 1, new Insets(0, 0, 20, 0));
        addCentered(createResultCard(), 2, new Insets(0, 0, 20, 0));

        fillDateComboBoxes();
        loadCustomers();
    }

    private JPanel createSearchCard() {
        JPanel card = CardFactory.createCard(760, 230);
        card.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 15, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        customerComboBox = FormFactory.createComboBox();

        dayComboBox = FormFactory.createGenericComboBox();
        monthComboBox = FormFactory.createGenericComboBox();
        yearComboBox = FormFactory.createGenericComboBox();

        FormFactory.addFormRow(formPanel, gbc, 0, "Customer *", customerComboBox);
        FormFactory.addFormRow(formPanel, gbc, 1, "Date *", createDatePanel());

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setOpaque(false);

        JButton searchButton = ButtonFactory.createPrimaryButton(
                "Search",
                this::searchBookings
        );

        buttonPanel.add(searchButton);

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

    private JPanel createResultCard() {
        JPanel card = CardFactory.createCard(900, 360);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {
                "Date",
                "Hour",
                "Customer",
                "Table",
                "Seats",
                "People",
                "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultTable = new JTable(tableModel);
        resultTable.setRowHeight(34);
        resultTable.setFont(AppTheme.SUBTITLE_FONT);
        resultTable.getTableHeader().setFont(AppTheme.BUTTON_FONT);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private void fillDateComboBoxes() {
        LocalDate today = LocalDate.now();

        for (int day = 1; day <= 31; day++) {
            dayComboBox.addItem(day);
        }

        for (int month = 1; month <= 12; month++) {
            monthComboBox.addItem(month);
        }

        for (int year = today.getYear() - 1; year <= today.getYear() + 2; year++) {
            yearComboBox.addItem(year);
        }

        dayComboBox.setSelectedItem(today.getDayOfMonth());
        monthComboBox.setSelectedItem(today.getMonthValue());
        yearComboBox.setSelectedItem(today.getYear());
    }

    private void loadCustomers() {
        try {
            ArrayList<String> customers = controller.getBookingCustomerNames();

            customerComboBox.removeAllItems();

            for (String customer : customers) {
                customerComboBox.addItem(customer);
            }

        } catch (SearchException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Loading error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void searchBookings() {
        try {
            String customerName = (String) customerComboBox.getSelectedItem();
            LocalDate date = getSelectedDate();

            ArrayList<BookingSearchResult> results =
                    controller.searchBookingsByCustomerAndDate(customerName, date);

            refreshTable(results);

        } catch (SearchException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Search error",
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

    private void refreshTable(ArrayList<BookingSearchResult> results) {
        tableModel.setRowCount(0);

        for (BookingSearchResult result : results) {
            tableModel.addRow(new Object[]{
                    result.getBookDate(),
                    result.getBookHour(),
                    result.getCustomerName(),
                    "Table " + result.getTableId(),
                    result.getNbSeats(),
                    result.getNbPerson(),
                    StatusHelper.getFrenchStatus(result.getStatusLabel())
            });
        }

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No booking found for these criteria.",
                    "No result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
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
}