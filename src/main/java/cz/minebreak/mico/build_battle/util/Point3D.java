package cz.minebreak.mico.build_battle.util;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Point3D implements ConfigurationSerializable {

    private int x, y, z;

    public Point3D() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Point3D(int x, int y, int z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public Point3D(Map<String, Object> params) {
        setX((int)params.get("x"));
        setY((int)params.get("y"));
        setZ((int)params.get("z"));
    }

    public Point3D add(Point3D point) {
        return new Point3D(x + point.x, y + point.y, z + point.z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void set(int x, int y, int z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("x", x);
        serialized.put("y", y);
        serialized.put("z", z);
        return serialized;
    }
}
