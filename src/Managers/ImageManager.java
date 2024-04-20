package Managers;
/**
 * ImageManager.java
 * Extended from Game Programming lab content
 */

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
   The ImageManager class manages the loading and processing of any image files.
*/
public class ImageManager {
	private static ImageManager instance = null;
    private static HashMap<String, Image> imgs = null;
    private static final String IMAGE_FOLDER = System.getProperty("user.dir") + File.separator + "assets" +  File.separator + "images";
    private static final String[] validExtensions = new String[] { "jpg", "jpeg", "png", "gif" };

    private ImageManager () {
        System.out.println("[IMAGE MANAGER] Initialising");
        ImageManager.imgs = new HashMap<String, Image>();
    }
    

    /* Accessors */

    public static HashMap<String, Image> getImagesHM() { return ImageManager.imgs; }
    
    /**
     * **Note:** the image is loaded from the "assets/images" folder. All image names follow the path
     * to the image from "assets/images/" without the extension.
     * 
     * @param name  the path to the image from "assets/images/" without the extension
     * @return      the image if it exists, null otherwise
     */
    public static Image getImage(String name) { return ImageManager.imgs.get(name); }


    /* Methods */

    public static synchronized ImageManager getInstance() {
        if (ImageManager.instance == null) {
            ImageManager.instance = new ImageManager();
            try { ImageManager.loadDefaultImages(); }
            catch (Exception e) { System.out.println("[ERROR] " + e); }
        }
        return ImageManager.instance;
    }

    private static void loadDefaultImages() throws Exception {
        try {
            loadImagesFromFolder("");
            System.out.println("[IMAGE MANAGER] Finished loading " + ImageManager.imgs.size() + " images");
        } catch (Exception e) {
            System.out.println("[ERROR] " + e);
            throw e;
        }
    }

    private static void loadImagesFromFolder(String folderName) throws Exception {
        System.out.println("[IMAGE MANAGER] Loading folder: " + (folderName.equals("") ? "base" : folderName));
        String path = IMAGE_FOLDER + (folderName.equals("") ? "" : File.separator + folderName);
        
        File file = new File(path);
        if (!file.exists()) throw new FileNotFoundException("Folder '" + path + "' does not exist");
        
        String[] files = file.list();
        if (files.length == 0 || files == null) throw new FileNotFoundException("Folder '" + path + "' is empty");
        
        for (String fileName : files) {
            if (!fileName.contains(".")) {
                loadImagesFromFolder(folderName + File.separator + fileName);
                continue;
            }
            if (!isExtensionValid(fileName.split("\\.")[1])) continue;
            String key = (folderName + File.separator + fileName.split("\\.")[0]).substring(1).replace(File.separatorChar, '_');
            ImageManager.imgs.put(
                key,
                loadImage(IMAGE_FOLDER + File.separator + folderName + File.separator + fileName)
            );
            System.out.println("[IMAGE MANAGER] Added: " + key);
        }
    }

    private static boolean isExtensionValid(String extension) {
        if (extension == null) return false;
        for (String validExtension : ImageManager.validExtensions) {
            if (validExtension.equals(extension)) return true;
        }
        return false;
    }

	public static Image loadImage (String path) {
		return new ImageIcon(path).getImage();
	}
    
    public static ImageIcon loadImageIcon (String path) {
        return new ImageIcon(path);
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

    public static Image flipImageHorizontally(Image img) {
        BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
    
        // Create the transform
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
    
        // Apply the transform
        AffineTransformOp atOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return Toolkit.getDefaultToolkit().createImage(atOp.filter(image, null).getSource());
    }
}