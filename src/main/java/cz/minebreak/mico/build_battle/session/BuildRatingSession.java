package cz.minebreak.mico.build_battle.session;

import cz.minebreak.mico.build_battle.item.BuildRatingItem;
import cz.minebreak.mico.build_battle.plot.Plot;
import cz.minebreak.mico.build_battle.plot.PlotManager;
import cz.minebreak.mico.build_battle.rate.BuildRateOption;
import cz.minebreak.mico.build_battle.time.CallbackDelay;
import cz.minebreak.mico.build_battle.time.ICallback;
import cz.minebreak.mico.build_battle.util.InvalidRateException;
import cz.minebreak.mico.build_battle.item.nbt.NbtTagNames;
import net.graymadness.minigame_api.helper.item.Nbt;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildRatingSession extends Session implements ICallback {

    private JavaPlugin plugin;
    private Plot currentPlot;
    private CallbackDelay callbackDelay;
    private CallbackDelay countDown;
    private long timeRatingPlot;

    private int currentPlotIndex;

    public BuildRatingSession(JavaPlugin plugin) {
        this.plugin = plugin;
        callbackDelay = new CallbackDelay(this, 0, plugin, (Object) null);
        countDown = new CallbackDelay((sender, args) -> {
            int remainingSeconds = (int) args[0];
            notifyPlayersCountdown(remainingSeconds);

            switch (remainingSeconds) {
                case 3:
                case 2:
                    countDown.setArgs(remainingSeconds - 1);
                    countDown.setDelayTicks(20);
                    countDown.run();
                default:
                    break;
            }
        }, 0, plugin, (Object) null);
    }

    @Override
    public boolean start(Object... args) {
        timeRatingPlot = (long) args[0];

        callbackDelay.setDelayTicks(timeRatingPlot);
        countDown.setDelayTicks(timeRatingPlot - 60);
        countDown.setArgs(3);
        countDown.run();

        givePlayersBuildVotingOptions();

        currentPlotIndex = 0;
        nextPlot();

        callbackDelay.run();
        return true;
    }

    @Override
    public void stop() {
        removeBuildVotingItemsFromPlayers();
        callbackDelay.cancel();
    }

    @Override
    public void reset() {

    }

    @Override
    public void callbackDelay(Object sender, Object... args) {
        if (currentPlotIndex >= PlotManager.getOccupiedPlotCount()) {
            return;
        }

        nextPlot();
        callbackDelay.run();
    }

    public void ratePlot(Player player, int rateNumber) throws InvalidRateException {
        currentPlot.rate(player, rateNumber);
    }

    public Plot getWinningPlot() {
        Plot winningPlot = null, currentPlot;

        for (int plotIndex = 0; plotIndex < PlotManager.getPlotCount(); plotIndex++) {

            currentPlot = PlotManager.getPlotAt(plotIndex);
            if (winningPlot == null || currentPlot.getRating().getTotalRate() > winningPlot.getRating().getTotalRate()) {
                winningPlot = currentPlot;
            }
        }

        return winningPlot;
    }

    /*
        Private methods
     */
    private void givePlayersBuildVotingOptions() {
        BuildRateOption[] rateOptions = BuildRateOption.values();

        for (Player player : playingPlayers) {
            for (int rateOptionNameIndex = 0; rateOptionNameIndex < rateOptions.length; rateOptionNameIndex++) {
                player.getInventory().setItem(rateOptionNameIndex, new BuildRatingItem(rateOptions[rateOptionNameIndex]));
                player.getInventory().setHeldItemSlot(1);
            }
            player.sendMessage("Start rating builds");
        }
    }

    private void nextPlot() {
        currentPlot = PlotManager.getPlotAt(currentPlotIndex++);
        teleportPlayersToPlot(currentPlot);
    }

    private void teleportPlayersToPlot(Plot plot) {
        for (Player player : playingPlayers) {
            player.teleport(plot.getPlotEnterLocation());
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.BLUE + "Plot of player " + ChatColor.GOLD + plot.getBuilder().getDisplayName());
        }
    }

    private void notifyPlayersCountdown(int remainingSeconds) {
        for (Player player : playingPlayers) {
            player.sendMessage(ChatColor.GREEN + "Voting ending in: " + remainingSeconds);
        }
    }

    private void removeBuildVotingItemsFromPlayers() {
        for (Player player : playingPlayers) {

            PlayerInventory playerInventory = player.getInventory();
            ItemStack[] playerItems = playerInventory.getContents();

            for (ItemStack playerItem : playerItems) {
                if (playerItem == null)
                    continue;

                if (isBuildRatingItem(playerItem)) {
                    playerInventory.remove(playerItem);
                }
            }
        }
    }

    private boolean isBuildRatingItem(ItemStack itemStack) {
        return Nbt.getNbt_Int(itemStack, NbtTagNames.RATE_OPTION_NUMBER, 0) != 0;
    }
}
