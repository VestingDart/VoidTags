package com.voidtags;

import com.voidtags.commands.TagsCommand;
import com.voidtags.listeners.PlayerListener;
import com.voidtags.manager.TagManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VoidTags extends JavaPlugin {

    private static VoidTags instance;
    private TagManager tagManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        tagManager = new TagManager(this);
        getCommand("tags").setExecutor(new TagsCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        tagManager.saveAll();
    }

    public static VoidTags getInstance() {
        return instance;
    }

    public TagManager getTagManager() {
        return tagManager;
    }
}
