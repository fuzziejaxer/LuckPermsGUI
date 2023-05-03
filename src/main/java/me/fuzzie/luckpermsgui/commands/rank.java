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

import java.util.ArrayList;
import java.util.List;

public class rank implements CommandExecutor {



    @Override
    public boolean onCommand(CommandSender Sender, Command cmd, String label, String[] args) {

        List<String> rankName = main.getInstance().getConfig().getStringList("ranks.rank-name");

        List<String> rankPrefix = main.getInstance().getConfig().getStringList("ranks.rank-prefix");

        List<String> rankItem = main.getInstance().getConfig().getStringList("ranks.rank-item");

        if (Sender instanceof Player) {
            Player player = (Player) Sender;
            if (args.length > 0) {
                Player target = Bukkit.getPlayer(args[0]);

                if (target != null) {
                    ChestGui gui = new ChestGui(5, (getChat(main.getInstance().getMessage().getString("rank.menu-title").replace("%player%", target.getName()))));
                    OutlinePane pane = new OutlinePane(0, 0, 9, 4);

                    for (int i = 0; i < rankName.size(); i++) {
                        // get current rank name
                        String currentRankName = rankName.get(i);

                        // create confirmation menu
                        ChestGui confirm = main.getInstance().getConfirm(player, target, "rank","lp user " + target.getName() + " parent set " + currentRankName);

                        // deal with items
                        ItemStack item = new ItemStack(Material.getMaterial(rankItem.get(i)));
                        String ItemName = (ChatColor.translateAlternateColorCodes('&', rankPrefix.get(i)));

                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ItemName);
                        ArrayList<String> loreList = new ArrayList<String>();
                        loreList.add(getChat(main.getInstance().getMessage().getString("rank.item-lore").replace("%group_name%", rankName.get(i)).replace("%prefix%", ItemName).replace("%player%", target.getName())));
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

                    OutlinePane background = main.getInstance().getBackground(0, 4, 9, 1);
                    gui.addPane(background);

                    // creates exit button
                    StaticPane footer = new StaticPane(0, 4, 9, 1);
                    footer.addItem(new GuiItem(main.getInstance().getExit(), event ->
                    {
                        event.setCancelled(true);
                        event.getWhoClicked().closeInventory();
                    }), 4, 0);

                    footer.addItem(main.getInstance().backButton(player, "lpgui " + target.getName()), 0, 0);

                    gui.addPane(footer);

                    gui.show(player);

                } else {
                    player.sendMessage(getChat(main.getInstance().getMessage().getString("offline-player")));
                }
            } else {
                Player target = player;

                ChestGui gui = new ChestGui(5, (getChat(main.getInstance().getMessage().getString("rank.menu-title").replace("%player%", target.getName()))));
                OutlinePane pane = new OutlinePane(0, 0, 9, 4);

                for (int i = 0; i < rankName.size(); i++) {
                    // get current rank name
                    String currentRankName = rankName.get(i);

                    // create confirmation menu
                    ChestGui confirm = main.getInstance().getConfirm(player, target, "rank","lp user " + target.getName() + " parent set " + currentRankName);

                    // deal with items
                    ItemStack item = new ItemStack(Material.getMaterial(rankItem.get(i)));
                    String ItemName = getChat(rankPrefix.get(i));

                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ItemName);
                    ArrayList<String> loreList = new ArrayList<String>();
                    loreList.add(getChat(main.getInstance().getMessage().getString("rank.item-lore").replace("%group_name%", rankName.get(i)).replace("%prefix%", ItemName).replace("%player%", target.getName())));
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

                OutlinePane background = main.getInstance().getBackground(0, 4, 9, 1);
                gui.addPane(background);

                // creates exit button
                StaticPane footer = new StaticPane(0, 4, 9, 1);
                footer.addItem(new GuiItem(main.getInstance().getExit(), event ->
                {
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                }), 4, 0);

                footer.addItem(main.getInstance().backButton(player, "lpgui " + target.getName()), 0, 0);

                gui.addPane(footer);

                gui.show(player);

            }
        }
        return true;
    }

    public String getChat(String text) {
        String message = ChatColor.translateAlternateColorCodes('&',text);
        return message;
    }
}
