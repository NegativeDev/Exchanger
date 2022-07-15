package games.negative.exchanger.listener;

import games.negative.exchanger.ExchangerPlugin;
import games.negative.exchanger.api.Events;
import games.negative.exchanger.event.ExchangerBlockPlaceEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class ExchangerPlayerListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        if (!(block.getState() instanceof TileState tile)) {
            player.sendMessage("not a tile block"); // test - will remove
            // Block is not a Tile, so it is clearly not an Exchanger block.
            return;
        }

        ItemStack hand = event.getItemInHand();
        ItemMeta itemMeta = hand.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();

        if (!data.has(ExchangerPlugin.EXCHANGER_BLOCK, PersistentDataType.BYTE)) {
            player.sendMessage("This block is not an Exchanger block."); // test - will remove
            // Block is not an Exchanger block.
            return;
        }

        // Block is an Exchanger block.

        // Spawn the invisible shulker that is used to identify the unique block.
        Location location = block.getLocation();
        World world = location.getWorld();
        if (world == null) // ???
            return;

        Shulker entity = (Shulker) world.spawnEntity(location, EntityType.SHULKER);
        PersistentDataContainer shulkerData = entity.getPersistentDataContainer();
        shulkerData.set(ExchangerPlugin.EXCHANGER_SHULKER, PersistentDataType.BYTE, (byte) 1);
        entity.setGlowing(true);
        entity.setSilent(true);
        entity.setAware(false);
        entity.setPeek(0);
        entity.setInvulnerable(true);
        entity.setInvisible(true);
        entity.setCollidable(false);
        entity.setAI(false);
        // Currently having an issue where there is still
        // a bug where the shulker still has a collision box.
        // therefore, i cannot interact with the chest.
        // It also seems like there is no way to change the collision box other than ArmorStands.

        Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();
        Team team;
        try {
            team = scoreboard.registerNewTeam("exchanger-" + entity.getEntityId());
        } catch (Exception exception) {
            team = scoreboard.getTeam("exchanger-" + entity.getEntityId());
        }
        if (team == null) // ???
            return;

        team.setColor(ChatColor.AQUA);
        team.addEntry(entity.getUniqueId().toString());

        Events.call(new ExchangerBlockPlaceEvent(player, block));
    }


}
