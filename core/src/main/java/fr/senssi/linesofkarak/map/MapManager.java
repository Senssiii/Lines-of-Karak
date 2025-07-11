package fr.senssi.linesofkarak.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapManager {
    public static TiledMap loadMap(String name){
        System.out.println("Map : "+name+".tmx");
        return new TmxMapLoader().load("textures/map/"+name+".tmx");
    }

    public static TiledMapTileLayer.Cell getCellAt(TiledMap map, int x, int y) {
        if (map == null) return null;

        // 1. Accéder à la couche que vous voulez vérifier (par exemple "ground")
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Tile Layer 1");
        if (layer == null) return null;

        // 2. Inverser l'axe Y si nécessaire (Tiled utilise Y vers le bas par défaut)
        int mapHeight = layer.getHeight();
        int invertedY = mapHeight - 1 - y;

        // 3. Récupérer la cellule
        return layer.getCell(x, invertedY);
    }
}
