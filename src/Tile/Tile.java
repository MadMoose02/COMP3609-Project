package Tile;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

public class Tile {
    
    private Image image;
    private int width;
    private int height;
    private boolean isSolid;
    private Point position;

    public Tile(Image image, int size, boolean isSolid) {
        this.image = image;
        this.width = this.height = size;
        this.isSolid = isSolid;
    }


    /** Accessors */

    public boolean isSolid() { return isSolid; }

    public Image getImage() { return image; }

    public int getSize() { return width; }

    public Point getPosition() { return position; }

    public int getX() { return (int) position.getX(); }

    public int getY() { return (int) position.getY(); }


    /** Mutators */

    public void setImage(Image image) { this.image = image; }

    public void setPosition(Point position) { this.position = position; }

    public void setPosition(int x, int y) { this.position = new Point(x, y); }


    /** Methods */

    public void draw(Graphics2D g2d, int x, int y) {
        g2d.drawImage(image, x, y, width, height, null);
    }
}
