package me.fuzzie.luckpermsgui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.fuzzie.luckpermsgui.commands.rank;
import me.fuzzie.luckpermsgui.commands.track;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
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
        getConfig();
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + ChatColor.YELLOW + "LuckPermsGUI");
        this.saveDefaultConfig();
        this.getCommand("lpguireload").setExecutor(this::lpguireload);
        this.getCommand("lpgui").setExecutor(this::onCommand);

        if (getConfig().getBoolean("ranks.enabled") == true) {
            this.getCommand("rank").setExecutor(new rank());
        }
        if(getConfig().getBoolean("tracks.enabled") == true) {
            this.getCommand("track").setExecutor(new track());
        }


        // creates messages.yml file
        createCustomConfig();


        // Bstats
        int pluginId = 18584;
        Metrics metrics = new Metrics(this, pluginId);



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

                    ChestGui gui = new ChestGui(5, (ChatColor.translateAlternateColorCodes('&', getMessage().getString("lpgui.menu-title"))));

                    StaticPane pane = new StaticPane(0, 0, 9, 4);

                    // create rank menu item
                    ItemStack rank = new ItemStack(Material.NAME_TAG);
                    ItemMeta rankMeta = rank.getItemMeta();
                    rankMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getMessage().getString("lpgui.rank-button")));
                    rank.setItemMeta(rankMeta);

                    pane.addItem(new GuiItem(rank, event ->
                    {
                        event.setCancelled(true);
                        player.performCommand("rank " + target.getName());
                    }), 3, 1);

                    // create track menu item
                    ItemStack track = new ItemStack(Material.LADDER);
                    ItemMeta trackMeta = track.getItemMeta();
                    trackMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getMessage().getString("lpgui.track-button")));
                    track.setItemMeta(trackMeta);

                    pane.addItem(new GuiItem(track, event ->
                    {
                        event.setCancelled(true);
                        player.performCommand("track " + target.getName());
                    }), 5, 1);

                    gui.addPane(pane);

                    OutlinePane background = getBackground(0, 0, 9, 5);
                    gui.addPane(background);


                    StaticPane footer = new StaticPane(0, 4, 9, 1);
                    footer.addItem(new GuiItem(getExit(), event ->
                    {
                        event.setCancelled(true);
                        event.getWhoClicked().closeInventory();
                    }), 4, 0);

                    gui.addPane(footer);

                    gui.show(player);

                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessage().getString("offline-player")));
                }
            } else {
                Player target = player;

                ChestGui gui = new ChestGui(5, (ChatColor.translateAlternateColorCodes('&', getMessage().getString("lpgui.menu-title"))));

                StaticPane pane = new StaticPane(0, 0, 9, 4);

                // create rank menu item
                ItemStack rank = new ItemStack(Material.NAME_TAG);
                ItemMeta rankMeta = rank.getItemMeta();
                rankMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getMessage().getString("lpgui.rank-button")));
                rank.setItemMeta(rankMeta);

                pane.addItem(new GuiItem(rank, event ->
                {
                    event.setCancelled(true);
                    player.performCommand("rank " + target.getName());
                }), 3, 1);

                // create track menu item
                ItemStack track = new ItemStack(Material.LADDER);
                ItemMeta trackMeta = track.getItemMeta();
                trackMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getMessage().getString("lpgui.track-button")));
                track.setItemMeta(trackMeta);

                pane.addItem(new GuiItem(track, event ->
                {
                    event.setCancelled(true);
                    player.performCommand("track " + target.getName());
                }), 5, 1);

                gui.addPane(pane);

                OutlinePane background = getBackground(0, 0, 9, 5);
                gui.addPane(background);


                StaticPane footer = new StaticPane(0, 4, 9, 1);
                footer.addItem(new GuiItem(getExit(), event ->
                {
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                }), 4, 0);

                gui.addPane(footer);

                gui.show(player);
            }


        }
        return true;
    }

    public ItemStack getExit() {
        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta meta = exit.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getMessage().getString("exit-button")));
        exit.setItemMeta(meta);

        return exit;
    }

    public LuckPerms getLP() throws IllegalStateException {
        LuckPerms api = LuckPermsProvider.get();

        return api;
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

    public GuiItem backButton(Player player, String cmd) {
        ItemStack backItem = new ItemStack(Material.getMaterial(main.getInstance().getConfig().getString("global.back-item")));
        ItemMeta backMeta = backItem.getItemMeta();
        backMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getMessage().getString("back-button")));
        backItem.setItemMeta(backMeta);

        GuiItem back = new GuiItem(backItem, event -> {
            event.setCancelled(true);
            player.performCommand(cmd);
        });

        return back;
    }



    public boolean lpguireload(CommandSender Sender, Command cmd, String label, String[] args) {

        if (Sender instanceof Player) {
            Player player = (Player) Sender;
            reloadConfig();
            String message = (ChatColor.translateAlternateColorCodes('&', getMessage().getString("reload-config")));
            player.sendMessage(message);
        }
        return true;
    }

    public FileConfiguration getMessage() {
        return this.customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "messages.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        /* User Edit:
            Instead of the above Try/Catch, you can also use
            YamlConfiguration.loadConfiguration(customConfigFile)
        */
    }



    public static main getInstance(){
        return plugin;
    }



}
