import businessPackage.OrderManager;
import controllerPackage.ApplicationController;
import exceptionPackage.OrderException;
import modelPackage.Order;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ApplicationController applicationController = new ApplicationController(new OrderManager());

        try {
            ArrayList<Order> orders = applicationController.getAllOrders();

            System.out.println("Liste des commandes :");
            for (Order order : orders) {
                System.out.println(order);
            }
        } catch (OrderException exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
