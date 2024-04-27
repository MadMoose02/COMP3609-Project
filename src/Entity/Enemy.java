package Entity;

import java.awt.Graphics2D;

import Game.Movement;

public class Enemy extends MovingEntity{

    public Enemy(int x, int y, int width, int height) {
        super(x, y, width, height);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void move(Movement direction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

    public void shoot() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shoot'");
    }

    public void enemyCollision() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'enemyCollision'");
    }
    
}
