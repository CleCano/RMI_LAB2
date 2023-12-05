package fr.ubs.scribble.shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * An oval
 *
 * @author Pascale Launay
 */
public class Oval extends Shape
{
    /**
     * Constructor
     */
    public Oval()
    {
        super("Oval");
    }

    @Override
    public void draw(Graphics2D g2d, Rectangle2D rect)
    {
        g2d.fill(new Ellipse2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));
    }

    public void drawIcon(Graphics2D g2d)
    {
        g2d.draw(new Ellipse2D.Double(1, 1, 22, 10));
    }
}
