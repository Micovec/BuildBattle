package cz.minebreak.mico.build_battle.session;

import cz.minebreak.mico.build_battle.item.PlotMenuItem;
import cz.minebreak.mico.build_battle.time.CallbackDelay;
import cz.minebreak.mico.build_battle.time.ICallback;
import cz.minebreak.mico.build_battle.vote.VoteOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

public class BuildingSession extends Session implements ICallback {

    private Scoreboard emptyScoreboard;
    private Scoreboard scoreboard;
    private Team teamTimeRemaining;

    private CallbackDelay callbackDelay;
    private long timeForSession;
    private long remainingTime;
    private static final String TIME_ENTRY = "Remaining time: ";

    private VoteOption winningVote;

    private JavaPlugin plugin;

    public BuildingSession(JavaPlugin plugin) {
        this.plugin = plugin;
        callbackDelay = new CallbackDelay(this, 20, plugin, (Object) null);
    }

    @Override
    public boolean start(Object... args) {
        winningVote = (VoteOption) args[0];
        remainingTime = timeForSession = (long) args[1];
        setUpScoreboards();
        teamTimeRemaining.setSuffix(getRemainingTimeStr(remainingTime));

        givePlayersPlotMenuItems();
        showScoreboards();

        for (Player player : playingPlayers) {
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.BLUE + "You can start building now");
            player.sendMessage(ChatColor.BLUE + "Theme: " + ChatColor.GOLD + winningVote.getName());
        }

        callbackDelay.run();

        return true;
    }

    @Override
    public void stop() {
        clearPlayersInventory();
        hideScoreboards();
        callbackDelay.cancel();
    }

    @Override
    public void reset() {

    }

    @Override
    public void callbackDelay(Object sender, Object... args) {
        remainingTime -= 20;
        teamTimeRemaining.setSuffix(getRemainingTimeStr(remainingTime));
        if (remainingTime > 0) {
            callbackDelay.run();
        }
    }

    /*
        Private methods
     */
    private void givePlayersPlotMenuItems() {
        for (Player player : playingPlayers) {
            player.getInventory().setItem(8, new PlotMenuItem());
        }
    }

    private void setUpScoreboards() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("build_battle", "", ChatColor.GOLD + "Build Battle");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        teamTimeRemaining = scoreboard.registerNewTeam("time_remaining");
        teamTimeRemaining.setSuffix(getRemainingTimeStr(timeForSession));
        teamTimeRemaining.addEntry(TIME_ENTRY);
        objective.getScore(TIME_ENTRY).setScore(3);

        objective.getScore("").setScore(2); // Empty line
        objective.getScore("Theme: " + ChatColor.DARK_AQUA + winningVote.getName()).setScore(1);

        emptyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    private void clearPlayersInventory() {
        for (Player player : playingPlayers) {
            player.getInventory().clear(); // Clear player's inventory
        }
    }

    private void showScoreboards() {
        for (Player player : playingPlayers) {
            player.setScoreboard(scoreboard);
        }
    }

    private void hideScoreboards() {
        for (Player player : playingPlayers) {
            player.setScoreboard(emptyScoreboard);
        }
    }

    private String getRemainingTimeStr(long time) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.YELLOW);

        long timeRemaining = time;
        long hours = timeRemaining / 72000;
        timeRemaining = timeRemaining % 72000 - hours;
        long minutes = timeRemaining / 1200;
        timeRemaining = timeRemaining % 1200 - minutes;
        long seconds = timeRemaining / 20;

        if (hours > 0) {
            stringBuilder.append(hours).append(":");
        }
        stringBuilder.append(String.format("%02d:%02d", minutes, seconds));

        return stringBuilder.toString();
    }
}
