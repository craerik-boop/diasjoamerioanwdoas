package it.scampiaparty.vipkits;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {

    private HashMap<UUID, Long> vipCooldowns = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("VIP Kits Plugin attivato!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("kit")) {
            if (args.length == 0) {
                player.sendMessage("§cUso: /kit <Ladro|Picciotto|Spacciatore|CapoMafia>");
                return true;
            }

            String kitName = args[0].toLowerCase();
            long currentTime = System.currentTimeMillis();
            long cooldown = vipCooldowns.getOrDefault(player.getUniqueId(), 0L);

            long waitTime = 0;
            switch (kitName) {
                case "ladro": waitTime = 5*60*1000; break;
                case "picciotto": waitTime = 3*60*1000; break;
                case "spacciatore": waitTime = 2*60*1000; break;
                case "capomafia": waitTime = 60*1000; break;
            }

            if (currentTime < cooldown + waitTime) {
                player.sendMessage("§cKit in cooldown!");
                return true;
            }

            giveKit(player, kitName);
            vipCooldowns.put(player.getUniqueId(), currentTime);
            player.sendMessage("§aHai ricevuto il kit " + kitName);
        }
        return true;
    }

    private void giveKit(Player player, String kit) {
        player.getInventory().clear();
        switch (kit) {
            case "ladro":
                giveArmor(player, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, 1);
                giveSword(player, 1);
                break;
            case "picciotto":
                giveArmor(player, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, 2);
                giveSword(player, 2);
                break;
            case "spacciatore":
                giveArmor(player, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, 3);
                giveSword(player, 3);
                break;
            case "capomafia":
                giveArmor(player, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, 4);
                giveSword(player, 5, true);
                giveBow(player);
                break;
        }
    }

    private void giveArmor(Player p, Material helm, Material chest, Material legs, Material boots, int protLevel) {
        ItemStack helmet = new ItemStack(helm); helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel);
        ItemStack chestplate = new ItemStack(chest); chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel);
        ItemStack leggings = new ItemStack(legs); leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel);
        ItemStack bootsItem = new ItemStack(boots); bootsItem.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel);
        p.getInventory().setHelmet(helmet);
        p.getInventory().setChestplate(chestplate);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setBoots(bootsItem);
    }

    private void giveSword(Player p, int sharpness) { giveSword(p, sharpness, false); }

    private void giveSword(Player p, int sharpness, boolean fireAspect) {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, sharpness);
        if (fireAspect) sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
        p.getInventory().addItem(sword);
    }

    private void giveBow(Player p) {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
        bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        p.getInventory().addItem(bow);
        p.getInventory().addItem(new ItemStack(Material.ARROW,1));
    }
}
