package Entity;
/**
 * Player.java
 * Extended from Game Programming lab content
 */

import java.awt.Image;
import java.awt.Point;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import Game.*;
import Managers.*;
import Tile.*;


public class Player extends MovingEntity {			

    private JPanel panel;
    private TileMap tileMap;

    /** Player's x coordinate in pixels */
    private int x;
    /** Player's y coordinate in pixels */
    private int y;

    private boolean jumping;
    private int timeElapsed;

    private boolean facingLeft;

    private boolean falling;
    private int initialVelocity;

    private Animation idleAnimation;
    private Animation walkAnimation;
    private Animation standAnimation;
    private Animation turnLeftAnimation;
    private Animation turnRightAnimation;
    private Animation jumpAnimation;

    private final int jumpHeight = 30;
    private final int frameDuration = 50;


    public Player (JPanel panel, TileMap t) {
        super(0, 0, 0, 0);
        this.panel = panel;
        tileMap = t;
        facingLeft = falling = jumping = false;
        setImage(ImageManager.getImage("player_idle_1"));
        setSize(62);
        setDX(2);
        setDY(5);
        loadPlayerAnimations();
    }

    private void loadPlayerAnimations() {
        String key = "player_idle_";
        int idx = 1;

        // Load idle animation
        idleAnimation = new Animation(false);
        int[] frameNums = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5};
        for (int frameNum : frameNums) {
            Image img = ImageManager.getImage(key + frameNum);
            idleAnimation.addFrame(img, frameDuration);
        }
        System.out.println("[PLAYER] Loaded idle animation");

        // Load stand animation
        key = "player_idle_";
        frameNums = new int[]{4, 3, 2, 1};
        standAnimation = new Animation(false);
        for (int frameNum : frameNums) {
            Image img = ImageManager.getImage(key + frameNum);
            standAnimation.addFrame(img, frameDuration);
        }
        System.out.println("[PLAYER] Loaded stand animation");
        
        // Load walk animation
        key = "player_walk_";
        idx = 1;
        walkAnimation = new Animation(false);
        while (true) {
            Image img = ImageManager.getImage(key + idx);
            if (img == null) break;
            walkAnimation.addFrame(img, frameDuration * 1);
            idx++;
        }
        System.out.println("[PLAYER] Loaded walk animation");
        
        // Load turn left animation
        key = "player_turn_";
        idx = 1;
        turnLeftAnimation = new Animation(false);
        while (true) {
            Image img = ImageManager.getImage(key + idx);
            if (img == null) break;
            turnLeftAnimation.addFrame(img, frameDuration * 2);
            idx++;
        }
        System.out.println("[PLAYER] Loaded turn left animation");

        // Load turn right animation
        key = "player_turn_";
        idx = turnLeftAnimation.getNumFrames();
        turnRightAnimation = new Animation(false);
        while (true) {
            Image img = ImageManager.getImage(key + idx);
            if (img == null) break;
            turnRightAnimation.addFrame(img, frameDuration * 2);
            idx--;
        }
        System.out.println("[PLAYER] Loaded turn right animation");

