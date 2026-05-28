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

    private final ApplicationController controller;

    private JPanel searchCard;
    private JPanel resultCard;
    private JPanel resultContentPanel;

    private JComboBox<String> customerComboBox;
    private JComboBox<Integer> dayComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> yearComboBox;

    public BookingSearchPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(
                createPageTitle("Recherche de réservations"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle("Rechercher les réservations d’un client à une date donnée"),
                1,
                new Insets(0, 0, 30, 0)
        );

        addCentered(
                createSearchCardWrapper(),
                2,
                new Insets(0, 0, 25, 0)
        );

        addCentered(
                createResultCardWrapper(),
                3,
                new Insets(0, 0, 0, 0)
        );

        loadCustomers();
    }

    private JPanel createSearchCardWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        searchCard = createSearchCard();
        wrapper.add(searchCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeSearchCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createSearchCard() {
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.SEARCH_CARD_MAX_WIDTH, 250);
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

        customerComboBox = FormFactory.createComboBox();

        dayComboBox = FormFactory.createGenericComboBox();
        monthComboBox = FormFactory.createGenericComboBox();
        yearComboBox = FormFactory.createGenericComboBox();

        fillDateComboBoxes();

        FormFactory.addFormRow(formPanel, constraints, 0, "Client *", customerComboBox);
        FormFactory.addFormRow(formPanel, constraints, 1, "Date *", createDatePanel());

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createDatePanel() {
        return FormFactory.createThreeColumnPanel(
                dayComboBox,
                monthComboBox,
                yearComboBox
        );
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(
                FlowLayout.RIGHT,
                AppTheme.COMPONENT_GAP_MEDIUM,
                AppTheme.COMPONENT_GAP_MEDIUM
        ));

        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 20));

        JButton searchButton = ButtonFactory.createPrimaryButton(
                "Rechercher",
                this::searchBookings
        );

        buttonPanel.add(searchButton);

        return buttonPanel;
    }

    private JPanel createResultCardWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        resultCard = createResultCard();
        wrapper.add(resultCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeResultCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createResultCard() {
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.TABLE_CARD_MAX_WIDTH, 380);
        card.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Résultats");
        titleLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        resultContentPanel = new JPanel(new BorderLayout());
        resultContentPanel.setOpaque(false);

        LoadingHelper.showEmpty(
                resultContentPanel,
                "Lancez une recherche pour afficher les résultats."
        );

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(resultContentPanel, BorderLayout.CENTER);

        return card;
    }

    private void resizeSearchCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.SEARCH_CARD_MAX_WIDTH;
        int minWidth = 620;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        searchCard.setPreferredSize(new Dimension(newWidth, 250));
        searchCard.setMinimumSize(new Dimension(minWidth, 230));
        searchCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        searchCard.revalidate();
        searchCard.repaint();
    }

    private void resizeResultCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.TABLE_CARD_MAX_WIDTH;
        int minWidth = 760;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        resultCard.setPreferredSize(new Dimension(newWidth, 380));
        resultCard.setMinimumSize(new Dimension(minWidth, 340));
        resultCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        resultCard.revalidate();
        resultCard.repaint();
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
        customerComboBox.setEnabled(false);
        customerComboBox.removeAllItems();
        customerComboBox.addItem("Chargement...");

        LoadingHelper.runWithLoading(
                resultContentPanel,
                "Chargement des clients...",
                controller::getBookingCustomerNames,
                this::displayCustomers,
                this::displayCustomerLoadingError
        );
    }

    private void displayCustomers(ArrayList<String> customers) {
        customerComboBox.removeAllItems();

        for (String customer : customers) {
            customerComboBox.addItem(customer);
        }

        customerComboBox.setEnabled(true);

        LoadingHelper.showEmpty(
                resultContentPanel,
                "Lancez une recherche pour afficher les résultats."
        );
    }

    private void displayCustomerLoadingError(Exception exception) {
        customerComboBox.removeAllItems();
        customerComboBox.setEnabled(false);

        LoadingHelper.showError(
                resultContentPanel,
                "Impossible de charger les clients."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de chargement",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void searchBookings() {
        try {
            String customerName = (String) customerComboBox.getSelectedItem();
            LocalDate selectedDate = getSelectedDate();

            if (customerName == null || customerName.isBlank() || customerName.equals("Chargement...")) {
                throw new IllegalArgumentException("Le client est obligatoire.");
            }

            LoadingHelper.runWithLoading(
                    resultContentPanel,
                    "Recherche des réservations...",
                    () -> controller.searchBookingsByCustomerAndDate(customerName, selectedDate),
                    this::displayResults,
                    this::displaySearchError
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

    private void displayResults(ArrayList<BookingSearchResult> results) {
        resultContentPanel.removeAll();

        if (results == null || results.isEmpty()) {
            LoadingHelper.showEmpty(
                    resultContentPanel,
                    "Aucune réservation trouvée pour ces critères."
            );
            return;
        }

        JTable table = createResultTable(results);
        JScrollPane scrollPane = new JScrollPane(table);

        resultContentPanel.add(scrollPane, BorderLayout.CENTER);
        resultContentPanel.revalidate();
        resultContentPanel.repaint();
    }

    private JTable createResultTable(ArrayList<BookingSearchResult> results) {
        String[] columns = {
                "Date",
                "Heure",
                "Client",
                "Table",
                "Places",
                "Personnes",
                "Statut"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (BookingSearchResult result : results) {
            tableModel.addRow(new Object[]{
                    DateHelper.formatShortDate(result.getBookDate()),
                    DateHelper.formatTime(result.getBookHour()),
                    result.getCustomerName(),
                    "Table " + result.getTableId(),
                    result.getNbSeats(),
                    result.getNbPerson(),
                    StatusHelper.getFrenchStatus(result.getStatusLabel())
            });
        }

        JTable table = new JTable(tableModel);

        table.setRowHeight(AppTheme.JTABLE_ROW_HEIGHT);
        table.setFont(AppTheme.TEXT_FONT);
        table.getTableHeader().setFont(AppTheme.TEXT_BOLD_FONT);
        table.getTableHeader().setPreferredSize(new Dimension(0, AppTheme.TABLE_HEADER_HEIGHT));

        return table;
    }

    private void displaySearchError(Exception exception) {
        LoadingHelper.showError(
                resultContentPanel,
                "Impossible d’effectuer la recherche."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de recherche",
                JOptionPane.ERROR_MESSAGE
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
}