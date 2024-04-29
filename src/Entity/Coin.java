package Entity;

import Tile.Tile;

public class Coin extends Collectible{
    private int numCoins;

    public Coin(Tile tile, Player player) {
        super("coin", tile.getX(), tile.getY(), 40, 40, player);
        this.numCoins = 0;
    }
    
    public Coin(int x, int y, int width, int height, Player player) {
        super("coin", x, y, width, height, player);
        this.numCoins = 0;
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
