package viewPackage.Order;

import modelPackage.Order;
import viewPackage.MainJFrame;
import viewPackage.ui.AppPage;
import viewPackage.ui.ButtonFactory;
import viewPackage.ui.CardFactory;
import viewPackage.ui.FormFactory;
import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Hashtable;

public class TakeAwayOrderFormPanel extends AppPage {
    private JTextField nameCustomerField;
    private JTextField telCustomerField;
    private JTextField commentField;
    private JSlider pickUpTimeSlider;
    private JLabel pickUpTimeValueLabel;

    public TakeAwayOrderFormPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        addCentered(createPageTitle("Nouvelle commande à emporter"), 0, new Insets(0, 0, 6, 0));
        addCentered(createPageSubtitle("Renseigner les informations du client"), 1, new Insets(0, 0, 25, 0));
        addCentered(createFormCard(), 2, new Insets(0, 0, 0, 0));
    }

    private JPanel createFormCard() {
        JPanel card = CardFactory.createCard(500, 350);
        card.setBorder(BorderFactory.createEmptyBorder(15, 35, 15, 35));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 8, 14, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameCustomerField = FormFactory.createTextField();
        telCustomerField = FormFactory.createTextField();
        commentField = FormFactory.createTextField();

        JPanel timePanel = createTimeSliderPanel();

        addFormRow(formPanel, gbc, 0, "Nom client *", nameCustomerField, 40);
        addFormRow(formPanel, gbc, 1, "Téléphone", telCustomerField, 40);
        addFormRow(formPanel, gbc, 2, "Heure de retrait *", timePanel, 110);
        addFormRow(formPanel, gbc, 3, "Commentaire", commentField, 40);

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
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
        label.setFont(AppTheme.BUTTON_FONT);
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

        buttonPanel.add(ButtonFactory.createSecondaryButton("Annuler", () -> mainWindow.goBack()));
        buttonPanel.add(ButtonFactory.createPrimaryButton("Choisir les produits", this::goToProductSelection));

        return buttonPanel;
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
        String nameCustomer = getRequiredText(nameCustomerField.getText(), "Nom client");
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