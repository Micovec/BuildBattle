package cz.minebreak.mico.build_battle.menu;

import cz.minebreak.mico.build_battle.config.BuildBattleConfig;
import cz.minebreak.mico.build_battle.util.ThemeNotFoundException;
import cz.minebreak.mico.build_battle.item.nbt.NbtTagNames;
import cz.minebreak.mico.build_battle.vote.VoteOption;
import cz.minebreak.mico.build_battle.vote.VoteListener;
import cz.minebreak.mico.build_battle.vote.Voting;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.graymadness.minigame_api.helper.item.ItemBuilder;
import net.graymadness.minigame_api.helper.item.Nbt;
import net.graymadness.minigame_api.menu.ItemMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemeVotingMenu extends ItemMenu implements VoteListener {

    //TODO: recreate voting menus for players?
    private static Map<Player, ThemeVotingMenu> votingPlayers = new HashMap<>();

    private List<ItemStack> paintings;
    private Voting themeVoting;

    public ThemeVotingMenu(Voting themeVoting) {
        super(Type.Chest5, ComponentBuilder.text(ChatColor.GOLD + "Vote for theme").bold(true).build());

        this.themeVoting = themeVoting;
        paintings = new ArrayList<>(BuildBattleConfig.MAX_THEMES_VOTING);
        themeVoting.addListener(this);
        addItemsToInventory();
    }

    @Override
    protected void onClick(@NotNull Player player, int slotNumber, @Nullable ItemStack clickedItem, @Nullable ItemStack playerHandItem, @NotNull InventoryClickEvent event) {
        if (!clickedItem.getType().equals((Material.PAINTING)))
            return;

        // Remove enchantments from all paintings
        for (ItemStack item : paintings) {
            item.removeEnchantment(Enchantment.LURE);
        }

        // Add enchantment: LORE onto clicked item
        clickedItem.addUnsafeEnchantment(Enchantment.LURE, 1);

        int option = Nbt.getNbt_Int(clickedItem, NbtTagNames.THEME_NUMBER); // Same number as row

        try {
            themeVoting.voteForOption(player, option);
            player.sendMessage("You voted for: " + themeVoting.getVoteAt(option).getName());
        } catch (ThemeNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onOpen(@NotNull Player player) {
        votingPlayers.put(player, this);
    }

    @Override
    protected void onClose(@NotNull Player player) {

    }

    @Override
    public void onVote(VoteOption voteOption) {
        updateVotes(voteOption.getVoteNumber(), voteOption.getVotes());
    }

    @Override
    public void onUnVote(VoteOption voteOption) {
        updateVotes(voteOption.getVoteNumber(), voteOption.getVotes());
    }

    public static ThemeVotingMenu getPlayerMenu(Player player) {
        return votingPlayers.get(player);
    }

    public static void removeVotingPlayerMenus() {
        votingPlayers.clear();
    }

    /*
        Private methods
     */
    private void addItemsToInventory() {
        for (int i = 0; i < BuildBattleConfig.MAX_THEMES_VOTING; i++) {
            VoteOption themeVoteOption = themeVoting.getVoteAt(i);

            ItemBuilder itemBuilder = new ItemBuilder(Material.PAINTING);
            itemBuilder.setName(ComponentBuilder.text(ChatColor.DARK_GREEN + "VOTING FOR THEME").build());
            itemBuilder.addLore(ComponentBuilder.text(ChatColor.WHITE + "Vote for").build());
            itemBuilder.addLore(ComponentBuilder.text(ChatColor.AQUA + themeVoteOption.getName()).build());
            itemBuilder.addLore(ComponentBuilder.text(ChatColor.WHITE + "").build());
            itemBuilder.addLore(ComponentBuilder.text(ChatColor.WHITE + "Current votes: " + themeVoting.getVotesAt(i)).build()); // Line space
            itemBuilder.addLore(ComponentBuilder.text(ChatColor.GOLD + "You are voting for " + ChatColor.AQUA + themeVoteOption.getName()).build());
            itemBuilder.hideAllFlags();

            ItemStack item = itemBuilder.build();
            item = Nbt.setNbt_Int(item, NbtTagNames.THEME_NUMBER, i);
            setItem(0, i, item);
            paintings.add(getInventory().getItem(i * 9));
        }
    }

    private void updateVotes(int option, int votes) {
        ItemStack item = paintings.get(option);
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = paintings.get(option).getItemMeta().getLore();

        String loreVotes = lore.get(3);
        loreVotes = loreVotes.replaceAll("\\d", String.valueOf(votes));

        lore.set(3, loreVotes);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }
}
