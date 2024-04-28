package Entity;

import java.awt.Graphics2D;
import java.awt.Image;
import Managers.ImageManager;


public abstract class Collectible extends Entity{
    private Player player;
	private Image spriteImage;
    private int width, height;

    public Collectible(String imgName, int x, int y, int width, int height, Player player) {
        super(0, 0, 0, 0);
		this.player = player;
        this.width = width;
        this.height = height;
		
        setImage(ImageManager.getImage(imgName));
        setSize(40, 40);
    }

    @Override
	public void draw (Graphics2D g2d, int x, int y) {
		g2d.drawImage(spriteImage, x, y, getWidth(), getHeight(), null);
	}

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }


	public boolean collidesWithPlayer () {
		if (this.collidesWith(player)) {
            System.out.println("Collided with player");
            return true;
        }

        return false;
	}
    
    public abstract void collect();
    
}
