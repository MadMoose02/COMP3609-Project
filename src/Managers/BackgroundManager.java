package Managers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

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

  	private ArrayList<Background> backgrounds;
    private JPanel panel;

  	public BackgroundManager(JPanel panel) {
        backgrounds = new ArrayList<>();
        this.panel = panel;
  	}

    public int getBackgroundCount() { return backgrounds.size(); }

    public void addBackground(Image image, int moveSize) { 
        backgrounds.add(new Background(panel, image, moveSize));

    }

  	public void moveRight() {
        if (backgrounds == null || backgrounds.size() == 0) { return; }
		for (Background bg: backgrounds) { bg.moveRight(); }
  	}

  	public void moveLeft() {
        if (backgrounds == null || backgrounds.size() == 0) { return; }
		for (Background bg: backgrounds) { bg.moveLeft(); }
  	}

  	/**
     * The draw method draws the backgrounds on the screen. The backgrounds are drawn from the 
     * back to the front.
     * 
     * @param g2 Graphics context to draw on
     */
  	public void draw (Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        if (backgrounds == null || backgrounds.size() == 0) { return; }
		for (Background bg: backgrounds) { bg.draw(g2); }
  	}

}

