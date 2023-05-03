package me.fuzzie.luckpermsgui.commands;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.fuzzie.luckpermsgui.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;


public class track implements CommandExecutor {

    static String motion;


    @Override
    public boolean onCommand(CommandSender Sender, Command cmd, String label, String[] args) {


        if (Sender instanceof Player) {
            Player player = (Player) Sender;
            if (args.length > 0) {
                Player target = Bukkit.getPlayer(args[0]);

                if (target != null) {

                    // List<String> rankItem = getConfig().getStringList("ranks.rank-item");
                    List<String> trackName = main.getInstance().getConfig().getStringList("tracks.track-name");
                    List<String> trackPrefix = main.getInstance().getConfig().getStringList("tracks.track-prefix");
                    List<String> trackItem = main.getInstance().getConfig().getStringList("tracks.track-item");

                    // create GUI
                    ChestGui gui = new ChestGui(4, (ChatColor.GREEN + "Track" + ChatColor.DARK_GRAY + "Menu"));
                    OutlinePane pane = new OutlinePane(0, 0, 9, 3);

                    // footer for main track menu

                    OutlinePane trackBackground = main.getInstance().getBackground(0, 3, 9, 1);
                    gui.addPane(trackBackground);

                    // create exit button

                    StaticPane footer2 = new StaticPane(0, 3, 9, 1);
                    footer2.addItem(new GuiItem(main.getInstance().getExit(), event ->
                    {
                        event.setCancelled(true);
                        event.getWhoClicked().closeInventory();
                    }), 4, 0);

                    footer2.addItem(main.getInstance().backButton(player, "lpgui " + target.getName()), 0, 0);

                    gui.addPane(footer2);

                    for (int i = 0; i < trackName.size(); i++) {
                        String currentTrack = trackName.get(i);
                        // create items in GUI
                        ItemStack item = new ItemStack(Material.getMaterial(trackItem.get(i)));
                        String itemName = (ChatColor.translateAlternateColorCodes('&', trackPrefix.get(i)));

                        // set item meta
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(itemName);
                        item.setItemMeta(meta);

                        // create specific GUI
                        ChestGui clickedGui = new ChestGui(3, (itemName + ChatColor.GRAY + " : Track Menu : " + ChatColor.YELLOW + target.getName()));

                        // add background
                        OutlinePane background = main.getInstance().getBackground(0, 0, 9, 3);
                        clickedGui.addPane(background);

                        // create confirm menu
                        ChestGui confirm = main.getInstance().getConfirm(player, target, "track","lp user " + target.getName() + motion + currentTrack);

                        // create body pane
                        String promoteItem = main.getInstance().getConfig().getString("tracks.promote-item");
                        ItemStack promote = new ItemStack(Material.getMaterial(promoteItem));
                        ItemMeta metaPromote = promote.getItemMeta();
                        metaPromote.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lPromote"));
                        promote.setItemMeta(metaPromote);

                        String demoteItem = main.getInstance().getConfig().getString("tracks.demote-item");
                        ItemStack demote = new ItemStack(Material.getMaterial(demoteItem));
                        ItemMeta metaDemote = demote.getItemMeta();
                        metaDemote.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lDemote"));
                        demote.setItemMeta(metaDemote);

                        StaticPane body = new StaticPane(0,1, 9, 1);
                        body.addItem(new GuiItem(demote, event ->
                        {
                            event.setCancelled(true);
                            if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == true) {
                                motion = " demote ";
                                confirm.show(player);
                            } else if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == false) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " demote " + currentTrack);
                            }
                        }), 2, 0);

                        body.addItem(new GuiItem(promote, event ->
                        {
                            event.setCancelled(true);
                            if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == true) {
                                motion = " promote ";
                                confirm.show(player);
                            } else if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == false) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " promote " + currentTrack);
                            }
                        }), 6, 0);
                        clickedGui.addPane(body);

                        // create footer pane
                        StaticPane footer = new StaticPane(0, 2, 9, 1);
                        footer.addItem(new GuiItem(main.getInstance().getExit(), event ->
                        {
                            event.setCancelled(true);
                            event.getWhoClicked().closeInventory();
                        }), 4, 0);
                        footer.addItem(main.getInstance().backButton(player, "track " + target.getName()), 0, 0);

                        clickedGui.addPane(footer);

                        // add original item
                        GuiItem guiItem = new GuiItem(item, event ->
                        {
                            event.setCancelled(true);
                            clickedGui.show(player);

                        });
                        pane.addItem(guiItem);
                    }

                    gui.addPane(pane);
                    gui.show(player);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getInstance().getMessage().getString("offline-player")));
                }
            } else {
                Player target = player;

                // List<String> rankItem = getConfig().getStringList("ranks.rank-item");
                List<String> trackName = main.getInstance().getConfig().getStringList("tracks.track-name");
                List<String> trackPrefix = main.getInstance().getConfig().getStringList("tracks.track-prefix");
                List<String> trackItem = main.getInstance().getConfig().getStringList("tracks.track-item");

                // create GUI
                ChestGui gui = new ChestGui(4, (ChatColor.GREEN + "Track" + ChatColor.DARK_GRAY + "Menu"));
                OutlinePane pane = new OutlinePane(0, 0, 9, 3);

                // footer for main track menu
                OutlinePane trackBackground = main.getInstance().getBackground(0, 3, 9, 1);
                gui.addPane(trackBackground);

                // create exit button
                StaticPane footer2 = new StaticPane(0, 3, 9, 1);
                footer2.addItem(new GuiItem(main.getInstance().getExit(), event ->
                {
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                }), 4, 0);
                footer2.addItem(main.getInstance().backButton(player, "lpgui " + target.getName()), 0, 0);

                gui.addPane(footer2);

                for (int i = 0; i < trackName.size(); i++) {
                    String currentTrack = trackName.get(i);
                    // create items in GUI
                    ItemStack item = new ItemStack(Material.getMaterial(trackItem.get(i)));
                    String itemName = (ChatColor.translateAlternateColorCodes('&', trackPrefix.get(i)));

                    // set item meta
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(itemName);
                    item.setItemMeta(meta);

                    // create specific GUI
                    ChestGui clickedGui = new ChestGui(3, (itemName + ChatColor.GRAY + " : Track Menu : " + ChatColor.YELLOW + target.getName()));

                    // add background
                    OutlinePane background = main.getInstance().getBackground(0, 0, 9, 3);
                    clickedGui.addPane(background);

                    // create confirm menu
                    ChestGui confirm = main.getInstance().getConfirm(player, target, "track","lp user " + target.getName() + motion + currentTrack);

                    // create body pane

                    String promoteItem = main.getInstance().getConfig().getString("tracks.promote-item");
                    ItemStack promote = new ItemStack(Material.getMaterial(promoteItem));
                    ItemMeta metaPromote = promote.getItemMeta();
                    metaPromote.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lPromote"));
                    promote.setItemMeta(metaPromote);

                    String demoteItem = main.getInstance().getConfig().getString("tracks.demote-item");
                    ItemStack demote = new ItemStack(Material.getMaterial(demoteItem));
                    ItemMeta metaDemote = demote.getItemMeta();
                    metaDemote.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lDemote"));
                    demote.setItemMeta(metaDemote);

                    StaticPane body = new StaticPane(0,1, 9, 1);
                    body.addItem(new GuiItem(demote, event ->
                    {
                        event.setCancelled(true);
                        if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == true) {
                            motion = " demote ";
                            confirm.show(player);
                        } else if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == false) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " demote " + currentTrack);
                        }
                    }), 2, 0);

                    body.addItem(new GuiItem(promote, event ->
                    {
                        event.setCancelled(true);
                        if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == true) {
                            motion = " promote ";
                            confirm.show(player);
                        } else if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == false) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " promote " + currentTrack);
                        }
                    }), 6, 0);
                    clickedGui.addPane(body);

                    // create footer pane
                    StaticPane footer = new StaticPane(0, 2, 9, 1);
                    footer.addItem(new GuiItem(main.getInstance().getExit(), event ->
                    {
                        event.setCancelled(true);
                        event.getWhoClicked().closeInventory();
                    }), 4, 0);

                    footer.addItem(main.getInstance().backButton(player, "track " + target.getName()), 0, 0);

                    clickedGui.addPane(footer);

                    // add original item
                    GuiItem guiItem = new GuiItem(item, event ->
                    {
                        event.setCancelled(true);
                        clickedGui.show(player);

                    });

                    pane.addItem(guiItem);
                }

                gui.addPane(pane);
                gui.show(player);
            }


        }
        return true;
    }




}
