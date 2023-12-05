package fr.ubs.scribble.shapes;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * Generic definition of a shape, that provide methods to draw a figure and an icon
 * that represents the figure
 *
 * @author Pascale Launay
 */
public abstract class Shape implements Serializable
{
    /**
     * the name of the shape
     */
    private final String name;

    /**
     * Constructor
     *
     * @param name the name of the shape
     */
    protected Shape(String name)
    {
        this.name = name;
    }

    /**
     * Give the name of the shape
     *
     * @return the name of the shape
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Change the x location of the given shape bounding box
     *
     * @param rect the shape bounding box to be modified
     * @param x    the new shape x location
     */
    public void setX(Rectangle2D rect, double x)
    {
        rect.setRect(x, rect.getY(), rect.getWidth(), rect.getHeight());
    }

    /**
     * Change the y location of the given shape bounding box
     *
     * @param rect the shape bounding box to be modified
     * @param y    the new shape y location
     */
    public void setY(Rectangle2D rect, double y)
    {
        rect.setRect(rect.getX(), y, rect.getWidth(), rect.getHeight());
    }

    /**
     * Change the width of the given shape bounding box
     *
     * @param rect  the shape bounding box to be modified
     * @param width the new shape width
     */
    public void setWidth(Rectangle2D rect, double width)
    {
        rect.setRect(rect.getX(), rect.getY(), width, rect.getHeight());
    }

    /**
     * Change the height of the given shape bounding box
     *
     * @param rect   the shape bounding box to be modified
     * @param height the new shape height
     */
    public void setHeight(Rectangle2D rect, double height)
    {
        rect.setRect(rect.getX(), rect.getY(), rect.getWidth(), height);
    }

    /**
     * Draw the shape
     *
     * @param g2d    graphics context used to draw the shape
     * @param rect   the shape bounding box to be modified
     */
    public abstract void draw(Graphics2D g2d, Rectangle2D rect);

    /**
     * draw an icon that represents the shape in a 24x12 area
     *
     * @param g2d graphics context used to draw the icon
     */
    public abstract void drawIcon(Graphics2D g2d);
}
