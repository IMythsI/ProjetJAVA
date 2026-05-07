import businessPackage.OrderManager;
import controllerPackage.ApplicationController;
import exceptionPackage.OrderException;
import modelPackage.Order;
import viewPackage.MainJFrame;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception exception) {
                System.out.println("Look and feel non chargé");
            }

            MainJFrame window = new MainJFrame();
            window.setVisible(true);
        });
    }
}
