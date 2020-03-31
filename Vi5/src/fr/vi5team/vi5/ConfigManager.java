package fr.vi5team.vi5;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
	public void renameMapConfig(String mapname, String newName){
		File oldf = new File(mapFolder,mapname+".yml");
		if (oldf.exists()) {
			File newf = new File(mapFolder,newName+".yml");
			oldf.renameTo(newf);
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+mapname+ChatColor.BLUE+" has been renamed to: "+ChatColor.GREEN+newName);
		}
		else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Impossible d'enregistrer les modifications faites sur la map "+ChatColor.AQUA+mapname+ChatColor.RED+" car elle n'existe pas.");
		}
	}
	
}
