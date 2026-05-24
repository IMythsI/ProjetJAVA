package viewPackage.ui;

import viewPackage.AbstractPanel;
import viewPackage.MainJFrame;
import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.awt.*;

public abstract class AppPage extends JPanel {
    protected MainJFrame mainWindow;
    protected JPanel mainPanel;

    public AppPage(MainJFrame mainWindow, boolean showBackButton) {
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(HeaderFactory.createHeader(mainWindow,showBackButton),BorderLayout.NORTH);

        add(createScrollableContent(), BorderLayout.CENTER);
    }

    private JScrollPane createScrollableContent() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(35, 30, 35, 30));

        JScrollPane scrollPane = new JScrollPane(mainPanel);

        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(25);
        scrollPane.getViewport().setBackground(AppTheme.BACKGROUND);

        return scrollPane;
    }

    protected void addCentered(Component component, int row, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = insets;

        mainPanel.add(component, gbc);
    }

    protected JLabel createPageTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.TITLE_FONT);
        label.setForeground(AppTheme.TEXT_PRIMARY);
        return label;
    }

    protected JLabel createPageSubtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.SUBTITLE_FONT);
        label.setForeground(AppTheme.TEXT_SECONDARY);
        return label;
    }

    protected void refreshPage() {
        revalidate();
        repaint();

        if (mainPanel != null) {
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }
}