package viewPackage.Dashboard;

import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class WaiterDashboardPanel extends AppPage {

    private JPanel cardsPanel;

    public WaiterDashboardPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        addCentered(
                createPageTitle("Espace serveur"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle("Gérez les commandes, les tables et les réservations"),
                1,
                new Insets(0, 0, 35, 0)
        );

        addCentered(
                createActionCardsPanel(),
                2,
                new Insets(0, 0, 35, 0)
        );

        /*addCentered(
                createInformationCard(),
                3,
                new Insets(0, 0, 0, 0)
        );*/
    }

    private JPanel createActionCardsPanel() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        cardsPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        cardsPanel.setOpaque(false);

        cardsPanel.add(CardFactory.createCardButton(
                "➕",
                "Nouvelle commande",
                "Créer une commande à emporter",
                () -> mainWindow.showTakeAwayOrderFormPanel()
        ));

        cardsPanel.add(CardFactory.createCardButton(
                "📅",
                "Réservations",
                "Lister et gérer les réservations",
                () -> mainWindow.showBookingListPanel()
        ));

        cardsPanel.add(CardFactory.createCardButton(
                "🪑",
                "Tables",
                "Voir l’état des tables",
                () -> mainWindow.showTableListPanel()
        ));

        cardsPanel.add(CardFactory.createCardButton(
                "🕒",
                "Commandes en cours",
                "Suivre les commandes ouvertes",
                () -> mainWindow.showOrderCardsPanel()
        ));

        wrapper.add(cardsPanel);

        wrapper.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                resizeActionCards(wrapper);
            }
        });

        return wrapper;
    }

    private void resizeActionCards(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = 900;
        int minWidth = 620;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        int newHeight = newWidth < 760 ? 320 : 350;

        Dimension size = new Dimension(newWidth, newHeight);

        cardsPanel.setPreferredSize(size);
        cardsPanel.setMinimumSize(new Dimension(minWidth, 300));
        cardsPanel.setMaximumSize(new Dimension(maxWidth, 350));

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createInformationCard() {
        JPanel card = CardFactory.createCard(720, 120);
        card.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Actions disponibles");
        titleLabel.setFont(AppTheme.TEXT_BOLD_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel descriptionLabel = new JLabel(
                "Utilisez les cartes ci-dessus pour accéder rapidement aux fonctionnalités principales du serveur."
        );
        descriptionLabel.setFont(AppTheme.TEXT_FONT);
        descriptionLabel.setForeground(AppTheme.TEXT_SECONDARY);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descriptionLabel, BorderLayout.CENTER);

        return card;
    }
}