package cz.minebreak.mico.build_battle.listener;

import cz.minebreak.mico.build_battle.BuildBattle;
import cz.minebreak.mico.build_battle.plot.PlotManager;
import cz.minebreak.mico.build_battle.util.BuildBattleState;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PlayerInteractBlockListener implements Listener {

    private BuildBattle plugin;

    public PlayerInteractBlockListener(BuildBattle plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK))
            return;

        if (!plugin.isPlayerPlaying(event.getPlayer()))
            return;

        if (plugin.getBuildBattleState() != BuildBattleState.BUILDING) {
            event.setCancelled(true);
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        Player player = event.getPlayer();

        Location actionBlockLoc = clickedBlock.getLocation();
        boolean placingBlock = event.getAction() == Action.RIGHT_CLICK_BLOCK;

        if (placingBlock) {
            switch (event.getBlockFace()) {
                case UP:
                    actionBlockLoc.add(new Vector(0, 1, 0));
                    break;
                case DOWN:
                    actionBlockLoc.add(new Vector(0, -1, 0));
                    break;
                case WEST:
                    actionBlockLoc.add(new Vector(-1, 0, 0));
                    break;
                case EAST:
                    actionBlockLoc.add(new Vector(1, 0, 0));
                    break;
                case NORTH:
                    actionBlockLoc.add(new Vector(0, 0, -1));
                    break;
                case SOUTH:
                    actionBlockLoc.add(new Vector(0, 0, 1));
                    break;
                default:
                    break;
            }
        }

        event.setCancelled(!PlotManager.canPlayerEditLocation(player, actionBlockLoc));
    }
}
