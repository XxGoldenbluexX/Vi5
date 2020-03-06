package fr.vi5team.vi5;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.vi5team.vi5.commands.Vi5BaseCommand;

public class Vi5Main extends JavaPlugin {
	
	private ConfigManager cfgmanager;
	private ArrayList<Game> gamesList= new ArrayList<Game>();
	
	@Override
	public void onEnable() {
		super.onEnable();
		cfgmanager = new ConfigManager(this);
		getCommand("vi5").setExecutor(new Vi5BaseCommand());
	}
	
	public ConfigManager getCfgmanager() {
		return cfgmanager;
	}
	
	public ArrayList<Game> getGamesList() {
		return gamesList;
	}
	
	public boolean isPlayerIngame(Player player) {
		boolean a=false;
		for (Game g : gamesList) {
			if (g.hasPlayer(player)){
				a=true;
			}
		}
		return a;
	}
}
