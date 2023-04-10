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

import java.util.ArrayList;
import java.util.List;

public class rank implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender Sender, Command cmd, String label, String[] args) {

        if (Sender instanceof Player) {
            Player player = (Player) Sender;

            if (args.length > 0) {
                Player target = Bukkit.getPlayer(args[0]);

                if (target != null) {


                    ChestGui gui = new ChestGui(5, (ChatColor.RED + "Rank " + ChatColor.DARK_GRAY + "Menu"));



                    OutlinePane pane = new OutlinePane(0, 0, 9, 4);




                    List<String> rankName = main.getInstance().getConfig().getStringList("ranks.rank-name");

                    List<String> rankPrefix = main.getInstance().getConfig().getStringList("ranks.rank-prefix");

                    List<String> rankItem = main.getInstance().getConfig().getStringList("ranks.rank-item");


                    // create arrays
                    String[] rankNameArray = new String[rankName.size()];
                    String[] rankPrefixArray = new String[rankPrefix.size()];



                    // put the list data into the arrays
                    for (int i = 0; i < rankNameArray.length; i++) {
                        rankNameArray[i] = rankName.get(i);
                    }

                    // get coloured prefix
                    for (int i = 0; i < rankPrefixArray.length; i++) {
                        rankPrefixArray[i] = (ChatColor.translateAlternateColorCodes('&', rankPrefix.get(i)));
                    }

                    // back button
                    ItemStack backItem = new ItemStack(Material.getMaterial(main.getInstance().getConfig().getString("global.back-item")));
                    ItemMeta backMeta = backItem.getItemMeta();
                    backMeta.setDisplayName(ChatColor.RED + "Back");
                    backItem.setItemMeta(backMeta);


                    for (int i = 0; i < rankNameArray.length; i++) {
                        // get current rank name
                        String currentRankName = rankNameArray[i];

                        // create confirmation menu
                        ChestGui confirm = new ChestGui(1, (ChatColor.GREEN + "Confirm:"));


                        // create background item
                        ItemStack bgItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                        ItemMeta BGmeta = bgItem.getItemMeta();
                        BGmeta.setDisplayName(" ");
                        bgItem.setItemMeta(BGmeta);

                        OutlinePane background = new OutlinePane(0, 0, 9, 1);
                        background.addItem(new GuiItem(bgItem, event -> event.setCancelled(true)));
                        background.setRepeat(true);
                        background.setPriority(Pane.Priority.LOWEST);

                        confirm.addPane(background);

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
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " parent set " + currentRankName);
                            event.getWhoClicked().closeInventory();
                        }), 6, 0);

                        GuiItem backConfirm = new GuiItem(backItem, event -> {
                            event.setCancelled(true);
                            player.performCommand("rank " + target.getName());
                        });

                        confirmPane.addItem(backConfirm, 0, 0);


                        confirm.addPane(confirmPane);


                        // deal with items
                        ItemStack item = new ItemStack(Material.getMaterial(rankItem.get(i)));
                        String ItemName = rankPrefixArray[i];

                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ItemName);
                        ArrayList<String> loreList = new ArrayList<String>();
                        loreList.add(ChatColor.WHITE + "will set " + ChatColor.GREEN + target.getName() + ChatColor.WHITE + " to " + ItemName);
                        meta.setLore(loreList);
                        item.setItemMeta(meta);




                        GuiItem guiItem = new GuiItem(item, event ->
                        {
                            event.setCancelled(true);

                            if (main.getInstance().getConfig().getBoolean("ranks.require-confirm") == true) {
                                confirm.show(player);
                            } else if (main.getInstance().getConfig().getBoolean("ranks.require-confirm") == false) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " parent set " + currentRankName);
                            }
                        });





                        pane.addItem(guiItem);

                    }
                    gui.addPane(pane);

                    // create background item
                    ItemStack bg = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                    ItemMeta meta = bg.getItemMeta();
                    meta.setDisplayName(" ");
                    bg.setItemMeta(meta);

                    // create exit button
                    ItemStack exit = new ItemStack(Material.BARRIER);
                    ItemMeta metaTwo = exit.getItemMeta();
                    metaTwo.setDisplayName(ChatColor.RED + "Exit");
                    exit.setItemMeta(metaTwo);

                    OutlinePane background = new OutlinePane(0, 4, 9, 1);
                    background.addItem(new GuiItem(bg, event -> event.setCancelled(true)));
                    background.setRepeat(true);
                    background.setPriority(Pane.Priority.LOWEST);

                    gui.addPane(background);

                    StaticPane footer = new StaticPane(0, 4, 9, 1);
                    footer.addItem(new GuiItem(exit, event ->
                    {
                        event.setCancelled(true);
                        event.getWhoClicked().closeInventory();
                    }), 4, 0);



                    GuiItem back = new GuiItem(backItem, event -> {
                        event.setCancelled(true);
                        player.performCommand("lpgui " + target.getName());
                    });

                    footer.addItem(back, 0, 0);

                    gui.addPane(footer);

                    gui.show(player);


                } else {
                    player.sendMessage(ChatColor.RED + "[!] " + ChatColor.WHITE + "please select an online player");
                }
            } else {
                player.sendMessage(ChatColor.RED + "[!] " + ChatColor.WHITE + "please specify a player \n usage: " + ChatColor.GREEN + "/rank <player>");
            }
        }
        return true;
    }
}
