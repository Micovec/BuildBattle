package cz.minebreak.mico.build_battle.item;

import cz.minebreak.mico.build_battle.item.nbt.NbtTagItem;
import cz.minebreak.mico.build_battle.item.nbt.NbtTagNames;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ChangeFloorItem extends NbtTagItem {
    public ChangeFloorItem() {
        super(Material.GRASS_BLOCK);

        setNbtTagBool(NbtTagNames.CHANGE_FLOOR_ITEM, true);
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Change floor");

        List<String> lore = new ArrayList<>();
        lore.add("Drag block here to");
        lore.add("change the floor.");
        itemMeta.setLore(lore);

        setItemMeta(itemMeta);
    }
}
