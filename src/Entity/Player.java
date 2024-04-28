package Entity;
/**
 * Player.java
 * Extended from Game Programming lab content
 */

import java.awt.Image;
import java.util.ArrayList;
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
    /** True if player is falling, false if not */
    private boolean falling;
    /** True if player is jumping, false if not */
    private boolean jumping;
    /** True if player is climbing upwards, false if not */
    private boolean climbingUp;
    /** True if player is climbing downwards, false if not */
    private boolean climbingDown;
    /** Player's health */
    private int health;
    /** Player's maximum health */
    private int maxHealth;
    /** Collection of all player animations */
    private HashMap<String, Animation> animations;


    public Player() {
        super(0, 0, 0, 0);
        tileMap = null;
        health = maxHealth = 4;
        climbingUp = climbingDown = false;
        facingLeft = crouching = false;
        initialVelocity = timeElapsed = 0;
        setSize(60);
        setDX(5);
        setDY(1);
        loadPlayerAnimations();
        setImage(ImageManager.getImage("player_idle_right_1"));
    }


    /* Accessors */

    /**
     * Returns the player's current health
     *  
     * @return the player's current health
     */
    public int getHealth() { return health; }

    /**
     * Returns the player's maximum health
     * 
     * @return the player's maximum health
     */
    public int getMaxHealth() { return maxHealth; }

    /**
     * Checks if the player is currently in the process of crouching
     * 
     * @return {@code true} if the player is crouching, {@code false} otherwise
     */
    public boolean isCrouching() { return crouching; }

    /**
     * Checks if the player is currently in the process of jumping
     * 
     * @return {@code true} if the player is jumping, {@code false} otherwise
     */
    public boolean isJumping() { return jumping; }
    
    /**
     * Checks if the player is currently in the process of falling
     * 
     * @return {@code true} if the player is falling, {@code false} otherwise
     */
    public boolean isFalling() { return falling; }

    /**
     * Checks if the player is currently in the process of climbing up
     * 
     * @return {@code true} if the player is climbing up, {@code false} otherwise
     */
    public boolean isClimbingUp() { return climbingUp; }

    /**
     * Checks if the player is currently in the process of climbing down
     * 
     * @return {@code true} if the player is climbing down, {@code false} otherwise
     */
    public boolean isClimbingDown() { return climbingDown; }
    
    /**
     * Checks if any of the player's walk animations are currently active
     * 
     * @return {@code true} if the player is walking, {@code false} otherwise
     */
    public boolean isWalking() { 
        return animations.get("walk_left").isStillActive() ||
                animations.get("walk_right").isStillActive() ||
                animations.get("crouch_left").isStillActive() ||
                animations.get("crouch_right").isStillActive();
    }

    /**
     * Checks if any of the player's turn animations are currently active
     * 
     * @return {@code true} if the player is turning, {@code false} otherwise
     */
    public boolean isTurning() { 
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
        int x = getX() + (getWidth()/2);
        int y = getY() + getHeight();
        if (tileMap.collidesWithTileCoords(x, y) == null) { return true; }
        return false;
    }

    private boolean isOnLadder() {
        int x = getX() + (getWidth()/2);
        int y = getY() + (getHeight()/2);
        ArrayList<Tile> tiles = tileMap.getTilesAtLocation(x, y);
        if (tiles == null) { return false; }
        for (Tile t : tiles) {
            if (t.getName().contains("ladder")) { return true; }
        }
        return false;
    }

    private boolean isAboveLadder() {
        int x = getX() + (getWidth()/2);
        int y = getY() + getHeight() + 1;
        ArrayList<Tile> tiles = tileMap.getTilesAtLocation(x, y);
        if (tiles == null) { return false; }
        for (Tile t : tiles) {
            if (t.getName().contains("ladder")) { return true; }
        }
        return false;
    }


    /* Mutators */
    
    /**
     * Sets the player's health. If the health is greater than the maximum health, the
     * health is defaulted to the maximum health
     * 
     * @param health the player's new health value
     */
    public void setHealth(int health) { 
        this.health = (health > maxHealth ? maxHealth : (health < 0 ? 0 : health));
    }

    /**
     * Sets the player's reference to the current tile map
     *  
     * @param tileMap the active tile map
     */
    public void setTileMap(TileMap tileMap) { this.tileMap = tileMap; }


    /* Methods */

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
                climbDown();
                break;

            case STAND:
                stand();
                climbUp();
                break;

            default:
                System.out.println ("[PLAYER] Unknown direction: " + direction);
                return;
        }
    }

    /**
     * Updates the player's position and animation
     */
    @Override
    public void update() {
        if (getX() > tileMap.getWidthPixels() - getWidth()) {
            setX(tileMap.getWidthPixels() - getWidth());
        }

        // Fix player clipping with ground
        ArrayList<Tile> collidedTiles = tileMap.getTilesAtLocation(getX() + getWidth() / 2, getY() + getHeight());
        if (collidedTiles != null && collidedTiles.size() > 0) {
            Tile solidTile = null;
            for (Tile t : collidedTiles) {
                if (t.isSolid()) { solidTile = t; }
            }
            if (solidTile != null) {
                setY(solidTile.getY() - getHeight());
            }
        }
        
        // Update all active animations
        for (Animation anim : animations.values()) { anim.update(); }
        
        // If the player is in freefall (in air but not jumping), fall down
        if (isInAir() && !(isClimbingUp() || isClimbingDown())) { fall(); } else { 
            jumping = falling = false; 
            if (!isCrouching()) stand();
        }

        // if the player is on a ladder, center on the ladder
        if ((isClimbingDown() || isClimbingUp()) && isOnLadder()) { 
            int x = TileMap.pixelsToTiles(getX() + (getWidth()/2));
            setX(TileMap.tilesToPixels(x) + getWidth());
        }

        // if the player is climbing up or down but is not on a ladder, stop climbing
        if (isClimbingUp() || isClimbingDown()) {
            if (!isOnLadder()) { climbingDown = climbingUp = false; }
        }

        // if there is a solid tile under the player, stop climbing down
        if (isClimbingDown()) {
            Tile solidTile = tileMap.collidesWithTileCoords(getX() + getWidth() / 2, getY() + getHeight());
            if (solidTile != null) {
                climbingDown = false;
                setY(solidTile.getY() - getHeight());
                stand();
            }
        }

        // if at least half of the player's height is above a solid tile, stop climbing up
        if (isClimbingUp()) {
            ArrayList<Tile> above = tileMap.getTilesAtLocation(getX() + getWidth() / 2, getY() - 1);
            if (above == null || above.size() == 0) { 
                Tile solidTile = null;
                for (Tile t : above) {
                    if (t.isSolid()) { solidTile = t; }
                }
                if (solidTile != null && getY() - solidTile.getY() > getHeight() / 2) {
                    climbingUp = false;
                    stand();
                    setY(solidTile.getY());
                }
            }
        }
        
        // Calculate the distance travelled since the last update
        timeElapsed = (System.currentTimeMillis() - timeStarted)/1000;
        int distance = (int) ((initialVelocity * timeElapsed) + (4.9 * timeElapsed * timeElapsed));
        initialVelocity += 0.5 * timeElapsed;
        
        // Update position based on trajectory
        if (isJumping()) {
            initialVelocity -= 4.9 * timeElapsed;
            setY(jumpHeightStart - distance);
            setX(getX() + (facingLeft ? -(getDX()) : getDX()) * 0.1001);
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

    private void doClimbAnimation() {
        if (animations.get("climb_up").isStillActive() || 
            animations.get("climb_down").isStillActive()) { return; }
        animations.get("climb_" + (isClimbingUp() ? "up" : "down")).start();
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

    private void moveLeft() {
        if (isClimbingUp() || isClimbingDown()) { return; }
        if (!facingLeft) {
            turnAround();
            return;
        }
        setX(getX() - getDX());
        doWalkAnimation();
    }

    private void moveRight() {
        if (isClimbingUp() || isClimbingDown()) { return; }
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
        initialVelocity = 400;
        timeElapsed = 0;
        timeStarted = System.currentTimeMillis();
        jumpHeightStart = getY();
        if (facingLeft) { setImage(ImageManager.getImage("player_jump_left_1")); }
        else { setImage(ImageManager.getImage("player_jump_right_1")); }
        doJumpAnimation();
    }

    public void crouch() {
        if (isCrouching()) { return; }
        int lastHeight = getHeight();
        if (facingLeft) { setImage(ImageManager.getImage("player_crouch_left_1")); }
        else { setImage(ImageManager.getImage("player_crouch_right_1")); }
        if (!crouching) {
            crouching = true;
            setY(getY() + (lastHeight - getHeight()));
        }
        doCrouchAnimation();
    }
    
    public void stand() {
        if (isClimbingUp()) { return; }
        int lastHeight = getHeight();
        if (facingLeft) { setImage(ImageManager.getImage("player_idle_left_1")); }
        else { setImage(ImageManager.getImage("player_idle_right_1")); }
        if (crouching) {
            crouching = false;
            setY(getY() - (getHeight() - lastHeight));
        }
    }

    public void climbUp() {
        if (!isClimbingUp()) {
            if (isOnLadder()) { climbingUp = true; }
            if (!climbingUp) { return; }
            setImage(ImageManager.getImage("player_climb_up_1"));
        }
        setY(getY() - (getDY() * 2));
        if (animations.get("climb_up").isStillActive()) { return; }
        doClimbAnimation();
    }

    public void climbDown() {
        if (!isClimbingDown()) {
            if (isAboveLadder()) { climbingDown = true; }
            if (!climbingDown) { return; }
            setImage(ImageManager.getImage("player_climb_down_1"));

            // position player on ladder
            ArrayList<Tile> tiles = tileMap.getTilesAtLocation(getX() + getWidth()/2, getY() + getHeight() + 10);
            for (Tile t : tiles) {
                if (t.getName().contains("ladder")) {
                    setY(getY() + getHeight());
                    break;
                }
            }
        }
        setY(getY() + (getDY() * 2));
        if (animations.get("climb_down").isStillActive()) { return; }
        doClimbAnimation();
    }

}