package Entity;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

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

    public Point getPosition() { return position; }

    public int getX() { return (int) position.getX(); }

    public int getY() { return (int) position.getY(); }

    public int getDX() { return dX; }

    public int getDY() { return dY; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public Image getImage() { return image; }

    public Dimension getSize() { return new Dimension(width, height); }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(position.getX(), position.getY(), width, height);
    }


    /* Mutators */

    public void setPosition(Point position) { this.position = position; }
    
    public void setPosition(int x, int y) { this.position = new Point(x, y); }
    
    public void setX(int x) { position.x += (position.x + x < 0) ? 0 : x; }

    public void setY(int y) { position.y += (position.y + y < 0) ? 0 : y; }

    public void setDX(int dX) { this.dX = dX; }

    public void setDY(int dY) { this.dY = dY; }

    public void setWidth(int width) { this.width = width; }

    public void setHeight(int height) { this.height = height; }

    public void setImage(Image image) { this.image = image; }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        height = (int) ((double) image.getHeight(null) / image.getWidth(null) * height);
        this.image = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    }
    
    public void setSize(int size) {
        setSize(size, size);
    }

    public void setSize(Dimension size) {
        setSize(size.width, size.height);
    }


    /* Methods */

    public abstract void draw(Graphics2D g2d, int x, int y);

    public boolean collidesWith(Entity entity) {
        if (this == entity) return false;
        return this.getBoundingRectangle().intersects(entity.getBoundingRectangle());
    }
}
