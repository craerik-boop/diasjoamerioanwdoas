package it.scampiaparty.trade;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Trade/Withdraw attivato!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player)sender;

        switch(cmd.getName().toLowerCase()) {
            case "trade": p.sendMessage("§aTrade aperto"); break;
            case "withdraw": p.sendMessage("§aWithdraw effettuato"); break;
            default: return false;
        }
        return true;
    }
}
