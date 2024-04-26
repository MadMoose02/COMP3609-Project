package Managers;

import java.io.*;
import java.awt.*;
import java.util.ArrayList;

import Game.GamePanel;
import Tile.*;



/**
 * TileMapManager.java <hr>
 * Extended from Game Programming lab content
 * Manages tile Images and "hosts" the Sprites used in the game.
 */
public class TileMapManager {

    private GamePanel panel;
    private ArrayList<Image> tileImages;
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
                if (tile >= 0 && tile < tileImages.size()) {
                    Tile newTile = new Tile(tileImages.get(tile), true);
                    newMap.setTile(x, y, newTile);
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
        tileImages = new ArrayList<>();
        char ch = 'A';
        while (true) {
            Image tileImage = ImageManager.getImage("tiles_tile_" + ch);
            if (tileImage == null) { break; } 
            else System.out.println("[TILEMAP MANAGER] Tile loaded: tile_" + ch);
            tileImages.add(tileImage.getScaledInstance(GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, Image.SCALE_SMOOTH));
            if (ch == 'Z') { break; }
            ch++;
        }
    }

}
