package Game;


import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import Tile.*;
import Entity.*;
import Managers.*;

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
	private TileMapManager tileManager;
	private TileMap	tileMap;

    private Movement lastMovement;
	private boolean levelChange;
	private int level;
	private boolean gameOver;

	public GamePanel () {
        ImageManager.getInstance();
        SoundManager.getInstance();
        SaveDataManager.getInstance();
		isRunning = isPaused = false;
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
        double deltaTime = 0;

		try {
			isRunning = true;
			while (isRunning) {
                deltaTime = Math.max(nextDrawTime - System.currentTimeMillis(), 0);
                if (!isPaused && !gameOver) { gameUpdate(); }
                gameRender();
                Thread.sleep((int) (deltaTime));
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
				System.out.println ("[GAMEPANEL] Tilemap Size: " + 
                    tileMap.getWidth() + " x " + tileMap.getHeight());
			}
			catch (Exception e) {
				e.printStackTrace();
                System.exit(0);
			}

			gameThread = new Thread(this);
			gameThread.start();

		}
	}


	public void pauseGame() {
		if (isRunning) {
			isPaused = !isPaused;
		}
	}


	public void endGame() {
		isRunning = false;
		SoundManager.stopMusicClip("background");
	}

	public void endLevel() {
		level += 1;
		levelChange = true;
	}


    public void handleKeyInput(int keyCode) {
        if (gameOver) { return; }
        Movement movement = Movement.INVALID;
        switch (keyCode) {

            // Begin crouching or climb ladder
            case KeyEvent.VK_DOWN:
                movement = Movement.CROUCH;
                tileMap.crouch();
                break;

            // Stop crouching or climb ladder
            case KeyEvent.VK_UP:
                movement = Movement.STAND;
                tileMap.stand();
                break;

            // Jump
            case KeyEvent.VK_SPACE:
                movement = Movement.JUMP;    
                tileMap.jump();
                break;
            
            // Move left
            case KeyEvent.VK_LEFT:
                movement = Movement.LEFT;
                tileMap.moveLeft();
                break;
            
            // Move right
            case KeyEvent.VK_RIGHT:
                movement = Movement.RIGHT;
                tileMap.moveRight();
                break;

            default:
                break;
        }
        
        if (movement != lastMovement) {
            System.out.println ("[GAMEPANEL] Action: " + movement.toString());
            lastMovement = movement;
        }
    }

}