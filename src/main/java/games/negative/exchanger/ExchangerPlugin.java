package games.negative.exchanger;

import games.negative.exchanger.command.CommandExchanger;
import games.negative.exchanger.listener.ExchangerPlayerListener;
import games.negative.exchanger.runnable.ExchangerBlockParticleRunnable;
import games.negative.framework.BasePlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public final class ExchangerPlugin extends BasePlugin {

    @Getter
    private static ExchangerPlugin instance;

    // This is the key we will be using to identify our custom "block".
    public static NamespacedKey EXCHANGER_BLOCK;

    // This is the representation of the Exchanger Block Item.
    @Getter @Setter
    private ItemStack exchangerBlock;

    @Override
    public void onEnable() {
        super.onEnable();
        Locale.init(this);
        // Plugin startup logic
        instance = this;
        // Initialize the key we will be using to identify our custom "block"
        EXCHANGER_BLOCK = new NamespacedKey(this, "exchanger");

        loadFiles(this, "config.yml");
        reloadConfig();

        registerCommands(
                new CommandExchanger(this)
        );

        registerListeners(
                new ExchangerPlayerListener(this)
        );

        new ExchangerBlockParticleRunnable().runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
