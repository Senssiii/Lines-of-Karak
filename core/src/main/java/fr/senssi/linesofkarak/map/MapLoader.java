package fr.senssi.linesofkarak.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapLoader {
    public static TiledMap loadMap(String name){
        System.out.println("textures/map/"+name+".tmx");
        return new TmxMapLoader().load("textures/map/"+name+".tmx");
    }
}
