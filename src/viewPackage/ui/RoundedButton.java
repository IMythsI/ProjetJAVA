package viewPackage.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {
    private final int arc;
    private boolean hovered;

    public RoundedButton(String text, int arc) {
        super(text);
        this.arc = arc;

        hovered = false;

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent event) {
                hovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        if (hovered) {
            g2.setColor(new Color(245, 248, 255));
        } else {
            g2.setColor(getBackground());
        }

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                arc,
                arc
        );

        g2.setColor(new Color(70, 120, 240, 60));
        g2.setStroke(new BasicStroke(1.2f));

        g2.drawRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                arc,
                arc
        );

        g2.dispose();

        super.paintComponent(graphics);
    }
}