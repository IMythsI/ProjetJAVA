package viewPackage.Dashboard;

import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;

public class ManagerDashboardPanel extends AppPage {

    public ManagerDashboardPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        addCentered(
                createPageTitle("Espace gérant"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle("Gérez le restaurant, les tables, les réservations et la carte"),
                1,
                new Insets(0, 0, 35, 0)
        );

        addCentered(
                createActionCardsPanel(),
                2,
                new Insets(0, 0, 35, 0)
        );
    }

    private JPanel createActionCardsPanel() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        cardsPanel.setOpaque(false);

        cardsPanel.add(CardFactory.createCardButton(
                "🪑",
                "Tables",
                "Voir et gérer les tables",
                () -> mainWindow.showTableListPanel()
        ));

        cardsPanel.add(CardFactory.createCardButton(
                "📅",
                "Réservations",
                "Lister les réservations",
                () -> mainWindow.showBookingListPanel()
        ));

        cardsPanel.add(CardFactory.createCardButton(
                "🕒",
                "Commandes",
                "Consulter les commandes",
                () -> mainWindow.showOrderListPanel()
        ));

        cardsPanel.add(CardFactory.createCardButton(
                "🔎",
                "Recherches",
                "Accéder aux recherches",
                () -> mainWindow.showBookingSearchPanel()
        ));

        cardsPanel.add(CardFactory.createCardButton(
                "🍽",
                "Carte/Menu",
                "Modifier les produits",
                () -> mainWindow.showProductManagementPanel()
        ));

        cardsPanel.add(CardFactory.createCardButton(
                "✅",
                "Validation",
                "Tester une capacité de table",
                () -> mainWindow.showBookingValidationPanel()
        ));

        wrapper.add(cardsPanel);
        return wrapper;
    }
}
