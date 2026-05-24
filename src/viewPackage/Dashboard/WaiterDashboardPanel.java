package viewPackage.Dashboard;

import viewPackage.MainJFrame;
import viewPackage.ui.AppPage;
import viewPackage.ui.CardFactory;
import viewPackage.ui.TableFactory;
import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.awt.*;

public class WaiterDashboardPanel extends AppPage {

    public WaiterDashboardPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        addCentered(
                createPageTitle("Dashboard Serveur"),
                0,
                new Insets(0, 0, 45, 0)
        );

        addCentered(
                createActionCardsPanel(),
                1,
                new Insets(0, 0, 45, 0)
        );

        addCentered(
                createRecentOrdersPanel(),
                2,
                new Insets(0, 0, 0, 0)
        );
    }

    private JPanel createActionCardsPanel() {
        JPanel wrapper = new JPanel(new GridLayout(2, 2, 35, 35));

        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(650, 350));


        wrapper.add(CardFactory.createCardButton(
                "➕",
                "Nouvelle commande",
                "",
                () -> mainWindow.showTakeAwayOrderFormPanel()
        ));

        wrapper.add(CardFactory.createCardButton(
                "📅",
                "Réservations",
                "",
                () -> mainWindow.showBookingListPanel()
        ));

        wrapper.add(CardFactory.createCardButton(
                "\uD83E\uDE91",
                "Tables",
                "",
                () -> mainWindow.showTableListPanel()
        ));

        wrapper.add(CardFactory.createCardButton(
                "🕒",
                "Commandes en cours",
                "",
                () -> mainWindow.showOrderCardsPanel()
        ));

        return wrapper;
    }

    private JPanel createRecentOrdersPanel() {
        JPanel card = CardFactory.createCard(520, 170);

        JLabel title = new JLabel("Commandes récentes");
        title.setFont(new Font("Arial", Font.BOLD, 15));
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JPanel listPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        listPanel.setOpaque(false);

        listPanel.add(createRecentOrderLine(
                "Table 4",
                "Pizza + Coca",
                "En préparation",
                AppTheme.WARNING
        ));

        listPanel.add(createRecentOrderLine(
                "Table 2",
                "Burger + Frites",
                "Prête",
                AppTheme.SUCCESS
        ));

        listPanel.add(createRecentOrderLine(
                "Table 7",
                "Salade César",
                "Servie",
                AppTheme.TEXT_SECONDARY
        ));

        card.add(title, BorderLayout.NORTH);
        card.add(listPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createRecentOrderLine(
            String table,
            String products,
            String status,
            Color statusColor
    ) {
        JPanel line = new JPanel(new GridLayout(1, 3));

        line.setOpaque(false);

        line.add(TableFactory.createCellLabel(
                table,
                AppTheme.TEXT_PRIMARY
        ));

        line.add(TableFactory.createCellLabel(
                products,
                AppTheme.TEXT_PRIMARY
        ));

        line.add(TableFactory.createCellLabel(
                status,
                statusColor
        ));

        return line;
    }
}