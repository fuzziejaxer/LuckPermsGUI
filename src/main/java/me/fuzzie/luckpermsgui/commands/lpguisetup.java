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

public class lpguisetup implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender Sender, Command cmd, String label, String[] args) {

        if (Sender instanceof Player) {
            Player player = (Player) Sender;

            // get lists from config
            List<String> rankName = main.getInstance().getConfig().getStringList("ranks.rank-name");
            List<String> rankPrefix = main.getInstance().getConfig().getStringList("ranks.rank-prefix");
            List<String> rankItem = main.getInstance().getConfig().getStringList("ranks.rank-item");

            if (rankName.size() != rankPrefix.size()) {
                player.sendMessage(ChatColor.RED + "[!] " + ChatColor.WHITE + "please make sure the config.yml is correct");
            } else {
                // create gui
                ChestGui gui = new ChestGui(5, (ChatColor.RED + "Groups To Create: "));
                OutlinePane pane = new OutlinePane(0, 0, 9, 4);

                // loop through all items
                for (int i = 0; i < rankName.size(); i++) {
                    // deal with items
                    ItemStack item = new ItemStack(Material.getMaterial(rankItem.get(i)));
                    String ItemName = (ChatColor.translateAlternateColorCodes('&', rankPrefix.get(i)));

                    if (main.getInstance().getConfig().getBoolean("global.setup.set-prefix") == true) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.WHITE + "Group: " + rankName.get(i) + " will be created with the prefix: " + ItemName);
                        item.setItemMeta(meta);
                    } else {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.WHITE + "Group: " + rankName.get(i) + " will be created with no prefix");
                        item.setItemMeta(meta);
                    }

                    GuiItem guiItem = new GuiItem(item, event ->
                    {
                        event.setCancelled(true);
                    });
                    pane.addItem(guiItem);
                }
                gui.addPane(pane);

                // create exit button
                ItemStack exit = new ItemStack(Material.BARRIER);
                ItemMeta metaTwo = exit.getItemMeta();
                metaTwo.setDisplayName(ChatColor.RED + "Exit");
                exit.setItemMeta(metaTwo);

                OutlinePane background = main.getInstance().getBackground(0, 4, 9, 1);
                gui.addPane(background);

                StaticPane footer = new StaticPane(0, 4, 9, 1);
                footer.addItem(new GuiItem(exit, event ->
                {
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                }), 4, 0);



                footer.addItem(main.getInstance().backButton(player, "lpgui " + player.getName()), 0, 0);

                gui.addPane(footer);

                StaticPane buttons = new StaticPane(0, 3, 9, 1);

                ItemStack deny = new ItemStack(Material.RED_WOOL);
                ItemMeta meta = deny.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lDeny"));
                deny.setItemMeta(meta);

                ItemStack confirm = new ItemStack(Material.GREEN_WOOL);
                ItemMeta meta2 = confirm.getItemMeta();
                meta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lConfirm"));
                confirm.setItemMeta(meta2);

                buttons.addItem(new GuiItem(deny, event ->
                {
                    event.setCancelled(true);
                    player.performCommand("lpgui " + player.getName());
                }), 3, 0);

                buttons.addItem(new GuiItem(confirm, event ->
                {
                    event.setCancelled(true);
                    createGroups(rankName, rankPrefix);
                    player.performCommand("rank " + player.getName());
                }), 5, 0);

                gui.addPane(buttons);

                gui.show(player);

            }




        }
        return true;
    }

    public static void createGroups(List<String> rankName, List<String> rankPrefix){
        for (int i = 0; i < rankName.size(); i++) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp creategroup " + rankName.get(i));


            Boolean setprefix = main.getInstance().getConfig().getBoolean("global.setup.set-prefix");
            if (setprefix != false) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp group " + rankName.get(i) + " meta setprefix " + rankPrefix.get(i) + "&f");
            }
        }
    }
}
