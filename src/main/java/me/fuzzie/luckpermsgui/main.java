package me.fuzzie.luckpermsgui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.fuzzie.luckpermsgui.commands.rank;
import me.fuzzie.luckpermsgui.commands.track;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;



public final class main extends JavaPlugin {

    private File customConfigFile;
    private FileConfiguration customConfig;

    private static main plugin;

    @Override
    public void onEnable() {
        createCustomConfig();
        getConfig();
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + ChatColor.YELLOW + "LuckPermsGUI");
        this.saveDefaultConfig();
        this.getCommand("rank").setExecutor(new rank());
        this.getCommand("lpguireload").setExecutor(this::lpguireload);
        this.getCommand("track").setExecutor(new track());
        this.getCommand("lpgui").setExecutor(this::onCommand);

        plugin = this;
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + "Disabled " + ChatColor.YELLOW + "LuckPermsGUI");
    }

    @Override
    public boolean onCommand(CommandSender Sender, Command cmd, String label, String[] args) {

        if (Sender instanceof Player) {
            Player player = (Player) Sender;

            if (args.length > 0) {
                Player target = Bukkit.getPlayer(args[0]);

                if (target != null) {

                    ChestGui gui = new ChestGui(5, (ChatColor.GREEN + "Luck" + ChatColor.GRAY + "Perms" + ChatColor.YELLOW + " GUI"));

                    StaticPane pane = new StaticPane(0, 0, 9, 4);

                    // create rank menu item
                    ItemStack rank = new ItemStack(Material.NAME_TAG);
                    ItemMeta rankMeta = rank.getItemMeta();
                    rankMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cRank &8Menu"));
                    rank.setItemMeta(rankMeta);

                    pane.addItem(new GuiItem(rank, event ->
                    {
                        event.setCancelled(true);
                        player.performCommand("rank " + target.getName());
                    }), 3, 1);

                    // create track menu item
                    ItemStack track = new ItemStack(Material.LADDER);
                    ItemMeta trackMeta = track.getItemMeta();
                    trackMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aTrack &8Menu"));
                    track.setItemMeta(trackMeta);

                    pane.addItem(new GuiItem(track, event ->
                    {
                        event.setCancelled(true);
                        player.performCommand("track " + target.getName());
                    }), 5, 1);

                    gui.addPane(pane);


                    // create exit button
                    ItemStack exit = new ItemStack(Material.BARRIER);
                    ItemMeta metaTwo = exit.getItemMeta();
                    metaTwo.setDisplayName(ChatColor.RED + "Exit");
                    exit.setItemMeta(metaTwo);

                    OutlinePane background = getBackground(0, 0, 9, 5);
                    gui.addPane(background);


                    StaticPane footer = new StaticPane(0, 4, 9, 1);
                    footer.addItem(new GuiItem(exit, event ->
                    {
                        event.setCancelled(true);
                        event.getWhoClicked().closeInventory();
                    }), 4, 0);

                    gui.addPane(footer);

                    gui.show(player);

                } else {
                    player.sendMessage(ChatColor.RED + "[!] " + ChatColor.WHITE + "please select an online player");
                }
            } else {
                player.sendMessage(ChatColor.RED + "[!] " + ChatColor.WHITE + "please specify a player \n usage: " + ChatColor.GREEN + "/lpgui <player>");
            }


        }
        return true;
    }

    public OutlinePane getBackground(int x, int y, int length, int height) {
        OutlinePane background = new OutlinePane(x, y, length, height);

        ItemStack bg = new ItemStack(Material.getMaterial(getConfig().getString("global.background-item")));
        ItemMeta meta = bg.getItemMeta();
        meta.setDisplayName(" ");
        bg.setItemMeta(meta);

        background.addItem(new GuiItem(bg, event -> event.setCancelled(true)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);

        return background;
    }



    public boolean lpguireload(CommandSender Sender, Command cmd, String label, String[] args) {

        if (Sender instanceof Player) {
            Player player = (Player) Sender;
            reloadConfig();
            player.sendMessage("reloaded config");
        }
        return true;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        customConfig= new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public static main getInstance(){
        return plugin;
    }


}
