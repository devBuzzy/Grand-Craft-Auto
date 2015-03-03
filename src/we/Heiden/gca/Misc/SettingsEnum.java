package we.Heiden.gca.Misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;


public enum SettingsEnum {
	
	GUN_SHOT_PARTICLES("On", "On", "Off"),
	GUN_RECHARGE_EFFECT("Med", "High", "Med", "Off"),
	TITLES("On", "On", "Off"),
	HOTBAR("On", "On", "Off"),
	GUN_ZOOM("120%", "200%", "160%", "120%", "80%", "40%", "Off"),
	PET_VISIBILITY("On", "On", "Off"),
	EXTRA_PARTICLES("On", "On", "Off"),
	QUALITY("Med", "High", "Med", "Low");
	
	public static HashMap<Player, HashMap<SettingsEnum, String>> options = new HashMap<Player, HashMap<SettingsEnum, String>>();
	
	private String def;
	private static Player p = null;
	private List<String> values;
	
	private SettingsEnum(String def, String... values) {
		this.def = def;
		List<String> ls = new ArrayList<String>();
		for (String v : values) ls.add(v);
		this.values = ls;
	}
	public static void setPlayer(Player player) {p = player;}
	
	public String getValue() {return options.get(p).get(this);}
	
	public void setDefault() throws Exception {
		if (p == null) throw new NoSuchFieldException();
		else options.get(p).put(this, this.getDefault());
	}
	
	public void setValue(String value) throws IllegalArgumentException, NoSuchFieldException {
		if (!values.contains(value)) throw new IllegalArgumentException();
		else if (p == null) throw new NoSuchFieldException(); else options.get(p).put(this, value);
	}
	
	public static void register(Player p) {
		HashMap<SettingsEnum, String> hm = new HashMap<SettingsEnum, String>();
		for (SettingsEnum option : SettingsEnum.values()) hm.put(option, option.getDefault()); options.put(p, hm);
	}
	
	public void restore(HashMap<SettingsEnum, String> dates) throws NoSuchFieldException {
		if (p == null) throw new NoSuchFieldException();
		else options.put(p, dates);
	}
	
	public String getDefault() {return this.def;}
	
	public List<String> getValues() {return this.values;}
}
