package Game;

import javax.swing.JPanel;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import Managers.*;
import Tile.*;
import Entity.*;

/**
   A component that displays all the game entities
*/

public class GamePanel extends JPanel implements Runnable {

    public static final int TILE_SIZE = 64;
    private final int FPS = 60;
	private boolean isRunning;
	private boolean isPaused;

	private Thread gameThread;

	private BufferedImage image;

    public Player player;

	private BirdAnimation animation;
	private volatile boolean isAnimShown;

	private TileMapManager tileManager;
	private TileMap	tileMap;

	private boolean levelChange;
	private int level;
	private boolean gameOver;

	public GamePanel () {
        ImageManager.getInstance();
        SoundManager.getInstance();
        SaveDataManager.getInstance();
		isRunning = isPaused = isAnimShown = false;
		image = new BufferedImage (600, 500, BufferedImage.TYPE_INT_RGB);
		level = 1;
		levelChange = false;
	}


	public void createGameEntities() {
        tileManager = new TileMapManager(this);
	}


	public void run () {
        double targetTime = 1000 / FPS;
        double nextDrawTime = System.currentTimeMillis() + targetTime;

		try {
			isRunning = true;
			while (isRunning) {
				if (!isPaused && !gameOver) { gameUpdate(); }
				gameRender();
                if (System.currentTimeMillis() < nextDrawTime) {
                    Thread.sleep((int)(nextDrawTime - System.currentTimeMillis()));
                }
			}
		}
		catch(InterruptedException e) {}
	}


	public void gameUpdate() {

		tileMap.update();

		if (levelChange) {
			levelChange = false;

			try {
				String filename = "map" + level + ".txt";
				tileMap = tileManager.loadMap(filename) ;
				int w, h;
				w = tileMap.getWidth();
				h = tileMap.getHeight();
				System.out.println ("[GAMEPANEL] Changing level to Level " + level);
				System.out.println ("[GAMEPANEL] Width of tilemap " + w);
				System.out.println ("[GAMEPANEL] Height of tilemap " + h);
			
            } catch (Exception e) {		// no more maps: terminate game
				gameOver = true;
				System.out.println(e);
				System.out.println("[GAMEPANEL] Game Over"); 
				return;
			}

			createGameEntities();
			return;
		}
	}


	public void gameRender() {
		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		tileMap.draw (imageContext);

		if (isAnimShown) animation.draw(imageContext);

		if (gameOver) {
			Color darken = new Color (0, 0, 0, 125);
			imageContext.setColor (darken);
			imageContext.fill (new Rectangle2D.Double (0, 0, 600, 500));
		}

		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.drawImage(image, 0, 0, 600, 500, null);
		imageContext.dispose();
	}


	public void startNewGame() {
		if (gameThread != null || !isRunning) {

			endGame();
			gameOver = false;
			level = 1;

			try {
                createGameEntities();
				tileMap = tileManager.loadMap("map1.txt");
				int w, h;
				w = tileMap.getWidth();
				h = tileMap.getHeight();
				System.out.println ("[GAMEPANEL] Tilemap Size: " + w + " x " + h);
			}
			catch (Exception e) {
				System.out.println(e);
                System.exit(0);
			}


			gameThread = new Thread(this);
			gameThread.start();			

		}
	}


	public void pauseGame() {
		if (isRunning) {
			isPaused = !isPaused;

			if (isAnimShown) {
				if (isPaused) { animation.stopSound(); }
				else { animation.playSound(); }
			}
		}
	}


	public void endGame() {
		isRunning = false;
		SoundManager.stopMusicClip("background");
	}

	
	public void moveLeft() {
		if (!gameOver) { tileMap.moveLeft(); }
	}


	public void moveRight() {
		if (!gameOver) { tileMap.moveRight(); }
	}


	public void jump() {
		if (!gameOver) { tileMap.jump(); }
	}

	
	public void showAnimation() {
		isAnimShown = true;
		animation.start();
	}


	public void endLevel() {
		level += 1;
		levelChange = true;
	}

}