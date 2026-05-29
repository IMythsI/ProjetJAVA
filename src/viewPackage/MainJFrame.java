package viewPackage;

import modelPackage.Book;
import modelPackage.Order;
import modelPackage.Table;
import viewPackage.Booking.BookingEditPanel;
import viewPackage.Booking.BookingFormPanel;
import viewPackage.Booking.BookingListPanel;
import viewPackage.Booking.BookingValidationPanel;
import viewPackage.Dashboard.WaiterDashboardPanel;
import viewPackage.Order.*;
import viewPackage.Search.BookingSearchPanel;
import viewPackage.Search.OrderSearchPanel;
import viewPackage.Search.ProductSearchPanel;
import viewPackage.Table.TableDetailPanel;
import viewPackage.Table.TableListPanel;
import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.Deque;

public class MainJFrame extends JFrame {

    private final JPanel mainPanelContainer;
    private final Deque<JPanel> navigationStack;

    public MainJFrame() {
        super("Gestion du restaurant");

        navigationStack = new ArrayDeque<>();

        setSize(1100, 720);
        setMinimumSize(new Dimension(
                AppTheme.WINDOW_MIN_WIDTH,
                AppTheme.WINDOW_MIN_HEIGHT
        ));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanelContainer = new JPanel(new BorderLayout());

        Container frameContainer = getContentPane();
        frameContainer.setLayout(new BorderLayout());
        frameContainer.add(mainPanelContainer, BorderLayout.CENTER);

        setJMenuBar(createMenuBar());

        showWelcomePanel();
    }

