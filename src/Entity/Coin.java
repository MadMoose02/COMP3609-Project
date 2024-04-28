package Entity;

public class Coin extends Collectible{
    private int numCoins;
    
    public Coin(String imgName, int x, int y, int width, int height, Player player) {
        super(imgName, x, y, width, height, player);
        this.numCoins = 0;
    }

    @Override
    public void collect() {
        this.numCoins++;
    }

    public int getNumCoins() {
        return this.numCoins;
    }

    public void resetNumCoins() {
        this.numCoins = 0;
    }

}
