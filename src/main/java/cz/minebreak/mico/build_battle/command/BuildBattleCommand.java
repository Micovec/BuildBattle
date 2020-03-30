package cz.minebreak.mico.build_battle.command;

import cz.minebreak.mico.build_battle.BuildBattle;
import cz.minebreak.mico.build_battle.config.BuildBattleConfig;
import cz.minebreak.mico.build_battle.plot.Plot;
import cz.minebreak.mico.build_battle.plot.PlotManager;
import net.graymadness.minigame_api.api.MinigameState;
import net.graymadness.minigame_api.helper.ChatInfo;
import net.graymadness.minigame_api.helper.CommandHelper;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuildBattleCommand implements CommandExecutor, TabCompleter {

    private BuildBattle plugin;

    private ChatInfo chatSuccess;
    private ChatInfo chatError;
    private ChatInfo chatInfo;

    public BuildBattleCommand(BuildBattle plugin) {
        this.plugin = plugin;

        chatSuccess = ChatInfo.SUCCESS;
        chatError = ChatInfo.ERROR;
        chatInfo = ChatInfo.GENERAL_INFO;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0 || (args.length > 0 && args[0].equals("help"))) {
            sendHelpMenu(commandSender);
            return true;
        }

        switch (args[0]) {
            case "grid":
                if (commandSender instanceof Player) {
                    return gridCommand((Player)commandSender, args);
                } else {
                    commandSender.sendMessage("! You have to be a player in order to run this command !");
                }
                break;
            case "plot":
                if (commandSender instanceof Player) {
                    return plotCommand((Player)commandSender, args);
                } else {
                    commandSender.sendMessage("! You have to be a player in order to run this command !");
                }
                break;
            default:
                sendHelpMenu(commandSender);
                break;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            return new ArrayList<>();
        } else {
            return args.length == 1 ? CommandHelper.filterOnly(args[0], Arrays.asList("help", "grid", "plot")) : new ArrayList<>();
        }
    }

    /*
        Private methods
     */
    private void sendHelpMenu(CommandSender addressee) {
        chatInfo.send(addressee, ComponentBuilder.text(ChatColor.GOLD + "/buildbattle help  " + ChatColor.WHITE + "    Show this help").build());
        chatInfo.send(addressee, ComponentBuilder.text(ChatColor.GOLD + "/buildbattle grid  " + ChatColor.WHITE + "    Create plot grid from the current player position").build());
        chatInfo.send(addressee, ComponentBuilder.text(ChatColor.GOLD + "/buildbattle plot  " + ChatColor.WHITE + "    Teleport to plot").build());
    }

    private boolean gridCommand(Player player, String[] args) {
        if (args.length != 8) {
            chatError.send(player, ComponentBuilder.text("Wrong usage:").build());
            chatError.send(player, ComponentBuilder.text("/buildbattle grid <x_count> <z_count> <x_spacing> <z_spacing> <x_size> <y_size> <z_size>").build());
            return false;
        } else if (plugin.getState() != MinigameState.Lobby) {
            chatError.send(player, ComponentBuilder.text("Minigame is not in Lobby state").build());
            return false;
        }

        int[] intArgs = new int[7];

        for (int i = 1; i < args.length; i++) {
            try {
                intArgs[i - 1] = Integer.parseInt(args[i]);
            } catch (NumberFormatException e) {
                chatError.send(player, ComponentBuilder.text(ChatColor.RED + " Argument at " + i + " is not a valid number").build());
                return false;
            }
        }

        BuildBattleConfig minigameConfig = plugin.getMinigameConfig();

        minigameConfig.setPlotXCount(intArgs[0]);
        minigameConfig.setPlotZCount(intArgs[1]);
        minigameConfig.setPlotXSpacing(intArgs[2]);
        minigameConfig.setPlotZSpacing(intArgs[3]);
        minigameConfig.setPlotXSize(intArgs[4]);
        minigameConfig.setPlotYSize(intArgs[5]);
        minigameConfig.setPlotZSize(intArgs[6]);

        Location playerPosition = player.getLocation();
        minigameConfig.setPlotXOrigin(playerPosition.getBlockX());
        minigameConfig.setPlotYOrigin(playerPosition.getBlockY());
        minigameConfig.setPlotZOrigin(playerPosition.getBlockZ());

        plugin.recreatePlots();

        chatSuccess.send(player, ComponentBuilder.text("Plot grid created successfully.").build());

        return true;
    }

    private boolean plotCommand(Player player, String[] args) {
        if (args.length != 2) {
            chatError.send(player, ComponentBuilder.text("Wrong usage:").build());
            chatError.send(player, ComponentBuilder.text("/buildbattle plot <plot_index>").build());
            return false;
        }

        int plotIndex;
        try {
            plotIndex = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            chatError.send(player, ComponentBuilder.text(ChatColor.RED + "Argument at " + 2 + " is not a valid number").build());
            return false;
        }

        Plot plot = PlotManager.getPlotAt(plotIndex);
        if (plot == null) {
            chatError.send(player, ComponentBuilder.text(ChatColor.RED + "Cannot find any plot with index of: " + plotIndex).build());
            return false;
        }

        player.teleport(plot.getPlotEnterLocation());
        player.sendMessage(plot.toString());

        chatSuccess.send(player, ComponentBuilder.text("Teleported to plot: " + plotIndex + "\n  Player: " + plot.getBuilder().getName()).build());

        return true;
    }
}
