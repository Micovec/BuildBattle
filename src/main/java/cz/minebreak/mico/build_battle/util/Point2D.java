package cz.minebreak.mico.build_battle.util;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Point2D implements ConfigurationSerializable {

    private int x,y;

    public Point2D() {
        setX(0);
        setY(0);
    }

    public Point2D(int x, int y) {
        setX(x);
        setY(y);
    }

    public Point2D(Map<String, Object> params) {
        setX((int)params.get("x"));
        setY((int)params.get("y"));
    }

    public Point2D add(Point2D point) {
        return new Point2D(x + point.x, y + point.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void set(int x, int y) {
        setX(x);
        setY(y);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> params = new HashMap<>();
        params.put("x", x);
        params.put("y", y);
        return params;
    }
}
