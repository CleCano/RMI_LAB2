package fr.ubs.scribble;

import fr.ubs.scribble.shapes.Shape;

import java.awt.*;
import java.util.ArrayList;

/**
 * The figures to be drawn: a list of figures and a new figure currently drawing
 *
 * @author Pascale Launay
 */
public class Figures extends ArrayList<Figure>
{
    /**
     * the figure that is currently drawing (may be null)
     */
    private Figure currentFigure;

    /**
     * Draw the figures using the given graphics context
     *
     * @param g2d   graphics context used to draw the figures
     * @param scale the scale to apply to draw figures
     * @param tx    the x translation to apply to draw figures
     * @param ty    the y translation to apply to draw figures
     */
    public void draw(Graphics2D g2d, double scale, double tx, double ty)
    {
        // draw figures
        for (Figure figure : this) {
            figure.draw(g2d, scale, tx, ty);
        }

        // draw the new figure if any
        if (currentFigure != null) {
            currentFigure.draw(g2d, scale, tx, ty);
        }
    }

    /**
     * Start drawing a new figure
     * 
     * @param shape the shape of the figure
     * @param color the color of the figure
     * @param x     the x location of the figure
     * @param y     the y location of the figure
     */
    public void createFigure(Shape shape, Color color, double x, double y)
    {
        this.currentFigure = new Figure(shape, color, x, y);
    }

    /**
     * Resize the currently drawing figure, if any
     *
     * @param width  the new width of the figure
     * @param height the new height of the figure
     */
    public void resizeCurrentFigure(double width, double height)
    {
        if (this.currentFigure != null) {
            this.currentFigure.setWidth(width);
            this.currentFigure.setHeight(height);
        }
    }

    /**
     * Stop drawing the new figure: add it to the list of figures if it is available
     *
     * @return the new figure (may be null)
     */
    public Figure addCurrentFigure()
    {
        Figure figure = null;
        if (currentFigure != null && !currentFigure.isEmpty()) {
            figure = currentFigure;
            figure.update();
            this.add(figure);
        }
        currentFigure = null;
        return figure;
    }

    /**
     * Stop moving or resizing the current figure
     * 
     * @return the figure that has been updated (maybe null)
     */
    public Figure updateCurrentFigure()
    {
        Figure figure = null;
        if (currentFigure != null && !currentFigure.isEmpty()) {   
            figure = currentFigure;
            figure.update(); 
        }
        currentFigure = null;
        return figure;
    }

    /**
     * Give the figure at the given location. If several figures stand at this location,
     * give the figure at the forefront
     *
     * @param x x coordinate of the location
     * @param y y coordinate of the location
     * @return a figure at the given location (may be null)
     */
    public Figure getFigureAt(double x, double y)
    {
        for (int i = this.size() - 1; i >= 0; i--) {
            if (this.get(i).isInside(x, y)) {
                return this.get(i);
            }
        }
        return null;
    }
}
