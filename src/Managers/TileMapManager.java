package Managers;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import Game.GamePanel;
import Tile.*;


/**
    The TileMapeManager class loads and manages tile Images and "host" Sprites used in the game. 
    Game Sprites are cloned from "host" Sprites.
*/
public class TileMapManager {

    private ArrayList<Tile> tiles;
    private GamePanel panel;
    private static final int TILE_SIZE = 64;
    private static final String MAP_FOLDER = System.getProperty("user.dir") + 
        File.separator + "assets" + File.separator + "maps";

    public TileMapManager(GamePanel panel) {
        System.out.println("[TILEMAP MANAGER] Initialising");
	    this.panel = panel;
        loadTileImages();
        //loadCreatureSprites();
        //loadPowerUpSprites();
    }


    /* Methods */

    public TileMap loadMap(String filename) throws IOException {
        System.out.println("[TILEMAP MANAGER] Loading map: " + filename);
        ArrayList<String> lines = new ArrayList<String>();
        int mapWidth = 0;
        int mapHeight = 0;
        filename = TileMapManager.MAP_FOLDER + File.separator + filename;

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                mapWidth = Math.max(mapWidth, line.length());
            }
        }
        
        // parse the lines to create a TileMap
        mapHeight = lines.size();
        
        TileMap newMap = new TileMap(panel, mapWidth, mapHeight);
        for (int y = 0; y < mapHeight; y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                char ch = line.charAt(x);
                
                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, tiles.get(tile));
                }
                /*
                // check if the char represents a sprite
                else if (ch == 'o') {
                    addSprite(newMap, coinSprite, x, y);
                }
                else if (ch == '!') {
                    addSprite(newMap, musicSprite, x, y);
                }
                else if (ch == '*') {
                    addSprite(newMap, goalSprite, x, y);
                }
                else if (ch == '1') {
                    addSprite(newMap, grubSprite, x, y);
                }
                else if (ch == '2') {
                    addSprite(newMap, flySprite, x, y);
                }
                */
            }
        }
        
        return newMap;
    }

    
    public void loadTileImages() {
        tiles = new ArrayList<Tile>();
        char ch = 'A';
        while (true) {
            Image tileImage = ImageManager.getImage("tiles_tile_" + ch);
            if (tileImage == null) { break; } 
            else System.out.println("[TILEMAP MANAGER] Tile loaded: tile_" + ch);
            tiles.add(new Tile(tileImage, TILE_SIZE, true));
            if (ch == 'Z') { break; }
            ch++;
        }
    }

}
