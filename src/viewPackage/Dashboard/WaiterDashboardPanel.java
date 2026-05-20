package viewPackage.Dashboard;

import viewPackage.*;

import javax.swing.*;
import java.awt.*;

public class WaiterDashboardPanel extends AbstractPanel {

    public WaiterDashboardPanel(MainJFrame mainWindow) {
        super(mainWindow);

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel topPanel = new JPanel(new BorderLayout());

        JButton backButton = createBackButton();

        JLabel title = new JLabel("Espace serveur", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 20, 20));

        cardsPanel.add(createCard(
                "Commandes",
                "Voir et gérer les commandes du service",
                "Ouvrir",
                () -> mainWindow.showOrderListPanel()
        ));

        cardsPanel.add(createCard(
                "Réservations",
                "Consulter les réservations du jour",
                "Ouvrir",
                () -> mainWindow.showBookingListPanel()
        ));

        cardsPanel.add(createCard(
                "Tables",
                "Voir l’état des tables",
                "Ouvrir",
                () -> mainWindow.showTableListPanel()
        ));

        cardsPanel.add(createCard(
                "Allergies",
                "Consulter les allergies des produits",
                "Ouvrir",
                () -> mainWindow.showAllergiesPanel()
        ));

        add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String description, String buttonText, Runnable action) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton button = new JButton(buttonText);
        button.addActionListener(event -> action.run());

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descriptionLabel, BorderLayout.CENTER);
        card.add(button, BorderLayout.SOUTH);

        return card;
    }
}
