package Entity;
/**
 * Player.java
 * Extended from Game Programming lab content
 */

import java.awt.Image;
import java.util.HashMap;
import java.awt.Color;
import java.awt.Graphics2D;

import Game.*;
import Tile.*;
import Managers.*;
import Animation.*;


public class Player extends MovingEntity {			

    /** Reference to the TileMap */
    private TileMap tileMap;
    /** Time elapsed since start of jump or fall */
    private double timeElapsed;
    /** Time at which jump or fall started */
    private double timeStarted;
    /** Initial velocity when falling */
    private double initialVelocity;
    /** Height at which player started to fall */
    private int fallHeightStart;
    /** Height at which player started to jump */
    private int jumpHeightStart;
    /** True if player is facing left, false if facing right */
    private boolean facingLeft;
    /** True if player is crouching, false if standing */
    private boolean crouching;
    private boolean falling;
    private boolean jumping;
    /** Collection of all player animations */
    private HashMap<String, Animation> animations;


    public Player() {
        super(0, 0, 0, 0);
        tileMap = null;
        facingLeft = crouching = false;
        initialVelocity = timeElapsed = 0;
        setSize(60);
        setDX(5);
        setDY(1);
        loadPlayerAnimations();
        setImage(ImageManager.getImage("player_idle_right_1"));
    }

    public void setTileMap(TileMap tileMap) { this.tileMap = tileMap; }

    @Override
    public synchronized void move(Movement direction) {
        if (isTurning()) { return; }
        switch (direction) {

            case LEFT:
                moveLeft();
                break;

            case RIGHT:
                moveRight();
                break;

            case JUMP:
                jump();
                break;

            case CROUCH:
                crouch();
                break;

            case STAND:
                stand();
                break;

            default:
                System.out.println ("[PLAYER] Unknown direction: " + direction);
                return;
        }
    }

    @Override
    public void update() {
        
        // Update all active animations
        for (Animation anim : animations.values()) { anim.update(); }
        
        // If the player is in freefall (in air but not jumping), fall down
        if (isInAir()) { fall(); } else { 
            jumping = falling = false; 
            if (!isCrouching()) stand();
        }
        
        // Calculate the distance travelled since the last update
        timeElapsed = (System.currentTimeMillis() - timeStarted)/1000;
        int distance = (int) ((initialVelocity * timeElapsed) + (4.9 * timeElapsed * timeElapsed));
        initialVelocity += 0.5 * timeElapsed;
        
        // Update position based on trajectory
        if (isJumping()) {
            initialVelocity -= 4.9 * timeElapsed;
            setY(jumpHeightStart - distance);
            setX(getX() + (facingLeft ? -(getDX()) : getDX()) * 0.15);
        }
        
        if (isFalling()) {
            initialVelocity += (9.8 * timeElapsed >= 60) ? 60 : 9.8 * timeElapsed;
            setY(fallHeightStart + distance);
        } 
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y) {
        Image img = getImage();
        for (Animation anim : animations.values()) {
            if (anim.isStillActive()) { img = anim.getImage(); }
        }

        // draw player's current image
        g2d.drawImage(
            img, x, y,
            img.getWidth(null), 
            img.getHeight(null), 
            null
        );

        // draw player hitbox with coordinates
        g2d.setColor(Color.RED);
        g2d.drawString("(" + x + ", " + y + ")", x, y - 22);
        g2d.setColor(Color.WHITE);
        g2d.drawString("(" + getX() + ", " + getY() + ")", x, y - 10);
        g2d.drawRect(x, y, getWidth(), getHeight());
    }

    private void loadPlayerAnimations() {
        String[] animationNames = new String[]{
            "idle_left",   "idle_right",
            "walk_left",   "walk_right",
            "turn_left",   "turn_right",
            "jump_left",   "jump_right",
            "crouch_left", "crouch_right",
            "fall_left",   "fall_right",
            "climb_up",    "climb_down"
        };
        animations = new HashMap<String, Animation>();
        for (String name : animationNames) {
            Animation anim = new PlayerAnimation("player_" + name, 5000);
            animations.put(name, anim);
            System.out.println("[PLAYER] Loaded animation: " + anim.getName());
        }
    }

    private boolean isCrouching() { return crouching; }

    private boolean isJumping() { return jumping; }
    
    private boolean isFalling() { return falling; }
    
