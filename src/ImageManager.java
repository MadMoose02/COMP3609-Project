/**
 * ImageManager.java
 * Extended from Game Programming lab content
 */

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.awt.image.BufferedImage;

/**
   The ImageManager class manages the loading and processing of any image files.
*/
public class ImageManager {
	private static ImageManager instance                   = null;
    private static HashMap<String, Image> imgs             = null;
    private static HashMap<String, BufferedImage> buffImgs = null;
    private static final String IMAGE_FOLDER               = System.getProperty("user.dir") + 
                                                             File.separator + "assets" +  
                                                             File.separator + "images";

    private ImageManager () {
        System.out.println("[IMAGE MANAGER] Initialising");
        ImageManager.imgs = new HashMap<String, Image>();
        ImageManager.buffImgs = new HashMap<String, BufferedImage>();
    }

    public static synchronized ImageManager getInstance() {
        if (ImageManager.instance == null) {
            ImageManager.instance = new ImageManager();
            try { ImageManager.loadDefaultImages(); }
            catch (Exception e) { System.out.println(e); }
        }
        return ImageManager.instance;
    }
    

    /* Accessors */
    public static HashMap<String, BufferedImage> getBufferedImagesHM() { return ImageManager.buffImgs; }

    public static HashMap<String, Image> getImagesHM() { return ImageManager.imgs; }
    
    public static BufferedImage getBufferedImage(String name) { return ImageManager.buffImgs.get(name); }
    
    public static Image getImage(String name) { return ImageManager.imgs.get(name); }

    private static void loadDefaultImages() throws Exception {
        File file = new File(IMAGE_FOLDER);
        if (!file.exists()) throw new FileNotFoundException("IMAGE_FOLDER does not exist");
        if (file.list().length == 0) throw new FileNotFoundException("IMAGE_FOLDER is empty");
        
        // Load all image files into HashMap
        for (String fileName : file.list()) {
            ImageManager.imgs.put(fileName.split("\\.")[0].toLowerCase(), ImageManager.loadImage(fileName));
            ImageManager.buffImgs.put(fileName.split("\\.")[0].toLowerCase(), ImageManager.loadBufferedImage(fileName));
        }
    }

	public static Image loadImage (String fileName) {
		return new ImageIcon(IMAGE_FOLDER + File.separator + fileName).getImage();
	}

    public static ImageIcon loadImageIcon (String fileName) {
        return new ImageIcon(IMAGE_FOLDER + File.separator + fileName);
    }

	public static BufferedImage loadBufferedImage(String filename) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File (IMAGE_FOLDER + File.separator + filename));
		} catch (Exception e) {
			System.out.println ("[IMAGE MANAGER] " + e);
		}
		return bi;
	}

	public static BufferedImage copyImage(BufferedImage src) {
		if (src == null) return null;
		BufferedImage copy = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = copy.createGraphics();
        g2d.drawImage(src, 0, 0, null);
        g2d.dispose();
        return copy; 
    }

}