package cz.minebreak.mico.build_battle.menu;

import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.graymadness.minigame_api.helper.item.PlayerHead;
import net.graymadness.minigame_api.menu.ItemMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HeadMenu extends ItemMenu {

    public HeadMenu() {
        super(Type.Chest3, ComponentBuilder.text("Heads").color(ChatColor.GOLD).build());

        setHeadItems();
    }

    @Override
    protected void onClick(@NotNull Player player, int i, @Nullable ItemStack clickedItem, @Nullable ItemStack handItem, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickedItem == null)
            return;

        player.getInventory().addItem(clickedItem);
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
    private void setHeadItems() {
        setItem(0, 0, PlayerHead.ArrowDown.build());
        setItem(1, 0, PlayerHead.ArrowLeft.build());
        setItem(2, 0, PlayerHead.ArrowRight.build());
        setItem(3, 0, PlayerHead.ArrowUp.build());
        setItem(4, 0, PlayerHead.Basketball.build());
        setItem(5, 0, PlayerHead.Cactus.build());
        setItem(6, 0, PlayerHead.Cake.build());
        setItem(7, 0, PlayerHead.Chest.build());
        setItem(8, 0, PlayerHead.CoconutBrown.build());
        setItem(0, 1, PlayerHead.CoconutGreen.build());
        setItem(1, 1, PlayerHead.Exclamation.build());
        setItem(2, 1, PlayerHead.Guard.build());
        //setItem(3, 1, PlayerHead.Melon.build());
        setItem(4, 1, PlayerHead.Present1.build());
        setItem(5, 1, PlayerHead.Present2.build());
        setItem(6, 1, PlayerHead.Prisoner.build());
        setItem(7, 1, PlayerHead.Pumpkin.build());
        setItem(8, 1, PlayerHead.Question.build());
        setItem(0, 2, PlayerHead.SoccerBall.build());
        setItem(1, 2, PlayerHead.Tnt1.build());
        setItem(2, 2, PlayerHead.Tnt2.build());
    }
}
