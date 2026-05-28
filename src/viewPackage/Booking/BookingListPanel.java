package viewPackage.Booking;

import controllerPackage.ApplicationController;
import exceptionPackage.BookingException;
import modelPackage.Book;
import viewPackage.MainJFrame;
import viewPackage.ui.AppTheme;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookingListPanel extends AppPage {
    private ApplicationController controller;

    private LocalDate selectedDate;
    private JLabel dateLabel;
    private JPanel bookingRowsPanel;
    private JPanel bookingCard;

    public BookingListPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();
        selectedDate = LocalDate.now();

        addCentered(createPageTitle("Réservations"), 0, new Insets(0, 0, 30, 0));
        addCentered(createDatePanel(), 1, new Insets(0, 0, 35, 0));
        addCentered(createBookingCard(), 2, new Insets(0, 0, 0, 0));

        loadBookings();
    }

    private JPanel createDatePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(900, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 10, 0, 10);

        JButton previousButton = ButtonFactory.createSecondaryButton("‹", this::previousDay);
        JButton nextButton = ButtonFactory.createSecondaryButton("›", this::nextDay);
        JButton newBookingButton = ButtonFactory.createPrimaryButton(
                "+ Nouvelle réservation",
                () -> mainWindow.showBookingFormPanel()
        );

        previousButton.setPreferredSize(new Dimension(45, 42));
        nextButton.setPreferredSize(new Dimension(45, 42));
        newBookingButton.setPreferredSize(new Dimension(210, 48));

        dateLabel = new JLabel(DateHelper.formatDate(selectedDate), SwingConstants.CENTER);
        dateLabel.setPreferredSize(new Dimension(260, 48));
        dateLabel.setFont(AppTheme.BUTTON_FONT);
        dateLabel.setOpaque(true);
        dateLabel.setBackground(Color.WHITE);
        dateLabel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        dateLabel.putClientProperty("FlatLaf.style", "arc:18");

        gbc.gridx = 0;
        panel.add(previousButton, gbc);

        gbc.gridx = 1;
        panel.add(dateLabel, gbc);

        gbc.gridx = 2;
        panel.add(nextButton, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, 60, 0, 10);
        panel.add(newBookingButton, gbc);

        return panel;
    }

    private JPanel createBookingCard() {
        bookingCard = TableFactory.createTableCard(850, 430);

        bookingCard.add(
                TableFactory.createHeaderRow(
                        "Heure",
                        "Client",
                        "Table",
                        "Personnes",
                        "Statut",
                        "Actions"
                ),
                BorderLayout.NORTH
        );

        bookingRowsPanel = new JPanel();
        bookingRowsPanel.setBackground(Color.WHITE);
        bookingRowsPanel.setLayout(new BoxLayout(bookingRowsPanel, BoxLayout.Y_AXIS));

        bookingCard.add(bookingRowsPanel, BorderLayout.CENTER);

        return bookingCard;
    }

    private void loadBookings() {
        bookingRowsPanel.removeAll();

        try {
            ArrayList<Book> bookings = controller.getBookingsByDate(selectedDate);

            if (bookings.isEmpty()) {
                LoadingHelper.showEmpty(
                        bookingRowsPanel,
                        "Aucune réservation pour cette date."
                );
            } else {
                for (Book booking : bookings) {
                    bookingRowsPanel.add(createBookingRow(booking));
                }
            }

            TableFactory.updateAdaptiveTableCardSize(
                    bookingCard,
                    bookings.size()
            );

        } catch (BookingException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        bookingRowsPanel.revalidate();
        bookingRowsPanel.repaint();
        refreshPage();
    }

    private JPanel createBookingRow(Book booking) {
        JButton editButton = ButtonFactory.createSmallIconButton(
                "✎",
                () -> mainWindow.showBookingEditPanel(booking)
        );

        JButton deleteButton = ButtonFactory.createSmallIconButton(
                "🗑",
                () -> confirmAndDeleteBooking(booking)
        );

        return TableFactory.createDataRow(
                TableFactory.createCellLabel(
                        DateHelper.formatTime(booking.getBookHour()),
                        AppTheme.TEXT_PRIMARY
                ),
                TableFactory.createCellLabel(
                        booking.getNameCustomer(),
                        AppTheme.TEXT_PRIMARY
                ),
                TableFactory.createCellLabel(
                        "Table " + booking.getTable().getIdTable(),
                        AppTheme.TEXT_PRIMARY
                ),
                TableFactory.createCellLabel(
                        String.valueOf(booking.getNbPerson()),
                        AppTheme.TEXT_PRIMARY
                ),
                TableFactory.createCellLabel(
                        StatusHelper.getFrenchStatus(
                                booking.getStatus().getStatusLabel()
                        ),
                        StatusHelper.getStatusColor(
                                booking.getStatus().getStatusLabel()
                        )
                ),
                TableFactory.createActionPanel(editButton, deleteButton)
        );
    }

    private void previousDay() {
        selectedDate = selectedDate.minusDays(1);
        refreshDate();
        loadBookings();
    }

    private void nextDay() {
        selectedDate = selectedDate.plusDays(1);
        refreshDate();
        loadBookings();
    }

    private void refreshDate() {
        dateLabel.setText(DateHelper.formatDate(selectedDate));
    }

    private void confirmAndDeleteBooking(Book booking) {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment supprimer la réservation de "
                        + booking.getNameCustomer()
                        + " pour la table "
                        + booking.getTable().getIdTable()
                        + " à "
                        + DateHelper.formatTime(booking.getBookHour())
                        + " ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controller.deleteBooking(booking);

            JOptionPane.showMessageDialog(
                    this,
                    "La réservation a bien été supprimée.",
                    "Suppression réussie",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadBookings();

        } catch (BookingException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur de suppression",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}