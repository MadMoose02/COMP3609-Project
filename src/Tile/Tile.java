package Tile;

import java.awt.Image;
import java.awt.Point;
import java.awt.Graphics2D;

/**
 * Tile.java <hr>
 * The Tile class contains the data for a single tile in the tile-based map. It contains the tile's
 * image, name, and whether it is a solid Tile (i.e. has Tile collision) or not.
 */
public class Tile {
    
    private String name;
    private Image image;
    private boolean isSolid;
    private Point position;

    public Tile(Image image, String name, boolean isSolid) {
        setImage(image);
        position = new Point(0, 0);
        this.name = name;
        this.isSolid = isSolid;
    }


    /** Accessors */

    public boolean isSolid() { return isSolid; }

    public Image getImage() { return image; }
    
    public String getName() { return name; }

    public Point getPosition() { return position; }

    public int getX() { return (int) position.getX(); }

    public int getY() { return (int) position.getY(); }

    public Tile clone() { return new Tile(image, name, isSolid); }


    /** Mutators */

    public void setImage(Image image) { this.image = image; }

    public void setName(String name) { this.name = name; }

    public void setPosition(Point position) { this.position = position; }

    public void setPosition(int x, int y) { this.position = new Point(x, y); }

    public void setIsSolid(boolean isSolid) { this.isSolid = isSolid; }


    /** Methods */

    public void draw(Graphics2D g2d, int x, int y) {
        g2d.drawImage(image, x, y, image.getWidth(null), image.getHeight(null), null);
    }
}
