/**
 * SoundManager.java
 * Extended from Game Programming lab content
 */

import java.io.*;
import java.util.HashMap;
import javax.sound.sampled.*;

/*
 * The SoundManager class manages the loading and processing of any audio files.
 */
public class SoundManager {
	private static SoundManager instance          = null;
	private static HashMap<String, Clip> music    = null;
	private static HashMap<String, Clip> sfx      = null;
    private static HashMap<DataKey, Float> volume = null;
    private static String currentMusic            = null;
    private static final String AUDIO_FOLDER      = System.getProperty("user.dir") + File.separator + 
                                                    "assets" + File.separator + "sounds";

	private SoundManager () {
        System.out.println("[SOUND MANAGER] Initialising");
		SoundManager.music  = new HashMap<String, Clip>();
		SoundManager.sfx    = new HashMap<String, Clip>();
        SoundManager.volume = new HashMap<DataKey, Float>();
        SoundManager.volume.put(DataKey.MUSIC_VOLUME, SaveDataManager.get(DataKey.MUSIC_VOLUME));
        SoundManager.volume.put(DataKey.SFX_VOLUME,   SaveDataManager.get(DataKey.SFX_VOLUME));
	}

    public static synchronized SoundManager getInstance() {
        SaveDataManager.getInstance();
		if (instance == null) {
            SoundManager.instance = new SoundManager();
            try { SoundManager.loadDefaultClips(); }
            catch (Exception e) { System.out.println(e); }
        }
		return SoundManager.instance;
	}

	private static void  loadDefaultClips() throws Exception {
        File musicFolder = new File(AUDIO_FOLDER + File.separator + "music");
        File sfxFolder   = new File(AUDIO_FOLDER + File.separator + "sfx");

        // Check if music folder exists and is not empty
        if (!musicFolder.exists()) throw new FileNotFoundException("Music folder does not exist");
        if (musicFolder.list().length == 0) throw new FileNotFoundException("Music folder is empty");

        // Check if sfx folder exists and is not empty
        if (!sfxFolder.exists()) throw new FileNotFoundException("SFX folder does not exist");
        if (sfxFolder.list().length == 0) throw new FileNotFoundException("SFX folder is empty");
        
        // Load all audio files into HashMap
        for (String fileName : musicFolder.list()) 
            SoundManager.music.put(fileName.split("\\.")[0].toLowerCase(), SoundManager.loadClip(DataKey.MUSIC_VOLUME, fileName));
        for (String fileName : sfxFolder.list())
            SoundManager.sfx.put(fileName.split("\\.")[0].toLowerCase(), SoundManager.loadClip(DataKey.SFX_VOLUME, fileName));
    }

	private static Clip  getClip(DataKey key, String title) {
        if (key == DataKey.MUSIC_VOLUME) return SoundManager.music.get(title);
        if (key == DataKey.SFX_VOLUME)   return SoundManager.sfx.get(title);
        System.out.println("[SOUND MANAGER] No such clip: " + title);
        return null;
    }

    public static  int  getFramePosition(DataKey key, String title) {
        Clip clip = SoundManager.getClip(key, title);
        if (clip != null) return clip.getFramePosition();
        System.out.println("[SOUND MANAGER] No such clip: " + title);
        return 0;
    }
	
    public static  float getVolume(DataKey key) { return SoundManager.volume.get(key); }

	public static  void  setVolume(DataKey key, float volume) { SoundManager.gainControl(key, volume); }
	
    private static Clip  loadClip (DataKey key, String fileName) {
        Clip clip = null;
		try {
            String folder = key == DataKey.MUSIC_VOLUME ? "music" : "sfx";
            File file = new File(AUDIO_FOLDER + File.separator + folder + File.separator + fileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL());
            clip = AudioSystem.getClip();
            clip.open(audioIn);
		}
		catch (Exception e) {
 			System.out.println ("[SOUND MANAGER] " + e);
		}
        return clip;
    }

    public static  void  playMusicClip(String title, boolean loop) {
        Clip clip = SoundManager.getClip(DataKey.MUSIC_VOLUME, title);
        if (clip != null) {
            clip.setFramePosition(0);
            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            else      clip.start();
            SoundManager.setVolume(DataKey.MUSIC_VOLUME, SaveDataManager.get(DataKey.MUSIC_VOLUME));
            SoundManager.currentMusic = title;
            System.out.println("[SOUND MANAGER] Playing: " + title);
        }
    }

    public static  void  stopMusicClip() {
        Clip clip = SoundManager.getClip(DataKey.MUSIC_VOLUME, SoundManager.currentMusic);
        if (clip != null) {
            clip.stop();
            System.out.println("[SOUND MANAGER] Stopped: " + currentMusic);
        }
        SoundManager.currentMusic = null;
    }
    
    public static  void  stopMusicClip(String title) {
        Clip clip = SoundManager.getClip(DataKey.MUSIC_VOLUME, title);
        if (clip != null) {
            clip.stop();
            System.out.println("[SOUND MANAGER] Stopped: " + currentMusic);
        }
    }

    public static  void  resumeClip(String title, boolean loop, int framePosition) {
        Clip clip = SoundManager.getClip(DataKey.MUSIC_VOLUME, title);
        if (clip != null) {
            clip.setFramePosition(framePosition);
            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            else      clip.start();
            SoundManager.setVolume(DataKey.SFX_VOLUME, SaveDataManager.get(DataKey.SFX_VOLUME));
            System.out.println("[SOUND MANAGER] Resumed: " + title);
        }
        
    }

    public static  void  playSFXClip(String title, boolean loop) {
        Clip clip = SoundManager.getClip(DataKey.SFX_VOLUME, title);
        if (clip != null) {
            clip.setFramePosition(0);
            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            else      clip.start();
            SoundManager.setVolume(DataKey.SFX_VOLUME, SaveDataManager.get(DataKey.SFX_VOLUME));
            System.out.println("[SOUND MANAGER] Playing: " + title);
        }
    }

    public static  void  stopSFXClip(String title) {
        Clip clip = SoundManager.getClip(DataKey.SFX_VOLUME, title);
        if (clip != null) {
            clip.stop();
            System.out.println("[SOUND MANAGER] Stopped: " + title);
        }
    }
    
    public static  void  stopAllClips() {
        for (String title : SoundManager.music.keySet()) SoundManager.stopMusicClip(title);
        for (String title : SoundManager.sfx.keySet()) SoundManager.stopSFXClip(title);
    }

    private static void  gainControl(DataKey key, float volume) {
        
        // Set volume
        volume = ((volume < 0) ? 0 : ((volume > 1) ? 1 : volume));
        SoundManager.volume.put(key, volume);

        // Set gain control
        switch (key) {
            case MUSIC_VOLUME:
                Clip clip = SoundManager.music.get(currentMusic);
                if (clip != null) {
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(volume));
                }
                break;

            case SFX_VOLUME:
                for (Clip c : SoundManager.sfx.values()) {
                    FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(volume));
                }
                break;
        
            default:
                System.out.println("[SOUND MANAGER] Attempted to set invalid volume key: " + key.toString());
                break;
        }
        
        System.out.println("[SOUND MANAGER] " + key.toString() +  " ==> " + volume);
        SaveDataManager.set(key, volume);
    }
}