package me.fuzzie.luckpermsgui.commands;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.fuzzie.luckpermsgui.main;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
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

public class grant implements CommandExecutor {



    @Override
    public boolean onCommand(CommandSender Sender, Command cmd, String label, String[] args) {
        if (Sender instanceof Player) {
            Player player = (Player) Sender;
            User user = main.getInstance().getLP().getUserManager().getUser(player.getName());

            List<String> permission = main.getInstance().getConfig().getStringList("permissions.permissions");
            int page = 0;

            if (args.length > 0) {
                if (args.length > 1) {

                    Player target = Bukkit.getPlayer(args[0]);
                    User targetUser = main.getInstance().getLP().getUserManager().getUser(args[0]);

                    if (target != null) {

                        page = Integer.parseInt(args[1]) - 1;
                        if (page <= 2 && page >= 0) {

                            ChestGui gui = new ChestGui(6, (getChat(main.getInstance().getMessage().getString("permissions.menu-title").replace("%player%", target.getName()))));

                            PaginatedPane pages = new PaginatedPane(0, 0, 9, 5);
                            OutlinePane pane = new OutlinePane(0, 0, 9, 5);
                            OutlinePane pane2 = new OutlinePane(0, 0, 9, 5);
                            OutlinePane pane3 = new OutlinePane(0, 0, 9, 5);

                            for (int i = 0; i < permission.size(); i++) {
                                int currentPage = 0;
                                if (targetUser.getCachedData().getPermissionData().checkPermission(permission.get(i)).asBoolean() == false) {
                                    if (main.getInstance().getConfig().getBoolean("permissions.require-permission") == false
                                            || user.getCachedData().getPermissionData().checkPermission(permission.get(i)).asBoolean() == true &&
                                            main.getInstance().getConfig().getBoolean("permissions.require-permission") == true) {

                                        ItemStack item = new ItemStack(Material.LIME_CONCRETE);
                                        ItemMeta meta = item.getItemMeta();
                                        meta.setDisplayName(ChatColor.GREEN + permission.get(i));
                                        item.setItemMeta(meta);


                                        int finalI = i;
                                        GuiItem guiItem = new GuiItem(item);


                                        if (i > 44) {
                                            if (i > 89) {
                                                pane3.addItem(guiItem);
                                                currentPage = 3;
                                            } else {
                                                pane2.addItem(guiItem);
                                                currentPage = 2;
                                            }
                                        } else {
                                            pane.addItem(guiItem);
                                            currentPage = 1;
                                        }

                                        int finalCurrentPage = currentPage;
                                        guiItem.setAction(event -> {
                                            event.setCancelled(true);
                                            DataMutateResult result = targetUser.data().add(Node.builder(permission.get(finalI)).build());
                                            main.getInstance().getLP().getUserManager().saveUser(targetUser);

                                            player.performCommand("grant " + target.getName() + " " + finalCurrentPage);
                                        });

                                    }
                                } else if (targetUser.getCachedData().getPermissionData().checkPermission(permission.get(i)).asBoolean() == true) {

                                    if (main.getInstance().getConfig().getBoolean("permissions.require-permission") == false
                                            || user.getCachedData().getPermissionData().checkPermission(permission.get(i)).asBoolean() == true &&
                                            main.getInstance().getConfig().getBoolean("permissions.require-permission") == true) {

                                        ItemStack item = new ItemStack(Material.RED_CONCRETE);
                                        ItemMeta meta = item.getItemMeta();
                                        meta.setDisplayName(ChatColor.RED + permission.get(i));
                                        item.setItemMeta(meta);

                                        int finalI = i;
                                        GuiItem guiItem = new GuiItem(item);

                                        if (i > 44) {
                                            if (i > 89) {
                                                pane3.addItem(guiItem);
                                                currentPage = 3;
                                            } else {
                                                pane2.addItem(guiItem);
                                                currentPage = 2;
                                            }
                                        } else {
                                            pane.addItem(guiItem);
                                            currentPage = 1;
                                        }

                                        int finalCurrentPage = currentPage;
                                        guiItem.setAction(event -> {
                                            event.setCancelled(true);
                                            DataMutateResult result = targetUser.data().remove(Node.builder(permission.get(finalI)).build());
                                            main.getInstance().getLP().getUserManager().saveUser(targetUser);

                                            player.performCommand("grant " + target.getName() + " " + finalCurrentPage);
                                        });

                                    }

                                }


                            }


                            pages.addPane(0, pane);
                            pages.addPane(1, pane2);
                            pages.addPane(2, pane3);
                            gui.addPane(pages);

                            OutlinePane background = main.getInstance().getBackground(0, 5, 9, 1);
                            gui.addPane(background);

                            StaticPane navigation = new StaticPane(0, 5, 9, 1);

                            ItemStack prev = new ItemStack(Material.ARROW);
                            ItemMeta prevMeta = prev.getItemMeta();
                            prevMeta.setDisplayName(ChatColor.RED + "Previous Page");
                            prev.setItemMeta(prevMeta);

                            navigation.addItem(new GuiItem(prev, event -> {
                                if (pages.getPage() > 0) {
                                    pages.setPage(pages.getPage() - 1);

                                    gui.update();

                                }
                                event.setCancelled(true);
                            }), 2, 0);

                            ItemStack next = new ItemStack(Material.ARROW);
                            ItemMeta meta = next.getItemMeta();
                            meta.setDisplayName(ChatColor.GREEN + "Next Page");
                            next.setItemMeta(meta);

                            navigation.addItem(new GuiItem(next, event -> {
                                if (pages.getPage() < pages.getPages() - 1) {
                                    pages.setPage(pages.getPage() + 1);

                                    gui.update();

                                }
                                event.setCancelled(true);
                            }), 6, 0);

                            navigation.addItem(new GuiItem(main.getInstance().getExit(), event -> {
                                event.getWhoClicked().closeInventory();
                                event.setCancelled(true);

                            }), 4, 0);

                            navigation.addItem(main.getInstance().backButton(player, "lpgui"), 0, 0);

                            gui.addPane(navigation);

                            pages.setPage(page);
                            gui.show(player);

                        } else {
                            player.sendMessage(getChat(main.getInstance().getMessage().getString("permissions.invalid-page")));
                        }
                    } else {
                        player.sendMessage(getChat(main.getInstance().getMessage().getString("offline-player")));
                    }

                } else {
                    Player target = Bukkit.getPlayer(args[0]);
                    User targetUser = main.getInstance().getLP().getUserManager().getUser(args[0]);

                    if (target != null) {

                        ChestGui gui = new ChestGui(6, (getChat(main.getInstance().getMessage().getString("permissions.menu-title").replace("%player%", target.getName()))));

                        PaginatedPane pages = new PaginatedPane(0, 0, 9, 5);
                        OutlinePane pane = new OutlinePane(0, 0, 9, 5);
                        OutlinePane pane2 = new OutlinePane(0, 0, 9, 5);
                        OutlinePane pane3 = new OutlinePane(0, 0, 9, 5);

                        for (int i = 0; i < permission.size(); i++) {
                            int currentPage = 0;
                            if (targetUser.getCachedData().getPermissionData().checkPermission(permission.get(i)).asBoolean() == false) {
                                if (main.getInstance().getConfig().getBoolean("permissions.require-permission") == false
                                        || user.getCachedData().getPermissionData().checkPermission(permission.get(i)).asBoolean() == true &&
                                        main.getInstance().getConfig().getBoolean("permissions.require-permission") == true) {

                                    ItemStack item = new ItemStack(Material.LIME_CONCRETE);
                                    ItemMeta meta = item.getItemMeta();
                                    meta.setDisplayName(ChatColor.GREEN + permission.get(i));
                                    item.setItemMeta(meta);


                                    int finalI = i;
                                    GuiItem guiItem = new GuiItem(item);


                                    if (i > 44) {
                                        if (i > 89) {
                                            pane3.addItem(guiItem);
                                            currentPage = 3;
                                        } else {
                                            pane2.addItem(guiItem);
                                            currentPage = 2;
                                        }
                                    } else {
                                        pane.addItem(guiItem);
                                        currentPage = 1;
                                    }

                                    int finalCurrentPage = currentPage;
                                    guiItem.setAction(event -> {
                                        event.setCancelled(true);
                                        DataMutateResult result = targetUser.data().add(Node.builder(permission.get(finalI)).build());
                                        main.getInstance().getLP().getUserManager().saveUser(targetUser);

                                        player.performCommand("grant " + target.getName() + " " + finalCurrentPage);
                                    });

                                }

                            } else if (targetUser.getCachedData().getPermissionData().checkPermission(permission.get(i)).asBoolean() == true) {

                                if (main.getInstance().getConfig().getBoolean("permissions.require-permission") == false
                                        || user.getCachedData().getPermissionData().checkPermission(permission.get(i)).asBoolean() == true &&
                                        main.getInstance().getConfig().getBoolean("permissions.require-permission") == true) {

                                    ItemStack item = new ItemStack(Material.RED_CONCRETE);
                                    ItemMeta meta = item.getItemMeta();
                                    meta.setDisplayName(ChatColor.RED + permission.get(i));
                                    item.setItemMeta(meta);

                                    int finalI = i;
                                    GuiItem guiItem = new GuiItem(item);

                                    if (i > 44) {
                                        if (i > 89) {
                                            pane3.addItem(guiItem);
                                            currentPage = 3;
                                        } else {
                                            pane2.addItem(guiItem);
                                            currentPage = 2;
                                        }
                                    } else {
                                        pane.addItem(guiItem);
                                        currentPage = 1;
                                    }

                                    int finalCurrentPage = currentPage;
                                    guiItem.setAction(event -> {
                                        event.setCancelled(true);
                                        DataMutateResult result = targetUser.data().remove(Node.builder(permission.get(finalI)).build());
                                        main.getInstance().getLP().getUserManager().saveUser(targetUser);

                                        player.performCommand("grant " + target.getName() + " " + finalCurrentPage);
                                    });

                                }

                            }


                        }


                        pages.addPane(0, pane);
                        pages.addPane(1, pane2);
                        pages.addPane(2, pane3);
                        gui.addPane(pages);

                        OutlinePane background = main.getInstance().getBackground(0, 5, 9, 1);
                        gui.addPane(background);

                        StaticPane navigation = new StaticPane(0, 5, 9, 1);

                        ItemStack prev = new ItemStack(Material.ARROW);
                        ItemMeta prevMeta = prev.getItemMeta();
                        prevMeta.setDisplayName(ChatColor.RED + "Previous Page");
                        prev.setItemMeta(prevMeta);

                        navigation.addItem(new GuiItem(prev, event -> {
                            if (pages.getPage() > 0) {
                                pages.setPage(pages.getPage() - 1);

                                gui.update();

                            }
                            event.setCancelled(true);
                        }), 2, 0);

                        ItemStack next = new ItemStack(Material.ARROW);
                        ItemMeta meta = next.getItemMeta();
                        meta.setDisplayName(ChatColor.GREEN + "Next Page");
                        next.setItemMeta(meta);

                        navigation.addItem(new GuiItem(next, event -> {
                            if (pages.getPage() < pages.getPages() - 1) {
                                pages.setPage(pages.getPage() + 1);

                                gui.update();

                            }
                            event.setCancelled(true);
                        }), 6, 0);

                        navigation.addItem(new GuiItem(main.getInstance().getExit(), event -> {
                            event.getWhoClicked().closeInventory();
                            event.setCancelled(true);

                        }), 4, 0);

                        navigation.addItem(main.getInstance().backButton(player, "lpgui"), 0, 0);

                        gui.addPane(navigation);

                        gui.show(player);

                    } else {
                        player.sendMessage(getChat(main.getInstance().getMessage().getString("offline-player")));
                    }
                }
            } else {
                player.sendMessage(getChat(main.getInstance().getMessage().getString("offline-player")));
            }

        }
        return true;
    }


    public String getChat(String text) {
        String message = ChatColor.translateAlternateColorCodes('&',text);
        return message;
    }
}
