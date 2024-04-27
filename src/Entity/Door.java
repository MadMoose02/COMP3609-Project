package Entity;

import java.awt.Graphics2D;
import java.awt.Image;

public class Door extends Entity{
    private Player player;
    private Image spriteImage;
    private String doorName;

    public Door(String doorName, int x, int y, int width, int height, Player player) {
        super(x, y, width, height);
        this.doorName = doorName;
        this.player = player;
    }

    @Override
    public void draw (Graphics2D g2d, int x, int y) {
		g2d.drawImage(spriteImage, x, y, getWidth(), getHeight(), null);
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
    
}
