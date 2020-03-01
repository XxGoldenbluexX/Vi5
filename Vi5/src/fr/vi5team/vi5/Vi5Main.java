package fr.vi5team.vi5;

import org.bukkit.plugin.java.JavaPlugin;

public class Vi5Main extends JavaPlugin {
	
	private ConfigManager cfgmanager;
	
	@Override
	public void onEnable() {
		cfgmanager = new ConfigManager(this);
		super.onEnable();
	}
	public ConfigManager getCfgmanager() {
		return cfgmanager;
	}
}
