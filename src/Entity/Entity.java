package Entity;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * The Entity class represents a game entity with position, size, and image properties.
 * It provides methods to access and modify these properties, as well as methods for drawing
 * the entity and checking for collision with other entities.
 */
public abstract class Entity {

    protected int dX;
    protected int dY;
    protected int width;
    protected int height;
    protected Image image;
    protected Point position;

    public Entity(int x, int y, int width, int height) {
        this.position = new Point(x, y);
        this.width = width;
        this.height = height;
        this.dX = 0;
        this.dY = 0;
        this.image = null;
    }


    /* Accessors */

    /**
     * Represents the entity's coordinate location in two-dimensional space.
     */
    public Point getPosition() { return position; }

    /**
     * Returns the x-coordinate of the entity's position.
     *
     * @return the x-coordinate of the entity's position
     */
    public int getX() { return (int) position.getX(); }

    /**
     * Returns the y-coordinate of the entity's position.
     *
     * @return the y-coordinate of the entity's position
     */
    public int getY() { return (int) position.getY(); }

    /**
     * Returns the X axis delta of the entity.
     *
     * @return the value of dX
     */
    public int getDX() { return dX; }

    /**
     * Returns the Y axis delta of the entity.
     *
     * @return the value of dY
     */
    public int getDY() { return dY; }

    /**
     * Returns the width of the entity.
     *
     * @return the width of the entity
     */
    public int getWidth() { return width; }

    /**
     * Returns the height of the entity.
     *
     * @return the height of the entity
     */
    public int getHeight() { return height; }

    /**
     * Returns the image associated with this entity.
     *
     * @return the image associated with this entity
     */
    public Image getImage() { return image; }

    /**
     * Returns the size of the entity as a Dimension object.
     *
     * @return A Dimension object representing the width and height of the entity.
     */
    public Dimension getSize() { return new Dimension(width, height); }

    /**
     * Returns the bounding rectangle of the entity.
     *
     * @return A Rectangle2D.Double object representing the entity's bounding rectangle.
     */
    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(position.getX(), position.getY(), width, height);
    }


    /* Mutators */

    /**
     * Sets the position of the entity to the specified Point.
     *
     * @param position The new position for the entity.
     */
    public void setPosition(Point position) { this.position = position; }

    /**
     * Sets the position of the entity to the specified x and y coordinates.
     *
     * @param x The new x-coordinate for the entity.
     * @param y The new y-coordinate for the entity.
     */
    public void setPosition(int x, int y) { this.position = new Point(x, y); }

    /**
     * Sets the x-coordinate of the entity. If the specified value is negative, the x-coordinate is set to 0.
     *
     * @param x The new x-coordinate for the entity.
     */
    public void setX(int x) { position.x = (x < 0) ? 0 : x; }

    /**
     * Sets the x-coordinate of the entity. If the specified value is negative, the x-coordinate is set to 0.
     *
     * @param x The new x-coordinate for the entity.
     */
    public void setX(float x) { position.x = (x < 0) ? 0 : Math.round(x); }

    /**
     * Sets the x-coordinate of the entity. If the specified value is negative, the x-coordinate is set to 0.
     *
     * @param x The new x-coordinate for the entity.
     */
    public void setX(double x) { position.x = (x < 0) ? 0 : Math.round((float) x); }

    /**
     * Sets the y-coordinate of the entity. If the specified value is negative, the y-coordinate is set to 0.
     *
     * @param y The new y-coordinate for the entity.
     */
    public void setY(int y) { position.y = (y < 0) ? 0 : y; }

    /**
     * Sets the y-coordinate of the entity. If the specified value is negative, the y-coordinate is set to 0.
     *
     * @param y The new y-coordinate for the entity.
     */
    public void setY(float y) { position.y = (y < 0) ? 0 : Math.round(y); }

    /**
     * Sets the y-coordinate of the entity. If the specified value is negative, the y-coordinate is set to 0.
     *
     * @param y The new y-coordinate for the entity.
     */
    public void setY(double y) { position.y = (y < 0) ? 0 : Math.round((float) y); }

    /**
     * Sets the horizontal speed of the entity.
     *
     * @param dX The new horizontal speed for the entity.
     */
    public void setDX(int dX) { this.dX = dX; }

    /**
     * Sets the vertical speed of the entity.
     *
     * @param dY The new vertical speed for the entity.
     */
    public void setDY(int dY) { this.dY = dY; }

    /**
     * Sets the width of the entity.
     *
     * @param width The new width for the entity.
     */
    public void setWidth(int width) { this.width = width; }

    /**
     * Sets the height of the entity.
     *
     * @param height the new height of the entity
     */
    public void setHeight(int height) { this.height = height; }

    /**
     * Updates the display image of the entity
     * 
     * @param image the new {@code Image} of this entity
     */
    public void setImage(Image image) { 
        this.image = image;
        setSize(image.getWidth(null), image.getHeight(null));
    }

    /**
     * Sets the size of the entity to the specified dimensions. The entity's image is also scaled
     * to match the new size (if it has an image).
     * 
     * @param width  the width of the entity
     * @param height the height of the entity
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        if (image == null) { return; }
        if (image.getWidth(null) == width && image.getHeight(null) == height) { return; }
        height = (int) ((double) image.getHeight(null) / image.getWidth(null) * height);
        this.image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
    
    /**
     * Sets the size of the entity to (size x size). The entity's image is also scaled to match the
     * new size (if it has an image).
     * 
     * @param size the pixel size ({@code int}) to apply onto the entity
     */
    public void setSize(int size) {
        setSize(size, size);
    }

    /**
     * Sets the size of the entity to the specified dimensions. The entity's image is also scaled
     * to match the new size (if it has an image).
     * 
     * @param size the {@code Dimension} to apply onto the entity
     */
    public void setSize(Dimension size) {
        setSize(size.width, size.height);
    }


    /* Methods */

    /**
     * Draws the entity onto the specified graphics context at the given x and y coordinates
     * 
     * @param g2d the graphics context to draw the entity onto
     * @param x   the x-coordinate to draw the entity at
     * @param y   the y-coordinate to draw the entity at
     */
    public abstract void draw(Graphics2D g2d, int x, int y);

    /**
     * Checks for entity collision between this entity and the specified entity
     * 
     * @param entity the entity to check for collision with
     * @return       {@code true} if the entities are colliding, {@code false} otherwise
     */
    public boolean collidesWith(Entity entity) {
        if (this == entity) return false;
        return this.getBoundingRectangle().intersects(entity.getBoundingRectangle());
    }
}
