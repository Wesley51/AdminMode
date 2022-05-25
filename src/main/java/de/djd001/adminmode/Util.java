package de.djd001.adminmode;

import org.bukkit.*;

public class Util
{
    public static Material toMaterial(String s) {
        if (s.matches("[1-9]*")) {
            return Material.getMaterial(String.valueOf((int)Integer.valueOf(s)));
        }
        s = s.toUpperCase();
        Material m = null;
        try {
            m = Material.valueOf(s);
        }
        catch (IllegalArgumentException ex) {}
        if (m == null) {
            Material[] values;
            for (int length = (values = Material.values()).length, i = 0; i < length; ++i) {
                final Material ma = values[i];
                if (ma.toString().replaceAll("_", "").equalsIgnoreCase(s) && (ma != Material.REDSTONE_TORCH || s.contains("off"))) {
                    return ma;
                }
            }
            Material[] values2;
            for (int length2 = (values2 = Material.values()).length, j = 0; j < length2; ++j) {
                final Material ma = values2[j];
                if (ma.toString().replaceAll("_", "").toLowerCase().contains(s.toLowerCase()) && (ma != Material.REDSTONE_TORCH || s.contains("off"))) {
                    return ma;
                }
            }
            return null;
        }
        return m;
    }
}
