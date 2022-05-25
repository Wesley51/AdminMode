package de.djd001.adminmode;

import org.bukkit.command.*;
import org.bukkit.entity.*;

public class AdminExecutor implements CommandExecutor
{
    private Main main;
    
    public AdminExecutor(final Main main) {
        this.main = main;
    }
    
    public boolean onCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("This command can only be used ingame!");
            return false;
        }
        final Player p = (Player)cs;
        this.main.toggleAdmin(p);
        if (this.main.isAdmin(p)) {
            p.sendMessage(this.main.toAdmin);
        }
        else {
            p.sendMessage(this.main.fromAdmin);
        }
        return true;
    }
}
