package me.fuzzie.luckpermsgui.commands;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.fuzzie.luckpermsgui.main;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.track.Track;
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

            List<String> trackName = main.getInstance().getConfig().getStringList("tracks.track-name");
            List<String> trackPrefix = main.getInstance().getConfig().getStringList("tracks.track-prefix");
            List<String> trackItem = main.getInstance().getConfig().getStringList("tracks.track-item");
            if (args.length > 0) {
                Player target = Bukkit.getPlayer(args[0]);
                User user = main.getInstance().getLP().getUserManager().getUser(args[0]);


                if (target != null) {

                    // create GUI
                    ChestGui gui = new ChestGui(4, (getChat(main.getInstance().getMessage().getString("track.menu-title").replace("%player%", target.getName()))));
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
                        // create a blank context
                        ImmutableContextSet set1 = ImmutableContextSet.empty();

                        // get the current track
                        Track track = main.getInstance().getLP().getTrackManager().getTrack(trackName.get(i));

                        // create items in GUI
                        ItemStack item = new ItemStack(Material.getMaterial(trackItem.get(i)));
                        String itemName = getChat(trackPrefix.get(i));

                        // set item meta
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(itemName);
                        item.setItemMeta(meta);

                        // create specific GUI
                        ChestGui clickedGui = new ChestGui(3, (getChat(main.getInstance().getMessage().getString("track.second-menu-title").replace("%player%", target.getName()))));

                        // add background
                        OutlinePane background = main.getInstance().getBackground(0, 0, 9, 3);
                        clickedGui.addPane(background);

                        // create body pane
                        String promoteItem = main.getInstance().getConfig().getString("tracks.promote-item");
                        ItemStack promote = new ItemStack(Material.getMaterial(promoteItem));
                        ItemMeta metaPromote = promote.getItemMeta();
                        metaPromote.setDisplayName(getChat(main.getInstance().getMessage().getString("track.promote")));
                        promote.setItemMeta(metaPromote);

                        String demoteItem = main.getInstance().getConfig().getString("tracks.demote-item");
                        ItemStack demote = new ItemStack(Material.getMaterial(demoteItem));
                        ItemMeta metaDemote = demote.getItemMeta();
                        metaDemote.setDisplayName(getChat(main.getInstance().getMessage().getString("track.demote")));
                        demote.setItemMeta(metaDemote);

                        StaticPane body = new StaticPane(0,1, 9, 1);
                        int finalI = i;
                        body.addItem(new GuiItem(demote, event ->
                        {
                            event.setCancelled(true);
                            if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == true) {
                                motion = "demote";
                                ChestGui c = getConfirm(player, user, motion, track);
                                c.show(player);
                            } else if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == false) {
                                track.demote(user, set1);
                                player.sendMessage(getChat(main.getInstance().getMessage().getString("track.demote-message").replace("%player%", target.getName()).replace("%track_name%", trackName.get(finalI))));
                            }
                        }), 2, 0);

                        body.addItem(new GuiItem(promote, event ->
                        {
                            event.setCancelled(true);
                            if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == true) {
                                motion = "promote";
                                ChestGui c = getConfirm(player, user, motion, track);
                                c.show(player);
                            } else if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == false) {
                                track.promote(user, set1);
                                player.sendMessage(getChat(main.getInstance().getMessage().getString("track.promote-message").replace("%player%", target.getName()).replace("%track_name%", trackName.get(finalI))));
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
                    player.sendMessage(getChat(main.getInstance().getMessage().getString("offline-player")));
                }
            } else {
                Player target = player;

                User user = main.getInstance().getLP().getUserManager().getUser(target.getName());

                // create GUI
                ChestGui gui = new ChestGui(4, (getChat(main.getInstance().getMessage().getString("track.menu-title").replace("%player%", target.getName()))));
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
                    // create a blank context
                    ImmutableContextSet set1 = ImmutableContextSet.empty();

                    // get the current track
                    Track track = main.getInstance().getLP().getTrackManager().getTrack(trackName.get(i));



                    // create items in GUI
                    ItemStack item = new ItemStack(Material.getMaterial(trackItem.get(i)));
                    String itemName = getChat(trackPrefix.get(i));

                    // set item meta
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(itemName);
                    item.setItemMeta(meta);

                    // create specific GUI
                    ChestGui clickedGui = new ChestGui(3, (getChat(main.getInstance().getMessage().getString("track.second-menu-title").replace("%player%", target.getName()))));

                    // add background
                    OutlinePane background = main.getInstance().getBackground(0, 0, 9, 3);
                    clickedGui.addPane(background);

                    // create body pane
                    String promoteItem = main.getInstance().getConfig().getString("tracks.promote-item");
                    ItemStack promote = new ItemStack(Material.getMaterial(promoteItem));
                    ItemMeta metaPromote = promote.getItemMeta();
                    metaPromote.setDisplayName(getChat(main.getInstance().getMessage().getString("track.promote")));
                    promote.setItemMeta(metaPromote);

                    String demoteItem = main.getInstance().getConfig().getString("tracks.demote-item");
                    ItemStack demote = new ItemStack(Material.getMaterial(demoteItem));
                    ItemMeta metaDemote = demote.getItemMeta();
                    metaDemote.setDisplayName(getChat(main.getInstance().getMessage().getString("track.demote")));
                    demote.setItemMeta(metaDemote);

                    StaticPane body = new StaticPane(0,1, 9, 1);
                    int finalI = i;
                    body.addItem(new GuiItem(demote, event ->
                    {
                        event.setCancelled(true);
                        if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == true) {
                            motion = "demote";
                            ChestGui c = getConfirm(player, user, motion, track);
                            c.show(player);
                        } else if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == false) {
                            track.demote(user, set1);
                            player.sendMessage(getChat(main.getInstance().getMessage().getString("track.demote-message").replace("%player%", target.getName()).replace("%track_name%", trackName.get(finalI))));
                        }
                    }), 2, 0);


                    body.addItem(new GuiItem(promote, event ->
                    {
                        event.setCancelled(true);
                        if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == true) {
                            motion = "promote";
                            ChestGui c = getConfirm(player, user, motion, track);
                            c.show(player);
                        } else if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == false) {
                            track.promote(user, set1);
                            player.sendMessage(getChat(main.getInstance().getMessage().getString("track.promote-message").replace("%player%", target.getName()).replace("%track_name%", trackName.get(finalI))));
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

    public ChestGui getConfirm(Player p, User user, String m, Track track) {
        ChestGui confirm = new ChestGui(1, (ChatColor.translateAlternateColorCodes('&', main.getInstance().getMessage().getString("confirm-menu.menu-title"))));

        // create blank context
        ImmutableContextSet set1 = ImmutableContextSet.empty();

        // make background
        OutlinePane backgroundConfirm = main.getInstance().getBackground(0, 0, 9, 1);
        confirm.addPane(backgroundConfirm);

        // create buttons
        ItemStack confirmItem = new ItemStack(Material.GREEN_WOOL);
        ItemMeta metaConfirm = confirmItem.getItemMeta();
        metaConfirm.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.getInstance().getMessage().getString("confirm-menu.confirm-button")));
        confirmItem.setItemMeta(metaConfirm);

        ItemStack deny = new ItemStack(Material.RED_WOOL);
        ItemMeta metaDeny = deny.getItemMeta();
        metaDeny.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.getInstance().getMessage().getString("confirm-menu.deny-button")));
        deny.setItemMeta(metaDeny);

        StaticPane confirmPane = new StaticPane(0, 0, 9, 1);

        confirmPane.addItem(new GuiItem(deny, event ->
        {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
        }), 2, 0);

        confirmPane.addItem(new GuiItem(confirmItem, event ->
        {
            event.setCancelled(true);
            if (m.equals("demote")) {
                track.demote(user, set1);
                p.sendMessage(getChat(main.getInstance().getMessage().getString("track.demote-message").replace("%player%", user.getUsername()).replace("%track_name%", track.getName())));
            } else if (m.equals("promote")) {
                track.promote(user, set1);
                p.sendMessage(getChat(main.getInstance().getMessage().getString("track.promote-message").replace("%player%", user.getUsername()).replace("%track_name%", track.getName())));
            }


            event.getWhoClicked().closeInventory();
        }), 6, 0);


        confirm.addPane(confirmPane);

        return confirm;
    }

    public String getChat(String text) {
        String message = ChatColor.translateAlternateColorCodes('&',text);
        return message;
    }


}
