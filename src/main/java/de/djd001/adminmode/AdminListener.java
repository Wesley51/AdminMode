package de.djd001.adminmode;

import jdk.vm.ci.hotspot.EventProvider;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class AdminListener implements Listener
{
    private Main main;

    public AdminListener(final Main main) {
        this.main = main;
    }

    
    @EventHandler
    public void onItemCollect(PlayerDropItemEvent e) {
        if (this.main.isAdmin(e.getPlayer())) {
            if (!this.main.dca) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e){
        Player p = (Player) e.getInventory().getViewers().get(0);
        if (this.main.isAdmin(p) && !this.main.chia && e.getClickedInventory() == p.getInventory()) {
            p.updateInventory();
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onItemCollect(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if (this.main.isAdmin(p) && !this.main.dca) {
                e.setCancelled(true);
            }
        }

    }
    
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        Player dam = null;
        if (e.getDamager() instanceof Player) {
            dam = (Player)e.getDamager();
        }
        else if (e.getDamager() instanceof Projectile) {
            final Projectile proj = (Projectile)e.getDamager();
            if (proj.getShooter() instanceof Player) {
                dam = (Player)proj.getShooter();
            }
        }
        if (dam != null) {
            if (this.main.isAdmin(dam)) {
                if (!this.main.ha) {
                    e.setCancelled(true);
                    return;
                }
                if (e.getEntity() instanceof Player) {
                    final Player t = (Player)e.getEntity();
                    if (this.main.commands.containsKey(dam.getItemInHand().getType())) {
                        final String s = this.main.commands.get(dam.getItemInHand().getType());
                        dam.chat(s.replaceAll("%a", dam.getName()).replaceAll("%p", t.getName()));
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        if (this.main.isAdmin(e.getPlayer())) {
            if (!this.main.inta) {
                e.setCancelled(true);
            }
            if (this.main.commands.containsKey(e.getPlayer().getInventory().getItemInMainHand().getType())) {
                final String s = this.main.commands.get(e.getPlayer().getInventory().getItemInMainHand().getType());
                if (!s.contains("%p")) {
                    e.getPlayer().chat(s.replaceAll("%a", e.getPlayer().getName()));
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onInteractEntity(final PlayerInteractEntityEvent e) {
        if (this.main.isAdmin(e.getPlayer())) {
            if (!this.main.inta) {
                e.setCancelled(true);
            }
            if (e.getRightClicked() instanceof Player) {
                final Player t = (Player)e.getRightClicked();
                if (this.main.commands.containsKey(e.getPlayer().getItemInHand().getType())) {
                    final String s = this.main.commands.get(e.getPlayer().getItemInHand().getType());
                    e.getPlayer().chat(s.replaceAll("%a", e.getPlayer().getName()).replaceAll("%p", t.getName()));
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(final PlayerQuitEvent e) {
        if (this.main.isAdmin(e.getPlayer())) {
            this.main.toggleAdmin(e.getPlayer());
        }
        this.main.setVanish(e.getPlayer(), false);
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onKick(final PlayerKickEvent e) {
        if (this.main.isAdmin(e.getPlayer())) {
            this.main.toggleAdmin(e.getPlayer());
        }
        this.main.setVanish(e.getPlayer(), false);
    }
}
