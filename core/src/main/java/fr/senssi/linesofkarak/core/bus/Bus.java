package fr.senssi.linesofkarak.core.bus;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import java.util.List;

public abstract class Bus<T> {
    private static final List<Bus<?>> allBuses = new ArrayList<>();

    protected final List<T> items = new ArrayList<>();

    public Bus() {
        allBuses.add(this);
    }

    public static List<Bus<?>> getAllBuses() {
        return allBuses;
    }

    public void add(T item) {
        items.add(item);
    }

    public void remove(T item) {
        items.remove(item);
    }

    public void clear() {
        items.clear();
    }

    public List<T> getItems() {
        return items;
    }

    public abstract void renderAll(Batch batch);

    public abstract void disposeAll();
}
