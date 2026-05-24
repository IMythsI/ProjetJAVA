package viewPackage;

import viewPackage.ui.AppPage;
import viewPackage.ui.ButtonFactory;
import viewPackage.ui.CardFactory;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends AppPage {

    public WelcomePanel(MainJFrame mainWindow) {
        super(mainWindow, false);

        addCentered(
                createPageTitle("Choisis ton rôle"),
                0,
                new Insets(0, 0, 14, 0)
        );

        addCentered(
                createPageSubtitle("Sélectionner votre profil"),
                1,
                new Insets(0, 0, 45, 0)
        );

        addCentered(
                createRoleCardsPanel(),
                2,
                new Insets(0, 0, 45, 0)
        );

        addCentered(
                createSecondaryButtonsPanel(),
                3,
                new Insets(0, 0, 0, 0)
        );
    }

    private JPanel createRoleCardsPanel() {
        JPanel wrapper = new JPanel(new GridLayout(2, 2, 35, 35));

        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(650, 350));
        wrapper.setMaximumSize(new Dimension(650, 350));
        wrapper.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));

        wrapper.add(CardFactory.createCardButton(
                "🍸",
                "Barman",
                "Gestion des boissons et commandes",
                () -> JOptionPane.showMessageDialog(this, "Page barman à créer.")
        ));

        wrapper.add(CardFactory.createCardButton(
                "🍽",
                "Serveur",
                "Commandes, réservations et tables",
                () -> mainWindow.showWaiterPanel()
        ));

        wrapper.add(CardFactory.createCardButton(
                "📊",
                "Gérant",
                "Statistiques et gestion du restaurant",
                () -> JOptionPane.showMessageDialog(this, "Page gérant à créer.")
        ));

        wrapper.add(CardFactory.createCardButton(
                "👨‍🍳",
                "Cuisinier",
                "Préparation et suivi des commandes",
                () -> JOptionPane.showMessageDialog(this, "Page cuisinier à créer.")
        ));

        return wrapper;
    }

    private JPanel createSecondaryButtonsPanel() {
        JPanel panel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        30,
                        0
                )
        );

        panel.setOpaque(false);

        panel.setPreferredSize(
                new Dimension(700, 55)
        );

        panel.setMaximumSize(
                new Dimension(700, 55)
        );

        panel.add(
                ButtonFactory.createSecondaryButton(
                        "Réservations",
                        () -> mainWindow.showBookingListPanel()
                )
        );

        panel.add(
                ButtonFactory.createSecondaryButton(
                        "Allergies",
                        () -> mainWindow.showAllergiesPanel()
                )
        );

        panel.add(
                ButtonFactory.createSecondaryButton(
                        "Carte/Menu",
                        () -> JOptionPane.showMessageDialog(
                                this,
                                "Carte/Menu à créer."
                        )
                )
        );

        return panel;
    }
}