package we.Heiden.gca.Stores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import we.Heiden.gca.Configs.PlayerConfig;
import we.Heiden.gca.Messages.JsonMessage;
import we.Heiden.gca.Messages.JsonMessage.ClickAction;
import we.Heiden.gca.Messages.JsonMessage.HoverAction;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Pets.Pets;
import we.Heiden.hs2.SQL.Operations;

public class PetStore implements BasicStore {
	
	public static HashMap<Player, Pets> pets = new HashMap<Player, Pets>();

	public String welcome() { return ChatColor.translateAlternateColorCodes('&', "&aPet Clerk &d-> &eWelcome!"); }
	
	public void options(Player p) {
		JsonMessage.sendJson(p, 
				JsonMessage.newJson("&6&l============================="), 
				JsonMessage.newJson()
					.add("          &9&l*").build()
					.add("&a&lDoggy Friend")
						.hoverEvent(HoverAction.Show_Entity, JsonMessage.newEntity("&bJust a Dog", EntityType.WOLF))
						.clickEvent(ClickAction.Run_Command, "/Store Pet Wolf").build()
					.add(" &d&l- &f&o500 Coins").build().build(), 
				JsonMessage.newJson()
					.add("          &9&l*").build()
					.add("&a&lNian Style")
						.hoverEvent(HoverAction.Show_Entity, JsonMessage.newEntity("&d&oMewTwo", EntityType.OCELOT))
						.clickEvent(ClickAction.Run_Command, "/Store Pet Cat").build()
						.add(" &d&l- &f&o750 Coins").build().build(), 
				JsonMessage.newJson("&6&l============================="));
	}
	
	public static void Buy(Player p, Pets pet, int cost) {
		FileConfiguration fc = PlayerConfig.load(p);
		Messager.load(p);
		if (!fc.contains("Pets")) fc.set("Pets", new ArrayList<String>());
		List<String> ls = fc.getStringList("Pets");
		if (ls.contains(pet.getType())) Messager.e1("You already have that pet");
		else {
			int money = Operations.getMoney(p);
			if (money < cost) Messager.e1("You can`t afford that");
			else {
				pets.put(p, pet);
				confirmation(p);
			}
		}
	}
	
	public static void confirmation(Player p) {
		JsonMessage.sendJson(p, 
				JsonMessage.newJson("&9&l========== &bAre you sure? &9&l=========="), 
				JsonMessage.newJson()
					.add("   &2--> ").build()
					.add("&a&lYES").clickEvent(ClickAction.Run_Command, "/Store Pet Confirm").build()
					.add(" &2<--  &4--> ").build()
					.add("&c&lNO").clickEvent(ClickAction.Run_Command, "/Store Pet Deny").build()
					.add(" &4<--").build().build(), 
				JsonMessage.newJson("&9&l==================================="));
	}
	
	public static void confirm(Player p) {
		Messager.load(p);
		if (!pets.containsKey(p)) Messager.e1("You can`t do that again");
		else {
			Pets pet = pets.get(p);
			pets.remove(p);
			FileConfiguration fc = PlayerConfig.load(p);
			List<String> ls = fc.getStringList("Pets");
			ls.add(pet.getType());
			fc.set("Pets", ls);
			PlayerConfig.save();
			Messager.s1("Pet Successfully Bought");
		}
	}
}
