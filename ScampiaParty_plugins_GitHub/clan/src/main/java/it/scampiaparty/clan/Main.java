package it.scampiaparty.clan;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.*;

public class Main extends JavaPlugin implements Listener {

    private Map<UUID, Clan> playerClans = new HashMap<>();
    private Map<UUID, Clan> pendingInvites = new HashMap<>();
    private boolean friendlyFire = false;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        friendlyFire = getConfig().getBoolean("friendly-fire", false);
        getLogger().info("Clan Plugin Avanzato attivato!");
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;
        Player damaged = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();
        if (!friendlyFire) {
            Clan clan1 = playerClans.get(damaged.getUniqueId());
            Clan clan2 = playerClans.get(damager.getUniqueId());
            if (clan1 != null && clan1.equals(clan2)) e.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("clan")) {
            if (args.length == 0) {
                player.sendMessage("§cUso: /clan create|invite|accept|deny|kick|info");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "create":
                    if (args.length < 2) { player.sendMessage("§cUso: /clan create <nome>"); return true; }
                    createClan(player, args[1]);
                    break;
                case "invite":
                    if (args.length < 2) { player.sendMessage("§cUso: /clan invite <player>"); return true; }
                    invitePlayer(player, args[1]);
                    break;
                case "accept":
                    acceptInvite(player);
                    break;
                case "deny":
                    denyInvite(player);
                    break;
                case "kick":
                    if (args.length < 2) { player.sendMessage("§cUso: /clan kick <player>"); return true; }
                    kickPlayer(player, args[1]);
                    break;
                case "info":
                    showClanInfo(player);
                    break;
            }
        }
        return true;
    }

    // --- METODI PRIVATI
    private void createClan(Player player, String name) {
        if (playerClans.containsKey(player.getUniqueId())) { player.sendMessage("§cSei già in un clan!"); return; }
        Clan clan = new Clan(name, player.getUniqueId());
        playerClans.put(player.getUniqueId(), clan);
        player.sendMessage("§aClan " + name + " creato! Sei il Capo.");
    }

    private void invitePlayer(Player player, String targetName) {
        Player target = getServer().getPlayer(targetName);
        if (target == null) { player.sendMessage("§cGiocatore non trovato."); return; }
        Clan clan = playerClans.get(player.getUniqueId());
        if (clan == null || !clan.isLeaderOrElder(player.getUniqueId())) {
            player.sendMessage("§cNon puoi invitare, sei solo un membro.");
            return;
        }
        pendingInvites.put(target.getUniqueId(), clan);
        target.sendMessage("§aSei stato invitato nel clan " + clan.getName() + "! Usa /clan accept o /clan deny");
        player.sendMessage("§aInvito inviato a " + target.getName());
    }

    private void acceptInvite(Player player) {
        Clan clan = pendingInvites.get(player.getUniqueId());
        if (clan == null) { player.sendMessage("§cNessun invito pendente."); return; }
        clan.addMember(player.getUniqueId());
        playerClans.put(player.getUniqueId(), clan);
        pendingInvites.remove(player.getUniqueId());
        player.sendMessage("§aSei entrato nel clan " + clan.getName());
    }

    private void denyInvite(Player player) {
        if (pendingInvites.remove(player.getUniqueId()) != null) player.sendMessage("§cInvito rifiutato.");
        else player.sendMessage("§cNessun invito pendente.");
    }

    private void kickPlayer(Player player, String targetName) {
        Player target = getServer().getPlayer(targetName);
        if (target == null) { player.sendMessage("§cGiocatore non trovato."); return; }
        Clan clan = playerClans.get(player.getUniqueId());
        if (clan == null || !clan.isLeaderOrElder(player.getUniqueId())) { player.sendMessage("§cNon puoi kickare nessuno."); return; }
        if (!clan.canKick(player.getUniqueId(), target.getUniqueId())) { player.sendMessage("§cNon puoi kickare questo giocatore per gerarchia."); return; }
        clan.removeMember(target.getUniqueId());
        playerClans.remove(target.getUniqueId());
        player.sendMessage("§aHai rimosso " + target.getName());
        target.sendMessage("§cSei stato rimosso dal clan " + clan.getName());
    }

    private void showClanInfo(Player player) {
        Clan clan = playerClans.get(player.getUniqueId());
        if (clan == null) { player.sendMessage("§cNon sei in un clan."); return; }
        player.sendMessage("§aClan: " + clan.getName() + " | Capo: " + clan.getLeaderName());
        player.sendMessage("§aMembri:");
        for (UUID uuid : clan.getMembers().keySet()) {
            player.sendMessage("- " + getServer().getOfflinePlayer(uuid).getName() + " (" + clan.getMembers().get(uuid) + ")");
        }
    }

    private static class Clan {
        private String name;
        private UUID leader;
        private Map<UUID, String> members = new HashMap<>(); // UUID -> RANK

        Clan(String name, UUID leader) { this.name = name; this.leader = leader; members.put(leader, "Capo"); }

        String getName() { return name; }
        String getLeaderName() { return "Capo"; }
        Map<UUID,String> getMembers() { return members; }

        void addMember(UUID uuid) { members.put(uuid, "Membro"); }
        boolean isLeaderOrElder(UUID uuid) {
            String rank = members.get(uuid);
            return rank != null && (rank.equals("Capo") || rank.equals("Anziano"));
        }
        boolean canKick(UUID kicker, UUID target) {
            String kickerRank = members.get(kicker);
            String targetRank = members.get(target);
            if (kickerRank == null || targetRank == null) return false;
            if (targetRank.equals("Capo")) return false;
            if (kickerRank.equals("Anziano") && targetRank.equals("Anziano")) return false;
            return true;
        }
        void removeMember(UUID uuid) { members.remove(uuid); }
    }
}
