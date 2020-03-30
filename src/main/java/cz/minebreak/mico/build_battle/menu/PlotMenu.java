package cz.minebreak.mico.build_battle.menu;

import cz.minebreak.mico.build_battle.item.ChangeFloorItem;
import cz.minebreak.mico.build_battle.item.HeadMenuItem;
import cz.minebreak.mico.build_battle.plot.Plot;
import cz.minebreak.mico.build_battle.plot.PlotManager;
import cz.minebreak.mico.build_battle.item.nbt.NbtTagNames;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.graymadness.minigame_api.helper.item.ItemBuilder;
import net.graymadness.minigame_api.helper.item.Nbt;
import net.graymadness.minigame_api.menu.ItemMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlotMenu extends ItemMenu {

    private Plot plot;

    public PlotMenu(Plot plot) {
        super(Type.Chest3, ComponentBuilder.text("Build edit").bold(true).color(ChatColor.DARK_GREEN).build());

        this.plot = plot;
        setMenuItems();
    }

    @Override
    protected void onClick(@NotNull Player player, int i, @Nullable ItemStack clickedItem, @Nullable ItemStack handItem, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (isChangeFloorItem(clickedItem)) {
            if (handItem != null) {
                Plot playerPlot = PlotManager.getPlayerPlot(player);
                if (playerPlot == null) {
                    // Something went wrong. Player is not registered inside PlotManager's plot collection
                    return;
                }

                playerPlot.changeFloor(handItem.getType());
            }
        } else if (isHeadsItem(clickedItem)) {
            HeadMenu headMenu = new HeadMenu();
            headMenu.open(player);
        }
    }

    @Override
    protected void onOpen(@NotNull Player player) {

    }

    @Override
    protected void onClose(@NotNull Player player) {

    }

    /*
        Private methods
     */
    private void setMenuItems() {
        setItem(5, 1, new ChangeFloorItem());
        setItem(3, 1, new HeadMenuItem());
    }

    private boolean isChangeFloorItem(ItemStack itemStack) {
        return itemStack != null && Nbt.getNbt_Bool(itemStack, NbtTagNames.CHANGE_FLOOR_ITEM, false);
    }

    private boolean isHeadsItem(ItemStack itemStack) {
        return itemStack != null && Nbt.getNbt_Bool(itemStack, NbtTagNames.PLAYER_HEAD_ITEM, false);
    }
}
