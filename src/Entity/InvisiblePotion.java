package Entity;

import Tile.Tile;

public class InvisiblePotion extends Collectible{
    private int numPotions;

    public InvisiblePotion(Tile tile, Player player) {
        super("potion", tile.getX(), tile.getY(), 30, 30, player);
        this.numPotions = 0;
    }

    @Override
    public void collect() {
        numPotions++;
    }

    public int getNumPotions() {
        return this.numPotions;
    }

    public void resetNumPotions() {
        this.numPotions = 0;
    }
    
    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "Potion{loc=(" + getX() + ", " + getY() +  "), dX=" + getDX() + ", dY=" + 
            getDY() + ", dimensions=" + getWidth() + "x" + getHeight() + '}';
    }
}
