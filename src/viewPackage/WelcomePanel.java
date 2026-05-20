package viewPackage;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {
    private MainJFrame mainWindow;
    private JComboBox<String> roleComboBox;

    public WelcomePanel(MainJFrame mainWindow) {
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createRolePanel(), BorderLayout.CENTER);
        add(createSecondaryButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 5, 5));

        JLabel titleLabel = new JLabel("Restaurant Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 38));

        JLabel subtitleLabel = new JLabel("Choisissez votre rôle", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        return titlePanel;
    }

    private JPanel createRolePanel() {
        JPanel rolePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel roleLabel = new JLabel("Rôle :");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        roleComboBox = new JComboBox<>(new String[]{
                "Serveur",
                "Cuisine",
                "Manager"
        });
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        roleComboBox.setPreferredSize(new Dimension(260, 38));

        JButton continueButton = new JButton("Continuer");
        continueButton.setFont(new Font("Arial", Font.BOLD, 16));
        continueButton.setPreferredSize(new Dimension(160, 38));
        continueButton.setFocusPainted(false);
        continueButton.addActionListener(event -> openSelectedRole());

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        rolePanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        rolePanel.add(roleComboBox, gbc);

        gbc.gridx = 2;
        rolePanel.add(continueButton, gbc);

        return rolePanel;
    }

    private JPanel createSecondaryButtonPanel() {
        JPanel secondaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        JButton tablesButton = createSmallButton("Cartes / tables");
        JButton allergiesButton = createSmallButton("Allergies");
        JButton bookingsButton = createSmallButton("Réservations");

        /*
        tablesButton.addActionListener(event -> mainWindow.showTablesPanel());
        allergiesButton.addActionListener(event -> mainWindow.showAllergiesPanel());
        bookingsButton.addActionListener(event -> mainWindow.showBookingsPanel());


         */
        secondaryPanel.add(tablesButton);
        secondaryPanel.add(allergiesButton);
        secondaryPanel.add(bookingsButton);

        return secondaryPanel;
    }

    private JButton createSmallButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 13));
        button.setPreferredSize(new Dimension(150, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void openSelectedRole() {
        String selectedRole = (String) roleComboBox.getSelectedItem();
        /*
        switch (selectedRole) {
            case "Serveur" -> mainWindow.showWaiterPanel();
            case "Cuisine" -> mainWindow.showCookPanel();
            case "Manager" -> mainWindow.showManagerPanel();
        }

         */
    }
}