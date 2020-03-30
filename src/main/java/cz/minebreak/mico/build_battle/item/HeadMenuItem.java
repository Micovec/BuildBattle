package cz.minebreak.mico.build_battle.item;

import cz.minebreak.mico.build_battle.item.nbt.NbtTagItem;
import cz.minebreak.mico.build_battle.item.nbt.NbtTagNames;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class HeadMenuItem extends NbtTagItem {

    public HeadMenuItem() {
        super(Material.PLAYER_HEAD);

        setNbtTagBool(NbtTagNames.PLAYER_HEAD_ITEM, true);
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Player Heads");
        setItemMeta(itemMeta);
    }
}
