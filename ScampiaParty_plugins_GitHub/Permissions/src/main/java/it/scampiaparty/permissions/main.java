package it.scampiaparty.permissions;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Permissions Plugin attivato!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player)sender;

        if (cmd.getName().equalsIgnoreCase("giveperm")) {
            if (args.length<2) { p.sendMessage("§cUso: /giveperm <player> <perm>"); return true; }
            Player target = Bukkit.getPlayer(args[0]);
            if (target==null) { p.sendMessage("§cPlayer non trovato"); return true; }
            target.addAttachment(this,true,args[1]);
            p.sendMessage("§aPermesso " + args[1] + " dato a " + target.getName());
            return true;
        }

        return false;
    }
}
