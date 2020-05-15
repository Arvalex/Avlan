package me.fenish.avlan.events;

import de.erethon.headlib.HeadLib;
import me.fenish.avlan.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

import static java.lang.StrictMath.floor;

public class SubGuiClick implements Listener {
    @EventHandler
    public void subinvClick(InventoryClickEvent e){
        if(e.getView().getTitle().contains("§8§lHEDEF »")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getRawSlot() < 54) {
                    ItemStack geri = HeadLib.WOODEN_ARROW_LEFT.toItemStack("§6Listeye geri dön");
                    if (e.getCurrentItem().equals(geri)) {
                        e.getWhoClicked().closeInventory();
                        Bukkit.getServer().dispatchCommand(e.getWhoClicked(), "avlan");
                    } else if (e.getCurrentItem().getType().equals(Material.STRUCTURE_VOID)) {
                        e.getWhoClicked().closeInventory();
                    } else if (e.getCurrentItem().getType().equals(Material.LIGHT_BLUE_BED)) {
                        e.getWhoClicked().closeInventory();
                        String key = e.getCurrentItem().getItemMeta().getDisplayName().replace("§b", "").toLowerCase();
                        String p = e.getView().getItem(4).getItemMeta().getDisplayName().replace("§a", "");
                        File folder = new File(Main.getInstance().getDataFolder().getParent(), "/BenimGuzelEvim/" + p + ".yml");
                        Location origin_loc = YamlConfiguration.loadConfiguration(folder).getLocation(key);
                        float new_yaw = -180 + (180 - (-180)) * new Random().nextFloat();
                        origin_loc.setYaw(new_yaw);
                        origin_loc.setPitch(0F);
                        Location loc = origin_loc.add(origin_loc.getDirection().multiply(120));
                        HashMap<String, Location> hm = new HashMap<>();
                        hm.put(p,loc);
                        Main.getInstance().util.hedefler.put(e.getWhoClicked().getUniqueId(), hm);
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.EVENT_RAID_HORN, 1,2);
                        Main.getInstance().bossbar.comBossbar((Player) e.getWhoClicked(),loc);
                    }
                }
            }
        }
    }
}
