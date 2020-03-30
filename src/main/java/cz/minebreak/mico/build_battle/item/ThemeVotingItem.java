package cz.minebreak.mico.build_battle.item;

import cz.minebreak.mico.build_battle.item.nbt.NbtTagItem;
import cz.minebreak.mico.build_battle.item.nbt.NbtTagNames;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

public class ThemeVotingItem extends NbtTagItem {

    public ThemeVotingItem() {
        super(Material.BOOK);

        setNbtTagBool(NbtTagNames.THEME_VOTING_ITEM, true);

        ItemMeta itemMeta = getItemMeta();
        itemMeta.addEnchant(Enchantment.LURE, 1, true);
        itemMeta.setDisplayName(ChatColor.GOLD + "Open Voting Menu");
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        setItemMeta(itemMeta);
    }
}
