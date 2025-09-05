package it.scampiaparty.bounty;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.*;

public class Main extends JavaPlugin {

    private Map<UUID,Integer> bounties = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Bounty attivato!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player)sender;

        if (cmd.getName().equalsIgnoreCase("bounty")) {
            if (args.length<2) { p.sendMessage("§cUso: /bounty <player> <amount>"); return true; }
            Player target = getServer().getPlayer(args[0]);
            if (target==null) { p.sendMessage("§cPlayer non trovato!"); return true; }
            int amount = Integer.parseInt(args[1]);
            bounties.put(target.getUniqueId(), bounties.getOrDefault(target.getUniqueId(),0)+amount);
            p.sendMessage("§aHai messo una bounty su " + target.getName() + " di " + amount + "$");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("bountystatus")) {
            Player target = (args.length>0)?getServer().getPlayer(args[0]):p;
            int bounty = bounties.getOrDefault(target.getUniqueId(),0);
            p.sendMessage("§6Bounty su " + target.getName() + ": " + bounty + "$");
            return true;
        }
        return false;
    }
}
