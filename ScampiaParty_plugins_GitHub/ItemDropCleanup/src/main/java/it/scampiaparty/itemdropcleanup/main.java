package it.scampiaparty.itemcleanup;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Item;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                getServer().getWorlds().forEach(w -> {
                    w.getEntitiesByClass(Item.class).forEach(i -> {
                        if (i.getTicksLived() > 1200) i.remove(); // 1 min = 1200 ticks
                    });
                });
            }
        }.runTaskTimer(this, 20L, 20L);
        getLogger().info("ItemDropCleanup attivo!");
    }
}
