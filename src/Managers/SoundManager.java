package Managers;

import java.io.*;
import java.util.HashMap;
import javax.sound.sampled.*;
import javax.sound.sampled.FloatControl.Type;

import Game.DataKey;


/**
 * SoundManager.java <hr>
 * Extended from Game Programming lab content
 */
public class SoundManager {
	private static SoundManager instance = null;
	private static HashMap<String, Clip> sfx = null;
	private static HashMap<String, Clip> music = null;
    private static HashMap<DataKey, Float> volume = null;
    private static String currentMusic = null;
    private static final String SOUND_FOLDER = System.getProperty("user.dir") + 
        File.separator + "assets" + File.separator + "sounds";

	private SoundManager () {
        System.out.println("[SOUND MANAGER] Initialising");
		sfx = new HashMap<String, Clip>();
		music = new HashMap<String, Clip>();
        volume = new HashMap<DataKey, Float>();
        volume.put(DataKey.MUSIC, SaveDataManager.get(DataKey.MUSIC));
        volume.put(DataKey.SFX, SaveDataManager.get(DataKey.SFX));
	}

	
    /* Accessors */

    private static Clip getClip(DataKey key, String title) {
        if (key == DataKey.MUSIC) return music.get(title);
        if (key == DataKey.SFX)   return sfx.get(title);
        System.out.println("[SOUND MANAGER] No such clip: " + title);
        return null;
    }

    public static int getFramePosition(DataKey key, String title) {
        Clip clip = getClip(key, title);
        if (clip != null) return clip.getFramePosition();
        System.out.println("[SOUND MANAGER] No such clip: " + title);
        return 0;
    }
	
    public static float getVolume(DataKey key) { return volume.get(key); }


    /* Mutators */

	public static void setVolume(DataKey key, float volume) { gainControl(key, volume); }
	

    /* Methods */

    public static synchronized SoundManager getInstance() {
        SaveDataManager.getInstance();
		if (instance == null) {
            instance = new SoundManager();
            try { loadDefaultClips(); }
            catch (Exception e) { System.out.println("[ERROR] " + e); }
        }
		return instance;
	}

	private static void loadDefaultClips() throws Exception {
        File musicFolder = new File(SOUND_FOLDER + File.separator + "music");
        File sfxFolder = new File(SOUND_FOLDER + File.separator + "sfx");

        // Check if music folder exists and is not empty
        if (!musicFolder.exists()) throw new FileNotFoundException("Music folder does not exist");
        if (musicFolder.list().length == 0) throw new FileNotFoundException("Music folder is empty");

        // Check if sfx folder exists and is not empty
        if (!sfxFolder.exists()) throw new FileNotFoundException("SFX folder does not exist");
        if (sfxFolder.list().length == 0) throw new FileNotFoundException("SFX folder is empty");
        
        // Load all audio files into HashMap
        for (String fileName : musicFolder.list()) {
            loadClip(DataKey.MUSIC, fileName);
            music.put(
                fileName.split("\\.")[0].toLowerCase(), 
                getClip(DataKey.MUSIC, fileName)
            );
        }
        for (String fileName : sfxFolder.list()) {
            loadClip(DataKey.SFX, fileName);
            sfx.put(
                fileName.split("\\.")[0].toLowerCase(),
                getClip(DataKey.SFX, fileName)
            );
        }
    }

    private static void loadClip (DataKey key, String fileName) {
        try {
            Clip clip = null;
            String folder = key == DataKey.MUSIC ? "music" : "sfx";
            File file = new File(SOUND_FOLDER + File.separator + folder + File.separator + fileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL());
            clip = AudioSystem.getClip();
            clip.open(audioIn);
		}
		catch (Exception e) {
 			System.out.println ("[SOUND MANAGER] " + e);
		}
    }

    public static void playMusicClip(String title, boolean loop) {
        Clip clip = getClip(DataKey.MUSIC, title);
        if (clip != null) {
            clip.setFramePosition(0);
            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            else      clip.start();
            setVolume(DataKey.MUSIC, SaveDataManager.get(DataKey.MUSIC));
            currentMusic = title;
            System.out.println("[SOUND MANAGER] Playing: " + title);
        }
    }

    public static void stopMusicClip() {
        Clip clip = getClip(DataKey.MUSIC, currentMusic);
        if (clip != null) {
            clip.stop();
            System.out.println("[SOUND MANAGER] Stopped: " + currentMusic);
        }
        currentMusic = null;
    }
    
    public static void stopMusicClip(String title) {
        Clip clip = getClip(DataKey.MUSIC, title);
        if (clip != null) {
            clip.stop();
            System.out.println("[SOUND MANAGER] Stopped: " + currentMusic);
        }
    }

    public static void resumeClip(String title, boolean loop, int framePosition) {
        Clip clip = getClip(DataKey.MUSIC, title);
        if (clip != null) {
            clip.setFramePosition(framePosition);
            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            else      clip.start();
            setVolume(DataKey.SFX, SaveDataManager.get(DataKey.SFX));
            System.out.println("[SOUND MANAGER] Resumed: " + title);
        }
        
    }

    public static void playSFXClip(String title, boolean loop) {
        Clip clip = getClip(DataKey.SFX, title);
        if (clip != null) {
            clip.setFramePosition(0);
            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            else      clip.start();
            setVolume(DataKey.SFX, SaveDataManager.get(DataKey.SFX));
            System.out.println("[SOUND MANAGER] Playing: " + title);
        }
    }

    public static void stopSFXClip(String title) {
        Clip clip = getClip(DataKey.SFX, title);
        if (clip != null) {
            clip.stop();
            System.out.println("[SOUND MANAGER] Stopped: " + title);
        }
    }
    
    public static void stopAllClips() {
        for (String title : music.keySet()) stopMusicClip(title);
        for (String title : sfx.keySet()) stopSFXClip(title);
    }

    private static void gainControl(DataKey key, float volume) {
        
        // Set volume
        volume = ((volume < 0) ? 0 : ((volume > 1) ? 1 : volume));
        SoundManager.volume.put(key, volume);

        // Set gain control
        switch (key) {
            case MUSIC:
                Clip clip = music.get(currentMusic);
                if (clip != null) {
                    FloatControl gainControl = (FloatControl) clip.getControl(Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(volume));
                }
                break;

            case SFX:
                for (Clip c : sfx.values()) {
                    FloatControl gainControl = (FloatControl) c.getControl(Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(volume));
                }
                break;
        
            default:
                System.out.println("[SOUND MANAGER] Attempted to set invalid volume key: " + 
                    key.toString());
                break;
        }
        
        System.out.println("[SOUND MANAGER] " + key.toString() +  " ==> " + volume);
        SaveDataManager.set(key, volume);
    }
}