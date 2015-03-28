package we.Heiden.gca.Stores;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import we.Heiden.gca.Messages.JsonMessage;
import we.Heiden.gca.Messages.JsonMessage.ClickAction;
import we.Heiden.gca.Messages.JsonMessage.HoverAction;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.hs2.Configs.PlayerConfig;
import we.Heiden.hs2.Messages.Chat;
import we.Heiden.hs2.SQL.Operations;

public class BankStore implements BasicStore {

	public String welcome() { return ChatColor.translateAlternateColorCodes('&', "&aDealership Owner &d-> &eWelcome!"); }
	
	public void options(Player p) {
		int bankMoney = 0;
		if (PlayerConfig.get(p).contains("Bank")) bankMoney = PlayerConfig.get().getInt("Bank");
		new Chat(p).msg("&b&lBank Money: &d" + bankMoney + " coins");
		JsonMessage.sendJson(p, 
				JsonMessage.newJson()
					.add("          ").build()
					.add("&aDeposit")
						.hoverEvent(HoverAction.Show_Text, "&2&oDeposit money")
						.clickEvent(ClickAction.Run_Command, "/Store Bank Deposit").build()
					.add("&c/&bWithdraw")
						.hoverEvent(HoverAction.Show_Text, "&9&oWithdraw money")
						.clickEvent(ClickAction.Run_Command, "/Store Bank Withdraw").build().build(), 
				JsonMessage.newJson()
					.add("     &6&l··&b&l> ").build()
					.add("&c&oRobbery Event")
						.hoverEvent(HoverAction.Show_Text, (Functions.canJoinRobbery() ? "&d&l•• &6&l►► &a&lEVENT OPEN &6&l◄◄ &d&l••" : "&cYou can join from 8:00 PM to 8:05 PM"))
						.clickEvent(ClickAction.Run_Command, "/Store Robbery Bank").build()
						.add(" &b&l<&6&l··").build().build());
	}
	
	public static void deposit(Player p, int amount) {
		int bankMoney = 0;
		if (PlayerConfig.get(p).contains("Bank")) bankMoney = PlayerConfig.get().getInt("Bank");
		bankMoney += amount;
		PlayerConfig.get().set("Bank", bankMoney);
		PlayerConfig.save();
		Operations.setMoney(p.getUniqueId(), Operations.getMoney(p)-amount);
		new Chat(p).msg("&a&l&oSuccess!");
	}
	
	public static void withdraw(Player p, int amount) {
		int bankMoney = 0;
		if (PlayerConfig.get(p).contains("Bank")) bankMoney = PlayerConfig.get().getInt("Bank");
		if (bankMoney < amount) new Chat(p).e("You don`t have enough Money");
		else {
			bankMoney -= amount;
			Operations.setMoney(p.getUniqueId(), Operations.getMoney(p)+amount);
			PlayerConfig.get().set("Bank", bankMoney);
			PlayerConfig.save();
			new Chat(p).msg("&a&l&oSuccess!");
		}
	}
	
	
}
