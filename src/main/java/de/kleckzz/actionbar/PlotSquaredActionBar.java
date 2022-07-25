package de.kleckzz.actionbar;

import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

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

public class PlotSquaredActionBar extends JavaPlugin {
    private String plot_free;
    private String plot_owner;
    private String plot_owner_yourself;

    public void onEnable() {
        if(!this.getServer().getPluginManager().isPluginEnabled("PlotSquared")) {
            this.getLogger().log(Level.SEVERE, "PlotSquared not found! To use this plugin you need to install PlotSquared!");
            this.getPluginLoader().disablePlugin(this);
        }

        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        this.plot_free = getConfig().getString("plot.free");
        this.plot_owner = getConfig().getString("plot.owner");
        this.plot_owner_yourself = getConfig().getString("plot.yourself");

        PlotAPI api = new PlotAPI();
        api.registerListener(this);
    }

    @Subscribe
    public void plotEnter(PlayerEnterPlotEvent event) {
        Player player = Bukkit.getPlayer(event.getPlotPlayer().getUUID());
        if (player == null) return;

        Plot plot = event.getPlot();
        int plotX = event.getPlot().getId().getX();
        int plotY = event.getPlot().getId().getY();

        if (plot.getOwners().isEmpty())
            sendActionbar(player, plot_free.replace("%plotX%", String.valueOf(plotX)).replace("%plotY%", String.valueOf(plotY)));
        else {
            String owner = getName(plot) + "";
            if (owner.equalsIgnoreCase(player.getName()))
                sendActionbar(player, plot_owner_yourself.replace("%plotX%", String.valueOf(plotX)).replace("%plotY%", String.valueOf(plotY)).replace("%owner%", String.valueOf(owner)));
            else
                sendActionbar(player, plot_owner.replace("%plotX%", String.valueOf(plotX)).replace("%plotY%", String.valueOf(plotY)).replace("%owner%", String.valueOf(owner)));
        }
    }

    public static String getName(Plot plot) {
        Set<UUID> uuid = plot.getOwners();
        UUID firstUuid = uuid.iterator().next();

        return Bukkit.getOfflinePlayer(firstUuid).getName();
    }

    public static void sendActionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
    }
}
