package games.negative.exchanger.listener;

import games.negative.exchanger.ExchangerPlugin;
import games.negative.exchanger.api.Events;
import games.negative.exchanger.event.ExchangerBlockBreakEvent;
import games.negative.exchanger.event.ExchangerBlockPlaceEvent;
import games.negative.framework.util.Utils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class ExchangerPlayerListener implements Listener {

    private final ExchangerPlugin plugin;

    public ExchangerPlayerListener(ExchangerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();

        ItemStack hand = event.getItemInHand();
        ItemMeta itemMeta = hand.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();

        if (!data.has(ExchangerPlugin.EXCHANGER_BLOCK, PersistentDataType.BYTE)) {
            // Block is not an Exchanger block.
            return;
        }

        Location location = block.getLocation();
        new BukkitRunnable() {
            @Override
            public void run() {
                Block delayedBlock = location.getBlock();
                if (delayedBlock.getState() instanceof TileState state) {
                    PersistentDataContainer data = state.getPersistentDataContainer();
                    data.set(ExchangerPlugin.EXCHANGER_BLOCK, PersistentDataType.BYTE, (byte) 1);
                    state.update(true);
                }
            }
        }.runTaskLater(plugin, 1);

        // Block is an Exchanger block.
        Events.call(new ExchangerBlockPlaceEvent(player, block));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!(block.getState() instanceof TileState tile)) {
            return;
        }

        PersistentDataContainer data = tile.getPersistentDataContainer();
        if (!data.has(ExchangerPlugin.EXCHANGER_BLOCK, PersistentDataType.BYTE)) {
            return;
        }

        // Block is an Exchanger block.
        event.setDropItems(false);

        Player player = event.getPlayer();
        // Add the item form the block to the player's inventory and play a sound.
        player.getInventory().addItem(plugin.getExchangerBlock().clone());
        player.playSound(player, Sound.ENTITY_CHICKEN_EGG, 1, 1);

        Events.call(new ExchangerBlockBreakEvent(player, block));
    }


}
