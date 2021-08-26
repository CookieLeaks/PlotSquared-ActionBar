package de.byaple.actionbar;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import com.google.common.eventbus.Subscribe;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.events.PlayerEnterPlotEvent;
import com.plotsquared.core.plot.Plot;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Setup extends JavaPlugin {

    public void onEnable() {
        PlotAPI api = new PlotAPI();
        api.registerListener(this);
    }

    @Subscribe
    public void plotEnter(PlayerEnterPlotEvent event) {
                Player player = Bukkit.getPlayer(event.getPlotPlayer().getName());
                Plot plot = event.getPlot();
                int plotX = event.getPlot().getId().getX();
                int plotY = event.getPlot().getId().getY();

                if (plot.getOwners().isEmpty()) {
                    sendActionbar(player, "&aFreies Plot" + " &7| " + plotX + ";" + plotY);
                } else {
                    String plot_owner = getName(plot);
                        sendActionbar(player, "&3" + plot_owner + " &7| " + plotX + ";" + plotY);
                }
    }


    ArrayList<UUID> uuid = new ArrayList<>();

    public static String getName(Plot plot) {
        Set<UUID> uuid = plot.getOwners();
        UUID firstuuid = uuid.iterator().next();
        return Bukkit.getOfflinePlayer(firstuuid).getName();
    }

    public static void sendActionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
    }
}
