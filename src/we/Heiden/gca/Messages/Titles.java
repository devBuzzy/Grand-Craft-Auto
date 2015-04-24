package we.Heiden.gca.Messages;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R1.PlayerConnection;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Titles {

	private PlayerConnection con;
	PacketPlayOutTitle title = null;
	PacketPlayOutTitle subtitle = null;
	PacketPlayOutTitle times = null;

	public Titles(Player p) {
		con = ((CraftPlayer) p).getHandle().playerConnection;
	}

	public Titles(Player p, String title) {
		con = ((CraftPlayer) p).getHandle().playerConnection;
		title(title);
	}

	public Titles title(String title) {
		IChatBaseComponent comp = ChatSerializer.a("{text:\""
				+ ChatColor.translateAlternateColorCodes('&', title) + "\"}");
		this.title = new PacketPlayOutTitle(EnumTitleAction.TITLE, comp);
		return this;
	}

	public Titles subtitle(String subtitle) {
		IChatBaseComponent comp = ChatSerializer
				.a("{text:\""
						+ ChatColor.translateAlternateColorCodes('&', subtitle)
						+ "\"}");
		this.subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, comp);
		return this;
	}

	public Titles times(int fadeIn, int displayTime, int fadeOut) {
		times = new PacketPlayOutTitle(fadeIn, displayTime, fadeOut);
		return this;
	}

	public void send() {
		if (title != null) {
			con.sendPacket(title);
			if (subtitle != null)
				con.sendPacket(subtitle);
			if (times != null)
				con.sendPacket(subtitle);
		}
	}
}