    //MENU
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createFileMenu());
        menuBar.add(createBookingMenu());
        menuBar.add(createSearchMenu());
        menuBar.add(createBusinessMenu());
        menuBar.add(createOrderMenu());
        menuBar.add(createTableMenu());

        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("Fichier");

        JMenuItem homeItem = createMenuItem(
                "Accueil",
                event -> showWelcomePanel()
        );

        JMenuItem exitItem = createMenuItem(
                "Quitter",
                event -> System.exit(0)
        );

        fileMenu.add(homeItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        return fileMenu;
    }

    private JMenu createBookingMenu() {
        JMenu bookingMenu = new JMenu("Réservations");

        JMenuItem listBookingsItem = createMenuItem(
                "Lister les réservations",
                event -> showBookingListPanel()
        );

        JMenuItem addBookingItem = createMenuItem(
                "Nouvelle réservation",
                event -> showBookingFormPanel()
        );

        JMenuItem editBookingItem = createMenuItem(
                "Modifier une réservation",
                event -> showBookingEditPanel()
        );

        JMenuItem deleteBookingItem = createMenuItem(
                "Supprimer une réservation",
                event -> showBookingDeletePanel()
        );

        bookingMenu.add(listBookingsItem);
        bookingMenu.addSeparator();
        bookingMenu.add(addBookingItem);
        bookingMenu.add(editBookingItem);
        bookingMenu.add(deleteBookingItem);

        return bookingMenu;
    }

    private JMenu createSearchMenu() {
        JMenu searchMenu = new JMenu("Recherches");

        JMenuItem bookingSearchItem = createMenuItem(
                "Réservations par client et date",
                event -> showBookingSearchPanel()
        );

        JMenuItem orderSearchItem = createMenuItem(
                "Commandes par employé et statut",
                event -> showOrderSearchPanel()
        );

        JMenuItem productSearchItem = createMenuItem(
                "Produits par type et allergène",
                event -> showProductSearchPanel()
        );

        searchMenu.add(bookingSearchItem);
        searchMenu.add(orderSearchItem);
        searchMenu.add(productSearchItem);

        return searchMenu;
    }

    private JMenu createBusinessMenu() {
        JMenu businessMenu = new JMenu("Métier");

        JMenuItem validateBookingItem = createMenuItem(
                "Valider une capacité de réservation",
                event -> showBookingValidationPanel()
        );

        businessMenu.add(validateBookingItem);

        return businessMenu;
    }

    private JMenu createOrderMenu() {
        JMenu orderMenu = new JMenu("Commandes");

        JMenuItem orderCardsItem = createMenuItem(
                "Commandes en cours",
                event -> showOrderCardsPanel()
        );

        JMenuItem orderListItem = createMenuItem(
                "Lister les commandes",
                event -> showOrderListPanel()
        );

        JMenuItem takeAwayOrderItem = createMenuItem(
                "Nouvelle commande à emporter",
                event -> showTakeAwayOrderFormPanel()
        );

        orderMenu.add(orderCardsItem);
        orderMenu.add(orderListItem);
        orderMenu.addSeparator();
        orderMenu.add(takeAwayOrderItem);

        return orderMenu;
    }

    private JMenu createTableMenu() {
        JMenu tableMenu = new JMenu("Tables");

        JMenuItem listTablesItem = createMenuItem(
                "Lister les tables",
                event -> showTableListPanel()
        );

        tableMenu.add(listTablesItem);

        return tableMenu;
    }

    private JMenuItem createMenuItem(String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);

        item.addActionListener(listener);

        return item;
    }

    //NAVIGATION
    public void changePanel(JPanel panel) {
        if (panel == null) {
            return;
        }

        JPanel currentPanel = getCurrentPanel();

        if (currentPanel != null) {
            navigationStack.push(currentPanel);
        }

        displayPanel(panel);
    }

    public void goBack() {
        if (navigationStack.isEmpty()) {
            showWelcomePanel();
            return;
        }

        JPanel previousPanel = navigationStack.pop();
        displayPanel(previousPanel);
    }

    private JPanel getCurrentPanel() {
        if (mainPanelContainer.getComponentCount() == 0) {
            return null;
        }

        Component currentComponent = mainPanelContainer.getComponent(0);

        if (currentComponent instanceof JPanel currentPanel) {
            return currentPanel;
        }

        return null;
    }

    private void displayPanel(JPanel panel) {
        mainPanelContainer.removeAll();
        mainPanelContainer.add(panel, BorderLayout.CENTER);

        mainPanelContainer.revalidate();
        mainPanelContainer.repaint();
    }

    public void showWelcomePanel() {
        navigationStack.clear();
        displayPanel(new WelcomePanel(this));
    }

    //DASHBOARD
    public void showWaiterPanel() {
        changePanel(new WaiterDashboardPanel(this));
    }

    //BOOKING
    public void showBookingListPanel() {
        changePanel(new BookingListPanel(this));
    }

    public void showBookingFormPanel() {
        changePanel(new BookingFormPanel(this));
    }

    public void showBookingEditPanel(Book booking) {
        if (booking == null) {
            showBookingEditPanel();
            return;
        }

        changePanel(new BookingEditPanel(this, booking));
    }

    public void showBookingEditPanel() {
        changePanel(new BookingListPanel(this));

        JOptionPane.showMessageDialog(
                this,
                "Sélectionnez une réservation puis cliquez sur le bouton modifier.",
                "Modification d'une réservation",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void showBookingDeletePanel() {
        changePanel(new BookingListPanel(this));

        JOptionPane.showMessageDialog(
                this,
                "Sélectionnez une réservation puis cliquez sur le bouton supprimer.",
                "Suppression d'une réservation",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    //SEARCH
    public void showBookingSearchPanel() {
        changePanel(new BookingSearchPanel(this));
    }

    public void showOrderSearchPanel() {
        changePanel(new OrderSearchPanel(this));
    }

    public void showProductSearchPanel() {
        changePanel(new ProductSearchPanel(this));
    }

    //BUSINESS
    public void showBookingValidationPanel() {
        changePanel(new BookingValidationPanel(this));
    }

    //ORDER
    public void showOrderListPanel() {
        changePanel(new OrderListPanel(this));
    }

    public void showOrderCardsPanel() {
        changePanel(new OrderCardsPanel(this));
    }

    public void showTakeAwayOrderFormPanel() {
        changePanel(new TakeAwayOrderFormPanel(this));
    }

    public void showProductSelectionPanel(Order order) {
        changePanel(new ProductSelectionPanel(this, order));
    }

    public void showProductSelectionPanel(Table table) {
        changePanel(new ProductSelectionPanel(this, table));
    }

    public void showOrderDetailPanel(Order order) {
        changePanel(new OrderDetailPanel(this, order));
    }

    //TABLE
    public void showTableListPanel() {
        changePanel(new TableListPanel(this));
    }

    public void showTableDetailPanel(Table table) {
        changePanel(new TableDetailPanel(this, table));
    }



    public void showAllergiesPanel() {
        showProductSearchPanel();
    }

    public void showMenuPanel() {
        showProductSearchPanel();
    }
}