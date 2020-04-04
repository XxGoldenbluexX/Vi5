package fr.vi5team.vi5;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

	private Vi5Main mainref;
	private File mapFolder;
	
	public ConfigManager(Vi5Main main) {
		mainref=main;
		if (!mainref.getDataFolder().exists()) {
			mainref.getDataFolder().mkdir();
		}
		File mapf = new File(mainref.getDataFolder(),"Maps");
		if (!mapf.exists()){
				mapf.mkdir();
		}
		mapFolder=mapf;
	}
	
	public YamlConfiguration getMapConfig(String mapname) {
		File f = new File(mapFolder,mapname+".yml");
		if (f.exists()) {
			YamlConfiguration config = new YamlConfiguration();
			try {
				config.load(f);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Impossible de charger la map "+ChatColor.AQUA+mapname);
				return null;
			}
			return config;
		}
		return null;
	}
	public ArrayList<String> getMapList() {
		String[] list = mapFolder.list();
		ArrayList<String> finalList=new ArrayList<String>();
		for (String s : list) {
			if (s.contains(".yml")) {
				finalList.add(s.replace(".yml", ""));
			}
		}
		return finalList;
	}
	
	public boolean saveMapConfig(String mapname, YamlConfiguration config) {
		File f = new File(mapFolder,mapname+".yml");
		if (!f.exists()) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Impossible d'enregistrer les modifications faites sur la map "+ChatColor.AQUA+mapname+ChatColor.RED+" car elle n'existe pas.");
			return false;
		}else {
			try {
				config.save(f);
			} catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Impossible d'enregistrer les modifications faites sur la map "+ChatColor.AQUA+mapname);
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}
	public YamlConfiguration createMapConfig(String mapname) {
		File f = new File(mapFolder,mapname+".yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Impossible de créer la map "+ChatColor.AQUA+mapname);
				e.printStackTrace();
				return null;
			}
		}
		return getMapConfig(mapname);
	}
	public boolean deleteMapConfig(String mapname){
		File f = new File(mapFolder,mapname+".yml");
		if(f.exists()) {
			f.delete();
			return true;
		}else {
			return false;
		}
	}
	public boolean renameMapConfig(String mapname, String newName){
		File oldf = new File(mapFolder,mapname+".yml");
		if (oldf.exists()) {
			File newf = new File(mapFolder,newName+".yml");
			if (oldf.renameTo(newf)) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+mapname+ChatColor.BLUE+" has been renamed to: "+ChatColor.GREEN+newName);
				return true;
			}else {
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+mapname+ChatColor.BLUE+" was unable to be renamed into: "+ChatColor.GREEN+newName);
				return false;
			}
		}
		else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"La map "+ChatColor.AQUA+mapname+ChatColor.RED+" n'existe pas.");
			return false;
		}
	}
	public List<String> getObjectNamesList(String mapName){
		YamlConfiguration cfg = getMapConfig(mapName);
		List<String> list = null;
		if (cfg!=null) {
			list = cfg.getStringList(mapName+".objectList");
		}
		return list;
	}
	public boolean setObjectNamesList(String mapName,List<String> list) {
		YamlConfiguration cfg = getMapConfig(mapName);
		if (cfg!=null) {
			cfg.set(mapName+".objectList", list);
			saveMapConfig(mapName, cfg);
			return true;
		}
		return false;
	}
	public List<String> getMapEscapesList(String mapName){
		YamlConfiguration cfg = getMapConfig(mapName);
		List<String> list = null;
		if (cfg!=null) {
			list = cfg.getStringList(mapName+".escapeList");
		}
		return list;
	}
	public boolean setMapEscapesList(String mapName,List<String> list) {
		YamlConfiguration cfg = getMapConfig(mapName);
		if (cfg!=null) {
			cfg.set(mapName+".escapeList", list);
			saveMapConfig(mapName, cfg);
			return true;
		}
		return false;
	}
	public List<String> getMapEntrancesList(String mapName){
		YamlConfiguration cfg = getMapConfig(mapName);
		List<String> list = null;
		if (cfg!=null) {
			list = cfg.getStringList(mapName+".entranceList");
		}
		return list;
	}
	public boolean setMapEntrancesList(String mapName,List<String> list) {
		YamlConfiguration cfg = getMapConfig(mapName);
		if (cfg!=null) {
			cfg.set(mapName+".entranceList", list);
			saveMapConfig(mapName, cfg);
			return true;
		}
		return false;
	}
}
