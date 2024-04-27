package Tile;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import Game.GamePanel;
import Managers.ImageManager;

public class TMXReader {

    private String mapName;
    private String folderPath;
    private String tmxFilePath;
    private DocumentBuilderFactory dbFactory;
    private HashMap<Integer, Tile> tileSet;
    private int mapWidth, mapHeight;
    private TileMap tileMap;
    private GamePanel panel;
    public static String MAP_FOLDER = System.getProperty("user.dir") + File.separator + 
        "assets" + File.separator + "maps";

    public TMXReader(GamePanel panel) {
        this.panel = panel;
        this.dbFactory = DocumentBuilderFactory.newInstance();
        this.tileSet = new HashMap<>();
        ImageManager.getInstance();
    }

    public TMXReader(GamePanel panel, String mapName) {
        this(panel);
        this.mapName = mapName;
        this.folderPath = MAP_FOLDER + File.separator + mapName;
        this.tmxFilePath = folderPath + File.separator + mapName + ".tmx";
    }


    /* Accessors */

    public int getMapWidth() { return mapWidth; }

    public int getMapHeight() { return mapHeight; }

    public HashMap<Integer, Tile> getTileSet() { return tileSet; }

    public Tile getTile(int firstGID) { return tileSet.get(firstGID); }

    public TileMap getTileMap() { return tileMap; }


    /* Mutators */

    public void setMapName(String mapName) { 
        this.mapName = mapName; 
        this.folderPath = MAP_FOLDER + File.separator + mapName;
        this.tmxFilePath = folderPath + File.separator + mapName + ".tmx";
    }

    
    /* Methods */

    public void loadTMXTileMap() throws Exception {
        if (this.mapName == null) {
            throw new Exception("Map name is not set");
        }

        if (this.folderPath == null || this.tmxFilePath == null) {
            throw new Exception("Path to TMX file is not set");
        }

        if (!new File(tmxFilePath).exists()) {
            throw new Exception("TMX file does not exist: '" + tmxFilePath + "'");
        }

        loadTMXTileMap(this.mapName);
    }

    public void loadTMXTileMap(String mapName) throws Exception {
        System.out.println("[TMX READER] Loading TMX Map: " + mapName);
        if (this.folderPath == null || this.tmxFilePath == null) {
            this.folderPath = MAP_FOLDER + File.separator + mapName;
            this.tmxFilePath = folderPath + File.separator + mapName + ".tmx";
        }
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(tmxFilePath);

            // Normalize the XML structure and extract the <tileset> elements
            doc.getDocumentElement().normalize();
            NodeList tilesetList = doc.getElementsByTagName("tileset");

            // Get map dimensions from <map> element
            Element mapElement = (Element) doc.getElementsByTagName("map").item(0);
            mapWidth = Integer.parseInt(mapElement.getAttribute("width"));
            mapHeight = Integer.parseInt(mapElement.getAttribute("height"));

            
            // Load tile data from <tileset> elements
            for (int i = 0; i < tilesetList.getLength(); i++) {

                // Get the tile id from the <firstgid> attribute
                int firstGid = Integer.parseInt(tilesetList.item(i).getAttributes().
                    getNamedItem("firstgid").getNodeValue());

                // Get TSX resource from <source> attribute
                Element tilesetElement = (Element) tilesetList.item(i);
                String imagePath = tilesetElement.getAttribute("source");
                if (imagePath != null && !imagePath.isEmpty()) {
                    tileSet.put(firstGid, getTileFromTSXResource(folderPath + File.separator + imagePath));
                    continue;
                }

                // Get source to Tile image from nested <image> element
                NamedNodeMap attributes = tilesetElement.getElementsByTagName("image").
                    item(0).getAttributes();
                for (int j = 0; j < attributes.getLength(); j++) {
                    if (attributes.item(j).getNodeName().equals("source")) {
                        imagePath = attributes.item(j).getNodeValue();
                        break;
                    }
                }
                Image image = ImageManager.loadImage(folderPath + File.separator + imagePath);
                tileSet.put(firstGid, new Tile(image, true));
            }

            // Load tile data from <layer> elements
            loadTileMapLayers(doc);

        } catch (Exception e) { throw e; }

        System.out.println("[TMX READER] Done loading Map: " + mapName + " (" + mapWidth + "x" + mapHeight + ")" + 
            " with " + tileSet.size() + " tiles");
    }

    private Tile getTileFromTSXResource(String tsxFilePath) {
        Tile tile = null;
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(tsxFilePath);

            // Normalize the XML structure and extract the <image> element
            doc.getDocumentElement().normalize();
            NodeList tilesetList = doc.getElementsByTagName("image");

            // Load in tile specified in <image> element
            Element tilesetElement = (Element) tilesetList.item(0);
            String imagePath = tilesetElement.getAttribute("source");
            Image image = ImageManager.loadImage(folderPath + File.separator + imagePath);
            tile = new Tile(image, true);
            
        } catch (Exception e) { e.printStackTrace(); }
        
        return tile;
    }

    private void loadTileMapLayers(Document doc) {
        tileMap = new TileMap(panel, mapWidth, mapHeight);
        NodeList layerList = doc.getElementsByTagName("layer");
        
        // Loop through each layer in the TMX file
        for (int i = 0; i < layerList.getLength(); i++) {
            System.out.println("[TMX READER] Loading layer: " + layerList.item(i).getAttributes().
                getNamedItem("name").getNodeValue());
            Element layerElement = (Element) layerList.item(i);
        
            // Get the data element from the current layer
            NodeList dataList = layerElement.getElementsByTagName("data");
            Element dataElement = (Element) dataList.item(0);

            // Get the CSV data and split it into an array
            int[][] tileIDs = getTileIDsFromCSV(dataElement.getTextContent().strip());
            
            // Arrange the tiles onto the map
            for (int y = 0; y < mapHeight; y++) {
                for (int x = 0; x < mapWidth; x++) {
                    if (tileIDs[y][x] == 0) { continue; }
                    System.out.println(" > " + tileIDs[y][x] + " @ " + x + ", " + y);
                    tileMap.setTile(x, y, tileSet.get(tileIDs[y][x]));
                }
            }
        }
    }

    private int[][] getTileIDsFromCSV(String csv) {
        String[] rows = csv.split("\n");
        int[][] tileIDs = new int[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] columns = rows[i].split(",");
            tileIDs[i] = new int[columns.length];
            for (int j = 0; j < columns.length; j++) {
                tileIDs[i][j] = Integer.parseInt(columns[j].trim());
            }
        }
        return tileIDs;
    }


    public static void main(String[] args) {
        try {
            GamePanel panel = new GamePanel();
            TMXReader tmxReader = new TMXReader(panel);
            tmxReader.loadTMXTileMap("ForestFrenzy");
        } catch (Exception e) { e.printStackTrace(); }
    }
}