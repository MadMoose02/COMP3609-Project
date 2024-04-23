package Managers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Game.DataKey;


/**
 * SaveDataManager.java <hr>
 * Manages the saving and loading of player related persitent game data.
 */
public class SaveDataManager {
    
    private static SaveDataManager instance = null;
    private static HashMap<DataKey, Float> data = null;

    public SaveDataManager() {
        System.out.println("[SAVE MANAGER] Initialising");
        data = new HashMap<DataKey, Float>();
    }

    
    /* Accessors */

    public static Float get(DataKey key) { 
        if (!data.containsKey(key)) { System.out.println("[SAVE MANAGER] No such key: " + key); }
        return data.get(key); 
    }


    /* Mutators */
    public static void set(DataKey key, Float value) { data.put(key, value); }


    /* Methods */

    public static SaveDataManager getInstance() {
        if (instance == null) {
            instance = new SaveDataManager();
            readSaveData();
        }
        return instance;
    }
    
    public static void writeSaveData() {
        try {
            File saveFile = new File("save.dat");
            ArrayList<String> lines = readFileIntoList(saveFile);
            
            // Check for missing keys
            HashMap<DataKey, Float> toAdd = new HashMap<>();
            for (DataKey key: DataKey.values()) {
                if (!data.containsKey(key)) { toAdd.put(key, 0f); }
            }
            if (!toAdd.isEmpty()) { data.putAll(toAdd); }

            // Update the lines
            for (DataKey dataKey: data.keySet()) {
                String keyString = dataKey.toString();
                boolean keyExistsInFile = lines.stream().anyMatch(
                    line -> line.trim().startsWith(keyString)
                );
                if (keyExistsInFile) { 
                    int index = 0;
                    for (int i = 0; i < lines.size(); i++) { 
                        if (lines.get(i).trim().startsWith(keyString)) { index = i; break; } 
                    }
                    lines.set(index, keyString + ": " + data.get(dataKey));
                    System.out.println("[SAVE MANAGER] Updated key: " + 
                        keyString + ": " + data.get(dataKey));
                } else { 
                    lines.add(keyString + ": " + data.get(dataKey));
                    System.out.println("[SAVE MANAGER] Added missing key: " + 
                        keyString + ": " + data.get(dataKey));
                }
            }
            
            // Write the contents back to the file
            try (PrintWriter writer = new PrintWriter(new FileWriter(saveFile))) {
                for (String line : lines) { writer.println(line); }
            }

            System.out.println("[SAVE MANAGER] Game data saved");

        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void readSaveData() {
        try {
            File saveFile = new File("save.dat");
            if (!saveFile.exists()) {
                saveFile.createNewFile();
                writeSaveData();
            }
            ArrayList<String> lines = readFileIntoList(saveFile);
            for (String line : lines) {
                String key = line.split(":")[0].trim();
                String value = line.split(":")[1].trim();
                for (DataKey dataKey: DataKey.values()) {
                    if (key.equals(dataKey.toString())) {
                        System.out.println("[SAVE MANAGER] Loaded key (" + 
                            dataKey.toString() + " : " + value + ")");
                        data.put(dataKey, Float.parseFloat(value));
                    }
                }
            }
            System.out.println("[SAVE MANAGER] Game data loaded");
        } 
        catch (IOException e) { e.printStackTrace(); }
    }

    private static ArrayList<String> readFileIntoList(File file) {
        ArrayList<String> lines = new ArrayList<>();
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) { lines.add(sc.nextLine()); }
        } catch (IOException e) { e.printStackTrace(); }
        return lines;
    }

}