    private boolean isWalking() { 
        return animations.get("walk_left").isStillActive() ||
                animations.get("walk_right").isStillActive() ||
                animations.get("crouch_left").isStillActive() ||
                animations.get("crouch_right").isStillActive();
    }

    private boolean isTurning() { 
        return animations.get("turn_left").isStillActive() || 
                animations.get("turn_right").isStillActive(); 
    }

    /**
     * Checks if the player is in the air. At least half of the player's width must not
     * collide with a tile to be considered in the air.
     * 
     * @return {@code true} if the player is in the air, {@code false} otherwise
     */
    public boolean isInAir() {
        int x = getX() + tileMap.getTileMapOffsetX() + (getWidth()/2);
        int y = getY() + tileMap.getTileMapOffsetY() + getHeight();
        if (tileMap.collidesWithTileCoords(x, y) == null) { return true; }
        return false;
    }
    
    private void turnAround() {
        if (isTurning() || isCrouching()) { return; }
        facingLeft = !facingLeft;
        if (isInAir()) { return; }
        animations.get((facingLeft) ? "turn_left" : "turn_right").start(); 
        setImage(ImageManager.getImage((facingLeft) ? 
            (crouching ? "player_crouch_left_1" : "player_idle_left_1") : 
            (crouching ? "player_crouch_right_1" : "player_idle_right_1")
        ));
    }
    
    private void doWalkAnimation() {
        if (isWalking() || isInAir()) { return; }
        Animation walk;
        if (crouching) { walk = animations.get((facingLeft) ? "crouch_left" : "crouch_right"); }
        else { walk = animations.get((facingLeft) ? "walk_left" : "walk_right"); }
        
        // Boundary checks before starting animation
        if ((getX() + getWidth()) >= tileMap.getWidthPixels()) {
            setX(tileMap.getWidthPixels() - width);
            if (!facingLeft) { turnAround(); }
            return;
        
        } else if (getX() <= 0) {
            setX(0);
            if (facingLeft) { turnAround(); }
            return;
        }

        walk.start();
    }

    private void doJumpAnimation() {
        if (isTurning() || isInAir()) { return; }
        if (facingLeft) { animations.get("jump_left").start(); } 
        else { animations.get("jump_right").start(); }
    }

    private void doFallAnimation() {
        if (facingLeft) { animations.get("fall_left").start(); }
        else { animations.get("fall_right").start(); }
    }

    private void doCrouchAnimation() {
        if (isCrouching()) { return; }
        if (facingLeft) { animations.get("crouch_left").start(); }
        else { animations.get("crouch_right").start(); }
    }

    private void moveLeft() {
        if (!facingLeft) {
            turnAround();
            return;
        }
        setX(getX() - getDX());
        doWalkAnimation();
    }

    private void moveRight() {
        if (facingLeft) {
            turnAround();
            return;
        }
        setX(getX() + getDX());
        doWalkAnimation();
    }

    private void fall() {
        if (!animations.get(facingLeft ? "fall_left" : "fall_right").isStillActive()) doFallAnimation();
        if (isJumping() || isFalling()) { return; }
        falling = true;
        initialVelocity = 60;
        timeElapsed = 0;
        timeStarted = System.currentTimeMillis();
        fallHeightStart = getY();
    }

    public void jump() {  
        if (isFalling() || isJumping()) { return; }
        if (isCrouching()) { 
            stand(); 
            return;
        }
        jumping = true;
        initialVelocity = 370;
        timeElapsed = 0;
        timeStarted = System.currentTimeMillis();
        jumpHeightStart = getY();
        if (facingLeft) { setImage(ImageManager.getImage("player_jump_left_1")); }
        else { setImage(ImageManager.getImage("player_jump_right_1")); }
        doJumpAnimation();
    }

    public void crouch() {
        if (isCrouching()) { return; }
        crouching = true;
        int lastHeight = getHeight();
        if (facingLeft) { setImage(ImageManager.getImage("player_crouch_left_1")); }
        else { setImage(ImageManager.getImage("player_crouch_right_1")); }
        setY(getY() + (lastHeight - getHeight()));
        doCrouchAnimation();
    }
    
    public void stand() {
        crouching = false;
        int lastHeight = getHeight();
        if (facingLeft) { setImage(ImageManager.getImage("player_idle_left_1")); }
        else { setImage(ImageManager.getImage("player_idle_right_1")); }
        setY(getY() - (getHeight() - lastHeight));
    }

}