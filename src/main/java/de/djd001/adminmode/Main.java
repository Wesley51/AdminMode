package de.djd001.adminmode;

import de.myzelyam.api.vanish.VanishAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.LuckPermsEvent;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.plugin.java.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.*;

public class Main extends JavaPlugin
{
    private boolean ci;
    private boolean tpback;
    boolean luckperms;
    boolean dca;
    boolean chia;
    boolean ha;
    boolean inta;
    private HashMap<UUID, ItemStack[]> inv;
    private HashMap<UUID, ItemStack[]> arm;
    private HashMap<UUID, Collection<PotionEffect>> pot;
    private HashMap<UUID, GameMode> mode;
    private HashMap<UUID, Location> location;
    private List<UUID> admin;
    public Collection<PotionEffect> effect;
    public HashMap<Integer, ItemStack> items;
    public HashMap<Material, String> commands;
    public String toAdmin;
    public String fromAdmin;
    public String PAPItrue;
    public String PAPIfalse;
    private InventorySaveLoader sl;
    private static LuckPerms api;
    
    public Main() {
        this.inv = new HashMap<>();
        this.arm = new HashMap<>();
        this.pot = new HashMap<>();
        this.mode = new HashMap<>();
        this.location = new HashMap<>();
        this.admin = new ArrayList<>();
        this.effect = new ArrayList<>();
        this.items = new HashMap<>();
        this.commands = new HashMap<>();
    }
    
    public void onEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("SuperVanish") && !Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
            getLogger().warning("You need SuperVanish or PremiumVanish installed to use this plugin! Download here: https://www.spigotmc.org/resources/supervanish-be-invisible.1331/");
            getServer().getPluginManager().disablePlugin(this);
        }
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Luckperms")){
            getLogger().info("Luckperms found! Hooking..");
            luckperms = true;
        }
        this.setupConfig();
        this.loadCommands();
        (this.sl = new InventorySaveLoader(this)).load();
        Bukkit.getPluginManager().registerEvents(new AdminListener(this), (Plugin)this);
        this.getCommand("admin").setExecutor(new AdminExecutor(this));
        this.getCommand("atp").setExecutor(new AdminTpExecutor(this));
        this.getCommand("aconfig").setExecutor(new AdminConfigExecutor(this));
        System.out.println("[AdminMode] Enabled AdminMode by djd001 and WesleyH");
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }
    }
    
    public void onDisable() {
        super.onDisable();
        Player[] onlinePlayers;
        for (int length = (onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, i = 0; i < length; ++i) {
            final Player pl = onlinePlayers[i];
            if (this.isAdmin(pl)) {
                this.toggleAdmin(pl);
            }
        }
    }
    
    public void toggleAdmin(final Player p) {
        final UUID u = p.getUniqueId();
        if (this.isAdmin(p)) {
            if (this.ci) {
                p.getInventory().setContents(this.inv.get(u));
                p.getInventory().setArmorContents(this.arm.get(u));
                for (final PotionEffect po : p.getActivePotionEffects()) {
                    p.removePotionEffect(po.getType());
                }
                p.addPotionEffects(this.pot.get(u));
            }
            if (this.tpback) {
                p.teleport(this.location.get(u));
            }
            p.setGameMode(this.mode.get(u));
            this.setVanish(p, false);
            this.inv.remove(u);
            this.arm.remove(u);
            this.pot.remove(u);
            this.mode.remove(u);
            this.admin.remove(u);
            this.location.remove(u);
            if (luckperms){
                for (String i: getConfig().getStringList("options.give_groups")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent remove " + i);
                }
            }
        }
        else {
            this.inv.put(u, p.getInventory().getContents());
            this.arm.put(u, p.getInventory().getArmorContents());
            this.pot.put(u, p.getActivePotionEffects());
            this.mode.put(u, p.getGameMode());
            this.location.put(u, p.getLocation());
            this.admin.add(u);
            this.setVanish(p, true);
            if (this.ci) {
                p.getInventory().clear();
                p.getInventory().setArmorContents(new ItemStack[4]);
                for (final PotionEffect po : p.getActivePotionEffects()) {
                    p.removePotionEffect(po.getType());
                }
                for (final int i : this.items.keySet()) {
                    if (i < 100) {
                        p.getInventory().setItem(i, this.items.get(i));
                    }
                    else if (i == 101) {
                        p.getInventory().setBoots(this.items.get(i));
                    }
                    else if (i == 102) {
                        p.getInventory().setLeggings(this.items.get(i));
                    }
                    else if (i == 103) {
                        p.getInventory().setChestplate(this.items.get(i));
                    }
                    else {
                        if (i != 104) {
                            continue;
                        }
                        p.getInventory().setHelmet(this.items.get(i));
                    }
                }
                p.addPotionEffects(this.effect);
            }
            p.setGameMode(GameMode.CREATIVE);
            if (luckperms){
                for (String i: getConfig().getStringList("options.give_groups")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent add " + i);
                }
            }
        }
    }
    
    public boolean isAdmin(final Player p) {
        return this.admin.contains(p.getUniqueId());
    }

    public List<UUID> getAdmin() {
        return admin;
    }
    
    public boolean isVanish(final Player p) {
        return VanishAPI.isInvisible(p);
    }
    
    public void setVanish(final Player p, final boolean vanish) {
        if (vanish && !VanishAPI.isInvisible(p)){
            VanishAPI.hidePlayer(p);
        }
        else if (VanishAPI.isInvisible(p)){
            VanishAPI.showPlayer(p);
        }
    }

    
    private void setupConfig() {
        this.saveDefaultConfig();
        this.ci = this.getConfig().getBoolean("options.change_inventory", true);
        this.dca = this.getConfig().getBoolean("options.drop_collect_admin", false);
        this.ha = this.getConfig().getBoolean("options.hit_admin", false);
        this.inta = this.getConfig().getBoolean("options.interact_admin", false);
        this.tpback = this.getConfig().getBoolean("options.tp_back", false);
        this.chia = this.getConfig().getBoolean("options.inventory_admin", false);
        this.PAPItrue = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.papi_state_true", "[Admin Mode]"));
        this.PAPIfalse = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.papi_state_false", ""));
        this.toAdmin = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.to_admin", "&4You are now in admin mode!"));
        this.fromAdmin = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.from_admin", "&aYou are now in player mode!"));
    }
    
    private void loadCommands() {
        int i = 0;
        if (this.getConfig().contains("commands")) {
            for (final String c : this.getConfig().getConfigurationSection("commands").getKeys(false)) {
                final String t = this.getConfig().getString("commands." + c);
                final Material m = Util.toMaterial(c);
                if (m != null) {
                    this.commands.put(m, t);
                    ++i;
                }
                else {
                    System.out.println("[AdminMode] Unknown material " + c);
                }
            }
        }
        System.out.println("[AdminMode] Loaded " + i + " commands from the config.");
    }
    
    public boolean changeInventory() {
        return this.ci;
    }
    
    public InventorySaveLoader getSaveLoader() {
        return this.sl;
    }
}
