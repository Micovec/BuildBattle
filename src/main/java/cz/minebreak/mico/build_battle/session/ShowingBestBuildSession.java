package cz.minebreak.mico.build_battle.session;

import cz.minebreak.mico.build_battle.plot.Plot;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ShowingBestBuildSession extends Session {

    private Plot winningPlot;

    @Override
    public boolean start(Object... args) {
        winningPlot = (Plot) args[0];

        teleportPlayersToWinnigPlot(winningPlot);

        return true;
    }

    @Override
    public void stop() {

    }

    @Override
    public void reset() {

    }

    /*
        Private methods
     */
    public void teleportPlayersToWinnigPlot(Plot winningPlot) {
        for (Player player : playingPlayers) {
            player.teleport(winningPlot.getPlotEnterLocation());
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.BLUE + "Winning plot of player " + ChatColor.GOLD + winningPlot.getBuilder().getDisplayName());
        }
    }
}
