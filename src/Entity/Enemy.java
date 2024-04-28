package Entity;

import java.awt.Graphics2D;

import Game.Movement;
import Managers.*;

import java.awt.Image;

public class Enemy extends MovingEntity {
    // Enemy can move across n number of tile 
    private final int minX; //first tile starting pos
    private final int maxX; //last tile starting pos

    private boolean facingLeft;
    private Image image;

    public Enemy(int minX, int maxX, int moveSpeed, String imageFilename) {
        super(0, 0, 0, 0);
        this.minX = minX;
        this.maxX = maxX;
        this.setDX(moveSpeed);
        facingLeft = true; 
        image = ImageManager.getImage(imageFilename);
    }

    @Override
    public void update() {
        if (facingLeft) {
            if (getX() <= minX) {changeDirection();} 
            else {setX(getX() - this.getDX());}
        } 
        else{
            if (getX() >= maxX) {changeDirection();} 
            else {setX(getX() + this.getDX());}
        }
    }

    private void changeDirection() {
        facingLeft = !facingLeft;
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y) {
        g2d.drawImage(
            image, x, y,
            image.getWidth(null), 
            image.getHeight(null), 
            null
        );
    }

    @Override
    public void move(Movement direction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }
}

