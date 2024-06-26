package Game;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JPanel;

public class Background {
  	private Image bgImage;
  	private int bgImageWidth;
 	private int bgX;
	private int backgroundX;
	private int backgroundX2;
	private int bgDX;
    

	public Background(JPanel panel, Image img, int bgDX) {
        this.bgImage = img;
        bgImageWidth = bgImage.getWidth(null);
        this.bgDX = bgDX;
  	}


  	public void moveRight() {

		if (bgX == 0) {
			backgroundX = 0;
			backgroundX2 = bgImageWidth;			
		}

		bgX -= bgDX;

		backgroundX = backgroundX - bgDX;
		backgroundX2 = backgroundX2 - bgDX;

		if ((bgX + bgImageWidth) % bgImageWidth == 0) {
			backgroundX = 0;
			backgroundX2 = bgImageWidth;
		}

  	}


  	public void moveLeft() {
	
		if (bgX == 0) {
			backgroundX = bgImageWidth * -1;
			backgroundX2 = 0;			
		}

		bgX += bgDX;
				
		backgroundX = backgroundX + bgDX;	
		backgroundX2 = backgroundX2 + bgDX;

		if ((bgX + bgImageWidth) % bgImageWidth == 0) {
			backgroundX = bgImageWidth * -1;
			backgroundX2 = 0;
		}			
   	}
 

  	public void draw (Graphics2D g2) {
		g2.drawImage(bgImage, backgroundX, 0, null);
		g2.drawImage(bgImage, backgroundX2, 0, null);
  	}
}
