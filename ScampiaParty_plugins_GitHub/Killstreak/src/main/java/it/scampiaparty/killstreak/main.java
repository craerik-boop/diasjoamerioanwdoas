package it.scampiaparty.killstreak;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;
import java.util.*;

public class Main extends JavaPlugin implements Listener {

    private Map<UUID,Integer> streaks = new HashMap<>();
    private Map<Integer,Integer> rewardByStreak = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this,this);
        rewardByStreak.put(5,200);
        rewardByStreak.put(10,500);
        rewardByStreak.put(20,1000);
        rewardByStreak.put(50,2000);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player killed = e.getEntity();
        Player killer = killed.getKiller();
        if (killer==null) return;

        UUID killerID = killer.getUniqueId();
        streaks.put(killerID, streaks.getOrDefault(killerID,0)+1);

        int streak = streaks.get(killerID);
        if (rewardByStreak.containsKey(streak)) {
            int reward = rewardByStreak.get(streak);
            killer.sendMessage("§6Hai raggiunto " + streak + " kill di fila! Premi: " + reward + "$");
        }

        streaks.put(killed.getUniqueId(),0);
    }

    public int getStreak(UUID player) { return streaks.getOrDefault(player,0); }
}
