package me.fenish.avlan.commands;

import me.fenish.avlan.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.*;

import static java.lang.StrictMath.floor;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase("avlan")) {
                Player p = (Player) sender;
                if(Main.getInstance().util.hedefler.get(p.getUniqueId()) == null) {
                    if (args.length == 0) {
                        Inventory inv = Main.getInstance().util.pagedInv(1);
                        Map<String, List<String>> result = Main.getInstance().util.invFromPage(1);
                        Main.getInstance().util.itemsToGui(p, inv, result);
                        p.openInventory(inv);
                    } else {
                        p.sendMessage("§7=§8[ §bAVLAN §8]§7= §cHatalı kullanım!\n"
                                + "§7=§8[ §bAVLAN §8]§7= Örnek: §e/Avlan");
                    }
                }else{
                    try {
                        HashMap<String, Location> info = Main.getInstance().util.hedefler.get(p.getUniqueId());
                        Inventory inv = Bukkit.createInventory(null, 45,"§c§l« §8§lTakip Menüsü §c§l»");
                        int[] black = new int[]{1,2,3,4,5,6,7,10,16,19,25,28,34,37,38,39,40,41,42,43};
                        int[] red = new int[]{0,8,9,17,18,26,27,35,36,44};
                        DecimalFormat format = new DecimalFormat("0.#");
                        Main.getInstance().util.setItemsFromIndex(inv, black, Material.BLACK_STAINED_GLASS_PANE);
                        Main.getInstance().util.setItemsFromIndex(inv, red, Material.RED_STAINED_GLASS_PANE);
                        String hedef = new ArrayList<>(info.keySet()).get(0);
                        Location hedef_loc = new ArrayList<>(info.values()).get(0);
                        String dist = format.format(floor(p.getLocation().distance(hedef_loc)));
                        String[] lore = new String[]{"","§9Hedefe Uzaklık: §e" + dist + " Blok"};
                        ItemStack kafa = Main.getInstance().util.getFromHeads(hedef);
                        ItemStack kapat = new ItemStack(Material.STRUCTURE_VOID);
                        ItemMeta kapatmeta = kapat.getItemMeta();
                        SkullMeta kmeta = (SkullMeta) kafa.getItemMeta();

                        kapatmeta.setDisplayName("§cTakibi iptal et");
                        kmeta.setDisplayName("§7Hedef: §b" + hedef);
                        kmeta.setLore(Arrays.asList(lore));

                        kapat.setItemMeta(kapatmeta);
                        kafa.setItemMeta(kmeta);

                        inv.setItem(21,kafa);
                        inv.setItem(23,kapat);
                        p.openInventory(inv);
                    } catch (IllegalArgumentException e){
                        p.sendMessage("§7=§8[ §bAVLAN §8]§7= §cTakip işlemin iptal edildi. Çünkü hedef ile aynı dünyada değilsin.");
                        Main.getInstance().util.hedefler.remove(p);
                    }
                }
            }
        }
        return true;
    }
}
