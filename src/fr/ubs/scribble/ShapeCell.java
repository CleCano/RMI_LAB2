package fr.ubs.scribble;

import fr.ubs.scribble.shapes.Shape;

import javax.swing.*;
import java.awt.*;

/**
 * A view that represents a shape in a list of shapes, composed of the image of the shape and its name
 *
 * @author Pascale Launay
 */
public class ShapeCell extends JPanel
{
    /**
     * the shape represented by this cell
     */
    private final Shape shape;

    /**
     * Constructor
     *
     * @param shape the shape represented by this cell
     */
    public ShapeCell(Shape shape)
    {
        this.shape = shape;
        this.setLayout(new BorderLayout(5, 5));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        add(new JLabel(shape.getName()), BorderLayout.CENTER);
        add(new IconCanvas(), BorderLayout.WEST);
    }

    /**
     * Canvas where the image of the shape is displayed
     */
    class IconCanvas extends JPanel
    {
        /**
         * Constructor
         */
        public IconCanvas()
        {
            setPreferredSize(new Dimension(24, 12));
        }

        @Override
        public void paintComponent(Graphics g)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setBackground(ShapeCell.this.getBackground());
            g2d.clearRect(0, 0, 24, 12);
            g2d.setColor(new Color(0, 0, 0x8b));
            shape.drawIcon((Graphics2D) g);
        }
    }
}
