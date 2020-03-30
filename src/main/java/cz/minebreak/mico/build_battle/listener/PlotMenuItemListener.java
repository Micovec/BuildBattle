package cz.minebreak.mico.build_battle.listener;

import cz.minebreak.mico.build_battle.BuildBattle;
import cz.minebreak.mico.build_battle.menu.PlotMenu;
import cz.minebreak.mico.build_battle.plot.PlotManager;
import cz.minebreak.mico.build_battle.item.nbt.NbtTagNames;
import net.graymadness.minigame_api.helper.item.Nbt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlotMenuItemListener implements Listener {

    private BuildBattle plugin;

    public PlotMenuItemListener(BuildBattle plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        if (isPlotMenuItem(event.getItemDrop().getItemStack()) && plugin.isPlayerPlaying(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!plugin.isPlayerPlaying(event.getPlayer())) {
            return;
        }

        switch (event.getAction()) {
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                ItemStack clickedItem = event.getItem();
                if (!isPlotMenuItem(clickedItem))
                    return;

                // Show menu
                PlotMenu plotMenu = new PlotMenu(PlotManager.getPlayerPlot(event.getPlayer()));
                plotMenu.open(event.getPlayer());

                event.setCancelled(true);
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!plugin.isPlayerPlaying((Player)event.getWhoClicked())) {
            return;
        }

        ItemStack itemStack = event.getCurrentItem();
        if (isPlotMenuItem(itemStack)) {
            event.setCancelled(true);
        }
    }

    private boolean isPlotMenuItem(ItemStack itemStack) {
        return itemStack != null && Nbt.getNbt_Bool(itemStack, NbtTagNames.PLOT_MENU_ITEM, false);
    }
}
