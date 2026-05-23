package viewPackage;

import viewPackage.stylePackage.AppTheme;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {
    private MainJFrame mainWindow;
    private CardFactory cardFactory = new CardFactory();

    public WelcomePanel(MainJFrame mainWindow) {
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());

        header.setPreferredSize(new Dimension(0, 70));
        header.setBackground(AppTheme.NAVBAR);
        header.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JLabel title = new JLabel("Restaurant Manager");
        title.setForeground(Color.WHITE);
        title.setFont(AppTheme.NAVBAR_FONT);

        header.add(title, BorderLayout.WEST);

        return header;
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 25, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("Choisis ton rôle");
        title.setFont(AppTheme.TITLE_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Sélectionner votre profil");
        subtitle.setFont(AppTheme.SUBTITLE_FONT);
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 14, 0);
        mainPanel.add(title, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 45, 0);
        mainPanel.add(subtitle, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 45, 0);
        mainPanel.add(createRoleCardsPanel(), gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(createSecondaryButtonsPanel(), gbc);

        return mainPanel;
    }

    private JPanel createRoleCardsPanel() {
        JPanel wrapper = new JPanel(new GridLayout(2, 2, 35, 35));

        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(520, 300));
        wrapper.setMaximumSize(new Dimension(520, 300));

        wrapper.add(cardFactory.createRoleCard(
                "🍸",
                "Barman",
                "Gestion des boissons et commandes",
                () -> JOptionPane.showMessageDialog(this, "Page barman à créer.")
        ));

        wrapper.add(cardFactory.createRoleCard(
                "🍽",
                "Serveur",
                "Commandes, réservations et tables",
                () -> mainWindow.showWaiterPanel()
        ));

        wrapper.add(cardFactory.createRoleCard(
                "📊",
                "Gérant",
                "Statistiques et gestion du restaurant",
                () -> JOptionPane.showMessageDialog(this, "Page gérant à créer.")
        ));

        wrapper.add(cardFactory.createRoleCard(
                "👨‍🍳",
                "Cuisinier",
                "Préparation et suivi des commandes",
                () -> JOptionPane.showMessageDialog(this, "Page cuisinier à créer.")
        ));

        return wrapper;
    }

    private JPanel createSecondaryButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));

        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(700, 55));
        panel.setMaximumSize(new Dimension(700, 55));

        panel.add(cardFactory.createSecondaryButton(
                "Réservations",
                () -> mainWindow.showBookingListPanel()
        ));

        panel.add(cardFactory.createSecondaryButton(
                "Allergies",
                () -> mainWindow.showAllergiesPanel()
        ));

        panel.add(cardFactory.createSecondaryButton(
                "Carte/Menu",
                () -> JOptionPane.showMessageDialog(this, "Carte/Menu à créer.")
        ));

        return panel;
    }
}