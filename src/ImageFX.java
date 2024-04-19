/**
 * ImageFX.java
 */

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public interface ImageFX {
	public void update();
	public void draw (Graphics2D g2d, AffineTransform at);
}