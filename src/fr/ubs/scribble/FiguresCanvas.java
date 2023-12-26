package fr.ubs.scribble;

import fr.ubs.scribble.shapes.Shape;
import fr.ubs.scribbleOnline.ScribbleClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;

/**
 * A canvas used to draw figures, composed of a list of figures and a figure currently drawing
 * <p>
 * A new figure is created when the mouse is pressed, and the location of the mouse is registered
 * (startX, startY). The figure is resized when the mouse is dragged, and it is added to the list of
 * figures when the mouse is released.
 * <p>
 * A figure is selected when the mouse is clicked on this figure, and deselected when the mouse is
 * clicked outside the figure. When a figure is selected, the mouse pointer's appearance changes when
 * moving on the figure, and the figure can be moved by dragging over it or deleted by pressing the
 * 'del' key.
 *
 * @author Pascale Launay
 */
public class FiguresCanvas extends JPanel implements MouseMotionListener, MouseListener, KeyListener
{
    /**
     * the initial width of the canvas
     */
    private static final int WIDTH = 800;

    /**
     * the initial height of the canvas
     */
    private static final int HEIGHT = 600;

    /**
     * the default cursor
     */
    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

    /**
     * the move cursor
     */
    private static final Cursor MOVE_CURSOR = new Cursor(Cursor.MOVE_CURSOR);

    /**
     * the north resize cursor
     */
    private static final Cursor N_RESIZE_CURSOR = new Cursor(Cursor.N_RESIZE_CURSOR);

    /**
     * the south resize cursor
     */
    private static final Cursor S_RESIZE_CURSOR = new Cursor(Cursor.S_RESIZE_CURSOR);

    /**
     * the west resize cursor
     */
    private static final Cursor W_RESIZE_CURSOR = new Cursor(Cursor.W_RESIZE_CURSOR);

    /**
     * the east resize cursor
     */
    private static final Cursor E_RESIZE_CURSOR = new Cursor(Cursor.E_RESIZE_CURSOR);

    /**
     * the north-west resize cursor
     */
    private static final Cursor NW_RESIZE_CURSOR = new Cursor(Cursor.NW_RESIZE_CURSOR);

    /**
     * the north-east resize cursor
     */
    private static final Cursor NE_RESIZE_CURSOR = new Cursor(Cursor.NE_RESIZE_CURSOR);

    /**
     * the south-west resize cursor
     */
    private static final Cursor SW_RESIZE_CURSOR = new Cursor(Cursor.SW_RESIZE_CURSOR);

    /**
     * the south-east resize cursor
     */
    private static final Cursor SE_RESIZE_CURSOR = new Cursor(Cursor.SE_RESIZE_CURSOR);

    /**
     * the figures currently drawn on the canvas
     */
    private final Figures figures;

    public Figures getFigures() {
        return figures;
    }

    /**
     * the figure from the list of figures that is selected (may be null)
     */
    private Figure selectedFigure;

    /**
     * the x coordinate of the mouse location when the mouse is pressed
     */
    private double startX;

    /**
     * the y coordinate of the mouse location when the mouse is pressed
     */
    private double startY;

    /**
     * the x coordinate of the selected figure location when the mouse is pressed
     */
    private double figureStartX;

    /**
     * the y coordinate of the selected figure location when the mouse is pressed
     */
    private double figureStartY;

    /**
     * the width of the selected figure location when the mouse is pressed
     */
    private double figureStartWidth;

    /**
     * the height of the selected figure location when the mouse is pressed
     */
    private double figureStartHeight;

    /**
     * the direction when resizing a figure
     */
    private Direction resizeDirection;

    /**
     * true when the mouse is currently dragging (either to resize the new figure or to move the selected figure)
     */
    private boolean dragging;

    /**
     * true when the cursor has a 'move' appearance, meaning that the mouse is over a selected figure, and dragging
     * will move the figure
     */
    private boolean moveCursor;

    /**
     * true when the cursor has a 'resize' appearance, meaning that the mouse is over a selected figure, and dragging
     * will resize the figure
     */
    private boolean resizeCursor;

    /**
     * the shape currently selected
     */
    private Shape shape;

    /**
     * the color currently selected
     */
    private Color color;

    /**
     * the scale to apply to draw figures
     */
    private double scale;

    /**
     * the x translation to apply to draw figures
     */
    private double tx;

    /**
     * the y translation to apply to draw figures
     */
    private double ty;

