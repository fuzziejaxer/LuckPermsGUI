package me.fuzzie.luckpermsgui.commands;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
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
            Player p = (Player) Sender;

            if (args.length > 0) {
                Player target = Bukkit.getPlayer(args[0]);

                if (target != null) {

                    // List<String> rankItem = getConfig().getStringList("ranks.rank-item");
                    List<String> trackName = main.getInstance().getConfig().getStringList("tracks.track-name");
                    List<String> trackPrefix = main.getInstance().getConfig().getStringList("tracks.track-prefix");
                    List<String> trackItem = main.getInstance().getConfig().getStringList("tracks.track-item");

                    // global exit button
                    ItemStack exit = new ItemStack(Material.BARRIER);
                    ItemMeta metaTwo = exit.getItemMeta();
                    metaTwo.setDisplayName(ChatColor.RED + "Exit");
                    exit.setItemMeta(metaTwo);


                    // create GUI
                    ChestGui gui = new ChestGui(4, (ChatColor.GREEN + "Track" + ChatColor.DARK_GRAY + "Menu"));
                    OutlinePane pane = new OutlinePane(0, 0, 9, 3);

                    // footer for main track menu

                    ItemStack trackbg = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                    ItemMeta Trackmeta = trackbg.getItemMeta();
                    Trackmeta.setDisplayName(" ");
                    trackbg.setItemMeta(Trackmeta);

                    OutlinePane trackBackground = new OutlinePane(0, 3, 9, 1);
                    trackBackground.addItem(new GuiItem(trackbg, event -> event.setCancelled(true)));
                    trackBackground.setRepeat(true);
                    trackBackground.setPriority(Pane.Priority.LOWEST);

                    gui.addPane(trackBackground);

                    // create exit button

                    StaticPane footer2 = new StaticPane(0, 3, 9, 1);
                    footer2.addItem(new GuiItem(exit, event ->
                    {
                        event.setCancelled(true);
                        event.getWhoClicked().closeInventory();
                    }), 4, 0);



                    // back button
                    ItemStack backItem = new ItemStack(Material.getMaterial(main.getInstance().getConfig().getString("global.back-item")));
                    ItemMeta backMeta = backItem.getItemMeta();
                    backMeta.setDisplayName(ChatColor.RED + "Back");
                    backItem.setItemMeta(backMeta);

                    GuiItem back = new GuiItem(backItem, event -> {
                        event.setCancelled(true);
                        p.performCommand("lpgui " + target.getName());
                    });

                    footer2.addItem(back, 0, 0);

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
                        // create background item
                        ItemStack bg = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                        ItemMeta meta2 = bg.getItemMeta();
                        meta2.setDisplayName(" ");
                        bg.setItemMeta(meta2);




                        OutlinePane background = new OutlinePane(0, 0, 9, 3);
                        background.addItem(new GuiItem(bg, event -> event.setCancelled(true)));
                        background.setRepeat(true);
                        background.setPriority(Pane.Priority.LOWEST);

                        clickedGui.addPane(background);




                        // create confirmation menu
                        ChestGui confirm = new ChestGui(1, (ChatColor.GREEN + "Confirm:"));

                        // create background item
                        ItemStack bgItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                        ItemMeta BGmeta = bgItem.getItemMeta();
                        BGmeta.setDisplayName(" ");
                        bgItem.setItemMeta(BGmeta);

                        OutlinePane backgroundConfirm = new OutlinePane(0, 0, 9, 1);
                        backgroundConfirm.addItem(new GuiItem(bgItem, event -> event.setCancelled(true)));
                        backgroundConfirm.setRepeat(true);
                        backgroundConfirm.setPriority(Pane.Priority.LOWEST);

                        confirm.addPane(backgroundConfirm);

                        // create buttons
                        ItemStack confirmItem = new ItemStack(Material.GREEN_WOOL);
                        ItemMeta metaConfirm = confirmItem.getItemMeta();
                        metaConfirm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lConfirm"));
                        confirmItem.setItemMeta(metaConfirm);

                        ItemStack deny = new ItemStack(Material.RED_WOOL);
                        ItemMeta metaDeny = deny.getItemMeta();
                        metaDeny.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lDeny"));
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
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + motion + currentTrack);
                            event.getWhoClicked().closeInventory();
                        }), 6, 0);

                        GuiItem backConfirm = new GuiItem(backItem, event -> {
                            event.setCancelled(true);
                            p.performCommand("track " + target.getName());
                        });

                        confirmPane.addItem(backConfirm, 0, 0);

                        confirm.addPane(confirmPane);





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
                                confirm.show(p);
                            } else if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == false) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " demote " + currentTrack);
                            }

                        }), 2, 0);

                        body.addItem(new GuiItem(promote, event ->
                        {
                            event.setCancelled(true);
                            if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == true) {
                                motion = " promote ";
                                confirm.show(p);
                            } else if (main.getInstance().getConfig().getBoolean("tracks.require-confirm") == false) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " promote " + currentTrack);
                            }

                        }), 6, 0);


                        clickedGui.addPane(body);



                        // create footer pane

                        StaticPane footer = new StaticPane(0, 2, 9, 1);
                        footer.addItem(new GuiItem(exit, event ->
                        {
                            event.setCancelled(true);
                            event.getWhoClicked().closeInventory();
                        }), 4, 0);

                        GuiItem back2 = new GuiItem(backItem, event -> {
                            event.setCancelled(true);
                            p.performCommand("track " + target.getName());
                        });

                        footer.addItem(back2, 0, 0);

                        clickedGui.addPane(footer);




                        // add original item

                        GuiItem guiItem = new GuiItem(item, event ->
                        {
                            event.setCancelled(true);
                            clickedGui.show(p);

                        });


                        pane.addItem(guiItem);
                    }


                    gui.addPane(pane);
                    gui.show(p);
                } else {
                    p.sendMessage(ChatColor.RED + "[!] " + ChatColor.WHITE + "please select an online player");
                }
            } else {
                p.sendMessage(ChatColor.RED + "[!] " + ChatColor.WHITE + "please specify a player \n usage: " + ChatColor.GREEN + "/track <player>");
            }


        }
        return true;
    }




}
