package we.Heiden.gca.Messages;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import net.minecraft.server.v1_8_R1.PlayerConnection;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
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
public class JsonMessage {

	public static void sendJson(Player p, String... json) {
		PlayerConnection con = ((CraftPlayer)p).getHandle().playerConnection;
		PacketPlayOutChat packet;
		IChatBaseComponent comp;
		for (String s : json) {
			comp = ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', s));
			packet = new PacketPlayOutChat(comp);
			con.sendPacket(packet);
		}
	}
	
	public static String newItem(String... Lines) {
		String lores = "";
		if (Lines.length > 1) {
			boolean first = true;
			int n = 0;
			for (String line : Lines) {
				n++;
				if (n > 1) {
					if (first) first = false;
					else lores += ", ";
					if (line.contains("\"")) line = line.replace("\"", "'");
					lores += "\\\"" + line + "\\\"";
				}
			}
		}
		if (Lines.length == 0) Lines[0] = "";
		if (Lines[0].contains("\"")) Lines[0] = Lines[0].replace("\"", "'");
		return "{id:1, tag:{display:{Name:" + Lines[0] + ", Lore:[" + lores + "]}}}";
	}
	
	@SuppressWarnings("deprecation")
	public static String newEntity(String name, EntityType entity) {
		short id = entity.getTypeId();
		String type = entity.getName();
		return "{id:" + id + ",name:" + name + ",type:" + type + "}";
	}

	public static Json newJson() { return new Json(true); }
	public static String newJson(String text) { return new Json(true).add(text).build().build();}
	
	public static final class Json {
		
		public static String json;
		public static String end;
		public static boolean first;
		
		public Builder add() {return new Builder();}
		public Builder add(String text) {return new Builder().setText(text);}
		
		public Json(boolean bol) {
			if (bol) {
				first = true;
				json = "{text:\"\", extra:[";
				end = "]}";
			}
		}
		
		public String build() { return json + end; }
	}
	
	public static final class Builder {
		private String text = null;
		private ClickAction click = null;
		private String clickValue = null;
		private HoverAction hover = null;
		private String hoverValue = null;
		
		public Builder() { }
		
		public Builder setText(String text) { this.text = text; return this; }
		public Builder clickEvent(ClickAction action, String value) { click = action; clickValue = value; return this; }
		public Builder hoverEvent(HoverAction action, String value) { hover = action; hoverValue = value; return this; }
		
		public Json build() {
			String extra = "{text:\"" + text + "\"";
			
			if (click != null) extra += ", clickEvent:{action:" + click.getString() + ", value:\"" + clickValue + "\"}";
			if (hover != null) extra += ", hoverEvent:{action:" + hover.getString() + ", value:\"" + hoverValue + "\"}";
			
			extra += "}";
			
			if (Json.first) Json.first = false;
			else extra = ", " + extra;
			
			Json.json += extra;
			return new Json(false);
		}
	}
	
	public static enum ClickAction {
		Run_Command("run_command"), Suggest_Command("suggest_command"), Open_Url("open_url"), Change_Page("change_page");
		
		private String str;
		private ClickAction(String str) { this.str = str; }
		public String getString() { return str; }
	}
	
	public static enum HoverAction {
		Show_Text("show_text"), Show_Item("show_item"), Show_Entity("show_entity"), Show_Achievement("show_achievement");
		
		private String str;
		private HoverAction(String str) { this.str = str; }
		public String getString() { return str; }
	}
}