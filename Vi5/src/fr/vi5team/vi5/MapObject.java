package fr.vi5team.vi5;


import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class MapObject implements Listener {
	private Player player;
	private String object_name;
	private ConfigManager ConfigManager = new ConfigManager(null);
	
	public List<Object> playerInObjectRange(String mapname) {
		YamlConfiguration mapconfig = ConfigManager.getMapConfig(mapname);
		if(player.getLocation() != null) {
			return Arrays.asList(player, mapname, object_name);
		}
		else {
			return null;
		}
	}
	public void pointCapture() {	
		
		}
}
