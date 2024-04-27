package Tile;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Graphics2D;

import Game.*;
import Entity.*;
import Managers.*;

/**
    The TileMap class contains the data for a tile-based
    map, including Sprites. Each tile is a reference to an
    Image. Images are used multiple times in the tile map.
    map.
*/

public class TileMap {

    private Tile[][] tiles;
    private Dimension screenSize;
    private Dimension mapSize;
    private int tilemapOffsetY;
    private BackgroundManager bgManager;
    private ArrayList<Entity> entities;
    private Player player;

    /**
        Creates a new TileMap with the specified width and
        height (in number of tiles) of the map.
    */
    public TileMap(GamePanel panel, int width, int height) {
        screenSize = new Dimension(panel.getSize().width, panel.getSize().height);
        mapSize = new Dimension(width, height);
        tilemapOffsetY = screenSize.height - tilesToPixels(mapSize.height);
        System.out.println("[TILEMAP] Map size: " + mapSize.width + "x" + mapSize.height);
        System.out.println("[TILEMAP] tilemapOffsetY: " + tilemapOffsetY);
        bgManager = new BackgroundManager(panel, 12);
        entities = new ArrayList<Entity>();
        tiles = new Tile[mapSize.width][mapSize.height];
        setupEntities();
        System.out.println("[TILEMAP] Player spawned @ (" + player.getX() + "," + player.getY() + ")");
    }
    
    
    private void setupEntities() {
        player = new Player(this);
        int x = GamePanel.TILE_SIZE * 4;
        int y = tilemapOffsetY;
        player.setPosition(x, y);

        // Add to entity list
        entities.add(player);
    }


    /**
     * Gets the width of this TileMap (number of pixels across).
     * 
     * @return width in number of pixels
     */
    public int getWidthPixels() { return tilesToPixels(mapSize.width); }


    /**
     * Gets the width of this TileMap (number of tiles across).
     * 
     * @return width in number of tiles
     */
    public int getWidth() { return mapSize.width; }


    /**
     * Gets the height of this TileMap (number of tiles down).
     * 
     * @return height in number of tiles
     */
    public int getHeight() { return mapSize.height; }


    public int getTileMapOffsetY() { return tilemapOffsetY; }

    public int getTileMapOffsetX() {
        int offsetX = screenSize.width / 2 - Math.round(player.getX()) - GamePanel.TILE_SIZE;
        offsetX = Math.min(offsetX, 0);
        return Math.max(offsetX, screenSize.width - tilesToPixels(mapSize.width));
    }

    /**
     * Gets the tile at the specified location (in number of tiles).
     * 
     * @param x col number of the tile
     * @param y row number of the tile
     * @return  The tile object at the location, or null if no tile is at the location or 
     *          the location is out of bounds.
     */
    public Tile getTile(int x, int y) {
        if (x < 0 || x >= mapSize.width || y < 0 || y >= mapSize.height) { return null; }
        return tiles[x][y];
    }

    /**
     * Gets the tile at the specified coordinates.
     * 
     * @param x The x coordinate of the tile
     * @param y The y coordinate of the tile
     * @return  The tile object at the location
     */
    public Tile getTileAtLocation(int x, int y) { 
        if (x < 0 || y < tilemapOffsetY) { return null; }
        return getTile(pixelsToTiles(x), pixelsToTiles(y)); 
    }

    /**
     * Sets the tile at the specified location (in tiles)
     * 
     * @param x    col number of the tile
     * @param y    row number of the tile
     * @param tile The tile object to place at the location
     */
    public void setTile(int x, int y, Tile tile) {
        int xPos = tilesToPixels(x);
        int yPos = tilesToPixels(y);
        tile.setPosition(xPos, yPos);
        tiles[x][y] = tile;
    }

    /**
     * Class method to convert a pixel position to a tile position.
     * 
     * @param pixels The pixel position to convert
     * @return       The tile position
     */
    public static int pixelsToTiles(float pixels) { return (int) Math.floor(pixels / GamePanel.TILE_SIZE); }

    /**
     * Class method to convert a pixel position to a tile position.
     * 
     * @param pixels The pixel position to convert
     * @return       The tile position
     */
    public static int pixelsToTiles(int pixels) { return pixelsToTiles((float) pixels); }

    /** 
     * Class method to get the number of pixels occupied by a specified number of tiles.
     * 
     * @param numTiles The number of tiles to convert to pixels
     * @return         The number of pixels occupied by the specified number of tiles
     */
    public static int tilesToPixels(int numTiles) { return numTiles * GamePanel.TILE_SIZE; }
    
