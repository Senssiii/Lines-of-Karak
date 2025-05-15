package fr.senssi.linesofkarak.entities.units.names;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NameGenerator {

    private static final ArrayList<String> names = new ArrayList<>();

    static {
        loadNamesFromJson("data/names.json");
    }

    private static void loadNamesFromJson(String path) {
        try {
            FileHandle file = Gdx.files.internal(path);
            Json json = new Json();
            List<String> loadedNames = json.fromJson(ArrayList.class, String.class, file);
            names.addAll(loadedNames);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des noms : " + e.getMessage());
        }
    }

    public static String getRandomName() {
        if (names.isEmpty()) return "Unnamed";
        return names.get(new Random().nextInt(names.size()));
    }
}
