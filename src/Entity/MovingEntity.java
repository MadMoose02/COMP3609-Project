package Entity;

import Game.Movement;

/**
 * MovingEntity.java
 */
public abstract class MovingEntity extends Entity {
    
    public MovingEntity(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Updates the current entity's internal state
     */
    public abstract void update();
    
    /**
     * Moves the current entity in the specified direction
     * 
     * @param direction the direction to move the entity
     * @see Movement
     */
    public abstract void move(Movement direction);
}
