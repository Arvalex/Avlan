package me.fenish.avlan.events;

import de.erethon.headlib.HeadLib;
import me.fenish.avlan.Main;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.List;
import java.util.Map;

public class MainGuiClick implements Listener {
    @EventHandler
    public void invClick(InventoryClickEvent e){
        if(e.getView().getTitle().contains("§9Avlan")){
            e.setCancelled(true);
            if(e.getCurrentItem() != null) {
                if (e.getRawSlot() < 54) {
                    ItemStack rightarrow = HeadLib.WOODEN_ARROW_RIGHT.toItemStack("§aSonraki Sayfa");
                    ItemStack leftarrow = HeadLib.WOODEN_ARROW_LEFT.toItemStack("§aÖnceki Sayfa");
                    Integer page = Integer.valueOf(StringUtils.substringBetween(e.getView().getTitle().replace("§c", ""), "§8[", "§8]"));


                    if (e.getCurrentItem().equals(rightarrow)) {
                        e.getWhoClicked().closeInventory();
                        Inventory inv = Main.getInstance().util.pagedInv(page + 1);
                        Map<String, List<String>> result = Main.getInstance().util.invFromPage(page + 1);
                        Main.getInstance().util.itemsToGui((Player) e.getWhoClicked(), inv, result);
                        e.getWhoClicked().openInventory(inv);
                    } else if (e.getCurrentItem().equals(leftarrow)) {
                        e.getWhoClicked().closeInventory();
                        Inventory inv = Main.getInstance().util.pagedInv(page - 1);
                        Map<String, List<String>> result = Main.getInstance().util.invFromPage(page - 1);
                        Main.getInstance().util.itemsToGui((Player) e.getWhoClicked(), inv, result);
                        e.getWhoClicked().openInventory(inv);
                    } else if (e.getCurrentItem().getType() != Material.COMPASS) {
                        e.getWhoClicked().closeInventory();
                        String player = e.getCurrentItem().getItemMeta().getDisplayName().replace("§a", "");
                        List<Object> locs = Main.getInstance().util.getLocFromName(player);
                        Inventory newinv = Bukkit.createInventory(null, 45, "§8§lHEDEF »§c§l " + player);
                        for(int i = 0; i<locs.size();i++){
                            File folder = new File(Main.getInstance().getDataFolder().getParent(), "/BenimGuzelEvim/" + player + ".yml");
                            Location loc = YamlConfiguration.loadConfiguration(folder).getLocation(String.valueOf(locs.get(i)));
                            if(loc.getWorld() != e.getWhoClicked().getWorld()){
                                locs.remove(i);
                            }
                        }
                        if (locs.size() == 1) {
                            ItemStack temp1 = Main.getInstance().util.getItem(Material.LIGHT_BLUE_BED, "§b" + StringUtils.capitalize((String) locs.get(0)));
                            newinv.setItem(31, temp1);
                        }
                        if (locs.size() == 2) {
                            ItemStack temp1 = Main.getInstance().util.getItem(Material.LIGHT_BLUE_BED, "§b" + StringUtils.capitalize((String) locs.get(0)));
                            ItemStack temp2 = Main.getInstance().util.getItem(Material.LIGHT_BLUE_BED, "§b" + StringUtils.capitalize((String) locs.get(1)));
                            newinv.setItem(30, temp1);
                            newinv.setItem(32, temp2);
                        }
                        if (locs.size() == 3) {
                            ItemStack temp1 = Main.getInstance().util.getItem(Material.LIGHT_BLUE_BED, "§b" + StringUtils.capitalize((String) locs.get(0)));
                            ItemStack temp2 = Main.getInstance().util.getItem(Material.LIGHT_BLUE_BED, "§b" + StringUtils.capitalize((String) locs.get(1)));
                            ItemStack temp3 = Main.getInstance().util.getItem(Material.LIGHT_BLUE_BED, "§b" + StringUtils.capitalize((String) locs.get(2)));
                            newinv.setItem(30, temp1);
                            newinv.setItem(31, temp2);
                            newinv.setItem(32, temp3);
                        }
                        if(locs.size() == 0){
                            ItemStack temp = Main.getInstance().util.getItem(Material.BARRIER, "§cLokasyonlar bu dünyadan değil!");
                            newinv.setItem(31,temp);
                        }
                        int[] glass = new int[]{1, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17};
                        Main.getInstance().util.setItemsFromIndex(newinv, glass, Material.BLACK_STAINED_GLASS_PANE);

                        ItemStack kafa = e.getCurrentItem();
                        ItemStack geri = HeadLib.WOODEN_ARROW_LEFT.toItemStack("§6Listeye geri dön");
                        ItemStack kapat = new ItemStack(Material.STRUCTURE_VOID);

                        ItemMeta kapatmeta = kapat.getItemMeta();
                        ItemMeta kafameta = kafa.getItemMeta();

                        List<String> lore = kafameta.getLore();
                        lore.clear();

                        kapatmeta.setDisplayName("§cMenüyü Kapat");
                        kafameta.setLore(lore);

                        kafa.setItemMeta(kafameta);
                        kapat.setItemMeta(kapatmeta);

                        newinv.setItem(0, geri);
                        newinv.setItem(4, kafa);
                        newinv.setItem(8, kapat);


                        e.getWhoClicked().openInventory(newinv);
                    }
                }
            }
        }
    }
}
