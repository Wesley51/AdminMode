package de.djd001.adminmode;

import org.bukkit.command.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class AdminTpExecutor implements CommandExecutor
{
    private Main main;
    
    public AdminTpExecutor(final Main main) {
        this.main = main;
    }
    
    public boolean onCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("This command can only be used ingame!");
            return false;
        }
        final Player p = (Player)cs;
        if (!this.main.isAdmin(p)) {
            p.sendMessage(ChatColor.RED + "You need to be in admin mode to teleport!");
            return true;
        }
        if (args.length <= 0) {
            return false;
        }
        final Player pl = Bukkit.getPlayer(args[0]);
        if (pl == null) {
            p.sendMessage(ChatColor.RED + "Player '" + args[0] + "' not online!");
            return true;
        }
        p.sendMessage(ChatColor.GREEN + "Teleporting to " + ChatColor.YELLOW + pl.getName());
        p.teleport(pl);
        return true;
    }
}
