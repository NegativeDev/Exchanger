package games.negative.exchanger.runnable;

import games.negative.exchanger.ExchangerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
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
import java.util.stream.Collectors;

public class ExchangerBlockParticleRunnable extends BukkitRunnable {

    private static final int radius = 10;
    private static final int particles = 2;

    private static final double particleOffsetX = 1;
    private static final double particleOffsetY = 0.52;
    private static final double particleOffsetZ = 1;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = player.getLocation();
            // Get all the blocks in an area around the player
            List<Block> blocks = new ArrayList<>();
            for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
                for (int y = location.getBlockY() - radius; y < location.getBlockY() + radius; y++) {
                    for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                        blocks.add(Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z));
                    }
                }
            }

            // Get all the Tile States in the area
            blocks.stream().filter(block -> !(block.getState() instanceof TileState))
                    .collect(Collectors.toSet())
                    .forEach(blocks::remove);

            Random random = new Random();
            for (Block block : blocks) {
                PersistentDataContainer data = ((TileState) block.getState()).getPersistentDataContainer();
                if (!data.has(ExchangerPlugin.EXCHANGER_BLOCK, PersistentDataType.BYTE)) {
                    continue;
                }
                // Spawn a particle for the block
                player.spawnParticle(
                        Particle.VILLAGER_HAPPY,
                        block.getLocation(), particles,
                        random.nextDouble(-particleOffsetX, particleOffsetX),
                        random.nextDouble(-particleOffsetY, particleOffsetY),
                        random.nextDouble(-particleOffsetZ, particleOffsetZ)
                );
            }
        }
    }
}
