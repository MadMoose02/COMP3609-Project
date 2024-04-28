package Entity;

public class InvisiblePotion extends Collectible{
    private int numPotions;

    public InvisiblePotion(String imgName, int x, int y, int width, int height, Player player) {
        super(imgName, x, y, width, height, player);
        this.numPotions = 0;
    }

    @Override
    public void collect() {
        numPotions++;
    }

    public int getNumPotions() {
        return this.numPotions;
    }

    public void resetNumPotions() {
        this.numPotions = 0;
    }
    
}
