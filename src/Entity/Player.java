package Entity;
/**
 * Player.java
 * Extended from Game Programming lab content
 */

import java.awt.Image;
import java.util.HashMap;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import Game.*;
import Tile.*;
import Managers.*;
import Animation.*;


public class Player extends MovingEntity {			

    private JPanel panel;
    private TileMap tileMap;

    /** Player's x coordinate in pixels */
    private int x;
    /** Player's y coordinate in pixels */
    private int y;

    /** Time elapsed since jump */
    private double timeElapsed;
    private double lastUpdateTime;
    /** Height at which player started to jump */
    private int jumpHeightStart;
    /** Height at which player starts to fall down */
    private int jumpHeightEnd;
    /** Maximum height at which player can jump (in pixels) */
    private final int MAX_JUMP_HEIGHT = 100;
    
    /** Height at which player started to fall */
    private int fallHeightStart;
    
    private boolean facingLeft;
    private boolean crouching;
    private int initialVelocity;

    /** Collection of all player animations */
    private HashMap<String, Animation> animations;


    public Player (JPanel panel, TileMap t) {
        super(0, 0, 0, 0);
        this.panel = panel;
        tileMap = t;
        facingLeft = crouching = false;
        setImage(ImageManager.getImage("player_idle_right_1"));
        setSize(62);
        setDX(7);
        setDY(1);
        loadPlayerAnimations();
    }

    public synchronized void move (Movement direction) {
        if (!panel.isVisible ()) return;
        if (isTurning()) return;
        switch (direction) {

            case LEFT:
                if (!facingLeft) {
                    doTurnAroundAnimation();
                    return;
                }
                setX(x - getDX());
                doWalkAnimation();
                break;

            case RIGHT:
                if (facingLeft) {
                    doTurnAroundAnimation();
                    return;
                }
                setX(x + getDX());
                doWalkAnimation();
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
    public void update () {
        if (!panel.isVisible ()) { return; }
        for (Animation anim : animations.values()) { anim.update(); }
        
        timeElapsed = (System.currentTimeMillis() - lastUpdateTime)/1000;
        int distance = (int) ((initialVelocity * timeElapsed) + (4.9 * Math.pow(timeElapsed, 2)));
        
        if (isJumping()) {
            setY(getY() - distance);
            System.out.println ("[PLAYER] Jumping: " + getY() + " (" + distance + ")");
            if (getY() <= jumpHeightEnd) {
                System.out.println ("[PLAYER] Jumping: Reached max height (" + getY() + "). Inverting trajectory.");
                fall();
            }
        }
        
        if (isFalling()) {
            setY(getY() + distance);
            System.out.println ("[PLAYER] Falling: " + getY());
            if (getY() >= fallHeightStart) { 
                System.out.println("[PLAYER] Falling: Reached ground (" + getY() + ")");
                setY(tileMap.getOffsetY());
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y) {
        if (!panel.isVisible ()) { return; }

        Image img = facingLeft ? ImageManager.flipImageHorizontally(getImage()) : getImage();
        for (Animation anim : animations.values()) {
            if (anim.isStillActive()) { img = anim.getImage(); }
        }

        x -= img.getWidth(null)/2;

        g2d.drawImage(img, x, y, img.getWidth(null), img.getHeight(null), null);
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
            System.out.println("[PLAYER] Loaded animation: " + anim.getName() + " (" + anim.getDuration() + "s)");
        }
    }

    private boolean isCrouching() { return crouching; }

    private boolean isJumping() { 
        return animations.get("jump_left").isStillActive() || 
                animations.get("jump_right").isStillActive(); 
    }
    
    private boolean isFalling() { 
        return animations.get("fall_left").isStillActive() || 
                animations.get("fall_right").isStillActive(); 
    }
    
    private boolean isWalking() { 
        return animations.get("walk_left").isStillActive() ||
                animations.get("walk_right").isStillActive(); 
    }

    private boolean isTurning() { 
        return animations.get("turn_left").isStillActive() || 
                animations.get("turn_right").isStillActive(); 
    }

    public boolean isInAir() {
        if (isJumping() || isFalling()) { return true; }
        if (tileMap.collidesWithTile(x, y + this.height + 1) != null) return true;
        return false;
    }

    private void doWalkAnimation() {
        if (!panel.isVisible ()) return;
        if (isWalking() || isInAir()) return;
        Animation walk;
        if (crouching) { walk = animations.get((facingLeft) ? "crouch_left" : "crouch_right"); }
        else { walk = animations.get((facingLeft) ? "walk_left" : "walk_right"); }
        
        // Boundary checks before starting animation
        if ((getX() + getWidth()) > tileMap.getWidthPixels()) {
            setX(tileMap.getWidthPixels() - width);
            if (!facingLeft) { doTurnAroundAnimation(); }
            return;
        }
        if (getX() < 0) {
            setX(0);
            if (facingLeft) { doTurnAroundAnimation(); }
            return;
        }

        walk.start();
    }

    private void doTurnAroundAnimation() {
        if (!panel.isVisible ()) return;
        if (isTurning() || isInAir()) return;
        facingLeft = !facingLeft;
        if (facingLeft) { animations.get("turn_left").start(); } 
        else { animations.get("turn_right").start(); }
    }

    private void doJumpAnimation() {
        if (!panel.isVisible ()) return;
        if (isTurning() || isInAir()) return;
        if (facingLeft) { animations.get("jump_left").start(); } 
        else { animations.get("jump_right").start(); }
    }

    private void doFallAnimation() {
        if (!panel.isVisible ()) return;
        if (facingLeft) { animations.get("fall_left").start(); }
        else { animations.get("fall_right").start(); }
    }

    private void doCrouchAnimation() {
        if (!panel.isVisible ()) return;
        if (isCrouching()) { return; }
        if (facingLeft) { animations.get("crouch_left").start(); }
        else { animations.get("crouch_right").start(); }
    }

    private void fall() {
        if (!panel.isVisible ()) return;
        for (Animation anim : animations.values()) { if (anim.isStillActive()) anim.stop(); }
        initialVelocity = 0;
        timeElapsed = 0;
        fallHeightStart = getY();
    }

    public void jump() {  
        if (!panel.isVisible ()) return;
        if (isInAir()) { return; }
        if (crouching) { crouching = false; }
        initialVelocity = 2;
        timeElapsed = 0;
        jumpHeightStart = getY();
        jumpHeightEnd = jumpHeightStart - MAX_JUMP_HEIGHT;
        System.out.println("[PLAYER] Beginning jump: " + jumpHeightStart + " -> " + jumpHeightEnd);
        doJumpAnimation();
    }

    public void crouch() {
        if (!panel.isVisible ()) return;
        if (isCrouching()) { return; }
        System.out.println("[PLAYER] Toggled player crouch");
        crouching = true;
        if (facingLeft) { setImage(ImageManager.getImage("player_crouch_left_1")); }
        else { setImage(ImageManager.getImage("player_crouch_right_1")); }
        doCrouchAnimation();
    }

    public void stand() {
        if (!panel.isVisible ()) return;
        if (!isCrouching()) { return; }
        crouching = false;
        if (facingLeft) { setImage(ImageManager.getImage("player_idle_left_1")); }
        else { setImage(ImageManager.getImage("player_idle_right_1")); }
        doCrouchAnimation();
    }
}