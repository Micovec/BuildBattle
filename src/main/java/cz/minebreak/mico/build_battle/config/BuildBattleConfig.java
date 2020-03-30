package cz.minebreak.mico.build_battle.config;

import cz.minebreak.mico.build_battle.util.Point2D;
import cz.minebreak.mico.build_battle.util.Point3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BuildBattleConfig {

    private FileConfiguration pluginConfig;

    private int minPlayers, maxPlayers;
    private boolean canJoinDuringWarmup;

    private Point2D plotCount;
    private Point2D plotSpacing;
    private Point3D plotSize;
    private Location plotOrigin;

    // All times are in-game ticks
    private long timeThemeVoting;
    private long timeBuilding;
    private long timeBuildRating;
    private long timeShowingBestBuild;

    private Material plotDefaultFloorMaterial;

    private ArrayList<String> themes;
    private ArrayList<Material> blacklistedMaterials;

    /**
     * DO NOT change MAX_THEMES_VOTING variable. But if you are forced to, remember to change chest type in
     * TopicVotingMenu's constructor and items layout.
     */
    public static final int MAX_THEMES_VOTING = 5;

    public BuildBattleConfig(FileConfiguration pluginConfig) {
        this.pluginConfig = pluginConfig;
        loadConfig();
    }

    public void saveConfig() {
        themes.sort(Comparator.comparing(String::toString));
        pluginConfig.set(THEMES, themes);

        pluginConfig.set(PLOT_SPACING, plotSpacing);
        pluginConfig.set(PLOT_SIZE, plotSize);
        pluginConfig.set(PLOT_ORIGIN, plotOrigin);
        pluginConfig.set(PLOT_COUNT, plotCount);

        pluginConfig.set(PLOT_DEFAULT_FLOOR_MATERIAL, plotDefaultFloorMaterial.name());
    }

    private void loadConfig() {
        minPlayers = pluginConfig.getInt(MIN_PLAYERS, 1);
        maxPlayers = pluginConfig.getInt(MAX_PLAYERS, 9);
        canJoinDuringWarmup = pluginConfig.getBoolean(CAN_JOIN_DURING_WARMUP, false);

        themes = (ArrayList<String>) pluginConfig.getStringList(THEMES);
        while (themes.size() < MAX_THEMES_VOTING) {
            themes.add(String.format("Theme #%d", themes.size()));
        }

        List<String> blacklistedMaterialsStr = pluginConfig.getStringList(BLACKLISTED_MATERIALS);
        blacklistedMaterials = new ArrayList<>();
        for (String materialStr : blacklistedMaterialsStr) {
            blacklistedMaterials.add(Material.getMaterial(materialStr));
        }

        plotSpacing = (Point2D) pluginConfig.get(PLOT_SPACING, new Point2D(10, 10));
        plotSize = (Point3D) pluginConfig.get(PLOT_SIZE, new Point3D(20, 20, 20));
        plotOrigin = (Location) pluginConfig.get(PLOT_ORIGIN, new Location(Bukkit.getWorlds().get(0), 0, 55, 0));
        plotCount = (Point2D) pluginConfig.get(PLOT_COUNT, new Point2D(5, 5));

        timeThemeVoting = pluginConfig.getLong(TIME_THEME_VOTING, 300);
        timeBuilding = pluginConfig.getLong(TIME_BUILDING, 1200);
        timeBuildRating = pluginConfig.getLong(TIME_BUILD_RATING, 400);
        timeShowingBestBuild = pluginConfig.getLong(TIME_SHOWING_BEST_BUILD, 400);

        plotDefaultFloorMaterial = Material.getMaterial(pluginConfig.getString(PLOT_DEFAULT_FLOOR_MATERIAL, "GRASS_BLOCK"));
    }

    /*
        Getters
     */
    public FileConfiguration getPluginConfig() {  return pluginConfig; }

    public boolean canJoinDuringWarmup() { return canJoinDuringWarmup; }

    public int getMaxPlayers() { return maxPlayers; }

    public int getMinPlayers() { return minPlayers; }

    public int getPlotXSpacing() { return plotSpacing.getX(); }

    public int getPlotZSpacing() { return plotSpacing.getY(); }

    public int getPlotXSize() { return plotSize.getX(); }

    public int getPlotYSize() { return plotSize.getY(); }

    public int getPlotZSize() {  return plotSize.getZ(); }

    public int getPlotXOrigin() { return plotOrigin.getBlockX(); }

    public int getPlotYOrigin() { return plotOrigin.getBlockX(); }

    public int getPlotZOrigin() { return plotOrigin.getBlockX(); }

    public int getPlotXCount() { return plotCount.getX(); }

    public int getPlotZCount() { return plotCount.getY(); }

    public Point2D getPlotCount() { return plotCount; }

    public Point2D getPlotSpacing() { return plotSpacing; }

    public Point3D getPlotSize() { return plotSize; }

    public Location getPlotOrigin() { return plotOrigin; }

    public Material getPlotDefaultFloorMaterial() {
        return plotDefaultFloorMaterial;
    }

    public ArrayList<String> getThemes() {
        return themes;
    }

    public long getTimeThemeVoting() { return timeThemeVoting; }

    public long getTimeBuilding() { return timeBuilding; }

    public long getTimeBuildRating() { return timeBuildRating; }

    public long getTimeShowingBestBuild() {
        return timeShowingBestBuild;
    }

    /*
        Setters
     */
    public void setPlotXSpacing(int xSpacing) {
        plotSpacing.setX(xSpacing);
    }

    public void setPlotZSpacing(int zSpacing) {
        plotSpacing.setY(zSpacing);
    }

    public void setPlotXSize(int xSize) {
        plotSize.setX(xSize);
    }

    public void setPlotYSize(int ySize) {
        plotSize.setY(ySize);
    }

    public void setPlotZSize(int zSize) {
        plotSize.setZ(zSize);
    }

    public void setPlotXOrigin(int xOrigin) { plotOrigin.setX(xOrigin); }

    public void setPlotYOrigin(int yOrigin) { plotOrigin.setY(yOrigin); }

    public void setPlotZOrigin(int zOrigin) { plotOrigin.setZ(zOrigin); }

    public void setPlotXCount(int xCount) { plotCount.setX(xCount); }

    public void setPlotZCount(int zCount) { plotCount.setY(zCount); }

    private static final String BLACKLISTED_MATERIALS = "blacklisted-materials";
    private static final String CAN_JOIN_DURING_WARMUP = "can-join-during-warmup";
    private static final String PLOT_DEFAULT_FLOOR_MATERIAL = "plot.default-floor-material";
    private static final String MAX_PLAYERS = "max-players";
    private static final String MIN_PLAYERS = "min-players";
    private static final String PLOT_SPACING = "plot.spacing";
    private static final String PLOT_SIZE = "plot.size";
    private static final String PLOT_ORIGIN = "plot.origin";
    private static final String PLOT_COUNT = "plot.count";
    private static final String THEMES = "themes";
    private static final String TIME_THEME_VOTING = "time-theme-voting";
    private static final String TIME_BUILDING = "time-building";
    private static final String TIME_BUILD_RATING = "time-build-rating";
    private static final String TIME_SHOWING_BEST_BUILD = "time-showing-best-build";
}
