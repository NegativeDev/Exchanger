package games.negative.exchanger.runnable;

import games.negative.exchanger.ExchangerPlugin;
import games.negative.framework.util.UtilPlayer;
import games.negative.framework.util.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ExchangerBlockParticleRunnable extends BukkitRunnable {

    private static final int radius = 10;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = player.getLocation();
            // Get all the blocks in an area around the player
            List<Block> blocks = new ArrayList<>();
            for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    Block blockAt = Objects.requireNonNull(location.getWorld()).getBlockAt(x, location.getBlockY(), z);
                    if (!(blockAt.getState() instanceof TileState tile))
                        continue;

                    PersistentDataContainer data = tile.getPersistentDataContainer();
                    if (!data.has(ExchangerPlugin.EXCHANGER_BLOCK, PersistentDataType.BYTE))
                        continue;

                    blocks.add(location.getWorld().getBlockAt(x, location.getBlockY(), z));
                }
            }

            Random random = new Random();
            for (Block block : blocks) {
                player.spawnParticle(
                        Particle.VILLAGER_HAPPY,
                        block.getLocation(), 1,
                        random.nextDouble(0, 1),
                        random.nextDouble(0, 1),
                        random.nextDouble(0, 1)
                );
            }
        }
    }
}
