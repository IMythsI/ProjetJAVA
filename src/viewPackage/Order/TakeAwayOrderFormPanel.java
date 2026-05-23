package viewPackage.Order;

import modelPackage.Order;
import viewPackage.AbstractPanel;
import viewPackage.MainJFrame;
import viewPackage.stylePackage.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Hashtable;

public class TakeAwayOrderFormPanel extends AbstractPanel {
    private JTextField nameCustomerField;
    private JTextField telCustomerField;
    private JTextField commentField;
    private JSlider pickUpTimeSlider;
    private JLabel pickUpTimeValueLabel;

    public TakeAwayOrderFormPanel(MainJFrame mainWindow) {
        super(mainWindow);

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 70));
        header.setBackground(AppTheme.NAVBAR);
        header.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 30));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 17));
        leftPanel.setOpaque(false);

        JButton backButton = createBackButton();

        JLabel title = new JLabel("Restaurant Manager");
        title.setForeground(Color.WHITE);
        title.setFont(AppTheme.NAVBAR_FONT);

        leftPanel.add(backButton);
        leftPanel.add(title);

        header.add(leftPanel, BorderLayout.WEST);

        return header;
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("Nouvelle commande à emporter");
        title.setFont(AppTheme.TITLE_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Renseigner les informations du client");
        subtitle.setFont(AppTheme.SUBTITLE_FONT);
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);

        JPanel card = createFormCard();

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 6, 0);
        mainPanel.add(title, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 25, 0);
        mainPanel.add(subtitle, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(card, gbc);

        return mainPanel;
    }

    private JPanel createFormCard() {
        JPanel card = new JPanel(new BorderLayout(20, 25));

        card.setBackground(AppTheme.CARD);
        card.setPreferredSize(new Dimension(500, 350));
        card.setMinimumSize(new Dimension(500, 350));
        card.setBorder(BorderFactory.createEmptyBorder(15, 35, 15, 35));

        card.putClientProperty("FlatLaf.style", "arc:20");

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 8, 14, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameCustomerField = createTextField();
        telCustomerField = createTextField();
        commentField = createTextField();

        JPanel timePanel = createTimeSliderPanel();

        addFormRow(formPanel, gbc, 0, "Nom client *", nameCustomerField, 40);
        addFormRow(formPanel, gbc, 1, "Téléphone", telCustomerField, 40);
        addFormRow(formPanel, gbc, 2, "Heure de retrait *", timePanel, 110);
        addFormRow(formPanel, gbc, 3, "Commentaire", commentField, 40);

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();

        field.setPreferredSize(null);
        field.setMinimumSize(new Dimension(300, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 14));

        return field;
    }

    private JPanel createTimeSliderPanel() {
        LocalTime now = LocalTime.now();

        int currentMinutes = now.getHour() * 60 + now.getMinute();
        currentMinutes = Math.round(currentMinutes / 15f) * 15;

        pickUpTimeSlider = new JSlider(0, 24 * 60, currentMinutes);

        pickUpTimeSlider.setPreferredSize(new Dimension(460, 100));

        pickUpTimeSlider.setMajorTickSpacing(6 * 60);
        pickUpTimeSlider.setMinorTickSpacing(15);
        pickUpTimeSlider.setPaintTicks(true);
        pickUpTimeSlider.setPaintLabels(true);
        pickUpTimeSlider.setSnapToTicks(true);

        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(0, new JLabel("00h"));
        labels.put(6 * 60, new JLabel("06h"));
        labels.put(12 * 60, new JLabel("12h"));
        labels.put(18 * 60, new JLabel("18h"));
        labels.put(24 * 60, new JLabel("24h"));

        pickUpTimeSlider.setLabelTable(labels);

        pickUpTimeValueLabel = new JLabel(formatTimeFromSlider(), SwingConstants.CENTER);
        pickUpTimeValueLabel.setFont(new Font("Arial", Font.BOLD, 22));
        pickUpTimeValueLabel.setForeground(AppTheme.PRIMARY);
        pickUpTimeValueLabel.setPreferredSize(new Dimension(85, 40));

        pickUpTimeSlider.addChangeListener(event ->
                pickUpTimeValueLabel.setText(formatTimeFromSlider())
        );

        JPanel timePanel = new JPanel(new BorderLayout(20, 0));
        timePanel.setOpaque(false);

        timePanel.add(pickUpTimeSlider, BorderLayout.CENTER);
        timePanel.add(pickUpTimeValueLabel, BorderLayout.EAST);

        return timePanel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row,
                            String labelText, JComponent field, int height) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(AppTheme.TEXT_PRIMARY);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;

        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;

        field.setPreferredSize(new Dimension(460, height));

        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);

        JButton cancelButton = new JButton("Annuler");
        JButton nextButton = new JButton("Choisir les produits");

        styleSecondaryButton(cancelButton);
        stylePrimaryButton(nextButton);

        cancelButton.addActionListener(event -> mainWindow.goBack());
        nextButton.addActionListener(event -> goToProductSelection());

        buttonPanel.add(cancelButton);
        buttonPanel.add(nextButton);

        return buttonPanel;
    }

    private void stylePrimaryButton(JButton button) {
        button.setPreferredSize(new Dimension(170, 40));
        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(AppTheme.PRIMARY);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");
    }

    private void styleSecondaryButton(JButton button) {
        button.setPreferredSize(new Dimension(110, 40));
        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(AppTheme.TEXT_PRIMARY);
        button.setBackground(AppTheme.CARD);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");
    }

    private String formatTimeFromSlider() {
        int totalMinutes = pickUpTimeSlider.getValue();

        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        if (hours == 24) {
            hours = 0;
        }

        return String.format("%02d:%02d", hours, minutes);
    }

    private void goToProductSelection() {
        try {
            Order order = createTakeAwayOrderFromForm();
            mainWindow.showProductSelectionPanel(order);

        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Order createTakeAwayOrderFromForm() {
        String nameCustomer = getRequiredText(
                nameCustomerField.getText(),
                "Nom client"
        );

        String telCustomer = emptyToNull(telCustomerField.getText());
        String comment = emptyToNull(commentField.getText());

        int totalMinutes = pickUpTimeSlider.getValue();

        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        if (hours == 24) {
            hours = 0;
        }

        LocalTime pickUpTime = LocalTime.of(hours, minutes);

        return new Order(
                null,
                comment,
                1,
                LocalDate.now(),
                true,
                pickUpTime,
                nameCustomer,
                telCustomer,
                null
        );
    }

    private String getRequiredText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " est obligatoire.");
        }

        return value.trim();
    }

    private String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}