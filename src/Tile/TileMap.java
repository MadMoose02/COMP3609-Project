package Tile;

import java.awt.Graphics2D;
import java.awt.Point;

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
    private int screenWidth, screenHeight;
    private int mapWidth, mapHeight;
    private int offsetY;
    
    private BackgroundManager bgManager;
    private Player player;
    private Heart heart;

    private GamePanel panel;

    /**
        Creates a new TileMap with the specified width and
        height (in number of tiles) of the map.
    */
    public TileMap(GamePanel panel, int width, int height) {

        this.panel = panel;

        screenWidth = panel.getSize().width;
        screenHeight = panel.getSize().height;

        mapWidth = width;
        mapHeight = height;

        offsetY = screenHeight - tilesToPixels(mapHeight);
        System.out.println("[TILEMAP] Map size: " + mapWidth + "x" + mapHeight);
        System.out.println("[TILEMAP] offsetY: " + offsetY);

        bgManager = new BackgroundManager(panel, 12);

        tiles = new Tile[mapWidth][mapHeight];
        player = new Player(panel, this);
        heart = new Heart(panel, player);

        int playerHeight = player.getHeight();
        int x = GamePanel.TILE_SIZE * 3;
        int y = ((mapHeight - 1) * GamePanel.TILE_SIZE) - playerHeight;
        player.setPosition(x, y);

        System.out.println("[TILEMAP] Player(" + player.getWidth() + "," + player.getHeight() + ") spawned @ (" + x + "," + y + ")");
    }


    /**
     * Gets the width of this TileMap (number of pixels across).
     * 
     * @return width in number of pixels
     */
    public int getWidthPixels() { return tilesToPixels(mapWidth); }


    /**
     * Gets the width of this TileMap (number of tiles across).
     * 
     * @return width in number of tiles
     */
    public int getWidth() { return mapWidth; }


    /**
     * Gets the height of this TileMap (number of tiles down).
     * 
     * @return height in number of tiles
     */
    public int getHeight() { return mapHeight; }


    public int getOffsetY() { return offsetY; }

    /**
     * Gets the tile at the specified location.
     * @param x row number of the tile
     * @param y col number of the tile
     * @return  null if no tile is at the location or if the location is out of bounds.
     */
    public Tile getTile(int x, int y) {
        if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight) {
            return null;
        }
        return tiles[x][y];
    }

    public Tile getTileAtLocation(int x, int y) {
        int xTile = pixelsToTiles(x);
        int yTile = pixelsToTiles(y);
        return getTile(xTile, yTile);
    }


    /**
        Sets the tile at the specified location.
    */
    public void setTile(int x, int y, Tile tile) { tiles[x][y] = tile; }


    /**
        Class method to convert a pixel position to a tile position.
    */

    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }


    /**
        Class method to convert a pixel position to a tile position.
    */

    public static int pixelsToTiles(int pixels) {
        return (int) Math.floor((float)pixels / GamePanel.TILE_SIZE);
    }


    /**
        Class method to convert a tile position to a pixel position.
    */

    public static int tilesToPixels(int numTiles) { return numTiles * GamePanel.TILE_SIZE; }
    
    
    /**
     * Checks if the specified coordinates collide with a tile.
     * @param x The x coordinate to check
     * @param y The y coordinate to check
     * @return  Colliding title. Null if no tile is at the location or if the location is out of bounds.
     */
    public Point collidesWithTile(int x, int y) {
        int offsetY = getOffsetY();
        int xTile = TileMap.pixelsToTiles(x);
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
        int offsetY = TileMap.pixelsToTiles(getOffsetY());
        int xTile = TileMap.pixelsToTiles(x);
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
        int offsetX = screenWidth / 2 - Math.round(player.getX()) - GamePanel.TILE_SIZE;
        offsetX = Math.max(offsetX, 0);
        offsetX = Math.min(offsetX, tilesToPixels(mapWidth) - screenWidth);
    
        // Calculate the player's x-coordinate relative to the viewable map
        int playerX = player.getX() - offsetX;

        // Check if the player is within 50 pixels of the left or right edge
        if (playerX < 50) {
            // Player is near the left edge, scroll map to the right
            offsetX = Math.max(player.getX() - 50, 0);
        } else if (playerX > screenWidth - 50) {
            // Player is near the right edge, scroll map to the left
            offsetX = Math.min(player.getX() + 50, tilesToPixels(mapWidth) - screenWidth);
        }

        // Clamp offsetX to the map bounds
        if (offsetX < 0) {
            offsetX = 0;
        } else if (offsetX > tilesToPixels(mapWidth) - screenWidth) {
            offsetX = tilesToPixels(mapWidth) - screenWidth;
        }
        
	    // draw the background first
	    bgManager.draw(g2);
        
        // draw the visible tiles
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(screenWidth) + 1;
        for (int y = 0; y < mapHeight; y++) {
            for (int x = firstTileX; x <= lastTileX; x++) {
                Tile tile = getTile(x, y);
                if (tile == null) continue;
                tile.draw(g2, tilesToPixels(x) + offsetX, tilesToPixels(y) + offsetY);
            }
        }

        // draw player
        player.draw(g2, player.getX(), player.getY() + offsetY);

	    // draw Heart sprite
        heart.draw(g2, 0, 0);
    }

    public void update() {
        player.update();

        if (heart.collidesWithPlayer()) {
            panel.endLevel();
            return;
        }

        if (heart.collidesWithPlayer()) {
            panel.endLevel();
        }
    }

    public void moveLeft() {
        player.move(Movement.LEFT);
        Point tilePos = collidesWithTile(player.getX(), player.getY());
        if (tilePos != null) {
            System.out.println ("[TILEMAP] Collision going left");
            player.setX(((int) tilePos.getX() + 1) * GamePanel.TILE_SIZE);
        }
        bgManager.moveLeft();
    }

    public void moveRight() {
        player.move(Movement.RIGHT);
        Point tilePos = collidesWithTile(player.getX() + player.getWidth(), player.getY());
        if (tilePos != null) {
            System.out.println ("[TILEMAP] Collision going right");
            player.setX(((int) tilePos.getX()) * GamePanel.TILE_SIZE - player.getWidth());
        }
        bgManager.moveRight();
    }

    public void jump() {
        player.move(Movement.JUMP);
    }

    public void stand() {
        player.move(Movement.STAND);
    }

    public void crouch() {
        player.move(Movement.CROUCH);
    }
}
