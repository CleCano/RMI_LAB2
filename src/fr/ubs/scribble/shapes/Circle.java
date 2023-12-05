package fr.ubs.scribble.shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * A circle
 *
 * @author Pascale Launay
 */
public class Circle extends Shape
{
    /**
     * Constructor
     */
    public Circle()
    {
        super("Circle");
    }


    @Override
    public void setWidth(Rectangle2D rect, double width)
    {
        rect.setRect(rect.getX(), rect.getY(), width, width);
    }

    @Override
    public void setHeight(Rectangle2D rect, double height)
    {
        rect.setRect(rect.getX(), rect.getY(), height, height);
    }

    @Override
    public void draw(Graphics2D g2d, Rectangle2D rect)
    {
        g2d.fill(new Ellipse2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));
    }

    public void drawIcon(Graphics2D g2d)
    {
        g2d.draw(new Ellipse2D.Double(7, 1, 10, 10));
    }
}