    /**
     * Checks if the specified coordinates collide with a tile.
     * 
     * @param x The x coordinate to check
     * @param y The y coordinate to check
     * @return  Location of colliding tile. Null if no tile is at the location or if the location is out of bounds.
     */
    public Point collidesWithTile(int x, int y) {
        int offsetY = getTileMapOffsetY();
        int xTile = TileMap.pixelsToTiles(x - getTileMapOffsetX());
        int yTile = TileMap.pixelsToTiles(y - offsetY);
        if (getTile(xTile, yTile) == null) { return null; }
        return getTile(xTile, yTile).getPosition();
    }

    public Point collidesWithTileDown(Entity e, int x, int y) {
        int xTileFrom = TileMap.pixelsToTiles(e.getX());
        int xTileTo = TileMap.pixelsToTiles(e.getX() + e.getWidth());
        int yTileFrom = TileMap.pixelsToTiles(e.getY());
        int yTileTo = TileMap.pixelsToTiles(e.getY() + e.getHeight());
    
        for (int yTile = yTileFrom; yTile <= yTileTo; yTile++) {
            for (int xTile = xTileFrom; xTile <= xTileTo; xTile++) {
                System.out.println ("[TILEMAP] Checking tile below (" + xTile + ", " + yTile + ")");
                Tile tile = getTile(xTile, yTile);
                if (tile != null && tile.isSolid()) { return tile.getPosition(); }
            }
        }
    
        return null;
    }

    public Point collidesWithTileUp(Entity e, int x, int y) {
        int offsetY = TileMap.pixelsToTiles(getTileMapOffsetY());
        int offsetX = TileMap.pixelsToTiles(getTileMapOffsetX());
        int xTile = TileMap.pixelsToTiles(x - offsetX);
        int yTileFrom = TileMap.pixelsToTiles(e.getY() - offsetY);
        int yTileTo = TileMap.pixelsToTiles(y - offsetY);
        
        for (int yTile = yTileFrom; yTile >= yTileTo; yTile--) {
            if (getTile(xTile, yTile) != null) {
                return new Point (xTile, yTile);
            }
            
            if (getTile(xTile + 1, yTile) != null) {
                int leftSide = (xTile + 1) * GamePanel.TILE_SIZE;
                if (x + e.getWidth() > leftSide) {
                    return new Point (xTile + 1, yTile);
                }
            }    
        }

        return null;
    }
 
    /**
     * Draws the tile map scene to the screen.
     * 
     * @param g2 The graphics context to draw to
     */
    public void draw(Graphics2D g2) {
        int offsetX = getTileMapOffsetX();
        
	    // draw the background first
	    bgManager.draw(g2);
        
        // draw the visible tiles
        Tile tile;
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(screenSize.width) + 1;
        for (int y = 0; y < mapSize.height; y++) {
            for (int x = firstTileX; x <= lastTileX; x++) {
                if ((tile = getTile(x, y)) == null) continue;
                tile.draw(g2, tile.getX() + offsetX, tile.getY() + tilemapOffsetY);
            }
        }

        // draw player
        player.draw(g2);
    }

    public void update() {
        for (Entity e : entities) { 
            if (e instanceof MovingEntity) { ((MovingEntity) e).update(); }
        }
    }

    public void moveLeft() {
        player.move(Movement.LEFT);
        Point tilePos = collidesWithTile(player.getX(), player.getY());
        if (tilePos == null) {
            bgManager.moveLeft();
            return;
        }
        System.out.println ("[TILEMAP] Collision going left");
        player.setX((int)tilePos.getX() + getTileMapOffsetX() + player.getWidth());
    }
    
    public void moveRight() {
        player.move(Movement.RIGHT);
        Point tilePos = collidesWithTile(player.getX() + player.getWidth(), player.getY());
        if (tilePos == null) { 
            bgManager.moveRight();
            return;
        }
        System.out.println ("[TILEMAP] Collision going right");
        player.setX((int) tilePos.getX() + getTileMapOffsetX() - player.getWidth());
    }

    public void jump() {
        player.move(Movement.JUMP);
        Point tilePos = collidesWithTileUp(player, player.getX(), player.getY());
        if (tilePos == null) { return; }
        System.out.println ("[TILEMAP] Collision going up");
        player.setY(((int) tilePos.getY()) * GamePanel.TILE_SIZE - player.getHeight());
    }

    public void stand() {
        player.move(Movement.STAND);
    }

    public void crouch() {
        player.move(Movement.CROUCH);
    }
}
