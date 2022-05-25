package de.djd001.adminmode;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class AdminConfigExecutor implements CommandExecutor
{
    private Main main;
    
    public AdminConfigExecutor(final Main main) {
        this.main = main;
    }
    
    public boolean onCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("This command can only be used ingame!");
            return false;
        }
        final Player p = (Player)cs;
        if (args.length == 0) {
            p.sendMessage(ChatColor.BLUE + "/" + label + " setinv  a-  " + "Set your current inventory and potion effects to the inventory players receive " + "when they switch to admin mode " + "7(The 'change_inventory' option has to be enabled");
            return true;
        }
        if (args[0].equalsIgnoreCase("setinv")) {
            if (this.main.changeInventory()) {
                this.main.getSaveLoader().save(p);
                p.sendMessage("Your inventory has been saved!");
            }
            else {
                p.sendMessage(ChatColor.RED + "Please enable the 'change_inventory' " + "option in the config at first.");
            }
            return true;
        }
        return false;
    }
}
