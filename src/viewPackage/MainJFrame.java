package viewPackage;

import modelPackage.*;
import viewPackage.Booking.*;
import viewPackage.Dashboard.*;
import viewPackage.Order.*;
import viewPackage.Table.*;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class MainJFrame extends JFrame {
    private Container frameContainer;

    private Stack<JPanel> navigationStack;

    public MainJFrame() {
        super("Restaurant Management");

        navigationStack = new Stack<>();

        setBounds(100, 100, 1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        frameContainer = this.getContentPane();
        frameContainer.setLayout(new BorderLayout());

        //setJMenuBar(createMenuBar());
        showWelcomePanel();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu orderMenu = new JMenu("Order");
        JMenuItem listOrderItem = new JMenuItem("List Orders");
        JMenuItem addOrderItem = new JMenuItem("New Order");

        listOrderItem.addActionListener(e -> showOrderListPanel());

        orderMenu.add(listOrderItem);
        orderMenu.add(addOrderItem);

        JMenu productMenu = new JMenu("Product");
        productMenu.add(new JMenuItem("List Products"));
        productMenu.add(new JMenuItem("New Product"));

        JMenu bookingMenu = new JMenu("Bookings");
        bookingMenu.add(new JMenuItem("List bookings"));
        bookingMenu.add(new JMenuItem("New booking"));

        JMenu fileMenu = new JMenu("File");
        JMenuItem mainItem = new JMenuItem("Main");
        JMenuItem exitItem = new JMenuItem("Exit");
        mainItem.addActionListener(event -> showWelcomePanel());
        exitItem.addActionListener(event -> System.exit(0));
        fileMenu.add(mainItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        menuBar.add(orderMenu);
        menuBar.add(productMenu);
        menuBar.add(bookingMenu);

        return menuBar;
    }

    public void changePanel(JPanel panel) {
        Component currentPanel = getContentPane();
        if (currentPanel instanceof JPanel currentJPanel) {
            navigationStack.push(currentJPanel);
        }
        setContentPane(panel);
        revalidate();
        repaint();
    }

    public void goBack() {
        if (!navigationStack.isEmpty()) {
            JPanel previousPanel = navigationStack.pop();
            setContentPane(previousPanel);
            revalidate();
            repaint();
        }
    }

    public void showWelcomePanel() {
        navigationStack.clear();
        setContentPane(new WelcomePanel(this));
        revalidate();
        repaint();
    }

    //Dashboard
    public void showWaiterPanel() {
        changePanel(new WaiterDashboardPanel(this));
    }

    //Order
    public void showOrderListPanel() {
        changePanel(new OrderListPanel(this));
    }

    //Table
    public void showTableDetailPanel(Table table) {
        changePanel(new TableDetailPanel(this, table));
    }

    public void showTableListPanel() {
        changePanel(new TableListPanel(this));
    }

    public void showBookingListPanel() {
        changePanel(new BookingListPanel(this));
    }

    public void showAllergiesPanel() {
        //changePanel(new RestaurantTableListPanel(this));
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

    public void showOrderCardsPanel() {
        changePanel(new OrderCardsPanel(this));
    }
}
