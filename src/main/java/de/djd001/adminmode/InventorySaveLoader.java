package de.djd001.adminmode;

import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import java.io.*;
import java.util.*;

public class InventorySaveLoader
{
    private File f;
    private Main main;
    
    public InventorySaveLoader(final Main main) {
        this.main = main;
        this.f = new File((main.getDataFolder().getAbsolutePath()) + "/inventory.adm");
        if (!this.f.exists()) {
            try {
                this.f.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void load() {
        final Collection<PotionEffect> e = new ArrayList<>();
        final HashMap<Integer, ItemStack> i = new HashMap<>();
        int c = -1;
        try {
            Throwable t = null;
            try {
                final Scanner s = new Scanner(this.f);
                try {
                    ++c;
                    while (s.hasNext()) {
                        ++c;
                        String l = s.nextLine();
                        final String pre = l.substring(0, 2);
                        l = l.replaceFirst(pre, "");
                        if (pre.equals("%p")) {
                            final String[] sp = l.split("\t");
                            e.add(new PotionEffect(PotionEffectType.getByName(sp[0]), (int)Integer.valueOf(sp[1]), (int)Integer.valueOf(sp[2])));
                        }
                        else {
                            if (!pre.equals("%i")) {
                                continue;
                            }
                            final String[] sp = l.split("\t");
                            i.put(Integer.valueOf(sp[3]), new ItemStack(Material.valueOf(sp[0]), (int)Integer.valueOf(sp[1]), (short)Short.valueOf(sp[2])));
                        }
                    }
                    this.main.items = i;
                    this.main.effect = e;
                }
                finally {
                    if (s != null) {
                        s.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t2 = null;
                    t = t2;
                }
                else {
                    final Throwable t2 = null;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }
        }
        catch (FileNotFoundException ex) {
            if (c == -1) {
                System.out.println("[AdminMode] Error while loading inventory.adm file");
            }
            else {
                System.out.println("[AdminMode] Error while parsing line " + c);
            }
        }
    }
    
    public void save(final Player p) {
        try {
            Throwable t = null;
            try {
                final PrintWriter w = new PrintWriter(this.f);
                try {
                    for (final PotionEffect pe : p.getActivePotionEffects()) {
                        w.println("%p" + pe.getType().getName() + "\t" + pe.getDuration() + "\t" + pe.getAmplifier());
                    }
                    int c = 0;
                    ItemStack[] contents;
                    for (int length = (contents = p.getInventory().getContents()).length, i = 0; i < length; ++i) {
                        final ItemStack is = contents[i];
                        if (is != null && is.getType() != Material.AIR) {
                            w.println("%i" + is.getType().toString() + "\t" + is.getAmount() + "\t" + is.getDurability() + "\t" + c);
                        }
                        ++c;
                    }
                    c = 101;
                    ItemStack[] armorContents;
                    for (int length2 = (armorContents = p.getInventory().getArmorContents()).length, j = 0; j < length2; ++j) {
                        final ItemStack is = armorContents[j];
                        if (is != null && is.getType() != Material.AIR) {
                            w.println("%i" + is.getType() + "\t" + is.getAmount() + "\t" + is.getDurability()+ "\t" + c);
                        }
                        ++c;
                    }
                    w.flush();
                }
                finally {
                    if (w != null) {
                        w.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t2 = null;
                    t = t2;
                }
                else {
                    final Throwable t2 = null;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.load();
    }
}
