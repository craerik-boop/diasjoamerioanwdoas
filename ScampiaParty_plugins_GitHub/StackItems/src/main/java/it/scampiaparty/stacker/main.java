package it.scampiaparty.stackitems;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("StackItems Plugin attivato!");
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        if (item.getType() == Material.SPLASH_POTION ||
            item.getType() == Material.ENDER_PEARL ||
            item.getType() == Material.MUSHROOM_SOUP ||
            item.getType() == Material.GOLDEN_APPLE) {
            item.setAmount(Math.min(item.getAmount(), 64)); // stack massimo 64
        }
    }
}
