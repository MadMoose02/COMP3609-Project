package Entity;

import java.awt.Graphics2D;

import Tile.Tile;

public class Coin extends Collectible{
    private int numCoins;
    private boolean visible;

    public Coin(int x, int y, int width, int height, Player player) {
        super("coin", x, y, width, height, player);
        this.numCoins = 0;
        this.visible = true;
    }
    
    public Coin(Tile tile, Player player) {
        this(tile.getX(), tile.getY(), 40, 40, player);
    }
    
    @Override
    public void collect() {
        this.numCoins++;
    }

    public int getNumCoins() {
        return this.numCoins;
    }

    public void resetNumCoins() {
        this.numCoins = 0;
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
        return "Coin{loc=(" + getX() + ", " + getY() +  "), dX=" + getDX() + ", dY=" + 
            getDY() + ", dimensions=" + getWidth() + "x" + getHeight() + '}';
    }
}
