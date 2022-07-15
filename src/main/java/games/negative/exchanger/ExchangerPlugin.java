package games.negative.exchanger;

import games.negative.exchanger.command.CommandExchanger;
import games.negative.exchanger.listener.ExchangerPlayerListener;
import games.negative.framework.BasePlugin;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExchangerPlugin extends BasePlugin {

    @Getter
    private static ExchangerPlugin instance;

    // This is the key we will be using to identify our custom "block".
    public static NamespacedKey EXCHANGER_BLOCK;

    // This is the key we will be using to identify invisible shulkers
    // that are used to give Exchanger blocks a special and cool glowing effect.
    public static NamespacedKey EXCHANGER_SHULKER;

    @Override
    public void onEnable() {
        super.onEnable();
        Locale.init(this);
        // Plugin startup logic
        instance = this;
        // Initialize the key we will be using to identify our custom "block"
        EXCHANGER_BLOCK = new NamespacedKey(this, "exchanger");
        // Initialize the key we will be using to identify invisible shulkers
        EXCHANGER_SHULKER = new NamespacedKey(this, "exchanger_shulker");

        loadFiles(this, "config.yml");
        reloadConfig();

        registerCommands(
                new CommandExchanger(this)
        );

        registerListeners(
                new ExchangerPlayerListener()
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
