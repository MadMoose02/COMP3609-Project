package Managers;
/**
 * SaveDataManager.java
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Game.DataKey;

public class SaveDataManager {
    
    private static SaveDataManager instance     = null;
    private static HashMap<DataKey, Float> data = null;

    public SaveDataManager() {
        System.out.println("[SAVEDATA MANAGER] Initialising");
        SaveDataManager.data = new HashMap<DataKey, Float>();
    }

    
    /* Accessors */

    public static Float get(DataKey key) { 
        if (!SaveDataManager.data.containsKey(key)) { System.out.println("[SAVEDATA MANAGER] No such key: " + key); }
        return SaveDataManager.data.get(key); 
    }


    /* Mutators */
    public static void set(DataKey key, Float value) { SaveDataManager.data.put(key, value); }


    /* Methods */

    public static SaveDataManager getInstance() {
        if (instance == null) {
            SaveDataManager.instance = new SaveDataManager();
            SaveDataManager.readSaveData();
        }
        return SaveDataManager.instance;
    }
    
    public static void writeSaveData() {
        try {
            File saveFile = new File("save.dat");
            ArrayList<String> lines = SaveDataManager.readFileIntoList(saveFile);
            
            // Check for missing keys
            HashMap<DataKey, Float> toAdd = new HashMap<>();
            for (DataKey key: DataKey.values()) {
                if (!SaveDataManager.data.containsKey(key)) { toAdd.put(key, 0f); }
            }
            if (!toAdd.isEmpty()) { SaveDataManager.data.putAll(toAdd); }

            // Update the lines
            for (DataKey dataKey: SaveDataManager.data.keySet()) {
                String keyString = dataKey.toString();
                boolean keyExistsInFile = lines.stream().anyMatch(line -> line.trim().startsWith(keyString));
                if (keyExistsInFile) { 
                    int index = 0;
                    for (int i = 0; i < lines.size(); i++) { 
                        if (lines.get(i).trim().startsWith(keyString)) { index = i; break; } 
                    }
                    lines.set(index, keyString + ": " + SaveDataManager.data.get(dataKey));
                    System.out.println("[SAVEDATA MANAGER] Updated key: " + keyString + ": " + SaveDataManager.data.get(dataKey));
                } else { 
                    lines.add(keyString + ": " + SaveDataManager.data.get(dataKey));
                    System.out.println("[SAVEDATA MANAGER] Added missing key: " + keyString + ": " + SaveDataManager.data.get(dataKey));
                }
            }
            
            // Write the contents back to the file
            try (PrintWriter writer = new PrintWriter(new FileWriter(saveFile))) {
                for (String line : lines) { writer.println(line); }
            }

            System.out.println("[SAVEDATA MANAGER] Game data saved");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readSaveData() {
        try {
            File saveFile = new File("save.dat");
            if (!saveFile.exists()) {
                saveFile.createNewFile();
                SaveDataManager.writeSaveData();
            }
            ArrayList<String> lines = readFileIntoList(saveFile);
            for (String line : lines) {
                String key = line.split(":")[0].trim();
                String value = line.split(":")[1].trim();
                for (DataKey dataKey: DataKey.values()) {
                    if (key.equals(dataKey.toString())) {
                        System.out.println("[SAVEDATA MANAGER] Loaded key (" + dataKey.toString() + " : " + value + ")");
                        SaveDataManager.data.put(dataKey, Float.parseFloat(value));
                    }
                }
            }
            System.out.println("[SAVEDATA MANAGER] Game data loaded");
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