package viewPackage;

import viewPackage.ui.*;

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
                new Insets(0, 0, 18, 0)
        );

        addCentered(
                new KitchenAnimationPanel(),
                2,
                new Insets(0, 0, 35, 0)
        );

        addCentered(
                createRoleCardsPanel(),
                3,
                new Insets(0, 0, 40, 0)
        );

        addCentered(
                createSecondaryButtonsPanel(),
                4,
                new Insets(0, 0, 0, 0)
        );
    }

    private JPanel createRoleCardsPanel() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 35, 35));
        cardsPanel.setOpaque(false);

        cardsPanel.add(createRoleCard(
                "🍸",
                "Barman",
                "Gestion des boissons et commandes",
                () -> mainWindow.showBarmanPanel()
        ));

        cardsPanel.add(createRoleCard(
                "🍽",
                "Serveur",
                "Commandes, réservations et tables",
                () -> mainWindow.showWaiterPanel()
        ));

        cardsPanel.add(createRoleCard(
                "📊",
                "Gérant",
                "Statistiques et gestion du restaurant",
                () -> mainWindow.showManagerPanel()
        ));

        cardsPanel.add(createRoleCard(
                "👨‍🍳",
                "Cuisinier",
                "Préparation et suivi des commandes",
                () -> mainWindow.showCookPanel()
        ));

        wrapper.add(cardsPanel);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeRoleCards(wrapper, cardsPanel);
            }
        });

        return wrapper;
    }

    private void resizeRoleCards(JPanel wrapper, JPanel cardsPanel) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = 800;
        int minWidth = 100;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        int newHeight;

        if (newWidth < 760) {
            newHeight = 320;
        } else {
            newHeight = 350;
        }

        Dimension size = new Dimension(newWidth, newHeight);

        cardsPanel.setPreferredSize(size);
        cardsPanel.setMinimumSize(new Dimension(minWidth, 350));
        cardsPanel.setMaximumSize(new Dimension(maxWidth, 400));

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JButton createRoleCard(String icon,
                                   String title,
                                   String description,
                                   Runnable action) {
        JButton card = new RoundedButton("", AppTheme.CARD_ARC);

        card.setLayout(new BorderLayout());
        card.setBackground(AppTheme.CARD);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createEmptyBorder(24, 26, 24, 26));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(AppTheme.EMOJI_FONT);
        iconLabel.setPreferredSize(AppTheme.SMALL_ICON_BUTTON_SIZE);
        iconLabel.setForeground(AppTheme.PRIMARY);
        iconLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.CARD_TITLE_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(AppTheme.SUBTITLE_FONT);
        descriptionLabel.setForeground(AppTheme.TEXT_SECONDARY);
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(titleLabel);
        centerPanel.add(descriptionLabel);

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);

        if (action != null) {
            card.addActionListener(event -> action.run());
        }

        return card;
    }

    private JPanel createSecondaryButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(
                FlowLayout.CENTER,
                35,
                0
        ));

        panel.setOpaque(false);

        JButton bookingsButton = ButtonFactory.createSecondaryButton(
                "Réservations",
                () -> mainWindow.showBookingListPanel()
        );

        JButton allergiesButton = ButtonFactory.createSecondaryButton(
                "Allergies",
                () -> mainWindow.showProductSearchPanel()
        );

        JButton menuButton = ButtonFactory.createSecondaryButton(
                "Carte/Menu",
                () -> mainWindow.showProductSearchPanel()
        );

        panel.add(bookingsButton);
        panel.add(allergiesButton);
        panel.add(menuButton);

        return panel;
    }
}