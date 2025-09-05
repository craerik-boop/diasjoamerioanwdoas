package it.scampiaparty.shop;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;

import java.util.*;

public class Main extends JavaPlugin implements Listener {

    private Map<UUID,Integer> balances = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this,this);
        getLogger().info("Shop GUI attivato!");
    }

    public void openShop(Player player) {
        Inventory inv = getServer().createInventory(null, 9*3, "§6Shop ScampiaParty");

        // Sezione 1 – Armor & Armi
        inv.setItem(0, createItem(Material.DIAMOND_HELMET,"§bDiamante Helmet","100$"));
        inv.setItem(1, createItem(Material.DIAMOND_CHESTPLATE,"§bDiamante Chestplate","100$"));
        inv.setItem(2, createItem(Material.DIAMOND_LEGGINGS,"§bDiamante Leggings","100$"));
        inv.setItem(3, createItem(Material.DIAMOND_BOOTS,"§bDiamante Boots","100$"));
        inv.setItem(4, createItem(Material.DIAMOND_SWORD,"§bDiamante Sword","100$"));
        inv.setItem(5, createItem(Material.DIAMOND_AXE,"§bDiamante Axe","100$"));
        inv.setItem(6, createItem(Material.BOW,"§bArco","100$"));

        // Sezione 2 – Pozioni e mele
        inv.setItem(9, createPotion("§aRegen I","REGENERATION",100));
        inv.setItem(10, createPotion("§aInstant Damage I","INSTANT_DAMAGE",100));
        inv.setItem(11, createPotion("§aStrength I","STRENGTH",100));
        inv.setItem(12, createPotion("§aFire Resist I","FIRE_RESISTANCE",100));
        inv.setItem(13, createPotion("§aSpeed II","SPEED",100));
        inv.setItem(14, createItem(Material.GOLDEN_APPLE,"§6Mela OP","1000$"));

        // Sezione 3 – Libri
        inv.setItem(18, createBook("§5Sharpness V","200$"));
        inv.setItem(19, createBook("§5Protection IV","200$"));
        inv.setItem(20, createBook("§5Unbreaking III","200$"));
        inv.setItem(21, createBook("§5Fire Aspect II","200$"));
        inv.setItem(22, createBook("§5Punch II","200$"));
        inv.setItem(23, createBook("§5Flame","200$"));
        inv.setItem(24, createBook("§5Infinity","200$"));
        inv.setItem(25, createBook("§5Power V","200$"));

        player.openInventory(inv);
    }

    private ItemStack createItem(Material mat, String name, String price) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name + " §7(" + price + ")");
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createPotion(String name, String type, int price) {
        ItemStack potion = new ItemStack(Material.POTION);
        ItemMeta meta = potion.getItemMeta();
        meta.setDisplayName(name + " §7(" + price + "$)");
        potion.setItemMeta(meta);
        return potion;
    }

    private ItemStack createBook(String name, String price) {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName(name + " §7(" + price + "$)");
        book.setItemMeta(meta);
        return book;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player)e.getWhoClicked();
        if (!e.getInventory().getName().equalsIgnoreCase("§6Shop ScampiaParty")) return;

        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        if (clicked==null || clicked.getType()==Material.AIR) return;

        int price = parsePrice(clicked.getItemMeta().getDisplayName());
        int bal = balances.getOrDefault(p.getUniqueId(),0);
        if (bal<price) {
            p.sendMessage("§cNon hai abbastanza soldi!");
            return;
        }
        balances.put(p.getUniqueId(), bal-price);
        p.getInventory().addItem(clicked);
        p.sendMessage("§aHai comprato: " + clicked.getItemMeta().getDisplayName());
    }

    private int parsePrice(String name) {
        try {
            String[] parts = name.split("\\(");
            String priceStr = parts[1].replace("§7$)","");
            return Integer.parseInt(priceStr);
        } catch(Exception ex) {
            return 0;
        }
    }

    public void addBalance(Player p, int amount) {
        balances.put(p.getUniqueId(), balances.getOrDefault(p.getUniqueId(),0)+amount);
    }
}
