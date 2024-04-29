package Entity;

import java.awt.Graphics2D;

import Tile.Tile;

public class Key extends Collectible{
    private int numKeys;
    private String keyName;
    private boolean visible;
    
    public Key(Tile tile, Player player) {
        super("key", tile.getX(), tile.getY(), 30, 30, player);
        this.numKeys = 0;
        this.keyName = tile.getName();
        this.visible = true;
    }

    @Override
    public void collect() {
        this.numKeys++;
    }

    public int getNumKeys() {
        return this.numKeys;
    }

    public void resetNumKeys() {
        this.numKeys = 0;
    }

    public String getKeyName() {
        return this.keyName;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    @Override
    public void draw(Graphics2D g2d, int x, int y) {
        if (!visible) { return; }
        super.draw(g2d, x, y);
    }

    @Override
    public boolean collidesWithPlayer() {
        if (!visible) { return false; }
        return super.collidesWithPlayer();
    }

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "Key{loc=(" + getX() + ", " + getY() +  "), dX=" + getDX() + ", dY=" + 
            getDY() + ", dimensions=" + getWidth() + "x" + getHeight() + '}';
    }
}
