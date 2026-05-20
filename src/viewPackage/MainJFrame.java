package viewPackage;

import viewPackage.Dashboard.WaiterDashboardPanel;
import viewPackage.Order.OrderListPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class MainJFrame extends JFrame {
    private Container frameContainer;

    private Stack<JPanel> navigationStack;

    public MainJFrame() {
        super("Restaurant Management");

        navigationStack = new Stack<>();

        setBounds(100, 100, 1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        frameContainer = this.getContentPane();
        frameContainer.setLayout(new BorderLayout());

        setJMenuBar(createMenuBar());
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

    private void showWelcomePanel() {
        changePanel(new WelcomePanel(this));
    }

    public void showWaiterPanel() {
        changePanel(new WaiterDashboardPanel(this));
    }

    public void showOrderListPanel() {
        changePanel(new OrderListPanel(this));
    }

    public void showBookingListPanel() {
        //changePanel(new BookingListPanel(this));
    }

    public void showTableListPanel() {
        //changePanel(new RestaurantTableListPanel(this));
    }

    public void showAllergiesPanel() {
        //changePanel(new RestaurantTableListPanel(this));
    }
}
