package viewPackage.Order;

import viewPackage.AbstractPanel;
import viewPackage.MainJFrame;

import controllerPackage.ApplicationController;
import exceptionPackage.*;
import modelPackage.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class OrderFormPanel extends AbstractPanel {
    private ApplicationController controller;

    private JTextField idOrderField;
    private JTextField commentField;
    private JTextField guestCountField;
    private JTextField orderDateField;
    private JCheckBox takeAwayCheckBox;
    private JTextField pickUpTimeField;
    private JTextField nameCustomerField;
    private JTextField telCustomerField;
    private JTextField idTableField;

    public OrderFormPanel(MainJFrame mainWindow) {
        super(mainWindow);
        controller = new ApplicationController();

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    public OrderFormPanel(MainJFrame mainWindow, Table table) {
        this(mainWindow);

        takeAwayCheckBox.setSelected(false);
        idTableField.setText(String.valueOf(table.getIdTable()));

        nameCustomerField.setText("");
        telCustomerField.setText("");
        pickUpTimeField.setText("");
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton backButton = createBackButton();

        JLabel titleLabel = new JLabel("Ajouter une commande", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        return topPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));

        idOrderField = new JTextField();
        commentField = new JTextField();
        guestCountField = new JTextField();
        orderDateField = new JTextField("2026-03-20");
        takeAwayCheckBox = new JCheckBox("Commande à emporter");
        pickUpTimeField = new JTextField();
        nameCustomerField = new JTextField();
        telCustomerField = new JTextField();
        idTableField = new JTextField();

        formPanel.add(new JLabel("ID commande *"));
        formPanel.add(idOrderField);

        formPanel.add(new JLabel("Commentaire"));
        formPanel.add(commentField);

        formPanel.add(new JLabel("Nombre de personnes *"));
        formPanel.add(guestCountField);

        formPanel.add(new JLabel("Date commande * (yyyy-mm-dd)"));
        formPanel.add(orderDateField);

        formPanel.add(new JLabel("À emporter ? *"));
        formPanel.add(takeAwayCheckBox);

        formPanel.add(new JLabel("Heure retrait (HH:mm)"));
        formPanel.add(pickUpTimeField);

        formPanel.add(new JLabel("Nom client"));
        formPanel.add(nameCustomerField);

        formPanel.add(new JLabel("Téléphone client"));
        formPanel.add(telCustomerField);

        formPanel.add(new JLabel("ID table"));
        formPanel.add(idTableField);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");

        saveButton.addActionListener(event -> saveOrder());
        cancelButton.addActionListener(event -> mainWindow.goBack());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        return buttonPanel;
    }

    private void saveOrder() {
        try {
            Order order = createOrderFromForm();
            controller.addOrder(order);

            JOptionPane.showMessageDialog(
                    this,
                    "Commande ajoutée avec succès.",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mainWindow.showOrderListPanel();

        } catch (IllegalArgumentException | OrderException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Order createOrderFromForm() {
        Integer idOrder = parseRequiredInteger(idOrderField.getText(), "ID commande");
        Integer guestCount = parseRequiredInteger(guestCountField.getText(), "Nombre de personnes");

        LocalDate orderDate = parseRequiredDate(orderDateField.getText(), "Date commande");

        Boolean isTakeAway = takeAwayCheckBox.isSelected();

        String comment = emptyToNull(commentField.getText());
        String nameCustomer = emptyToNull(nameCustomerField.getText());
        String telCustomer = emptyToNull(telCustomerField.getText());

        LocalTime pickUpTime = parseOptionalTime(pickUpTimeField.getText(), "Heure retrait");

        Table table = null;
        Integer idTable = parseOptionalInteger(idTableField.getText(), "ID table");

        if (idTable != null) {
            table = new Table(idTable, null, null);
        }

        validateOrderBusinessRules(isTakeAway, nameCustomer, telCustomer, pickUpTime, table);

        return new Order(
                idOrder,
                comment,
                guestCount,
                orderDate,
                isTakeAway,
                pickUpTime,
                nameCustomer,
                telCustomer,
                table
        );
    }

    private void validateOrderBusinessRules(Boolean isTakeAway, String nameCustomer,
                                            String telCustomer, LocalTime pickUpTime,
                                            Table table) {
        if (isTakeAway) {
            if (nameCustomer == null) {
                throw new IllegalArgumentException("Le nom du client est obligatoire pour une commande à emporter.");
            }

            if (table != null) {
                throw new IllegalArgumentException("Une commande à emporter ne peut pas être liée à une table.");
            }

            if (pickUpTime == null) {
                throw new IllegalArgumentException("L'heure de retrait est obligatoire pour une commande à emporter.");
            }
        } else {
            if (table == null) {
                throw new IllegalArgumentException("Une commande sur place doit être liée à une table.");
            }

            if (nameCustomer != null || telCustomer != null || pickUpTime != null) {
                throw new IllegalArgumentException("Une commande sur place ne doit pas contenir de client ni d'heure de retrait.");
            }
        }
    }

    private Integer parseRequiredInteger(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " est obligatoire.");
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + " doit être un nombre entier.");
        }
    }

    private Integer parseOptionalInteger(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + " doit être un nombre entier.");
        }
    }

    private LocalDate parseRequiredDate(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " est obligatoire.");
        }

        try {
            return LocalDate.parse(value);
        } catch (Exception exception) {
            throw new IllegalArgumentException(fieldName + " doit respecter le format yyyy-mm-dd.");
        }
    }

    private LocalTime parseOptionalTime(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalTime.parse(value);
        } catch (Exception exception) {
            throw new IllegalArgumentException(fieldName + " doit respecter le format HH:mm.");
        }
    }

    private String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
