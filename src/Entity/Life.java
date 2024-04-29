package Entity;

import Tile.Tile;

public class Life extends Collectible{
    private int numLives;

    public Life(Tile tile, Player player) {
        super("life", tile.getX(), tile.getY(), 30, 30, player);
        this.numLives = 3;
    }

    @Override
    public void collect() {
        if (this.numLives < 3)
            this.numLives = this.numLives + 1;
    }

    public int getLives() {
        return this.numLives;
    }

    public void resetLives() {
        this.numLives = 3;
    }
    
    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "Life{loc=(" + getX() + ", " + getY() +  "), dX=" + getDX() + ", dY=" + 
            getDY() + ", dimensions=" + getWidth() + "x" + getHeight() + '}';
    }
}
