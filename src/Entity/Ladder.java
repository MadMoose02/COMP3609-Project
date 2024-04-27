package Entity;

import java.awt.Graphics2D;
import java.awt.Image;

import Managers.ImageManager;

public class Ladder extends Entity{
    private Player player;
    private Image spriteImage;

    public Ladder(int x, int y, int width, int height, Player player) {
        super(x, y, width, height);
        this.player = player;

        setImage(ImageManager.getImage("ladder"));
        setSize(40, 40);
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y) {
        g2d.drawImage(spriteImage, x, y, getWidth(), getHeight(), null);
    }

    public boolean collidesWithPlayer () {
		if (this.collidesWith(player)) {
            System.out.println("Collided with player");
            return true;
        }
        return false;
	}   
    
}