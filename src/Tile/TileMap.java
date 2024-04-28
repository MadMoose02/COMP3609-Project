package Tile;

/**
 * TileMap.java <hr>
 * The TileMap class contains the data for a tile-based map, including Sprites. 
 * Each tile is a reference to an Image. Images are used multiple times in the tile map.
 */

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import Game.*;
import Entity.*;
import Managers.*;

public class TileMap {

    private BackgroundManager bgManager;
    private HashMap<String, TileLayer> tileLayers;
    private Dimension screenSize;
    private Dimension mapSize;
    private int tilemapOffsetY;
    private ArrayList<Entity> entities;
    private Player player;

    public TileMap(GamePanel panel, Player player, BackgroundManager bgManager, int width, int height) {
        screenSize = new Dimension(panel.getSize().width, panel.getSize().height);
        mapSize = new Dimension(width, height);
        tilemapOffsetY = Math.max(0, screenSize.height - tilesToPixels(mapSize.height));
        System.out.println("[TILEMAP] tilemapOffsetY: " + tilemapOffsetY);
        entities = new ArrayList<Entity>();
        tileLayers = new HashMap<String, TileLayer>();
        this.player = player;
        this.bgManager = bgManager;
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


    public int getTileMapOffsetY() { 
        int offsetY = screenSize.height / 2 - Math.round(player.getY()) - player.getHeight();
        offsetY = Math.min(offsetY, 0);
        return Math.max(offsetY, screenSize.height - tilesToPixels(mapSize.height));
    }

    public int getTileMapOffsetX() {
        int offsetX = screenSize.width / 2 - Math.round(player.getX()) - player.getWidth();
        offsetX = Math.min(offsetX, 0);
        return Math.max(offsetX, screenSize.width - tilesToPixels(mapSize.width));
    }

    /**
     * Gets the tile at the specified location (in number of tiles).
     * 
     * @param col col number of the tile
     * @param row row number of the tile
     * @return    The tile object at the location, or null if no tile is at the location or 
     *            the location is out of bounds.
     */
    public ArrayList<Tile> getTiles(int col, int row) {
        if (col < 0 || col >= mapSize.width || row < 0 || row >= mapSize.height) { return null; }
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        for (TileLayer layer : tileLayers.values()) {
            if (layer.getTile(col, row) == null) { continue; } 
            tiles.add(layer.getTile(col, row));
        }
        return tiles;
    }

    /**
     * Gets the tile layer with the specified name.
     * 
     * @param layerName The name of the layer
     * @return          The layer object if it exists, or null if it doesn't
     */
    public TileLayer getLayer(String layerName) { return tileLayers.get(layerName); }

    /**
     * Gets the tile at the specified location (in number of tiles) 
     * from the specified layer.
     * 
     * @param layerName The name of the layer
     * @param col       The col number of the tile
     * @param row       The row number of the tile
     * @return          The tile object at the location, or null if no tile is at the location or
     *                  the location is out of bounds.
     */
    public Tile getLayerTile(String layerName, int col, int row) {
        if (!tileLayers.containsKey(layerName)) { return null; }
        if (col < 0 || col >= mapSize.width || row < 0 || row >= mapSize.height) { return null; }
        return tileLayers.get(layerName).getTile(col, row);
    }

    /**
     * Gets the tile at the specified coordinates.
     * 
     * @param x The x coordinate of the tile
     * @param y The y coordinate of the tile
     * @return  The tile object at the location
     */
    public ArrayList<Tile> getTilesAtLocation(int x, int y) { 
        if (x < 0 || y < tilemapOffsetY) { return null; }
        return getTiles(pixelsToTiles(x), pixelsToTiles(y)); 
    }

    /**
     * Lazy initializer for the player.
     * 
     * @param player The player object
     */
    public void setPlayer(Player player) {
        player.setPosition(GamePanel.TILE_SIZE * 4, tilemapOffsetY);
        player.setTileMap(this);
        this.player = player;
        System.out.println("[TILEMAP] Player spawned at " + player.getX() + ", " + player.getY());
    }

    /**
     * Lazy initializer for the background manager.
     * 
     * @param bgManager The background manager object
     */
    public void setBackgroundManager(BackgroundManager bgManager) {
        this.bgManager = bgManager;
    }

    /**
     * Set up all the enities particular to this tile map (e.g. enemies, etc.)
     */
    public void setupEntities() {
        TileLayer coinLayer = tileLayers.get("Coin");
        TileLayer enemyLayer = tileLayers.get("Enemy");
        TileLayer doorLayer = tileLayers.get("Door");
        TileLayer dangerObjectLayer = tileLayers.get("DangerObject");
        TileLayer invisibePotionLayer = tileLayers.get("InvisiblePotion");
        TileLayer lifeLayer = tileLayers.get("Life");
        TileLayer keyLayer = tileLayers.get("Key");
        TileLayer ladderLayer = tileLayers.get("Ladder");

        for (int y = 0; y < mapSize.height; y++) {
            for (int x = 0; x < mapSize.width; x++) {
                if (coinLayer != null){
                    Tile t = coinLayer.getTile(x, y);
                    if (t == null) { continue; }
                    else {entities.add(new Coin(
                        "coin.png",
                        t.getX(), 
                        t.getY(), 1, 1, player));}
                }

                if (doorLayer == null) { 
                    Tile t = doorLayer.getTile(x, y);
                    if (t == null) { continue; }
                    else {entities.add(new Door(
                        "door.png",
                        t.getX(), 
                        t.getY(), 1, 1, player));}
                }

                if (dangerObjectLayer!=null){
                    Tile t = dangerObjectLayer.getTile(x, y);
                    if (t == null) { continue; }
                    else {entities.add(new DangerObject(
                        "dangerObject.png",
                        t.getX(), 
                        t.getY(), 1, 1, player));}
                }

                if (invisibePotionLayer != null){
                    Tile t = invisibePotionLayer.getTile(x, y);
                    if (t == null) { continue; }
                    else {entities.add(new InvisiblePotion(
                        "invisiblePotion.png",
                        t.getX(), 
                        t.getY(), 1, 1, player));}
                }

                if (lifeLayer != null){
                    Tile t = lifeLayer.getTile(x, y);
                    if (t == null) { continue; }
                    else {entities.add(new Life(
                        "life.png",
                        t.getX(), 
                        t.getY(), 1, 1, player));}
                }

                if (keyLayer != null){
                    Tile t = keyLayer.getTile(x, y);
                    if (t == null) { continue; }
                    else {entities.add(new Key(
                        "key.png",
                        t.getX(), 
                        t.getY(), 1, 1, player));}
                }

                if (ladderLayer != null) {
                    Tile t = ladderLayer.getTile(x, y);
                    if (t == null) { continue; }
                    else {entities.add(new Ladder(
                        t.getX(), 
                        t.getY(), 1, 1, player));}
                }
            }
        }
        return;
    }

    /**
     * Adds a new tile layer with the specified name
     * 
     * @param name  The name of the tile layer
     * @param layer The tile layer object
     */
    public void addTileLayer(String name, TileLayer layer) { tileLayers.put(name, layer); }

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
     * @return  Location of colliding tile. Null if no tile is at the location or if the location 
     *          is out of bounds.
     */
    public Point collidesWithTileCoords(int x, int y) {
        System.out.println("Checking " + x + ", " + y);
        ArrayList<Tile> tiles = getTilesAtLocation(
            x - getTileMapOffsetX(), 
            y - getTileMapOffsetY()
        );
        if (tiles == null || tiles.size() == 0) { return null; }
        for (Tile t : tiles) {
            if (t != null && t.isSolid()) { return t.getPosition(); } 
        }
        return null;
    }

    /**
     * Checks if the specified row and column collide with a tile.
     * 
     * @param row The row to check
     * @param col The column to check
     * @return    Location of colliding tile. Null if no tile is at the location or if the location 
     *            is out of bounds.
     */
    public Point collidesWithTile(int row, int col) {
        ArrayList<Tile> tiles = getTiles(row, col);
        if (tiles == null || tiles.size() == 0) { return null; }
        for (Tile t : tiles) { if (t != null && t.isSolid()) { return t.getPosition(); } }
        return null;
    }

    public Point collidesWithTileDown(Entity e, int x, int y) {
        int xTileFrom = TileMap.pixelsToTiles(e.getX());
        int xTileTo = TileMap.pixelsToTiles(e.getX() + e.getWidth());
        int yTileFrom = TileMap.pixelsToTiles(e.getY());
        int yTileTo = TileMap.pixelsToTiles(e.getY() + e.getHeight());
    
        for (int yTile = yTileFrom; yTile <= yTileTo; yTile++) {
            for (int xTile = xTileFrom; xTile <= xTileTo; xTile++) {
                System.out.println ("[TILEMAP] Checking tile below (" + xTile + ", " + yTile + ")");
                Point loc = collidesWithTile(xTile, yTile);
                if (loc != null) { return loc; }
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
            ArrayList<Tile> tiles = getTiles(xTile, yTile);
            if (tiles == null || tiles.size() == 0) { continue; }
            for (Tile t : tiles) { if (t.isSolid()) { return t.getPosition(); } }
            
            tiles = getTiles(xTile + 1, yTile);
            if (tiles.size() > 0) {
                int leftSide = (xTile + 1) * GamePanel.TILE_SIZE;
                if (x + e.getWidth() > leftSide) {
                    for (Tile t : tiles) { if (t.isSolid()) { return t.getPosition(); } }
                }
            }
        }

        return null;
    }
 
    /**
     * Draws the tile map scene to the screen. The BackgroundManager draws its background
     * layers first, then the tile layers and finally the entities.
     * 
     * @param g2 The graphics context to draw to
     */
    public void draw(Graphics2D g2) {
        bgManager.draw(g2);
        
        // draw the visible tiles
        int firstTileX = pixelsToTiles(-getTileMapOffsetX());
        int lastTileX = firstTileX + pixelsToTiles(screenSize.width) + 1;
        int firstTileY = pixelsToTiles(-getTileMapOffsetY());
        int lastTileY = firstTileY + pixelsToTiles(screenSize.height) + 1;
        TileLayer terrain = getLayer("Terrain");
        TileLayer decoration1 = getLayer("Decoration 1");
        TileLayer decoration2 = getLayer("Decoration 2");
        TileLayer entity = getLayer("Entity");

        // draw terrain layer
        if (terrain == null) {
            throw new RuntimeException("Terrain layer not found");
        }
        renderTileLayer(g2, terrain, firstTileX, lastTileX, firstTileY, lastTileY);
        
        // draw decoration layers
        if (decoration1 != null) { 
            renderTileLayer(g2, decoration1, firstTileX, lastTileX, firstTileY, lastTileY); 
        }
        if (decoration2 != null) { 
            renderTileLayer(g2, decoration2, firstTileX, lastTileX, firstTileY, lastTileY); 
        }

        // draw entities
        if (entity != null) { 
            renderTileLayer(g2, entity, firstTileX, lastTileX, firstTileY, lastTileY); 
        }

        // draw player
        int x = player.getX() + getTileMapOffsetX();
        int y = player.getY() + getTileMapOffsetY();
        player.draw(g2, x, y);
    }

    private void renderTileLayer(Graphics2D g2d, TileLayer layer, int x1, int x2, int y1, int y2) {
        Tile t;
        for (int y = y1; y < y2; y++) {
            for (int x = x1; x <= x2; x++) {
                if ((t = layer.getTile(x, y)) == null) { continue; }
                int xPos = x * GamePanel.TILE_SIZE + getTileMapOffsetX();
                int yPos = y * GamePanel.TILE_SIZE + getTileMapOffsetY();
                t.draw(g2d, xPos, yPos);
            }
        }
    }

    public void update() {
        player.update();
        for (Entity e : entities) { 
            if (e instanceof MovingEntity) { ((MovingEntity) e).update(); }
        }
    }

    public void moveLeft() {
        player.move(Movement.LEFT);
        Point tilePos = collidesWithTileCoords(player.getX(), player.getY());
        if (tilePos == null) { 
            bgManager.moveLeft();
            return; 
        }
        System.out.println ("[TILEMAP] Collision going left");
        player.setX((int)tilePos.getX() + getTileMapOffsetX() + player.getWidth());
    }
    
    public void moveRight() {
        player.move(Movement.RIGHT);
        Point tilePos = collidesWithTileCoords(player.getX() + player.getWidth(), player.getY());
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
