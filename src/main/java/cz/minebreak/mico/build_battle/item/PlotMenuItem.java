package cz.minebreak.mico.build_battle.item;

import cz.minebreak.mico.build_battle.item.nbt.NbtTagItem;
import cz.minebreak.mico.build_battle.item.nbt.NbtTagNames;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class PlotMenuItem extends NbtTagItem {

    public PlotMenuItem() {
        super(Material.CRAFTING_TABLE);

        setNbtTagBool(NbtTagNames.PLOT_MENU_ITEM, true);
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Open Item Menu");
        setItemMeta(itemMeta);
    }
}
