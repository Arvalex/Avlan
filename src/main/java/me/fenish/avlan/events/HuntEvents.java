package me.fenish.avlan.events;

import me.fenish.avlan.Main;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HuntEvents implements Listener {
    @EventHandler
    public void huntClick(InventoryClickEvent e){
        if(e.getView().getTitle().contains("§8§lTakip Menüsü")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getRawSlot() < 54) {
                   if (e.getCurrentItem().getType().equals(Material.STRUCTURE_VOID)) {
                       Main.getInstance().util.hedefler.remove((e.getWhoClicked().getUniqueId()));
                       e.getWhoClicked().sendMessage("§7=§8[ §bAVLAN §8]§7= §cTakip işlemin iptal edildi. Listeden yeni bir hedef seçebilirsin.");
                       ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1,2);
                       e.getWhoClicked().closeInventory();
                   }
                }
            }
        }
    }
    @EventHandler
    public void onDisc(PlayerQuitEvent e){
        if(Main.getInstance().util.hedefler.get(e.getPlayer().getUniqueId()) != null) {
            Main.getInstance().util.hedefler.remove(e.getPlayer().getUniqueId());
        }
    }
}
