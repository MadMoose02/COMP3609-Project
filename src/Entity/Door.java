package Entity;

import java.awt.Graphics2D;

import Game.GamePanel;
import Tile.Tile;

public class Door extends Entity {
    private Player player;
    private String doorName;
    private boolean visible;

    public Door(Tile tile, Player player) {
        super(tile.getX(), tile.getY(), GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        this.player = player;
        this.doorName = tile.getName();
        this.visible = true;
        setImage(tile.getImage());
        setSize(GamePanel.TILE_SIZE);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void draw (Graphics2D g2d, int x, int y) {
        if (!visible) { return; }
        g2d.drawImage(getImage(), x, y, getWidth(), getHeight(), null);
	}

    public boolean collidesWithPlayer () {
        if (!visible) { return false; }
		if (this.collidesWith(player)) { return true; }
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
