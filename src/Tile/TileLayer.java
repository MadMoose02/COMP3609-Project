package Tile;

/**
 * TileLayer.java <hr>
 * Represents a layer of tiles in a tile-based map. This class facilitates the superimposition
 * of tiles on top of each other. Tiles are stored in a 2D array. Multiple Tiles can then be
 * drawn by using separate TileLayers.
 */

public class TileLayer {
    
    private Tile[][] tiles;
    private int width;
    private int height;

    public TileLayer(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
    }


    /* Accessors */

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public Tile getTile(int x, int y) { return tiles[x][y]; }


    /* Mutators */

    public void setWidth(int width) { this.width = width; }
    
    public void setHeight(int height) { this.height = height; }
    
    public void setTile(int x, int y, Tile tile) { tiles[x][y] = tile; }
    
}
