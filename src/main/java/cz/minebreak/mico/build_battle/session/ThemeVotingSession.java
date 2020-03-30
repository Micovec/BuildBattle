package cz.minebreak.mico.build_battle.session;

import cz.minebreak.mico.build_battle.config.BuildBattleConfig;
import cz.minebreak.mico.build_battle.item.ThemeVotingItem;
import cz.minebreak.mico.build_battle.menu.ThemeVotingMenu;
import cz.minebreak.mico.build_battle.plot.PlotManager;
import cz.minebreak.mico.build_battle.item.nbt.NbtTagNames;
import cz.minebreak.mico.build_battle.util.NotEnoughPlotsException;
import cz.minebreak.mico.build_battle.vote.VoteOption;
import cz.minebreak.mico.build_battle.vote.Voting;
import net.graymadness.minigame_api.helper.item.Nbt;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Random;

public class ThemeVotingSession extends Session {

    private List<String> themes;
    private Voting themeVoting;

    public ThemeVotingSession(List<String> themes) {
        this.themes = themes;
    }

    @Override
    public boolean start(Object... args) {
        givePlayersCreativeMode();

        try {
            teleportPlayersToPlots();
        } catch (NotEnoughPlotsException e) {
            e.printStackTrace();
            return false;
        }

        return setUpThemeVoting();
    }

    @Override
    public void stop() {
        removeThemeVotingItemsFromPlayers();
        closeThemeVotingMenus();

        ThemeVotingMenu.removeVotingPlayerMenus();
    }

    @Override
    public void reset() {
        themeVoting.resetVoting();
        PlotManager.removePlayersFromPlots();
    }

    public VoteOption getWinningOption() {
        return themeVoting.getWinningVote();
    }

    /*
        Private methods
     */
    private void givePlayersCreativeMode() {
        for (Player player : playingPlayers) {
            player.setGameMode(GameMode.CREATIVE);
        }
    }

    private void teleportPlayersToPlots() throws NotEnoughPlotsException {
        for (Player player : playingPlayers) {
            // Assign plot to player
            PlotManager.assignPlayerToNextEmptyPlot(player);

            player.teleport(PlotManager.getPlayerPlot(player).getPlotEnterLocation());
            player.sendMessage("You have been teleported to plot. Vote for theme");
        }
    }

    private boolean setUpThemeVoting() {
        Random rnd = new Random();
        VoteOption[] selectedThemes = new VoteOption[BuildBattleConfig.MAX_THEMES_VOTING];

        for (int i = 0; i < selectedThemes.length; i++) {
            try {
                String themeName = themes.get(rnd.nextInt(themes.size()));
                selectedThemes[i] = new VoteOption(themeName, i);
                themes.remove(selectedThemes[i].getName()); // Remove the theme so it won't get repeated
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return false;
            }
        }

        // Add themes back
        for (VoteOption selectedTheme : selectedThemes) {
            themes.add(selectedTheme.getName());
        }

        themeVoting = new Voting(selectedThemes);
        for (Player player : playingPlayers) {
            ThemeVotingMenu votingMenu = new ThemeVotingMenu(themeVoting);

            player.getInventory().setHeldItemSlot(0);
            votingMenu.open(player);
            player.getInventory().setItem(0, new ThemeVotingItem());
        }

        return true;
    }

    private void removeThemeVotingItemsFromPlayers() {
        for (Player player : playingPlayers) {

            PlayerInventory playerInventory = player.getInventory();
            ItemStack[] playerItems = playerInventory.getContents();

            for (ItemStack playerItem : playerItems) {
                if (playerItem == null)
                    continue;

                if (isThemeVotingItem(playerItem)) {
                    playerInventory.remove(playerItem);
                }
            }
        }
    }

    private boolean isThemeVotingItem(ItemStack itemStack) {
        return Nbt.getNbt_Bool(itemStack, NbtTagNames.THEME_VOTING_ITEM, false);
    }

    private void closeThemeVotingMenus() {
        for (Player player : playingPlayers) {
            player.closeInventory();
        }
    }
}
