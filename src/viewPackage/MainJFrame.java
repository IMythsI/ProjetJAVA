package viewPackage;

import modelPackage.*;
import viewPackage.Booking.*;
import viewPackage.Dashboard.*;
import viewPackage.Order.*;
import viewPackage.Table.*;
import viewPackage.Search.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Stack;

public class MainJFrame extends JFrame {

    private JPanel mainPanelContainer;
    private Stack<JPanel> navigationStack;

    public MainJFrame() {
        super("Restaurant Management");

        navigationStack = new Stack<>();

        setBounds(100, 100, 1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanelContainer = new JPanel(new BorderLayout());

        Container frameContainer = getContentPane();
        frameContainer.setLayout(new BorderLayout());
        frameContainer.add(mainPanelContainer, BorderLayout.CENTER);

        setJMenuBar(createMenuBar());

        showWelcomePanel();
    }

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
        JMenu fileMenu = new JMenu("File");

        JMenuItem homeItem = createMenuItem("Home", e -> showWelcomePanel());
        JMenuItem exitItem = createMenuItem("Exit", e -> System.exit(0));

        fileMenu.add(homeItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        return fileMenu;
    }

    private JMenu createBookingMenu() {
        JMenu bookingMenu = new JMenu("Bookings");

        JMenuItem listBookingsItem = createMenuItem(
                "List bookings",
                e -> showBookingListPanel()
        );

        JMenuItem addBookingItem = createMenuItem(
                "New booking",
                e -> showBookingFormPanel()
        );

        JMenuItem editBookingItem = createMenuItem(
                "Edit booking",
                e -> showBookingEditPanel()
        );

        JMenuItem deleteBookingItem = createMenuItem(
                "Delete booking",
                e -> showBookingDeletePanel()
        );

        bookingMenu.add(listBookingsItem);
        bookingMenu.addSeparator();
        bookingMenu.add(addBookingItem);
        bookingMenu.add(editBookingItem);
        bookingMenu.add(deleteBookingItem);

        return bookingMenu;
    }

    private JMenu createSearchMenu() {
        JMenu searchMenu = new JMenu("Search");

        JMenuItem bookingSearchItem = createMenuItem(
                "Bookings by customer and date",
                e -> showBookingSearchPanel()
        );

        JMenuItem orderSearchItem = createMenuItem(
                "Orders by waiter and status",
                e -> showOrderSearchPanel()
        );

        JMenuItem productSearchItem = createMenuItem(
                "Products by type and allergy",
                e -> showProductSearchPanel()
        );

        searchMenu.add(bookingSearchItem);
        searchMenu.add(orderSearchItem);
        searchMenu.add(productSearchItem);

        return searchMenu;
    }

    private JMenu createBusinessMenu() {
        JMenu businessMenu = new JMenu("Business");

        JMenuItem validateBookingItem = createMenuItem(
                "Validate booking capacity",
                e -> showBookingValidationPanel()
        );

        businessMenu.add(validateBookingItem);

        return businessMenu;
    }

    private JMenu createOrderMenu() {
        JMenu orderMenu = new JMenu("Orders");

        JMenuItem orderCardsItem = createMenuItem(
                "Current orders",
                e -> showOrderCardsPanel()
        );

        JMenuItem orderListItem = createMenuItem(
                "List orders",
                e -> showOrderListPanel()
        );

        JMenuItem takeAwayOrderItem = createMenuItem(
                "New takeaway order",
                e -> showTakeAwayOrderFormPanel()
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
                "List tables",
                e -> showTableListPanel()
        );

        tableMenu.add(listTablesItem);

        return tableMenu;
    }

    private JMenuItem createMenuItem(String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(listener);
        return item;
    }

    public void changePanel(JPanel panel) {
        Component currentComponent = null;

        if (mainPanelContainer.getComponentCount() > 0) {
            currentComponent = mainPanelContainer.getComponent(0);
        }

        if (currentComponent instanceof JPanel currentPanel) {
            navigationStack.push(currentPanel);
        }

        mainPanelContainer.removeAll();
        mainPanelContainer.add(panel, BorderLayout.CENTER);

        mainPanelContainer.revalidate();
        mainPanelContainer.repaint();
    }

    public void goBack() {
        if (!navigationStack.isEmpty()) {
            JPanel previousPanel = navigationStack.pop();

            mainPanelContainer.removeAll();
            mainPanelContainer.add(previousPanel, BorderLayout.CENTER);

            mainPanelContainer.revalidate();
            mainPanelContainer.repaint();
        }
    }

    public void showWelcomePanel() {
        navigationStack.clear();

        mainPanelContainer.removeAll();
        mainPanelContainer.add(new WelcomePanel(this), BorderLayout.CENTER);

        mainPanelContainer.revalidate();
        mainPanelContainer.repaint();
    }


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
        changePanel(new BookingEditPanel(this, booking));
    }

    public void showBookingEditPanel() {
        showNotImplementedMessage("Edit booking");
    }

    public void showBookingDeletePanel() {
        showNotImplementedMessage("Delete booking");
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

    //TABLE
    public void showTableListPanel() {
        changePanel(new TableListPanel(this));
    }

    public void showTableDetailPanel(Table table) {
        changePanel(new TableDetailPanel(this, table));
    }

    //OTHER
    public void showAllergiesPanel() {
        showNotImplementedMessage("Allergies");
    }

    private void showNotImplementedMessage(String featureName) {
        JOptionPane.showMessageDialog(
                this,
                featureName + " is not implemented yet.",
                "Feature not implemented",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}