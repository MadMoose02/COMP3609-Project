package Animation;

import java.awt.Image;

import Managers.ImageManager;

public class PlayerAnimation extends Animation {

    private double frameDuration;

    public PlayerAnimation(String name, long frameDuration) {
        super(name, false);
        this.frameDuration = frameDuration;
        loadPlayerAnimation();
    }

    private void loadPlayerAnimation() {
        int idx = 1;
        while (true) {
            Image img = ImageManager.getImage(name + '_' + idx);
            if (img == null) break;
            addFrame(img, frameDuration);
            idx++;
        }
    }
}