        // Load jump animation
        key = "player_jump_";
        idx = 1;
        jumpAnimation = new Animation(false);
        while (true) {
            Image img = ImageManager.getImage(key + idx);
            if (img == null) break;
            jumpAnimation.addFrame(img, frameDuration);
            idx++;
        }
        System.out.println("[PLAYER] Loaded jump animation");
    }

    public synchronized void move (Movement direction) {

        if (!panel.isVisible ()) return;
        if (turnLeftAnimation.isStillActive() || turnRightAnimation.isStillActive()) return;
        
        // Handle player movement
        switch (direction) {

            case LEFT:
                if (!facingLeft) {
                    doTurnAroundAnimation();
                    return;
                }
                System.out.println ("[PLAYER] Moved left: " + getX());
                setX(x - getDX());
                break;

            case RIGHT:
                if (facingLeft) {
                    doTurnAroundAnimation();
                    return;
                }
                System.out.println ("[PLAYER] Moved right: " + getX());
                setX(x + getDX());
                if (x + width > tileMap.getWidthPixels()) {
                    setX(tileMap.getWidthPixels() - width);
                    return;
                }
                break;

            case JUMP:
                System.out.println ("[PLAYER] Jumped: " + getX());
                if (!jumping) {
                    jump();
                    return;
                }
                break;

            default:
                System.out.println ("[PLAYER] Unknown direction: " + direction);
                return;
        }
        doWalkAnimation();
        if (isInAir()) { fall(); }
    }

    private void doWalkAnimation() {
        if (!panel.isVisible ()) return;
        if (walkAnimation.isStillActive()) return;
        walkAnimation.start();
        System.out.println("[PLAYER] Walk animation started");
    }

    private void doTurnAroundAnimation() {
        if (!panel.isVisible ()) return;
        if (turnLeftAnimation.isStillActive() || turnRightAnimation.isStillActive()) return;
        facingLeft = !facingLeft;

        if (facingLeft) { turnLeftAnimation.start(); } 
        else { turnRightAnimation.start(); }
        System.out.println("[PLAYER] Turn animation started");
    }

    private void doJumpAnimation() {
        if (!panel.isVisible ()) return;
        if (turnLeftAnimation.isStillActive() || turnRightAnimation.isStillActive()) return;
        jumpAnimation.start();
        System.out.println("[PLAYER] Jump animation started");
    }

    public boolean isInAir() {
        if (jumping || falling) { return true; }
        if (tileMap.collidesWithTile(x, y + this.height + 1) != null) return true;
        return false;
    }

    private void fall() {
        if (!panel.isVisible ()) return;
        jumping = false;
        falling = true;
        timeElapsed = 0;
        initialVelocity = 0;
    }

    public void jump () {  
        if (!panel.isVisible ()) return;
        jumping = true;
        falling = false;
        initialVelocity = 70;
        doJumpAnimation();
    }

    @Override
    public void update () {
        timeElapsed++;
        if (!panel.isVisible ()) { return; }
        
        int distance = (int) (initialVelocity * timeElapsed - 4.9 * Math.pow(timeElapsed, 2));

        // Update all active animations
        if (turnLeftAnimation.isStillActive())  { turnLeftAnimation.update(); }
        if (turnRightAnimation.isStillActive()) { turnRightAnimation.update(); }
        if (walkAnimation.isStillActive())      { walkAnimation.update(); }
        if (jumpAnimation.isStillActive())      { jumpAnimation.update(); }

        // Achieved max trajectory height, start falling
        if (jumping && (getY() - distance) >= jumpHeight) {
            fall();
            jumping = false;
        }

        if (jumping) {
            Point tilePos = tileMap.collidesWithTileUp(this, getX(), getY() - getDY());	
            
            // handle collision with tile above player
            if (tilePos == null) {
                setY(getY() - getDY());
                System.out.println ("[PLAYER] Jumping: No collision (" + getY() + ")");
            
            } else {
                System.out.println ("[PLAYER] Jumping: Collision with tile at (" + tilePos.getX() + ", " + tilePos.getY() + ")");
                int offsetY = tileMap.getOffsetY();
                int topTileY = ((int) tilePos.getY()) * GamePanel.TILE_SIZE + offsetY;
                int bottomTileY = topTileY + GamePanel.TILE_SIZE;
                setY(bottomTileY);
                fall();
                jumping = false;
            }
        }
        
        if (falling) {
            Point tilePos = tileMap.collidesWithTileDown(this, getX(), getY() + getDY());	
            
            // handle collision with tile below player
            if (tilePos == null) {
                setY(getY() + getDY());
                System.out.println ("[PLAYER] Falling: No collision (" + getY() + ")");
            
            } else {
                System.out.println ("[PLAYER] Falling: Collision with tile at (" + tilePos.getX() + ", " + tilePos.getY() + ")");
                // int offsetY = tileMap.getOffsetY();
                int topTileY = ((int) tilePos.getY()) - getHeight();
                setY(topTileY - this.height);
                falling = false;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y) {
        if (!panel.isVisible ()) { return; }

        Image img = facingLeft ? ImageManager.flipImageHorizontally(idleAnimation.getImage()) : idleAnimation.getImage();

        if (turnLeftAnimation.isStillActive())  { img = turnLeftAnimation.getImage(); }
        if (turnRightAnimation.isStillActive()) { img = turnRightAnimation.getImage(); }
        if (jumpAnimation.isStillActive())      { img = jumpAnimation.getImage(); }
        if (walkAnimation.isStillActive())      { 
            img = facingLeft ? ImageManager.flipImageHorizontally(walkAnimation.getImage()) : walkAnimation.getImage(); 
        }

        x -= img.getWidth(null)/2;

        g2d.drawImage(img, x, y, img.getWidth(null), img.getHeight(null), null);
    }
}