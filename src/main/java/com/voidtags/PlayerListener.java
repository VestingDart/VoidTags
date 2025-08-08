package com.voidtags;

import com.voidtags.VoidTags;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final VoidTags plugin;

    public PlayerListener(VoidTags plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String tag = plugin.getTagManager().getTag(player);
        if (tag != null) {
            player.displayName(Component.text(tag + " " + player.getName()));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;

        if (event.getView().title().toString().contains("Wähle deinen Tag")) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked.getType() == Material.NAME_TAG) {
                String tag = clicked.getItemMeta().getDisplayName();
                plugin.getTagManager().setTag(player, tag);
                player.sendMessage("Dein Tag wurde auf " + tag + " gesetzt.");
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.NAME_TAG) {
            TagGUI.open(event.getPlayer());
        }
    }
}
