package me.fenish.avlan.utils;

import de.erethon.headlib.HeadLib;
import me.fenish.avlan.Main;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class MainUtils {
    public static HashMap<UUID, HashMap<String, Location>> hedefler = new HashMap<>();

    public HashMap<String, List<String>> page(LinkedHashMap<String, List<String>> l, Integer page, Integer lines) {
        HashMap<String, List<String>> result = new HashMap<>();
        int first = (page - 1) * (lines);
        int last = page * lines;
        for (int i = first; i < last; i++) {
            if (i < l.size()) {
                Object key = l.keySet().toArray()[i];
                List<String> values = l.get(key);
                result.put((String) key, values);

            } else {
                break;
            }
        }
        return result;

    }

    public LinkedHashMap<String, List<String>> getCoords() {
        File folder = new File(Main.getInstance().getDataFolder().getParent(), "/BenimGuzelEvim/");
        LinkedHashMap<String, List<String>> items = new LinkedHashMap<>();
        List<String> keys = null;
        for (File f : folder.listFiles()) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
            if (cfg.getKeys(false) != null) {
                keys = new ArrayList<>();
                for (Object o : cfg.getKeys(false)) {
                    keys.add(String.valueOf(o));
                }
                if (keys.size() != 0) {
                    items.put(f.getName(), keys);
                }
            }
        }
        return items;
    }
    public void itemsToGui(Player p, Inventory inv, Map<String, List<String>> result) {
        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            String key = entry.getKey();
            String pname = key.replace(".yml", "");
            if (!pname.equalsIgnoreCase(p.getName())){
                List<String> lore = new ArrayList<>();
                ItemStack pitem = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta pmeta = (SkullMeta) pitem.getItemMeta();

                lore.add("");
                lore.add("§7Sahip olunan lokasyonlar");
                lore.add("§7╚═» §b" + entry.getValue().size());

                pmeta.setDisplayName("§a" + pname);
                pmeta.setLore(lore);

                pitem.setItemMeta(pmeta);
                inv.addItem(pitem);
            }
        }

        if (inv.getItem(44) == null) {
            inv.setItem(53, new ItemStack(Material.AIR));
        }

        p.openInventory(inv);
        checkTextures(p);
    }

    public Inventory pagedInv(Integer page) {
        Inventory inv = Bukkit.createInventory(null, 54, "§9Avlan §8[§c" + page + "§8]");
        ItemStack rightarrow = HeadLib.WOODEN_ARROW_RIGHT.toItemStack("§aSonraki Sayfa"); //53
        inv.setItem(53, rightarrow);
        if (page > 1) {
            ItemStack leftarrow = HeadLib.WOODEN_ARROW_LEFT.toItemStack("§aÖnceki Sayfa");
            inv.setItem(45, leftarrow);
        }
        return inv;

    }
    public void checkTextures(Player p){
        InventoryView inv = p.getOpenInventory();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < 45; i++) {
                    if(p.getOpenInventory().getTitle().contains("§9Avlan ")) {
                        try {
                            String pname = inv.getItem(i).getItemMeta().getDisplayName().replace("§a", "");
                            ItemStack head = inv.getItem(i);
                            String name = head.getItemMeta().getDisplayName();
                            List<String> lore = head.getItemMeta().getLore();

                            ItemStack kafa = getFromHeads(pname);
                            ItemMeta meta = kafa.getItemMeta();
                            meta.setDisplayName(name);
                            meta.setLore(lore);

                            kafa.setItemMeta(meta);
                            if(p.getOpenInventory().getItem(i) != null) {
                                p.getOpenInventory().setItem(i, kafa);
                            }else{
                                break;
                            }
                        } catch (NullPointerException e) {
                            break;
                        }
                    }else{
                        break;
                    }
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }
    public List<Object> getLocFromName(String s){
        LinkedHashMap<String, List<String>> items = Main.getInstance().util.getCoords();
        List values = null;
        for (Map.Entry<String, List<String>> entry : items.entrySet()) {
            String key = entry.getKey().replace(".yml", "");
            if(key.equalsIgnoreCase(s)) {
                values = entry.getValue();
            }
        }
        return values;
    }
    public Map<String, List<String>> invFromPage(Integer page) {
        LinkedHashMap<String, List<String>> items = Main.getInstance().util.getCoords();
        HashMap<String, List<String>> result = Main.getInstance().util.page(items, page, 45);
        TreeMap<String, List<String>> sorted = new TreeMap<>();
        sorted.putAll(result);

        return sorted;
    }
    public ItemStack getFromHeads(String name) {
        net.minecraft.server.v1_15_R1.ItemStack nms = CraftItemStack.asNMSCopy(new ItemStack(Material.PLAYER_HEAD));
        NBTTagCompound tag = nms.getOrCreateTag();
        tag.setString("SkullOwner", name);
        nms.setTag(tag);
        ItemStack pitem = CraftItemStack.asBukkitCopy(nms);
        return pitem;
        //return heads.get(name);
    }
    public ItemStack getItem(Material m,String n){
        ItemStack i = new ItemStack(m);
        ItemMeta meta = i.getItemMeta();
        String[] lore = new String[]{"§7Rota oluşturmak için §e[TIKLA]"};
        if(m.equals(Material.BARRIER)) {
            lore = new String[]{"§7Başka dünyalara gitmeyi deneyin."};
        }
        meta.setLore(Arrays.asList(lore));
        meta.setDisplayName(n);
        i.setItemMeta(meta);
        return i;
    }
    public void setItemsFromIndex(Inventory inv, int[] index, Material m){
        for (int i : index) {
            ItemStack cam = new ItemStack(m);
            ItemMeta meta = cam.getItemMeta();
            meta.setDisplayName(" ");
            cam.setItemMeta(meta);
            inv.setItem(i, cam);
        }
    }
}