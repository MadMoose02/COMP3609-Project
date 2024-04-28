package Managers;


import java.util.ArrayList;

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


    public ArrayList<TileMap> loadTileMaps() {
        String[] mapNames = {
            "ForestFrenzy", "WinterWasteland", "GraveyardShift", "MysteryCastle"
        };

        ArrayList<TileMap> maps = new ArrayList<>();
        for (String mapName : mapNames) {
            try { 
                tmxReader.loadTMXTileMap(mapName);
                maps.add(tmxReader.getTileMap());

            } catch (Exception e) { e.printStackTrace(); }
        }

        return maps;
    }

}
