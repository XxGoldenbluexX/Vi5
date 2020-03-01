package fr.vi5team.vi5;

import org.bukkit.plugin.java.JavaPlugin;

import fr.vi5team.vi5.commands.Vi5BaseCommand;

public class Vi5Main extends JavaPlugin {
	
	private ConfigManager cfgmanager;
	
	@Override
	public void onEnable() {
		super.onEnable();
		cfgmanager = new ConfigManager(this);
		getCommand("vi5").setExecutor(new Vi5BaseCommand());
	}
	public ConfigManager getCfgmanager() {
		return cfgmanager;
	}
}
