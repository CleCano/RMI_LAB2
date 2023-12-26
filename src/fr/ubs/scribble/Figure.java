package fr.ubs.scribble;

import fr.ubs.scribble.shapes.Shape;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * A figure to be drawn. Composed of a shape, a color, a location and a size
 *
 * @author Pascale Launay
 */
public class Figure implements Serializable
{
    /**
     * the last unique id
     */
    private static int LAST_ID;

    /**
     * the shape of the figure
     */
    private final Shape shape;

    /**
     * figure unique identifier
     */
    private int id;

    /**
     * the color of the figure
     */
    private Color color;

    /**
     * the figure bounding box
     */
    private Rectangle2D rect;

    /**
     * true if the figure is selected
     */
    private boolean selected;

    /**
     * Constructor
     *
     * @param shape the shape of the figure
     * @param color the color of the figure
     * @param x     the x location of the figure
     * @param y     the y location of the figure
     */
    public Figure(Shape shape, Color color, double x, double y)
    {
        this.id = ++LAST_ID;
        this.shape = shape;
        this.color = color;
        this.rect = new Rectangle2D.Double(x, y, 0, 0);
    }

    /**
     * Give the figure unique id
     *
     * @return the figure unique id
     */
    public int getId()
    {
        return id;
    }

    /**
     * true if the figure has a null width or height
     *
     * @return true if the figure is empty
     */
    public boolean isEmpty()
    {
        return rect.getWidth() == 0 || rect.getHeight() == 0;
    }

    /**
     * Check whether the given point is inside the bounding box of the figure
     *
     * @param x x coordinate of the point
     * @param y y coordinate of the point
     * @return true if the point is inside the figure
     */
    public boolean isInside(double x, double y)
    {
        return x >= rect.getX() - 2 && x <= rect.getX() + rect.getWidth() + 2 &&
                y >= rect.getY() - 2 && y <= rect.getY() + rect.getHeight() + 2;
    }

    /**
     * Give the direction of the mouse location, if it is at the middle or corner of one of the borders
     * @param x the mouse x location
     * @param y the mouse y location
     * @return the direction, null if the mouse is not at some border
     */
    public Direction getBorderLocation(double x, double y)
    {
        double centerX = rect.getX() + rect.getWidth() / 2;
        double centerY = rect.getY() + rect.getHeight() / 2;
        boolean west = x <= rect.getX() + 4;
        boolean east = x >= rect.getX() + rect.getWidth() - 4;
        boolean middle = x >= centerX - 4 && x <= centerX + 4;
        if (y <= rect.getY() + 4) { // north
            return west ? Direction.NORTH_WEST : east ? Direction.NORTH_EAST : middle ? Direction.NORTH : null;
        } else if (y >= rect.getY() + rect.getHeight() - 4) { // south
            return west ? Direction.SOUTH_WEST : east ? Direction.SOUTH_EAST : middle ? Direction.SOUTH : null;
        } else if (y >= centerY - 4 && y <= centerY + 4) { // middle
            return west ? Direction.WEST : east ? Direction.EAST : null;
        }
        return null;
    }

    /**
     * Check whether the figure is selected
     *
     * @return true if the figure is selected
     */
    public boolean isSelected()
    {
        return this.selected;
    }

    /**
     * Change the selected state of the figure
     *
     * @param selected true if the figure is selected
     */
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    /**
     * Give the x location of the upper left point of the figure bounding box
     *
     * @return x location of the figure
     */
    public double getX()
    {
        return rect.getX();
    }

    /**
     * Give the y location of the upper left point of the figure bounding box
     *
     * @return y location of the figure
     */
    public double getY()
    {
        return rect.getY();
    }

    /**
     * Give the width of the figure bounding box
     *
     * @return width of the figure
     */
    public double getWidth()
    {
        return rect.getWidth();
    }

    /**
     * Give the height of the figure bounding box
     *
     * @return height of the figure
     */
    public double getHeight()
    {
        return rect.getHeight();
    }

