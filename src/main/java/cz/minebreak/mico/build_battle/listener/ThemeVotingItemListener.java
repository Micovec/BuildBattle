package cz.minebreak.mico.build_battle.listener;

import cz.minebreak.mico.build_battle.BuildBattle;
import cz.minebreak.mico.build_battle.menu.ThemeVotingMenu;
import cz.minebreak.mico.build_battle.item.nbt.NbtTagNames;
import net.graymadness.minigame_api.helper.item.Nbt;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ThemeVotingItemListener implements Listener {

    private BuildBattle plugin;

    public ThemeVotingItemListener(BuildBattle plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        if (isVotingThemeItem(event.getItemDrop().getItemStack()) && plugin.isPlayerPlaying(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        switch (event.getAction()) {
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                if (isVotingThemeItem(event.getItem())) {
                    ThemeVotingMenu menu = ThemeVotingMenu.getPlayerMenu(event.getPlayer());
                    menu.open(event.getPlayer());
                }
                break;
            default:
                break;
        }
    }

    private boolean isVotingThemeItem(ItemStack itemStack) {
        return Nbt.getNbt_Bool(itemStack, NbtTagNames.THEME_VOTING_ITEM, false);
    }
}
