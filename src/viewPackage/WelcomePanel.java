package viewPackage;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {
    private JLabel titleLabel;
    private JLabel subtitleLabel;

    public WelcomePanel() {
        setLayout(new BorderLayout());

        titleLabel = new JLabel("Restaurant Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));

        subtitleLabel = new JLabel("Bienvenue dans l'application de gestion du restaurant", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        add(titleLabel, BorderLayout.CENTER);
        add(subtitleLabel, BorderLayout.SOUTH);
    }
}
