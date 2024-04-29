package Entity;

import java.awt.Graphics2D;
import java.awt.Image;

import Game.GamePanel;
import Tile.Tile;

public class DangerObject extends Entity{
    private Image spriteImage;
    private Player player;

    public DangerObject(Tile tile, Player player) {
        super(tile.getX(), tile.getY(), GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        this.player = player;   

        setImage(tile.getImage());
        setSize(GamePanel.TILE_SIZE);
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y) {
        g2d.drawImage(spriteImage, x, y, getWidth(), getHeight(), null);
    }

    public boolean collidesWithPlayer () {
		if (this.collidesWith(player)) { return true; }
        return false;
	}
    
}


