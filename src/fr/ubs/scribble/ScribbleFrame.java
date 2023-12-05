package fr.ubs.scribble;

import fr.ubs.scribble.shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * Application entry point
 */
public class ScribbleFrame extends JFrame
{

    /**
     * Print a usage message and exit
     */
    private static void usage()
    {
        System.out.println("Usage: java ScribbleFrame");
        System.exit(-1);
    }

    /**
     * Launch the ScribblePad app
     *
     * @param args NONE
     */
    public static void main(String[] args)
    {
        if (args.length > 0) {
            usage();
        }
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    ScribbleFrame frame = new ScribbleFrame();
                    frame.pack();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Constructor. Initialize the frame widgets
     * 
     * @throws Exception if any error occurs while constructing the frame
     */
    public ScribbleFrame() throws Exception
    {
        super("Scribble Pad");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setBackground(Color.WHITE);
        setLayout(new BorderLayout(5, 5));
        FiguresCanvas canvas = new FiguresCanvas();
        add(makeToolbar(canvas), BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
    }

    /**
     * Create the toolbar that contains the selectors for shapes and colors
     *
     * @param canvas the canvas to be notified when a new shape or color is selected
     * @return the toolbar
     */
    private JPanel makeToolbar(FiguresCanvas canvas) throws Exception
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.setBackground(Color.WHITE);

        // initialize the shapes list
        ShapesFactory factory = new ShapesFactory();
        Set<Shape> instances = factory.makeShapes();
        Shape[] shapes = new Shape[instances.size()];
        if (shapes.length == 0) {
            throw new Exception("No shapes found in the application classpath");
        }
        instances.toArray(shapes);

        // initialize the shape selector
        JComboBox<Shape> shapeSelector = new JComboBox<>(shapes);
        shapeSelector.setRenderer((list, shape, index, isSelected, cellHasFocus) -> new ShapeCell(shape));
        panel.add(shapeSelector);
        shapeSelector.addItemListener(event -> canvas.setShape((Shape) shapeSelector.getSelectedItem()));
        canvas.setShape(shapes[0]);
        
        // initialize the color selector
        ColorButton colorSelector = new ColorButton(new Color(0, 0, 0x8b), "Fill color");
        colorSelector.addActionListener(event -> {
            if (event.getActionCommand().equals(ColorButton.ACTION_COLOR)) {
                canvas.setColor(colorSelector.getColor());
            }
        });
        canvas.setColor(colorSelector.getColor());
        panel.add(colorSelector);

        return panel;
    }
}
