package cz.minebreak.mico.build_battle.item;

import cz.minebreak.mico.build_battle.item.nbt.NbtTagItem;
import cz.minebreak.mico.build_battle.item.nbt.NbtTagNames;
import cz.minebreak.mico.build_battle.rate.BuildRateOption;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BuildRatingItem extends NbtTagItem {

    public BuildRatingItem(BuildRateOption rateOption) {
        super(rateOption.getMaterial());

        setNbtTagInt(NbtTagNames.RATE_OPTION_NUMBER, rateOption.getRateValue());
        ItemMeta itemMeta = getItemMeta();

        itemMeta.setDisplayName(rateOption.getChatColor() + rateOption.getName());
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Right or left click with this item in your hand to vote");
        itemMeta.setLore(lore);

        setItemMeta(itemMeta);
    }
}
