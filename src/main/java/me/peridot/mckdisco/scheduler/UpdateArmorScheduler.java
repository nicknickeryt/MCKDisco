package me.peridot.mckdisco.scheduler;

import api.peridot.periapi.packets.PacketSender;
import me.peridot.mckdisco.MCKDisco;
import me.peridot.mckdisco.enums.DiscoType;
import me.peridot.mckdisco.user.User;
import me.peridot.mckdisco.util.EquipmentUtil;
import me.peridot.mckdisco.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UpdateArmorScheduler {

    private final MCKDisco plugin;

    public UpdateArmorScheduler(MCKDisco plugin) {
        this.plugin = plugin;
    }

    public void start() {
        EquipmentUtil equipment = new EquipmentUtil();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                User user = plugin.getUserCache().createUser(player);
                DiscoType discoType = user.getDiscoType();

                ItemStack helmet = null;
                ItemStack chestplate = null;
                ItemStack leggings = null;
                ItemStack boots = null;

                if (discoType != null) {
                    if (discoType == DiscoType.RANDOM) {
                        helmet = Util.createColorArmor(new ItemStack(Material.LEATHER_HELMET), Util.randomColor());
                        chestplate = Util.createColorArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Util.randomColor());
                        leggings = Util.createColorArmor(new ItemStack(Material.LEATHER_LEGGINGS), Util.randomColor());
                        boots = Util.createColorArmor(new ItemStack(Material.LEATHER_BOOTS), Util.randomColor());
                    } else if (discoType == DiscoType.SMOOTH) {
                        helmet = Util.createColorArmor(new ItemStack(Material.LEATHER_HELMET), Color.fromRGB(ColorScheduler.redSmooth, ColorScheduler.greenSmooth, ColorScheduler.blueSmooth));
                        chestplate = Util.createColorArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.fromRGB(ColorScheduler.redSmooth, ColorScheduler.greenSmooth, ColorScheduler.blueSmooth));
                        leggings = Util.createColorArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.fromRGB(ColorScheduler.redSmooth, ColorScheduler.greenSmooth, ColorScheduler.blueSmooth));
                        boots = Util.createColorArmor(new ItemStack(Material.LEATHER_BOOTS), Color.fromRGB(ColorScheduler.redSmooth, ColorScheduler.greenSmooth, ColorScheduler.blueSmooth));
                    } else if (discoType == DiscoType.POLICE) {
                        helmet = Util.createColorArmor(new ItemStack(Material.LEATHER_HELMET), Color.fromRGB(ColorScheduler.redPolice, 0, ColorScheduler.bluePolice));
                        chestplate = Util.createColorArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.fromRGB(ColorScheduler.redPolice, 0, ColorScheduler.bluePolice));
                        leggings = Util.createColorArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.fromRGB(ColorScheduler.redPolice, 0, ColorScheduler.bluePolice));
                        boots = Util.createColorArmor(new ItemStack(Material.LEATHER_BOOTS), Color.fromRGB(ColorScheduler.redPolice, 0, ColorScheduler.bluePolice));
                    } else if (discoType == DiscoType.GRAY) {
                        helmet = Util.createColorArmor(new ItemStack(Material.LEATHER_HELMET), Color.fromRGB(ColorScheduler.grayShade, ColorScheduler.grayShade, ColorScheduler.grayShade));
                        chestplate = Util.createColorArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.fromRGB(ColorScheduler.grayShade, ColorScheduler.grayShade, ColorScheduler.grayShade));
                        leggings = Util.createColorArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.fromRGB(ColorScheduler.grayShade, ColorScheduler.grayShade, ColorScheduler.grayShade));
                        boots = Util.createColorArmor(new ItemStack(Material.LEATHER_BOOTS), Color.fromRGB(ColorScheduler.grayShade, ColorScheduler.grayShade, ColorScheduler.grayShade));
                    }
                } else {
                        helmet = player.getInventory().getHelmet();
                        chestplate = player.getInventory().getChestplate();
                        leggings = player.getInventory().getLeggings();
                        boots = player.getInventory().getBoots();

                }
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Object helmetPacket;
                    Object chestplatePacket;
                    Object leggingsPacket;
                    Object bootsPacket;
                    if (!onlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                        helmetPacket = equipment.createEquipmentPacket(player, 5, helmet, false);
                        chestplatePacket = equipment.createEquipmentPacket(player, 4, chestplate, false);
                        leggingsPacket = equipment.createEquipmentPacket(player, 3, leggings, false);
                        bootsPacket = equipment.createEquipmentPacket(player, 2, boots, false);

                        PacketSender.sendPacket(onlinePlayer, helmetPacket, chestplatePacket, leggingsPacket, bootsPacket);
                    }
                }

                Object helmetPacket;
                Object chestplatePacket;
                Object leggingsPacket;
                Object bootsPacket;
                if (!user.isRealArmorPacket()) {
                    continue;
                }
                if (discoType == null) {
                    continue;
                }
                ItemStack[] armor = user.getArmor();
                if (armor != null) {
                    helmet = armor[3];
                    chestplate = armor[2];
                    leggings = armor[1];
                    boots = armor[0];

                    helmetPacket = equipment.createEquipmentPacket(player, 5, helmet, true);
                    chestplatePacket = equipment.createEquipmentPacket(player, 4, chestplate, true);
                    leggingsPacket = equipment.createEquipmentPacket(player, 3, leggings, true);
                    bootsPacket = equipment.createEquipmentPacket(player, 2, boots, true);

                    PacketSender.sendPacket(player, helmetPacket, chestplatePacket, leggingsPacket, bootsPacket);

                    player.getInventory().setArmorContents(armor);

                    user.setRealArmorPacket(false);
                }
            }
        }, 0, 1);
    }

}
