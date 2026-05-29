package viewPackage.Booking;

import controllerPackage.ApplicationController;
import exceptionPackage.BookingException;
import modelPackage.Book;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookingListPanel extends AppPage {

    private final ApplicationController controller;

    private LocalDate selectedDate;
    private JLabel dateLabel;
    private JPanel bookingRowsPanel;
    private JPanel bookingCard;
    private JPanel wrapper;

    public BookingListPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();
        selectedDate = LocalDate.now();

        addCentered(
                createPageTitle("Réservations"),
                0,
                new Insets(0, 0, 25, 0)
        );

        addCentered(
                createDatePanel(),
                1,
                new Insets(0, 0, 30, 0)
        );

        addCentered(
                createBookingCardWrapper(),
                2,
                new Insets(0, 0, 0, 0)
        );

        loadBookings();
    }

    private JPanel createDatePanel() {
        JPanel wrapper = new JPanel(new FlowLayout(
                FlowLayout.CENTER,
                AppTheme.COMPONENT_GAP_MEDIUM,
                0
        ));

        wrapper.setOpaque(false);

        JButton previousButton = ButtonFactory.createSecondaryButton(
                "‹",
                this::previousDay
        );

        JButton nextButton = ButtonFactory.createSecondaryButton(
                "›",
                this::nextDay
        );

        JButton newBookingButton = ButtonFactory.createPrimaryButton(
                "+ Nouvelle réservation",
                () -> mainWindow.showBookingFormPanel()
        );

        previousButton.setPreferredSize(new Dimension(52, AppTheme.BUTTON_HEIGHT));
        nextButton.setPreferredSize(new Dimension(52, AppTheme.BUTTON_HEIGHT));
        newBookingButton.setPreferredSize(new Dimension(230, AppTheme.BUTTON_HEIGHT));

        dateLabel = createDateLabel();

        wrapper.add(previousButton);
        wrapper.add(dateLabel);
        wrapper.add(nextButton);
        wrapper.add(Box.createHorizontalStrut(AppTheme.COMPONENT_GAP_LARGE));
        wrapper.add(newBookingButton);

        return wrapper;
    }

    private JLabel createDateLabel() {
        JLabel label = new JLabel(DateHelper.formatDate(selectedDate), SwingConstants.CENTER);

        label.setPreferredSize(new Dimension(280, AppTheme.BUTTON_HEIGHT));
        label.setMinimumSize(new Dimension(220, AppTheme.BUTTON_HEIGHT));
        label.setFont(AppTheme.TEXT_BOLD_FONT);
        label.setForeground(AppTheme.TEXT_PRIMARY);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));

        return label;
    }

    private JPanel createBookingCardWrapper() {
        wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        bookingCard = createBookingCard();

        wrapper.add(bookingCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeBookingCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createBookingCard() {
        JPanel card = TableFactory.createTableCard(
                AppTheme.TABLE_CARD_MAX_WIDTH,
                AppTheme.TABLE_MIN_HEIGHT
        );

        card.add(
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
        bookingRowsPanel.setOpaque(false);
        bookingRowsPanel.setLayout(new BoxLayout(bookingRowsPanel, BoxLayout.Y_AXIS));

        card.add(bookingRowsPanel, BorderLayout.CENTER);

        return card;
    }

    private void resizeBookingCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.TABLE_CARD_MAX_WIDTH;
        int minWidth = 760;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        int currentHeight = bookingCard.getPreferredSize().height;

        bookingCard.setPreferredSize(new Dimension(newWidth, currentHeight));
        bookingCard.setMinimumSize(new Dimension(minWidth, AppTheme.TABLE_MIN_HEIGHT));
        bookingCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        bookingCard.revalidate();
        bookingCard.repaint();
    }

    private void loadBookings() {
        LoadingHelper.runWithLoading(
                bookingRowsPanel,
                "Chargement des réservations...",
                () -> controller.getBookingsByDate(selectedDate),
                this::displayBookings,
                this::displayLoadingError
        );
    }

    private void displayBookings(ArrayList<Book> bookings) {
        bookingRowsPanel.removeAll();

        if (bookings == null || bookings.isEmpty()) {
            LoadingHelper.showEmpty(
                    bookingRowsPanel,
                    "Aucune réservation pour cette date."
            );

            TableFactory.updateAdaptiveTableCardSize(bookingCard, 1);
            refreshPage();
            resizeBookingCard(wrapper);
            return;
        }

        for (Book booking : bookings) {
            bookingRowsPanel.add(createBookingRow(booking));
        }

        TableFactory.updateAdaptiveTableCardSize(
                bookingCard,
                bookings.size()
        );

        bookingRowsPanel.revalidate();
        bookingRowsPanel.repaint();

        refreshPage();
        resizeBookingCard(wrapper);
    }

    private void displayLoadingError(Exception exception) {
        LoadingHelper.showError(
                bookingRowsPanel,
                "Impossible de charger les réservations."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de chargement",
                JOptionPane.ERROR_MESSAGE
        );
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
                        StatusHelper.getFrenchStatus(booking.getStatus().getStatusLabel()),
                        StatusHelper.getStatusColor(booking.getStatus().getStatusLabel())
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

        deleteBooking(booking);
    }

    private void deleteBooking(Book booking) {
        LoadingHelper.runWithLoading(
                bookingRowsPanel,
                "Suppression de la réservation...",
                () -> {
                    controller.deleteBooking(booking);
                    return null;
                },
                ignored -> {
                    JOptionPane.showMessageDialog(
                            this,
                            "La réservation a bien été supprimée.",
                            "Suppression réussie",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    loadBookings();
                },
                this::displayDeleteError
        );
    }

    private void displayDeleteError(Exception exception) {
        LoadingHelper.showError(
                bookingRowsPanel,
                "Impossible de supprimer la réservation."
        );

        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de suppression",
                JOptionPane.ERROR_MESSAGE
        );

        loadBookings();
    }
}