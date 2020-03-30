package cz.minebreak.mico.build_battle.rate;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum BuildRateOption {

    // HAS to start with 1 not 0. When getting integer nbt tag the default value cannot be negative
    POOP("Poop", 1, Material.GRAY_WOOL, ChatColor.DARK_GRAY),
    OK("OK", 2, Material.WHITE_WOOL, ChatColor.WHITE),
    GOOD("GOOD", 3, Material.GREEN_WOOL, ChatColor.DARK_GREEN),
    VERY_GOOD("VERY GOOD", 4, Material.CYAN_WOOL, ChatColor.DARK_AQUA),
    EPIC("EPIC", 5, Material.PURPLE_WOOL, ChatColor.DARK_PURPLE),
    LEGENDARY("LEGENDARY", 6, Material.ORANGE_WOOL, ChatColor.GOLD);

    private String name;
    private int rateValue;
    private Material material;
    private ChatColor chatColor;

    BuildRateOption(String name, int rateValue, Material material, ChatColor chatColor) {
        this.name = name;
        this.rateValue = rateValue;
        this.material = material;
        this.chatColor = chatColor;
    }

    public String getName() {
        return name;
    }

    public int getRateValue() {
        return rateValue;
    }

    public Material getMaterial() {
        return material;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }
}
