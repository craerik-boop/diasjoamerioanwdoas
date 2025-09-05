package it.scampiaparty.combatlog;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.Bukkit;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    private HashMap<UUID, Long> combatTagged = new HashMap<>();
    private int combatDuration = 15; // secondi

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Combat-Log attivato!");
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;
        Player damager = (Player) e.getDamager();
        Player damaged = (Player) e.getEntity();
        long time = System.currentTimeMillis();
        combatTagged.put(damager.getUniqueId(), time);
        combatTagged.put(damaged.getUniqueId(), time);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        long currentTime = System.currentTimeMillis();
        if (combatTagged.containsKey(p.getUniqueId())) {
            long lastHit = combatTagged.get(p.getUniqueId());
            if ((currentTime - lastHit) < (combatDuration * 1000)) {
                e.setCancelled(true);
                p.sendMessage("§cSei in combattimento! Non puoi usare comandi.");
            }
        }
    }

    public boolean isInCombat(Player p) {
        if (!combatTagged.containsKey(p.getUniqueId())) return false;
        long lastHit = combatTagged.get(p.getUniqueId());
        return (System.currentTimeMillis() - lastHit) < (combatDuration * 1000);
    }
}
