package Managers;

import java.awt.Graphics2D;
import javax.swing.JPanel;

import Game.Background;


/**
 * BackgroundManager.java <hr>
 * BackgroundManager manages many backgrounds (wraparound images used for the game's background). 
 *
 * Backgrounds 'further back' move slower than ones nearer the foreground of the game, 
 * creating a parallax distance effect.
 *
 * When a sprite is instructed to move left or right, the sprite doesn't actually move, instead the 
 * backgrounds move in the opposite direction (right or left).
 */
public class BackgroundManager {

	private String bgImages[] = {
        "layer_08",
        "layer_07",
        "layer_06",
        "layer_05",
        "layer_04",
        "layer_03",
        "layer_02",
        "layer_01"
    };

    // pixel amounts to move each background left or right
    // a move amount of 0 makes a background stationary
  	private int moveAmount[] = {1, 2, 3, 4, 4, 4, 5, 10};

  	private Background[] backgrounds;
  	private int numBackgrounds;

  	public BackgroundManager(JPanel panel, int moveSize) {
        numBackgrounds = bgImages.length;
        backgrounds = new Background[numBackgrounds];
        for (int i = 0; i < numBackgrounds; i++) {
            backgrounds[i] = new Background(panel, "layers_" + bgImages[i], moveAmount[i]);
        }
        System.out.println ("[BACKGROUND MANAGER] Loaded " + numBackgrounds + " backgrounds");
  	} 

  	public void moveRight() { 
		for (int i=0; i < numBackgrounds; i++) { backgrounds[i].moveRight(); }
  	}

  	public void moveLeft() {
		for (int i=0; i < numBackgrounds; i++) { backgrounds[i].moveLeft(); }
  	}

  	/**
     * The draw method draws the backgrounds on the screen. The backgrounds are drawn from the 
     * back to the front.
     * 
     * @param g2 Graphics context to draw on
     */

  	public void draw (Graphics2D g2) { 
		for (int i=0; i < numBackgrounds; i++) { backgrounds[i].draw(g2); }
  	}

}

