package Entity;

import java.awt.Graphics2D;

/**
 * MovingEntity.java
 */
public abstract class MovingEntity extends Entity {
    

    public MovingEntity(int x, int y, int width, int height) {
        super(x, y, width, height);
    }


    public abstract void update();

    public abstract void draw(Graphics2D g2d, int x, int y);
}
