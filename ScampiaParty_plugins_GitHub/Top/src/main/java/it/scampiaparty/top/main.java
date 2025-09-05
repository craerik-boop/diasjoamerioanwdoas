package it.scampiaparty.top;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;

public class Main extends JavaPlugin {

    private Map<UUID, Integer> playerKills = new HashMap<>();
    private Map<String, Integer> clanKills = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Top Plugin attivato!");
    }

    public void addKill(Player player, String clanName) {
        playerKills.put(player.getUniqueId(), playerKills.getOrDefault(player.getUniqueId(), 0) + 1);
        if (clanName != null && !clanName.isEmpty()) {
            clanKills.put(clanName, clanKills.getOrDefault(clanName, 0) + 1);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("topplayers")) {
            player.sendMessage("§6--- Top Players ---");
            playerKills.entrySet().stream()
                    .sorted(Map.Entry.<UUID,Integer>comparingByValue().reversed())
                    .limit(10)
                    .forEach(entry -> player.sendMessage("- " + getServer().getOfflinePlayer(entry.getKey()).getName() + ": " + entry.getValue() + " kill"));
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("topclans")) {
            player.sendMessage("§6--- Top Clans ---");
            clanKills.entrySet().stream()
                    .sorted(Map.Entry.<String,Integer>comparingByValue().reversed())
                    .limit(10)
                    .forEach(entry -> player.sendMessage("- " + entry.getKey() + ": " + entry.getValue() + " kill"));
            return true;
        }
        return false;
    }
}
