package Entity;

import java.awt.Graphics2D;

import Game.GamePanel;
import Tile.Tile;

public class Door extends Entity {
    private Player player;
    private String doorName;

    public Door(Tile tile, Player player) {
        super(tile.getX(), tile.getY(), GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        this.player = player;
        this.doorName = tile.getName();
        setImage(tile.getImage());
        setSize(GamePanel.TILE_SIZE);
    }

    @Override
    public void draw (Graphics2D g2d, int x, int y) {
		g2d.drawImage(getImage(), x, y, getWidth(), getHeight(), null);
	}

    public boolean collidesWithPlayer () {
		if (this.collidesWith(player)) {
            System.out.println("Collided with player");
            return true;
        }
        return false;
	}

    public boolean unlockDoor(Key key) {
        if (key.getKeyName().equals(this.doorName)) { return true;}
        else return false;
    }
    
    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "Door{loc=(" + getX() + ", " + getY() +  "), dX=" + getDX() + ", dY=" + 
            getDY() + ", dimensions=" + getWidth() + "x" + getHeight() + '}';
    }
}
