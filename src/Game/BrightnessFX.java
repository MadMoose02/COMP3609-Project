package Game;
import Managers.*;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class BrightnessFX implements ImageFX {
	private int width;		// width of the image
	private int height;		// height of the image

	private int x;
	private int y;

	private BufferedImage spriteImage;		// image for sprite effect
	private BufferedImage copy;			// copy of image

	Graphics2D g2;

	int brightness, brightnessChange;		// to alter the brightness of the image

	public BrightnessFX (String filename, int width, int height, int ypos, int xpos) {
		this.x = xpos;
		this.y = ypos;
		this.width = width;
		this.height = height;

		brightness = -150;				// range is -255 to 255; negative values darken the
							// image and positive values brighten the image
 
		brightnessChange = 0;			// increase of brightness in each update

		spriteImage = ImageManager.loadBufferedImage(filename);
	}


	private int truncate (int colourValue) {	// keeps colourValue within [0..255] range
		if (colourValue > 255)
			return 255;

		if (colourValue < 0)
			return 0;

		return colourValue;
	}


	private int brighten (int pixel) {

    	int alpha, red, green, blue;
		int newPixel;
		
		alpha = (pixel >> 24) & 255;
		red = (pixel >> 16) & 255;
		green = (pixel >> 8) & 255;
		blue = pixel & 255;

		// Increase or decrease the value of the RGB components based on the value of brightness

		red = red + brightness;
		green = green + brightness;
		blue = blue + brightness;

		// Check the boundaries for 8-bit RGB components [0..255]

		red = truncate (red);
		green = truncate (green);
		blue = truncate (blue);
		
		newPixel = blue | (green << 8) | (red << 16) | (alpha << 24);

		return newPixel;
	}


	public void draw (Graphics2D g2) {

		copy = ImageManager.copyImage(spriteImage);		
							// make copy of image for brightness effect

		int imWidth = copy.getWidth();
		int imHeight = copy.getHeight();

    	int [] pixels = new int[imWidth * imHeight];
    	copy.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

		for (int i=0; i<pixels.length; i++) {
			pixels[i] = brighten(pixels[i]);
		}

    	copy.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);	

		g2.drawImage(copy, x, y, width, height, null);

	}


	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, width, height);
	}


	@Override
	public void update() {				// modify brightness (-255 to 255)
	
		brightness = brightness + brightnessChange;

		if (brightness > 255) {
			brightness = 255;
			brightnessChange = -1 * brightnessChange;
		}
		else
		if (brightness < -255) {
			brightness = -255;
			brightnessChange = -1 * brightnessChange;
		}		
	}

	@Override
	public void draw(Graphics2D g2d, AffineTransform at) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'draw'");
	}


	
}