    ScribbleClient client;
    /**
     * Constructor
     * @throws RemoteException
     */
    public FiguresCanvas(ScribbleClient client_) throws RemoteException
    {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        this.client = client_;
        this.figures = client.getFiguresBox().getFigures();

        // register listeners
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    /**
     * Draws the canvas content: all figures and the currently drawing figure if any
     *
     * @param g the graphics context.
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        if (width > 0 && height > 0) {
            // compute the transform to be applied to the figures
            scale = Math.min(1.0 * width / WIDTH, 1.0 * height / HEIGHT);
            tx = (width - WIDTH * scale) / 2;
            ty = (height - HEIGHT * scale) / 2;

            // draw the background
            g2d.setColor(Color.BLACK);
            g2d.fill(new Rectangle2D.Double(0, 0, width, height));
            g2d.setColor(Color.WHITE);
            g2d.fill(new Rectangle2D.Double(tx, ty, WIDTH * scale, HEIGHT * scale));

            // draw the figures
            figures.draw(g2d, scale, tx, ty);
        }
    }

    /**
     * Set the currently selected shape
     *
     * @param shape the new shape
     */
    public void setShape(Shape shape)
    {
        this.shape = shape;
        requestFocusInWindow();
    }

    /**
     * Set the currently selected color and change the color of the selected figure, if any
     *
     * @param color the new color
     */
    public void setColor(Color color)
    {
        this.color = color;
        if (this.selectedFigure != null) {
            this.selectedFigure.setColor(color);
            // !!! the figure color has been updated
            try {
                this.client.getFiguresBox().updateFigure(selectedFigure);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            repaint();
        }
        requestFocusInWindow();
    }

    /**
     * Called when the mouse is pressed: either start moving the selected figure (if moveCursor) or start a
     * new figure drawing
     *
     * @param event the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent event)
    {
        requestFocusInWindow();
        double x = getX(event);
        double y = getY(event);
        startX = x;
        startY = y;
        if (moveCursor || resizeCursor) { // start moving or resizing the selected figure
            figureStartX = selectedFigure.getX();
            figureStartY = selectedFigure.getY();
            figureStartWidth = selectedFigure.getWidth();
            figureStartHeight = selectedFigure.getHeight();
        } else { // create a new figure
            figures.createFigure(shape, color, startX, startY);
        }
    }

    /**
     * Called when the mouse is released. Adds the currently drawing figure if any or else select the figure
     * at the mouse location if any
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent event)
    {
        double x = getX(event);
        double y = getY(event);
        if (dragging) { // a figure is currently drawing or moving
            if (!moveCursor && !resizeCursor) {
                Figure selectedFigure = figures.addCurrentFigure();
                select(selectedFigure, x, y);
                // !!! the figure has been added
                try {
                    this.client.getFiguresBox().addFigure(selectedFigure);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (selectedFigure != null) {
                selectedFigure.update();
                // !!! the figure location or size has been updated
                try {
                    this.client.getFiguresBox().updateFigure(selectedFigure);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else { // select the figure at the mouse location or deselect the currently selected figure
            Figure selectedFigure = figures.getFigureAt(x, y);
            select(selectedFigure, x, y);
        }
        dragging = false;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released)
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent event)
    {
        // NOTHING
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent event)
    {
        // NOTHING
    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent event)
    {
        // NOTHING
    }

    /**
     * Called when the mouse is dragged. Move the selected figure if the cursor has the 'move' appearance
     * or resize the currently drawing figure
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent event)
    {
        double x = getX(event);
        double y = getY(event);
        if (!dragging && (Math.abs(x - startX) > 2 || Math.abs(y - startY) > 2)) {
            // start dragging
            if (!moveCursor && !resizeCursor) {
                select(null, x, y);
            }
            dragging = true;
        }
        if (dragging) {
            if (moveCursor) { // move the selected figure
                selectedFigure.setX(figureStartX + x - startX);
                selectedFigure.setY(figureStartY + y - startY);
            } else if (resizeCursor) { // resize the selected figure
                if (resizeDirection.isWest()) {
                    selectedFigure.setWidth(figureStartWidth + startX - x);
                    selectedFigure.setX(figureStartX + x - startX);
                } else if (resizeDirection.isEast()) {
                    selectedFigure.setWidth(figureStartWidth + x - startX);
                }
                if (resizeDirection.isNorth()) {
                    selectedFigure.setHeight(figureStartHeight + startY - y);
                    selectedFigure.setY(figureStartY + y - startY);
                } else if (resizeDirection.isSouth()) {
                    selectedFigure.setHeight(figureStartHeight + y - startY);
                }                
            } else { // resize the currently drawing figure
                figures.resizeCurrentFigure(x - startX, y - startY);
            }
            repaint();
        }
    }

    /**
     * Called when the mouse is moved (but not dragged). Changes the cursor appearance if the
     * mouse enters or leaves a selected figure ('move' appearance)
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent event)
    {
        double x = getX(event);
        double y = getY(event);
        if (dragging) { // the selected figure is moving or resizing. Do not change the cursor
            return;
        }
        Figure figure = figures.getFigureAt(x, y);
        if (figure != null && figure.isSelected()) { // enters a selected figure
            setMoveCursor(figure, x, y);
        } else if ((moveCursor || resizeCursor) && (figure == null || !figure.isSelected())) { // leaves a selected figure
            setCursor(DEFAULT_CURSOR);
            moveCursor = false;
            resizeCursor = false;
        }
    }

    private void setMoveCursor(Figure figure, double x, double y)
    {
        Direction location = figure.getBorderLocation(x, y);
        if (location == null) {
            moveCursor = true;
            resizeCursor = false;
            setCursor(MOVE_CURSOR);
        } else {
            moveCursor = false;
            resizeCursor = true;
            resizeDirection = location;
            switch (location) {
                case NORTH:
                    setCursor(N_RESIZE_CURSOR);
                    break;
                case SOUTH:
                    setCursor(S_RESIZE_CURSOR);
                    break;
                case WEST:
                    setCursor(W_RESIZE_CURSOR);
                    break;
                case EAST:
                    setCursor(E_RESIZE_CURSOR);
                    break;
                case NORTH_WEST:
                    setCursor(NW_RESIZE_CURSOR);
                    break;
                case NORTH_EAST:
                    setCursor(NE_RESIZE_CURSOR);
                    break;
                case SOUTH_WEST:
                    setCursor(SW_RESIZE_CURSOR);
                    break;
                case SOUTH_EAST:
                    setCursor(SE_RESIZE_CURSOR);
                    break;
            }
        }
    }

    /**
     * Invoked when a key has been typed.
     *
     * @param event the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent event)
    {
        // NOTHING
    }

    /**
     * Invoked when a key has been pressed.
     *
     * @param event the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.VK_DELETE && this.selectedFigure != null) {
            figures.remove(this.selectedFigure);
            // !!! the figure has been removed
            try {
                this.client.getFiguresBox().removeFigure(this.selectedFigure);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.selectedFigure = null;
            repaint();
        }
    }

    /**
     * Invoked when a key has been released.
     *
     * @param event the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent event)
    {
        // NOTHING
    }

    /**
     * Unselect the given figure if it is selected
     * 
     * @param figure the figure to be unselected
     */
    public void unselect(Figure figure)
    {
        if (this.selectedFigure != null && this.selectedFigure.getId() == figure.getId()) {
            this.selectedFigure.setSelected(false);
            this.selectedFigure = null;
            System.out.println("Figure :"+ figure.getId()+" unselected");
        }
    }

    /**
     * Select the given figure (if not null) and unselect the previously selected figure (if any)
     *
     * @param selectedFigure the figure to be selected (may be null)
     * @param x the x location of the mouse
     * @param y the y location of the mouse
     */
    private void select(Figure selectedFigure, double x, double y)
    {
        if (selectedFigure == this.selectedFigure) {
            return;
        }
        if (this.selectedFigure != null) {
            // unselect the previously selected figure
            this.selectedFigure.setSelected(false);
            this.selectedFigure = null;
            repaint();
        }
        if (selectedFigure != null) {
            this.selectedFigure = selectedFigure;
            this.selectedFigure.setSelected(true);
            setMoveCursor(selectedFigure, x, y);
            repaint();
        }
    }

    /**
     * Give the x location of the mouse represented by the given mouse event after applying the transform
     * applied to the canvas
     *
     * @param event the mouse event
     * @return the x location of the mouse
     */
    private double getX(MouseEvent event)
    {
        return (event.getX() - tx) / scale;
    }

    /**
     * Give the y location of the mouse represented by the given mouse event after applying the transform
     * applied to the canvas
     *
     * @param event the mouse event
     * @return the y location of the mouse
     */
    private double getY(MouseEvent event)
    {
        return (event.getY() - ty) / scale;
    }
}
