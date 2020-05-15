package me.fenish.avlan.utils;

import me.fenish.avlan.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

import static java.lang.StrictMath.floor;

public class BossBar {
    public void comBossbar(Player p, Location old){
        DecimalFormat format = new DecimalFormat("0.#");
        double start_dist = floor(p.getLocation().distance(old));
        String dist = format.format(start_dist);
        final String[] color = {"§c"};
        org.bukkit.boss.BossBar bar = Bukkit.createBossBar("§fHedefe Uzaklık §c" + dist + " Blok", BarColor.RED, BarStyle.SEGMENTED_6);
        bar.addPlayer(p);
        bar.setVisible(true);
            new BukkitRunnable() {
            @Override
            public void run() {
                if (p.isOnline()) {
                    double curr_dist = floor(p.getLocation().distance(old));
                    double progress = ((curr_dist * 100) / start_dist)/100;
                    if(progress < 1.0) bar.setProgress(progress);
                    if (progress > 0.5) {
                        bar.setColor(BarColor.RED);
                        color[0] = "§c";
                    } else if (progress <= 0.5 && progress > 0.35) {
                        bar.setColor(BarColor.YELLOW);
                        color[0] = "§e";
                    } else if (progress <= 0.35 && progress > 0.15) {
                        bar.setColor(BarColor.GREEN);
                        color[0] = "§a";
                    } else if (progress <= 0.15) {
                        bar.removeAll();
                        Main.getInstance().util.hedefler.remove(p.getUniqueId());
                        p.sendMessage("§7=§8[ §bAVLAN §8]§7= §aHedefe 150 blok uzaklıkta olduğun için takip işlemi otomatik olarak sonlandırıldı burdan sonrasını kendin aramak zorundasın :)");
                        cancel();
                    }
                    bar.setTitle("§fHedefe Uzaklık " + color[0] + format.format(curr_dist) + " Blok");
                    if(Main.getInstance().util.hedefler.get(p.getUniqueId()) == null){
                        bar.removeAll();
                        cancel();
                    }
                }else{
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 5);
    }
}
