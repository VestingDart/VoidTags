package com.voidtags;

import com.voidtags.VoidTags;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TagGUI {

    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Wähle deinen Tag");

        for (String key : VoidTags.getInstance().getConfig().getConfigurationSection("tags").getKeys(false)) {
            String tagDisplay = VoidTags.getInstance().getConfig().getString("tags." + key);
            ItemStack item = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(MiniMessage.miniMessage().deserialize(tagDisplay));
            item.setItemMeta(meta);
            inv.addItem(item);
        }

        player.openInventory(inv);
    }
}
