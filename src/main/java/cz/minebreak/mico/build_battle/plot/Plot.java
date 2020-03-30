package cz.minebreak.mico.build_battle.plot;

import cz.minebreak.mico.build_battle.rate.BuildRateOption;
import cz.minebreak.mico.build_battle.rate.RateOption;
import cz.minebreak.mico.build_battle.rate.Rating;
import cz.minebreak.mico.build_battle.util.InvalidRateException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Plot {

    private Location bottomCorner; // Position of the least summary of any X,Y,Z position block inside plot
    private Location topCorner; // Position of the biggest summary of any X,Y,Z position block inside plot
    private Location plotEnterLocation;

    private Player builder;

    private Rating rating;

    public Plot(Location bottomCorner, Location topCorner) {
        this.bottomCorner = bottomCorner;
        this.topCorner = topCorner;

        BuildRateOption[] buildRateOptions = BuildRateOption.values();
        int rateNameCount = buildRateOptions.length;
        RateOption[] voteOptions = new RateOption[rateNameCount];

        for (int rateNameIndex = 0; rateNameIndex < rateNameCount; rateNameIndex++) {
            voteOptions[rateNameIndex] = new RateOption(buildRateOptions[rateNameIndex].getName(), buildRateOptions[rateNameIndex].getRateValue() - 1);
        }

        rating = new Rating(voteOptions);

        double centerX = bottomCorner.getBlockX();
        double centerY = (bottomCorner.getBlockY() + topCorner.getBlockY()) / 2d;
        double centerZ = (bottomCorner.getBlockZ() + topCorner.getBlockZ()) / 2d + 0.5;
        plotEnterLocation = new Location(bottomCorner.getWorld(), centerX, centerY, centerZ);
        plotEnterLocation.setYaw(-90);
    }

    public void rate(Player player, int rateNumber) throws InvalidRateException {
        rating.rate(player, rateNumber - 1);
    }

    public Location getBottomCorner() {
        return bottomCorner;
    }

    public Location getTopCorner() {
        return topCorner;
    }

    public Location getPlotEnterLocation() {
        return plotEnterLocation;
    }

    public Player getBuilder() {
        return builder;
    }

    public Rating getRating() {
        return rating;
    }

    public void assignBuilder(Player builder) {
        this.builder = builder;
    }

    public void removeBuilder() {
        this.builder = null;
    }

    public void changeFloor(Material material) {
        World currentWorld = Bukkit.getWorlds().get(0);

        for (int z = bottomCorner.getBlockZ(); z <= topCorner.getBlockZ(); z++) {
            for (int x = bottomCorner.getBlockX(); x <= topCorner.getBlockX(); x++) {
                Block block = currentWorld.getBlockAt(x, bottomCorner.getBlockY(), z);
                block.setType(material);
            }
        }
    }

    public void clearBuildingArea() {
        World currentWorld = Bukkit.getWorlds().get(0);

        for (int z = bottomCorner.getBlockZ(); z <= topCorner.getBlockZ(); z++) {
            for (int y = bottomCorner.getBlockY(); y <= topCorner.getBlockY(); y++) {
                for (int x = bottomCorner.getBlockX(); x <= topCorner.getBlockX(); x++) {
                    Block block = currentWorld.getBlockAt(x, y, z);
                    block.setType(Material.AIR);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Plot: player={");
        builder.append(this.builder);
        builder.append("}, bottomCorner={");
        builder.append(bottomCorner);
        builder.append("}, topCorner={");
        builder.append(topCorner);
        builder.append("}, center={");
        builder.append(plotEnterLocation);
        builder.append("}");

        return builder.toString();
    }
}
