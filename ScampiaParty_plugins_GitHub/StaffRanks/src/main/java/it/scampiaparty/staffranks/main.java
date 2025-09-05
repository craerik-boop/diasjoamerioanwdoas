package it.scampiaparty.staff;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Staff Ranks attivo!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player)sender;

        switch(cmd.getName().toLowerCase()) {
            case "freeze": p.sendMessage("§aComando freeze attivato"); break;
            case "inv": p.sendMessage("§aModalità invisibile attivata"); break;
            case "fly": p.sendMessage("§aFly attivato"); break;
            default: return false;
        }
        return true;
    }
}
