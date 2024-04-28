package Game;
import Managers.*;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class DisintegrateFX implements ImageFX {

	private int width;		// width of the image
	private int height;		// height of the image

	private int x;
	private int y;

	private BufferedImage spriteImage;		// image for sprite effect
	private BufferedImage copy;			// copy of image

	Graphics2D g2;

	int time, timeChange;				// to control when the image is grayed


	public DisintegrateFX (String filename, int width, int height, int ypos, int xpos) {
		this.x = xpos;
		this.y = ypos;
		this.width = width;
		this.height = height;

		time = 0;				// range is 0 to 70
		timeChange = 1;				// how to increment time in game loop

		spriteImage = ImageManager.loadBufferedImage(filename);
		copy = ImageManager.copyImage(spriteImage);		
							//  make a copy of the original image

	}


  	public void eraseImageParts(BufferedImage im, int interval) {

    		int imWidth = im.getWidth();
    		int imHeight = im.getHeight();

    		int [] pixels = new int[imWidth * imHeight];
    		im.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

		for (int i = 0; i < pixels.length; i = i + interval) {
      			pixels[i] = 0;    // make transparent (or black if no alpha)
		}
  
    		im.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
  	}


	public void draw (Graphics2D g2) {

		if (time == 10)
			eraseImageParts(copy, 11);
		else
		if (time == 20)
			eraseImageParts(copy, 7);
		else
		if (time == 30)
			eraseImageParts(copy, 5);
		else
		if (time == 40)
			eraseImageParts(copy, 3);
		else
		if (time == 50)
			eraseImageParts(copy, 2);
		else
		if (time == 60)
			eraseImageParts(copy, 1);
		else
		if (time == 70)
			copy = ImageManager.copyImage(spriteImage);

		g2.drawImage(copy, x, y, width, height, null);

	}


	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, width, height);
	}


	public void update() {				// modify time
	
		time = time + timeChange;

		if (time > 70)			
			time = 0;
	}


    @Override
    public void draw(Graphics2D g2d, AffineTransform at) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

}