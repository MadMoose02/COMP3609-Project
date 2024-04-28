package Entity;

public class Coin extends Collectible{
    private int numCoins;

    public Coin(int x, int y, Player player) {
        super("coin", x, y, 40, 40, player);
        this.numCoins = 0;
    }
    
    public Coin(int x, int y, int width, int height, Player player) {
        super("coin", x, y, width, height, player);
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
