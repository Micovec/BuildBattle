package cz.minebreak.mico.build_battle;

import cz.minebreak.mico.build_battle.command.BuildBattleCommand;
import cz.minebreak.mico.build_battle.listener.BuildRatingItemListener;
import cz.minebreak.mico.build_battle.listener.PlayerInteractBlockListener;
import cz.minebreak.mico.build_battle.listener.PlotMenuItemListener;
import cz.minebreak.mico.build_battle.listener.ThemeVotingItemListener;
import cz.minebreak.mico.build_battle.config.BuildBattleConfig;
import cz.minebreak.mico.build_battle.plot.PlotManager;
import cz.minebreak.mico.build_battle.session.*;
import cz.minebreak.mico.build_battle.time.CallbackDelay;
import cz.minebreak.mico.build_battle.time.ICallback;
import cz.minebreak.mico.build_battle.util.*;
import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.api.IMinigame;
import net.graymadness.minigame_api.api.MinigameState;
import net.graymadness.minigame_api.event.MinigameEndedEvent;
import net.graymadness.minigame_api.event.MinigameStartedEvent;
import net.graymadness.minigame_api.event.MinigameStateChangedEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

//TODO: Translate all displayed strings
//TODO: Replace all send messages with strings for ComponentBuilder
public final class BuildBattle extends JavaPlugin implements IMinigame, ICallback {

    private MinigameState minigameState;
    private BuildBattleConfig buildBattleConfig;
    private PluginManager pluginManager;

    private CallbackDelay callbackDelay;
    private List<Player> playingPlayers;

    private ThemeVotingSession themeVotingSession;
    private BuildingSession buildingSession;
    private BuildRatingSession buildRatingSession;
    private ShowingBestBuildSession showingBestBuildSession;

    private BuildBattleState buildBattleState;

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(Point2D.class);
        ConfigurationSerialization.registerClass(Point3D.class);

        minigameState = MinigameState.Lobby;
        buildBattleState = BuildBattleState.LOBBY;

        getConfig().options().copyDefaults(true);
        buildBattleConfig = new BuildBattleConfig(getConfig());

        themeVotingSession = new ThemeVotingSession(buildBattleConfig.getThemes());
        buildingSession = new BuildingSession(this);
        buildRatingSession = new BuildRatingSession(this);
        showingBestBuildSession = new ShowingBestBuildSession();

        pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ThemeVotingItemListener(this), this);
        pluginManager.registerEvents(new PlayerInteractBlockListener(this), this);
        pluginManager.registerEvents(new BuildRatingItemListener(this, buildRatingSession), this);
        pluginManager.registerEvents(new PlotMenuItemListener(this), this);

        getCommand("buildbattle").setExecutor(new BuildBattleCommand(this));

        API.registerMinigame(this);
    }

    @Override
    public void onDisable() {
        buildBattleConfig.saveConfig();
        saveConfig();
    }

    @Override
    public @NotNull String getCodename() {
        return "BuildBattle";
    }

    @Override
    public @NotNull Map<String, List<Player>> getRoles() {
        return null;
    }

    @Override
    public @NotNull MinigameState getState() {
        return minigameState;
    }

    @Override
    public boolean canJoinDuringWarmup() {
        return buildBattleConfig.canJoinDuringWarmup();
    }

    @Override
    public int getMinPlayers() {
        return buildBattleConfig.getMinPlayers();
    }

    @Override
    public int getMaxPlayers() {
        return buildBattleConfig.getMaxPlayers();
    }

    public BuildBattleConfig getMinigameConfig() {
        return buildBattleConfig;
    }

    public boolean isPlayerPlaying(Player player) {
        if (playingPlayers == null)
            return false;

        return playingPlayers.contains(player);
    }

    public BuildBattleState getBuildBattleState() {
        return buildBattleState;
    }

    @Override
    public void start() {
        recreatePlots();

        playingPlayers = Bukkit.getWorlds().get(0).getPlayers();

        themeVotingSession.setPlayingPlayers(playingPlayers);
        buildingSession.setPlayingPlayers(playingPlayers);
        buildRatingSession.setPlayingPlayers(playingPlayers);
        showingBestBuildSession.setPlayingPlayers(playingPlayers);

        startThemeVotingSection();
    }

    @Override
    public void stop() {
        resetMinigame();
    }

    @Override
    public void callbackDelay(Object sender, Object... args) {
        switch ((BuildBattleState)args[0]) {
            case VOTING_THEME:
                startBuildingSection();
                break;
            case BUILDING:
                startBuildRatingSection();
                break;
            case BUILD_RATING:
                startShowingBestBuildSection();
                break;
            case SHOWING_BEST_BUILD:
            default:
                stop();
                break;
        }
    }

    /**
     * Called from command /buildbattle grid to recreate plots
     */
    public void recreatePlots() {
        PlotManager.removePlots();
        PlotManager.createPlots(Bukkit.getWorlds().get(0), buildBattleConfig.getPlotOrigin(), buildBattleConfig.getPlotCount(), buildBattleConfig.getPlotSpacing(), buildBattleConfig.getPlotSize(), buildBattleConfig.getPlotDefaultFloorMaterial());
    }

    /*
        Private methods
     */

    /**
     * Calls a MinigameStateChangedEvent with current Minigame state
     */
    private void callStateChangeEvent() {
        pluginManager.callEvent(new MinigameStateChangedEvent(this, minigameState));
    }

    /*
        Section starts
     */
    private void startThemeVotingSection() {
        boolean startResult = themeVotingSession.start(buildBattleConfig.getThemes());
        if (!startResult) {
            stop();
        }

        callbackDelay = new CallbackDelay(this, buildBattleConfig.getTimeThemeVoting(), this, BuildBattleState.VOTING_THEME);
        callbackDelay.run();

        pluginManager.callEvent(new MinigameStartedEvent(this));
        minigameState = MinigameState.Warmup;
        buildBattleState = BuildBattleState.VOTING_THEME;
        callStateChangeEvent();
    }

    private void startBuildingSection() {
        themeVotingSession.stop();

        boolean startResult = buildingSession.start(themeVotingSession.getWinningOption(), buildBattleConfig.getTimeBuilding());
        if (!startResult) {
            stop();
        }

        callbackDelay.setDelayTicks(buildBattleConfig.getTimeBuilding());
        callbackDelay.setArgs(BuildBattleState.BUILDING);
        callbackDelay.run();

        minigameState = MinigameState.InProgress;
        buildBattleState = BuildBattleState.BUILDING;
        callStateChangeEvent();
    }

    private void startBuildRatingSection() {
        buildingSession.stop();

        boolean startResult = buildRatingSession.start(buildBattleConfig.getTimeBuildRating());
        if (!startResult) {
            stop();
        }

        callbackDelay.setDelayTicks(buildBattleConfig.getTimeBuildRating() * PlotManager.getOccupiedPlotCount());
        callbackDelay.setArgs(BuildBattleState.BUILD_RATING);
        callbackDelay.run();

        buildBattleState = BuildBattleState.BUILD_RATING;
    }

    private void startShowingBestBuildSection() {
        buildRatingSession.stop();

        boolean startResult = showingBestBuildSession.start(buildRatingSession.getWinningPlot());
        if (!startResult) {
            stop();
        }

        callbackDelay.setDelayTicks(buildBattleConfig.getTimeShowingBestBuild());
        callbackDelay.setArgs(BuildBattleState.SHOWING_BEST_BUILD);
        callbackDelay.run();

        buildBattleState = BuildBattleState.SHOWING_BEST_BUILD;
    }

    /*
        Reseting minigame
     */
    private void resetMinigame() {
        themeVotingSession.reset();
        buildingSession.reset();
        buildRatingSession.reset();
        showingBestBuildSession.reset();

        callbackDelay.cancel();
        givePlayersSurvivalMode(playingPlayers);
        teleportPlayersToSpawn(playingPlayers);
        playingPlayers.clear();

        pluginManager.callEvent(new MinigameEndedEvent(this));
        minigameState = MinigameState.Lobby;
        buildBattleState = BuildBattleState.LOBBY;
        callStateChangeEvent();
    }

    private void givePlayersSurvivalMode(List<Player> players) {
        for (Player player : players) {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    private void teleportPlayersToSpawn(List<Player> players) {
        Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
        for (Player player : players) {
            player.teleport(spawn);
        }
    }
}
