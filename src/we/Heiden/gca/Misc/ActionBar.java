/*
 * =============================================
 * =============================================
 * ============ All Rights Reserved ============
 * =============== CavariuX 2015 ===============
 * =============================================
 * =============================================
 */

package we.Heiden.gca.Misc;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import net.minecraft.server.v1_8_R1.PlayerConnection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * ********************************************* <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 *         <b>All rights reserved <p>
 *           Heiden Team 2015 <p></b>
 * ********************************************* 
 **/
public class ActionBar {

    public static void sendMessage(Player p, String message, Boolean broadcast)  {
        if (message == null) message = "";
        message = ChatColor.translateAlternateColorCodes('&', message);
        message = message.replaceAll("%player%", p.getName());
        IChatBaseComponent chat = ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chat, (byte) 2);
        if (!broadcast) {
            PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
            con.sendPacket(packet);
        } else for (Player p2 : Bukkit.getOnlinePlayers()) {
                PlayerConnection con = ((CraftPlayer) p2).getHandle().playerConnection;
                con.sendPacket(packet);
            }
    }
    
    public static void sendMessage(Player p, String message) {
    	sendMessage(p, message, false);
    }
    
    public static void sendMessage(String message) {
    	if (message == null) message = "";
        message = ChatColor.translateAlternateColorCodes('&', message);
        IChatBaseComponent chat = ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chat, (byte) 2);
        for (Player p : Bukkit.getOnlinePlayers()) {
        	PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
        	con.sendPacket(packet);
        }
    }
}
