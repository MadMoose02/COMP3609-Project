package Managers;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


/**
 * FontManager.java <hr>
 * Manages the usage of custom fonts.
 */
public class FontManager {
    
    private static FontManager instance;
    private static HashMap<String, Font> fonts;
    private static final String FONTS_FOLDER = System.getProperty("user.dir") + 
        File.separator + "assets" + File.separator + "fonts";

    public FontManager() {
        System.out.println("[FONT MANAGER] Initialising");
        fonts = new HashMap<>();
    }

    public static synchronized FontManager getInstance() {
        if (fonts == null) {
            instance = new FontManager();
            try { loadDefaultFonts(); }
            catch (Exception e) { System.out.println(e); }
        }
        return instance;
    }

    public static void loadDefaultFonts() {
        File folder = new File(FONTS_FOLDER);
        for (File file : folder.listFiles()) {
            String fontName = file.getName().split("\\.")[0];
            String ext = file.getName().split("\\.")[1].toLowerCase();
            switch (ext) {

                case "ttf":
                    fonts.put(fontName, loadTTF(fontName + ".ttf"));
                    break;

                case "otf":
                    fonts.put(fontName, loadOTF(fontName + ".otf"));
                    break;

                default:
                    throw new IllegalArgumentException("[FONT MANAGER] Unsupported font format: " + ext);
            }
        }
    }

    public static Font loadTTF(String filename) {
        File file = new File(FONTS_FOLDER + File.separator + filename);
        Font customFont = null;
        try { customFont = Font.createFont(Font.TRUETYPE_FONT, file); } 
        catch (FontFormatException | IOException e) { e.printStackTrace(); }
        System.out.println("[FONT MANAGER] Font loaded: " + filename);
        return customFont;
    }
    
    public static Font loadOTF(String filename) {
        File file = new File(FONTS_FOLDER + File.separator + filename);
        Font customFont = null;
        try { customFont = Font.createFont(Font.TRUETYPE_FONT, file); } 
        catch (FontFormatException | IOException e) { e.printStackTrace(); }
        System.out.println("[FONT MANAGER] Font loaded: " + filename);
        return customFont;
    }

    public static Font getFont(String fontName) {
        if (!fonts.containsKey(fontName)) {
            System.out.println("[FONT MANAGER] No such font exists: " + fontName);
        }
        return fonts.get(fontName);
    }
}
