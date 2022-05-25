package de.djd001.adminmode;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {
    private final Main plugin;

    public PlaceholderAPI(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "AdminMode";
    }

    @Override
    public @NotNull String getAuthor() {
        return "WesleyH";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0";
    }
    @Override
    public boolean persist() {
        return true;
    }
    @Override
    public String onRequest(OfflinePlayer p, String s){
        // Return "[Mod Mode] if true
        if(s.equalsIgnoreCase("state")) {
            if (plugin.isAdmin((Player) p)) {
                return plugin.PAPItrue;
            } else return plugin.PAPIfalse;
        }else if (s.equalsIgnoreCase("amount")){
            return String.valueOf(plugin.getAdmin().size());
        }
        return null;

    }
}
