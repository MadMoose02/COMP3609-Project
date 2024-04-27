package Entity;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.Image;

import Managers.ImageManager;


public class Heart extends Entity {

	private Player player;

	private Image spriteImage;

	int time, timeChange;
	boolean originalImage, grayImage;


	public Heart (JPanel panel, Player player) {
        super(0, 0, 0, 0);
		this.player = player;

		time = 0; // range is 0 to 10
		timeChange = 1; // set to 1
		originalImage = true;
		grayImage = false;
		setImage(ImageManager.getImage("heart"));
        setSize(40, 40);
	}


    @Override
	public void draw (Graphics2D g2) {
		g2.drawImage(spriteImage, getX(), getY(), getWidth(), getHeight(), null);
	}


	public boolean collidesWithPlayer () {
		if (this.collidesWith(player)) {
            System.out.println("Collided with player");
            return true;
        }

        return false;
	}
}