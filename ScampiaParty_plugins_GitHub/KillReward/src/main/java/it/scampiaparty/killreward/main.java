package it.scampiaparty.killreward;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;
import java.util.*;

public class Main extends JavaPlugin implements Listener {

    private Map<UUID,Integer> balances = new HashMap<>();
    private Map<UUID,Integer> kills = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this,this);
        getLogger().info("Kill Reward attivato!");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player killed = e.getEntity();
        Player killer = killed.getKiller();
        if (killer==null) return;

        // Aggiunge soldi
        int reward = 100;
        balances.put(killer.getUniqueId(), balances.getOrDefault(killer.getUniqueId(),0)+reward);

        // Aggiorna kill
        kills.put(killer.getUniqueId(), kills.getOrDefault(killer.getUniqueId(),0)+1);

        killer.sendMessage("§aHai ricevuto " + reward + "$ per la kill! Totale kill: " + kills.get(killer.getUniqueId()));
    }

    public int getKills(UUID player) { return kills.getOrDefault(player,0); }
    public int getBalance(UUID player) { return balances.getOrDefault(player,0); }
}
