package Entity;

public class LifePotion extends Collectible{
    private int numLives;

    public LifePotion(String imgName, int x, int y, int width, int height, Player player) {
        super(imgName, x, y, width, height, player);
        this.numLives = 3;
    }

    @Override
    public void collect() {
        if (this.numLives < 3)
            this.numLives = this.numLives + 1;
    }

    public int getLives() {
        return this.numLives;
    }

    public void resetLives() {
        this.numLives = 3;
    }
    
}
