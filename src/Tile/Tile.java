package Tile;

import java.awt.Image;
import java.awt.Point;
import java.awt.Graphics2D;

public class Tile {
    
    private Image image;
    private boolean isSolid;
    private Point position;

    public Tile(Image image, boolean isSolid) {
        setImage(image);
        position = new Point(0, 0);
        this.isSolid = isSolid;
    }


    /** Accessors */

    public boolean isSolid() { return isSolid; }

    public Image getImage() { return image; }

    public Point getPosition() { return position; }

    public int getX() { return (int) position.getX(); }

    public int getY() { return (int) position.getY(); }

    public Tile clone() { return new Tile(image, isSolid); }


    /** Mutators */

    public void setImage(Image image) { this.image = image; }

    public void setPosition(Point position) { this.position = position; }

    public void setPosition(int x, int y) { this.position = new Point(x, y); }

    public void setIsSolid(boolean isSolid) { this.isSolid = isSolid; }


    /** Methods */

    public void draw(Graphics2D g2d, int x, int y) {
        g2d.drawImage(image, x, y, image.getWidth(null), image.getHeight(null), null);
    }
}
