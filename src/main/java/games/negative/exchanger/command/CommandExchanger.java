package games.negative.exchanger.command;

import com.google.common.base.Preconditions;
import games.negative.exchanger.ExchangerPlugin;
import games.negative.exchanger.Locale;
import games.negative.framework.command.Command;
import games.negative.framework.command.SubCommand;
import games.negative.framework.command.annotation.CommandInfo;
import games.negative.framework.util.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@CommandInfo(name = "exchanger", permission = "exchanger.admin")
public class CommandExchanger extends Command {

    private final ExchangerPlugin plugin;
    private ItemStack exchangerBlock;

    public CommandExchanger(ExchangerPlugin plugin) {
        this.plugin = plugin;
        reload();

        addSubCommands(
                new Reload(),
                new Give()
        );
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        // should i make a prompt when they do /exchanger ?
        // nahhhh, i don't want to make a prompt. im lazy lmfao
    }

    @CommandInfo(name = "give", args = "player")
    private class Give extends SubCommand {

        @Override
        public void onCommand(CommandSender sender, String[] args) {
            Player player = getPlayer(args[0]);
            if (player == null) {
                Locale.PLAYER_NOT_ONLINE.send(sender);
                return;
            }

            player.getInventory().addItem(exchangerBlock.clone());
            Locale.EXCHANGER_BLOCK_RECEIVED.send(player);
            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

            Locale.GENERIC_SUCCESS.send(sender);
            if (sender instanceof Player operator) {
                operator.playSound(operator, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
        }
    }

    @CommandInfo(name = "reload")
    private class Reload extends SubCommand {

        @Override
        public void onCommand(CommandSender commandSender, String[] strings) {
            plugin.reloadConfig();
            Locale.CONFIG_RELOADED.send(commandSender);
            reload();

        }
    }

    private void reload() {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection block = config.getConfigurationSection("exchanger-block");
        Preconditions.checkNotNull(block, "Missing exchanger-block section in config");

        Material material = Material.getMaterial(block.getString("material", "TRAPPED_CHEST"));
        String displayName = block.getString("display_name", "&9&lExchanger Block");
        List<String> lore = block.getStringList("lore");

        assert material != null;
        exchangerBlock = new ItemStack(material);
        ItemMeta itemMeta = exchangerBlock.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(Utils.color(displayName));
        itemMeta.setLore(Utils.color(lore));

        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.set(ExchangerPlugin.EXCHANGER_BLOCK, PersistentDataType.BYTE, (byte) 1);
        exchangerBlock.setItemMeta(itemMeta);
    }
}
