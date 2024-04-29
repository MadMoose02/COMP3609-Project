package Managers;

import Game.GamePanel;
import Tile.*;

/**
 * TileMapManager.java <hr>
 * Extended from Game Programming lab content
 * Manages tile Images and "hosts" the Sprites used in the game.
 */
public class TileMapManager {

    private TMXReader tmxReader;

    public TileMapManager(GamePanel panel) {
        System.out.println("[TILEMAP MANAGER] Initialising");
        this.tmxReader = new TMXReader(panel);
    }


    /* Methods */


    public TileMap loadTileMap(String name) {
        TileMap map = null;
        try { 
            tmxReader.loadTMXTileMap(name);
            map = tmxReader.getTileMap();

        } catch (Exception e) { e.printStackTrace(); }

        return map;
    }

}
