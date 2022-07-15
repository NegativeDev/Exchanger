package games.negative.exchanger;

import games.negative.framework.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum Locale {

    PLAYER_NOT_ONLINE("PLAYER-NOT-ONLINE", Collections.singletonList(
            "&cThe requested player is not online."
    )),

    CONFIG_RELOADED("CONFIG-RELOADED", Collections.singletonList(
            "&aConfiguration reloaded."
    )),

    GENERIC_SUCCESS("GENERIC-SUCCESS", Collections.singletonList(
            "&aSuccess."
    )),

    EXCHANGER_BLOCK_RECEIVED("EXCHANGER-BLOCK-RECEIVED", Collections.singletonList(
            "&aYou have received an Exchanger block."
    )),

    ;
    private final String id;
    private final List<String> defaultMessage;
    private Message message;

    @SneakyThrows
    public static void init(JavaPlugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "messages.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!configFile.exists()) {
            Arrays.stream(values()).forEach(locale -> {
                String id = locale.getId();
                List<String> defaultMessage = locale.getDefaultMessage();

                config.set(id, defaultMessage);
            });

        } else {
            Arrays.stream(values()).filter(locale -> {
                String id = locale.getId();
                return (config.get(id, null) == null);
            }).forEach(locale -> config.set(locale.getId(), locale.getDefaultMessage()));

        }
        config.save(configFile);

        // Creates the message objects
        Arrays.stream(values()).forEach(locale ->
                locale.message = new Message(config.getStringList(locale.getId())
                        .toArray(new String[0])));
    }

    public void send(CommandSender sender) {
        message.send(sender);
    }

    public void send(Iterable<CommandSender> players) {
        message.send(players);
    }

    public void broadcast() {
        message.broadcast();
    }

    public Message replace(String placeholder, String replacement) {
        return message.replace(placeholder, replacement);
    }
}
