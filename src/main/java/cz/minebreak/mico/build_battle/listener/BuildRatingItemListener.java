package cz.minebreak.mico.build_battle.listener;

import cz.minebreak.mico.build_battle.BuildBattle;
import cz.minebreak.mico.build_battle.session.BuildRatingSession;
import cz.minebreak.mico.build_battle.util.InvalidRateException;
import cz.minebreak.mico.build_battle.item.nbt.NbtTagNames;
import net.graymadness.minigame_api.helper.item.Nbt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BuildRatingItemListener implements Listener {

    private BuildBattle plugin;
    private BuildRatingSession buildRatingSession;

    public BuildRatingItemListener(BuildBattle plugin, BuildRatingSession buildRatingSection) {
        this.plugin = plugin;
        this.buildRatingSession = buildRatingSection;
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        if (isRatingItem(event.getItemDrop().getItemStack()) && plugin.isPlayerPlaying(event.getPlayer())) {
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
                if (!isRatingItem(clickedItem))
                    return;

                int rateNumber = Nbt.getNbt_Int(clickedItem, NbtTagNames.RATE_OPTION_NUMBER, 0);

                try {
                    buildRatingSession.ratePlot(event.getPlayer(), rateNumber);
                    event.getPlayer().sendMessage("You rated: " + event.getItem().getItemMeta().getDisplayName());
                } catch (InvalidRateException e) {
                    e.printStackTrace();
                }
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
        if (itemStack != null) {
            event.setCancelled(isRatingItem(itemStack));
        }
    }

    private boolean isRatingItem(ItemStack itemStack) {
        return itemStack != null && Nbt.getNbt_Int(itemStack, NbtTagNames.RATE_OPTION_NUMBER, 0) != 0;
    }
}
