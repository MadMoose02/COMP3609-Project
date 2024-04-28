package Entity;

public class Key extends Collectible{
    private int numKeys;
    private String keyName;
    
    public Key(String imgName, int x, int y, int width, int height, Player player) {
        super(imgName, x, y, width, height, player);
        this.numKeys = 0;
        this.keyName = imgName;
    }

    @Override
    public void collect() {
        this.numKeys++;
    }

    public int getNumKeys() {
        return this.numKeys;
    }

    public void resetNumKeys() {
        this.numKeys = 0;
    }

    public String getKeyName() {
        return this.keyName;
    }
    
}
