package fr.ubs.scribble.shapes;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A square
 *
 * @author Pascale Launay
 */
public class Square extends Shape
{
    /**
     * Constructor
     */
    public Square()
    {
        super("Square");
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
        g2d.fill(rect);
    }

    public void drawIcon(Graphics2D g2d)
    {
        g2d.draw(new Rectangle2D.Double(7, 1, 10, 10));
    }
}
