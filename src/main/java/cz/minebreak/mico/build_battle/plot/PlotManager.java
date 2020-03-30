package cz.minebreak.mico.build_battle.plot;

import cz.minebreak.mico.build_battle.util.NotEnoughPlotsException;
import cz.minebreak.mico.build_battle.util.Point2D;
import cz.minebreak.mico.build_battle.util.Point3D;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlotManager {

    private PlotManager() {}

    private static List<Plot> plots = new ArrayList<>(); // Contains all plots
    private static Map<Player, Plot> playerPlots = new HashMap<>();

    public static void createPlots(World world, Location origin, Point2D count, Point2D spacing, Point3D plotSize, Material floorMaterial) {
        for (int z = 0; z < count.getY(); z++) {
            for (int x = 0; x < count.getX(); x++) {
                int bottomCornerX = origin.getBlockX() + x * (spacing.getX() + 1) + x * plotSize.getX();
                int bottomCornerY = origin.getBlockY();
                int bottomCornerZ = origin.getBlockZ() + z * (spacing.getY() + 1) + z * plotSize.getZ();

                int topCornerX = origin.getBlockX() + x * (spacing.getX() + 1) + (x + 1) * plotSize.getX();
                int topCornerY = origin.getBlockY() + plotSize.getY();
                int topCornerZ = origin.getBlockZ() + z * (spacing.getY() + 1) + (z + 1) * plotSize.getZ();

                Location bottomCorner = new Location(world, bottomCornerX, bottomCornerY, bottomCornerZ);
                Location topCorner = new Location(world, topCornerX, topCornerY, topCornerZ);

                Plot newPlot = new Plot(bottomCorner, topCorner);
                newPlot.clearBuildingArea();
                newPlot.changeFloor(floorMaterial);

                plots.add(newPlot);
            }
        }
    }

    public static void removePlots() {
        plots.clear();
        playerPlots.clear();
    }

    public static void assignPlayerToNextEmptyPlot(Player player) throws NotEnoughPlotsException {
        for (Plot plot : plots) {
            if (plot.getBuilder() == null) {
                plot.assignBuilder(player);
                playerPlots.put(player, plot);
                return;
            }
        }

        throw new NotEnoughPlotsException("Cannot assign plot to player: " + player.toString());
    }

    public static void removePlayersFromPlots() {
        for (Plot plot : plots) {
            plot.removeBuilder();
        }

        playerPlots.clear();
    }

    public static Plot getPlayerPlot(Player player) {
        return playerPlots.get(player);
    }

    public static Plot getPlotAt(int index) {
        return plots.get(index);
    }

    public static int getPlotCount() {
        return plots.size();
    }

    public static int getOccupiedPlotCount() {
        return playerPlots.size();
    }

    public static boolean canPlayerEditLocation(Player player, Location location) {
        if (!playerPlots.containsKey(player)) // Player is not playing BuildBattle
            return true;

        Plot playerPlot = playerPlots.get(player);

        // Could be merged but that will produce a really long line
        if (location.getBlockX() < playerPlot.getBottomCorner().getBlockX())
            return false;
        if (location.getBlockY() < playerPlot.getBottomCorner().getBlockY())
            return false;
        if (location.getBlockZ() < playerPlot.getBottomCorner().getBlockZ())
            return false;
        if (location.getBlockX() > playerPlot.getTopCorner().getBlockX())
            return false;
        if (location.getBlockY() > playerPlot.getTopCorner().getBlockY())
            return false;
        if (location.getBlockZ() > playerPlot.getTopCorner().getBlockZ())
            return false;

        return true;
    }
}
