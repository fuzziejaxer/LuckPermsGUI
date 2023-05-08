package me.fuzzie.luckpermsgui.commands;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.fuzzie.luckpermsgui.main;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
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
                User user = main.getInstance().getLP().getUserManager().getUser(args[0]);

                if (target != null) {
                    ChestGui gui = new ChestGui(5, (getChat(main.getInstance().getMessage().getString("rank.menu-title").replace("%player%", target.getName()))));
                    OutlinePane pane = new OutlinePane(0, 0, 9, 4);

                    for (int i = 0; i < rankName.size(); i++) {
                        // get current rank name
                        String currentRankName = rankName.get(i);

                        // create confirmation menu
                        ChestGui confirm = getConfirm(player, user, "rank", rankName, i);

                        // deal with items
                        ItemStack item = new ItemStack(Material.getMaterial(rankItem.get(i)));
                        String ItemName = (ChatColor.translateAlternateColorCodes('&', rankPrefix.get(i)));

                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ItemName);
                        ArrayList<String> loreList = new ArrayList<String>();
                        loreList.add(getChat(main.getInstance().getMessage().getString("rank.item-lore").replace("%group_name%", rankName.get(i)).replace("%prefix%", ItemName).replace("%player%", target.getName())));
                        meta.setLore(loreList);
                        item.setItemMeta(meta);

                        int finalI = i;
                        GuiItem guiItem = new GuiItem(item, event ->
                        {
                            event.setCancelled(true);

                            if (main.getInstance().getConfig().getBoolean("ranks.require-confirm") == true) {
                                confirm.show(player);
                            } else if (main.getInstance().getConfig().getBoolean("ranks.require-confirm") == false) {
                                // remove current group
                                String primaryGroup = user.getPrimaryGroup();
                                user.data().remove(Node.builder("group." + primaryGroup).build());

                                // set new group
                                String groupname = rankName.get(finalI);
                                InheritanceNode node = InheritanceNode.builder(groupname).build();
                                user.data().add(node);

                                main.getInstance().getLP().getUserManager().saveUser(user);

                                player.sendMessage(getChat(main.getInstance().getMessage().getString("rank.chat-message").replace("%group_name%", rankName.get(finalI)).replace("%player%", user.getUsername())));
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
                User user = main.getInstance().getLP().getUserManager().getUser(target.getName());

                ChestGui gui = new ChestGui(5, (getChat(main.getInstance().getMessage().getString("rank.menu-title").replace("%player%", target.getName()))));
                OutlinePane pane = new OutlinePane(0, 0, 9, 4);

                for (int i = 0; i < rankName.size(); i++) {
                    // get current rank name
                    String currentRankName = rankName.get(i);

                    // create confirmation menu
                    ChestGui confirm = getConfirm(player, user, "rank", rankName, i);

                    // deal with items
                    ItemStack item = new ItemStack(Material.getMaterial(rankItem.get(i)));
                    String ItemName = getChat(rankPrefix.get(i));

                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ItemName);
                    ArrayList<String> loreList = new ArrayList<String>();
                    loreList.add(getChat(main.getInstance().getMessage().getString("rank.item-lore").replace("%group_name%", rankName.get(i)).replace("%prefix%", ItemName).replace("%player%", target.getName())));
                    meta.setLore(loreList);
                    item.setItemMeta(meta);

                    int finalI = i;
                    GuiItem guiItem = new GuiItem(item, event ->
                    {
                        event.setCancelled(true);

                        if (main.getInstance().getConfig().getBoolean("ranks.require-confirm") == true) {
                            confirm.show(player);
                        } else if (main.getInstance().getConfig().getBoolean("ranks.require-confirm") == false) {
                            // remove current group
                            String primaryGroup = user.getPrimaryGroup();
                            user.data().remove(Node.builder("group." + primaryGroup).build());

                            // set new group
                            String groupname = rankName.get(finalI);
                            InheritanceNode node = InheritanceNode.builder(groupname).build();
                            user.data().add(node);

                            main.getInstance().getLP().getUserManager().saveUser(user);

                            player.sendMessage(getChat(main.getInstance().getMessage().getString("rank.chat-message").replace("%group_name%", rankName.get(finalI)).replace("%player%", user.getUsername())));
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

    public ChestGui getConfirm(Player player, User user, String backCmd, List<String> rankName, int i) {
        ChestGui confirm = new ChestGui(1, (ChatColor.translateAlternateColorCodes('&', main.getInstance().getMessage().getString("confirm-menu.menu-title"))));

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
                // remove current group
                String primaryGroup = user.getPrimaryGroup();
                user.data().remove(Node.builder("group." + primaryGroup).build());

                // set new group
                String groupname = rankName.get(i);
                InheritanceNode node = InheritanceNode.builder(groupname).build();
                user.data().add(node);

                main.getInstance().getLP().getUserManager().saveUser(user);

                player.sendMessage(getChat(main.getInstance().getMessage().getString("rank.chat-message").replace("%group_name%", rankName.get(i)).replace("%player%", user.getUsername())));

                event.getWhoClicked().closeInventory();

        }), 6, 0);



        confirmPane.addItem(main.getInstance().backButton(player, backCmd + " " + user.getUsername()), 0, 0);

        confirm.addPane(confirmPane);

        return confirm;
    }



    public String getChat(String text) {
        String message = ChatColor.translateAlternateColorCodes('&',text);
        return message;
    }
}
