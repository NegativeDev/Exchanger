package games.negative.exchanger.api;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

@UtilityClass
public class Events {

    /**
     * An alternate way to call events rather than using Bukkit.getServer().getPluginManager().callEvent(event).
     * @param event The event to call.
     */
    public void call(Event event) {
        Bukkit.getServer().getPluginManager().callEvent(event);
    }
}
