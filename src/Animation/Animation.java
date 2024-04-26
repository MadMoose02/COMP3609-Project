package Animation;
import java.awt.Image;
import java.util.ArrayList;


/**
    The Animation class manages a series of images (frames) and
    the amount of time to display each frame.
*/
public class Animation {

    /** name of the animation */
    protected String name;
    /** collection of frames for this animation */
    protected ArrayList<AnimFrame> frames;    
    /** current frame being displayed */
    protected int currFrameIndex;			    
    /** time that the animation has run for already */
    protected double animTime;			        
    /** start time of the animation or time since last update */
    protected double startTime;					
    /** total duration of the animation in milliseconds */
    protected double animationRuntime;			    
    /** if the animation should loop */
    protected boolean loop;                   
    /** if the animation is active */
    protected boolean isActive;


    public Animation(String name, boolean loop) {
        this.frames = new ArrayList<AnimFrame>();
        this.name = name;
        this.loop = loop;
        this.isActive = false;
        this.animationRuntime = 0;
    }

    /** Accessors */

    /**
     * Gets this Animation's current image. Returns null if this animation has no images.
     */
    public synchronized Image getImage() {
        return (frames.size() == 0) ? null : getFrame(currFrameIndex).image;
    }

    /**
     * Gets the name of this animation.
     */
    public String getName() { return name; }

    /**
     * Gets the runtime duration of this animation in seconds.
     * 
     * @return duration in seconds
     */
    public double getDuration() { return animationRuntime/1000; }
    
    /**
     * Gets the runtime duration of this animation in milliseconds.
     * 
     * @return duration in milliseconds
     */
    public double getDurationMillis() { return animationRuntime; }
    
    /**
     * Gets the number of frames in this animation.
     */
    public int getNumFrames() {	return frames.size(); }

    /**
     * Gets the current frame index.
     * 
     * @return current frame index
     */
    public int getCurrFrameIndex() { return currFrameIndex; }

    /**
     * Gets the frame at the specified index.
     * 
     * @param i index of frame in animation
     * @return  frame at specified index
     */
    private AnimFrame getFrame(int i) { return frames.get(i); }

    /**
     * Checks if the animation is still active (i.e. still has more frames to display).
     * 
     * @return true if the animation is still active, false otherwise
     */
    public boolean isStillActive() { return isActive; }


    /** Mutators */

    /** 
     * Adds an image to the animation with the specified duration (time to display the image).
     * 
     * @param image    image (AKA frame) to add to animation
     * @param duration time (in milliseconds) to display the image
     */
    public synchronized void addFrame(Image image, double duration) {
        animationRuntime += duration;
        frames.add(new AnimFrame(image, animationRuntime));
    }


    /** Methods */

    /**
     * Starts this animation over from the beginning.
     */
    public synchronized void start() {
	    isActive = true;
        animTime = currFrameIndex = 0;
	    startTime = System.currentTimeMillis();
        System.out.println("[ANIMATION] Started animation: " + name);
    }

    /**
     * Terminates this animation.
     */
    public synchronized void stop() { 
        isActive = false;
        System.out.println("[ANIMATION] Stopped animation: " + name);
    }

    /**
     * Updates this animation's current image (frame), if neccesary.
     */
    public synchronized void update() {
	    if (!isActive) { return; }
        double ellapsedTime = System.currentTimeMillis() - startTime;

        // if no frames in animation to display
        if (frames.size() == 0) { return; }
        animTime += ellapsedTime;                           // add elapsed time to amount of time animation has run for
        if (animTime >= animationRuntime) {			        // if the time animation has run for > total duration
            if (loop) {
                animTime = animTime % animationRuntime;	    // reset time animation has run for
                currFrameIndex = 0;				            // reset current frame to first frame
            
            } else { isActive = false; }					// animation is finished
        }

        // animation is no longer active
        if (!isActive) { return; }
        
        // set frame corresponding to time animation has run for
        while (animTime > getFrame(currFrameIndex).endTime) {
            currFrameIndex++;
        }
    }


    /**
     * Represents an Animation Frame
     */
    private class AnimFrame {

        /** Image for this particular frame */
        Image image;
        /** Time (in milliseconds) when screen time of this frame expires */
        double endTime;

        public AnimFrame(Image image, double endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }
}
