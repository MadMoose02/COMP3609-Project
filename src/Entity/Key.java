package Entity;

import Tile.Tile;

public class Key extends Collectible{
    private int numKeys;
    private String keyName;
    
    public Key(Tile tile, Player player) {
        super("key", tile.getX(), tile.getY(), 30, 30, player);
        this.numKeys = 0;
        this.keyName = tile.getName();
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
