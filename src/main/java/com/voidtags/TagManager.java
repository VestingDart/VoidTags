package com.voidtags;

import com.voidtags.VoidTags;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TagManager {

    private final VoidTags plugin;
    private final Map<UUID, String> playerTags = new HashMap<>();
    private Connection connection;

    public TagManager(VoidTags plugin) {
        this.plugin = plugin;
        setupDatabase();
        loadAll();
    }

    private void setupDatabase() {
        try {
            if (plugin.getConfig().getString("database.type").equalsIgnoreCase("mysql")) {
                String url = "jdbc:mysql://" + plugin.getConfig().getString("database.host") + ":" +
                        plugin.getConfig().getInt("database.port") + "/" + plugin.getConfig().getString("database.name");
                connection = DriverManager.getConnection(url,
                        plugin.getConfig().getString("database.user"),
                        plugin.getConfig().getString("database.pass"));
            } else {
                connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/tags.db");
            }
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tags (uuid TEXT PRIMARY KEY, tag TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setTag(Player player, String tag) {
        playerTags.put(player.getUniqueId(), tag);
        try {
            PreparedStatement ps = connection.prepareStatement("REPLACE INTO tags (uuid, tag) VALUES (?,?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, tag);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getTag(Player player) {
        return playerTags.getOrDefault(player.getUniqueId(), null);
    }

    public void loadAll() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tags");
            while (rs.next()) {
                playerTags.put(UUID.fromString(rs.getString("uuid")), rs.getString("tag"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        for (Map.Entry<UUID, String> entry : playerTags.entrySet()) {
            try {
                PreparedStatement ps = connection.prepareStatement("REPLACE INTO tags (uuid, tag) VALUES (?,?)");
                ps.setString(1, entry.getKey().toString());
                ps.setString(2, entry.getValue());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String parseTag(String tag) {
        return MiniMessage.miniMessage().deserialize(tag).toString();
    }
}