    /**
     * Give the color of the figure
     *
     * @return the color of the figure
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * Change x the location of the figure
     *
     * @param x x location of the upper left point of the figure bounding box
     */
    public void setX(double x)
    {
        this.shape.setX(this.rect, x);
    }

    /**
     * Change the y location of the figure
     *
     * @param y y location of the upper left point of the figure bounding box
     */
    public void setY(double y)
    {
        this.shape.setY(this.rect, y);
    }

    /**
     * Change the figure color
     *
     * @param color the new figure color
     */
    public void setColor(Color color)
    {
        this.color = color;
    }
    
    /**
     * Change the width of the figure
     *
     * @param width  width of the figure bounding box
     */
    public void setWidth(double width)
    {
        this.shape.setWidth(this.rect, width);
    }


    /**
     * Change the height of the figure
     *
     * @param height  height of the figure bounding box
     */
    public void setHeight(double height)
    {
        this.shape.setHeight(this.rect, height);
    }

    /**
     * Draw the figure using the given graphics context
     *
     * @param g2d   the graphics context
     * @param scale the scale to apply to draw figures
     * @param tx    the x translation to apply to draw figures
     * @param ty    the y translation to apply to draw figures
     */
    public void draw(Graphics2D g2d, double scale, double tx, double ty)
    {
        g2d.setColor(color);
        shape.draw(g2d, makeRectangle(rect.getX() * scale + tx, rect.getY() * scale + ty, rect.getWidth() * scale, rect.getHeight() * scale));
        if (selected) {
            drawSelection(g2d, scale, tx, ty);
        }
    }

    private Rectangle2D makeRectangle(double x, double y, double width, double height)
    {
        if (width < 0) {
            x += width;
            width = -width;
        }
        if (height < 0) {
            y += height;
            height = -height;
        }
        return new Rectangle2D.Double(x, y, width, height);
    }

    /**
     * Update the figure size and location: if the width or height are negative, make them
     * positive and change the location accordingly
     */
    public void update()
    {
        if (rect.getWidth() < 0) {
            rect.setRect(rect.getX()+rect.getWidth(), rect.getY(), -rect.getWidth(), rect.getHeight());
        }
        if (rect.getHeight() < 0) {
            rect.setRect(rect.getX(), rect.getY()+rect.getHeight(), rect.getWidth(), -rect.getHeight());
        }
    }

    /**
     * Draw small squares around the figure that show that the figure is selected
     *
     * @param g2d   the graphics context
     * @param scale the scale to apply to draw figures
     * @param tx    the x translation to apply to draw figures
     * @param ty    the y translation to apply to draw figures
     */
    private void drawSelection(Graphics2D g2d, double scale, double tx, double ty)
    {
        g2d.setColor(new Color(0, 0, 0x8b));
        g2d.setStroke(new BasicStroke(.5f));
        double x0 = rect.getX() - 2, x1 = (x0 + rect.getWidth() / 2) * scale + tx, x2 = (x0 + rect.getWidth()) * scale + tx;
        double y0 = rect.getY() - 2, y1 = (y0 + rect.getHeight() / 2) * scale + ty, y2 = (y0 + rect.getHeight()) * scale + ty;
        x0 = x0 * scale + tx;
        y0 = y0 * scale + ty;
        double[][] points = {{x0, y0}, {x0, y1}, {x0, y2}, {x1, y0}, {x1, y2}, {x2, y0}, {x2, y1}, {x2, y2}};
        for (double[] p : points) {
            g2d.draw(new Rectangle2D.Double(p[0], p[1], 4, 4));
        }
    }

    @Override
    public String toString()
    {
        return shape.getName() + " " + rect;
    }

    public static int getLAST_ID() {
        return LAST_ID;
    }

    public Shape getShape() {
        return shape;
    }

    public Rectangle2D getRect() {
        return rect;
    }
}
