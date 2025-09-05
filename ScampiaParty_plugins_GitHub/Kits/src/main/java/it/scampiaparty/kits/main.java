package it.scampiaparty.kits;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Kits Plugin attivato!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("kit")) {
            if (args.length == 0) { player.sendMessage("§cUso: /kit <assasin|tank|archer|fighter|mage>"); return true; }
            String kit = args[0].toLowerCase();
            player.getInventory().clear();

            switch(kit) {
                case "assasin":
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
                    player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                    player.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
                    player.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
                    player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
                    break;
                case "tank":
                    player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                    player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                    player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                    player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                    player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
                    player.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP,2));
                    break;
                case "archer":
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
                    ItemStack bow = new ItemStack(Material.BOW);
                    bow.addEnchantment(Enchantment.ARROW_DAMAGE,3);
                    bow.addEnchantment(Enchantment.ARROW_KNOCKBACK,1);
                    player.getInventory().addItem(bow);
                    player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                    player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                    player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                    player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
                    break;
                case "mage":
                    player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                    player.getInventory().addItem(new ItemStack(Material.POTION,4)); // esempio pozioni cura
                    player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
                    player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                    player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                    player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
                    break;
            }
            player.sendMessage("§aHai ricevuto il kit " + kit);
        }
        return true;
    }
}
