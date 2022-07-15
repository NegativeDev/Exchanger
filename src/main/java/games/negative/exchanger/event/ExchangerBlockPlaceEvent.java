package games.negative.exchanger.event;

import games.negative.framework.event.PluginEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Getter
public class ExchangerBlockPlaceEvent extends PluginEvent {

    private final Player player;
    private final Block block;
}
