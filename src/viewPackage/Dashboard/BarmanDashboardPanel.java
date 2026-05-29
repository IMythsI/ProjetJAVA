package viewPackage.Dashboard;

import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;

public class BarmanDashboardPanel extends AppPage {

    public BarmanDashboardPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        addCentered(
                createPageTitle("Espace barman"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle("Gérez les boissons et suivez les commandes"),
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

        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 30, 30));
        cardsPanel.setOpaque(false);

        cardsPanel.add(CardFactory.createCardButton(
                "🕒",
                "Commandes en cours",
                "Voir les commandes à préparer",
                () -> mainWindow.showOrderCardsPanel()
        ));

        cardsPanel.add(CardFactory.createCardButton(
                "🍹",
                "Carte des boissons",
                "Consulter les boissons",
                () -> mainWindow.showProductSearchPanel()
        ));

        wrapper.add(cardsPanel);

        return wrapper;
    }
}
