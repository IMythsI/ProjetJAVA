package viewPackage.ui;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    private final int arc;

    public RoundedButton(String text, int arc) {
        super(text);
        this.arc = arc;

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();

        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int inset = 2;
        int width = getWidth() - inset * 2;
        int height = getHeight() - inset * 2;

        if (getModel().isPressed()) {
            graphics2D.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            graphics2D.setColor(getBackground().brighter());
        } else {
            graphics2D.setColor(getBackground());
        }

        graphics2D.fillRoundRect(
                inset,
                inset,
                width,
                height,
                arc,
                arc
        );

        graphics2D.dispose();

        super.paintComponent(graphics);
    }

    @Override
    protected void paintBorder(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();

        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int inset = 2;
        int width = getWidth() - inset * 2;
        int height = getHeight() - inset * 2;

        graphics2D.setColor(AppTheme.BORDER);

        graphics2D.drawRoundRect(
                inset,
                inset,
                width,
                height,
                arc,
                arc
        );

        graphics2D.dispose();
    }
}