package Entity;

import Game.Movement;

/**
 * MovingEntity.java
 */
public abstract class MovingEntity extends Entity {
    
    public MovingEntity(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public abstract void update();
    
    public abstract void move(Movement direction);
}
