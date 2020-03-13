package fr.vi5team.vi5;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Game implements Listener {
	
	private Vi5Main mainref;
	private String name="Vi5Game";
	private String mapname="SolarIndustries";
	private ConfigManager cfgManager;
	private boolean started=false;
	private ArrayList<Location> gardeSpawns=new ArrayList<Location>();
	private ArrayList<Location> voleurMinimapSpawns=new ArrayList<Location>();
	private ArrayList<MapObject> mapObjects=new ArrayList<MapObject>();
	
	HashMap<Player,PlayerWrapper> playersInGame = new HashMap<Player,PlayerWrapper>();//Liste des joueurs présents dans la partie et de leur wrapper
	
	public Game(Vi5Main main,ConfigManager cfgm) {
		mainref=main;
		cfgManager=cfgm;
	}
	
	public boolean addPlayer(Player player) {
		if (playersInGame.containsKey(player)) {
			return false;
		}else{
			if (mainref.isPlayerIngame(player)) {
				return false;
			}else {
				PlayerWrapper wrap = new PlayerWrapper();
				playersInGame.put(player, wrap);
				messagePlayersInGame(ChatColor.GOLD+player.getName()+ChatColor.DARK_GREEN+" joined this vi5");
				return true;
			}
		}
	}
	
	public PlayerWrapper getPlayerWrapper(Player player) {
		return playersInGame.get(player);
	}
	
	public boolean removePlayer(Player player) {
		if (playersInGame.containsKey(player)) {
			playersInGame.remove(player);
			messagePlayersInGame(ChatColor.GOLD+player.getName()+ChatColor.RED+" leaved this game");
			return false;
		}else {
			return false;
		}
	}
	public String getName() {
		return name;
	}
	
	public boolean hasPlayer(Player player) {
		return playersInGame.containsKey(player);
	}
	
	public void messagePlayersInGame(String message) {
		for (Player p : playersInGame.keySet()) {
			p.sendMessage(ChatColor.AQUA+"["+name+"]"+message);
		}
	}
	public void messageGardes(String message) {
		for (Player p : playersInGame.keySet()) {
			p.sendMessage(ChatColor.AQUA+"["+name+"]"+message);
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		removePlayer(player);
		return;
	}

	public ConfigManager getConfigManagerRef() {
		return cfgManager;
	}

	public void setConfigManagerRef(ConfigManager cfgManager) {
		this.cfgManager = cfgManager;
	}

	public boolean is_Started() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}
	
	public boolean is_playersReady() {
		boolean r=true;
		for (PlayerWrapper w : playersInGame.values()) {
			if (!w.is_ready()){
				r=false;
			}
		}
		return r;
	}
	public boolean is_playerReady(Player player) {
		if (playersInGame.containsKey(player)) {
			return playersInGame.get(player).is_ready();
		}else {
			return false;
		}
	}
	
	public boolean is_playerReady(PlayerWrapper wrap) {
		//cette methode est une surcharge de is_playerReady(Player player) mais pour un playerWrapper;
		//C'est donc possible d'avoir une fonction du meme nom, ca s'appel la surcharge;
		if (playersInGame.containsValue(wrap)) {
			return wrap.is_ready();
		}else {
			return false;
		}
	}
	
	public void start() {
		//lancement de la partie, que les joueurs soient prêts ou non;
		if (!loadMap(mapname)) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"["+name+"]"+ChatColor.DARK_RED+"Impossible de charger la map, la partie ne peut se lancer");
			return;
		}
	};
	
	public boolean loadMap(String map_name) {
		YamlConfiguration mapcfg = cfgManager.getMapConfig(map_name);
		if (mapcfg.equals(null)){
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> impossible de charger le fichier config pour "+ChatColor.ITALIC+map_name);
			return false;
		}
		Location loc;//variable utilisée pour les maneuvres
		int i=0;
		//récolter les spawns des gardes
		gardeSpawns.clear();
		int nbValues = mapcfg.getInt("gardeSpawns.number",-1);
		if (nbValues==-1) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> gardeSpawns.number n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
			return false;
		}
		for (i=0;i<nbValues;i++) {
			loc = mapcfg.getLocation("gardeSpawns."+i);
			if (!loc.equals(null)) {
				gardeSpawns.add(loc);
			}
		}
		//récolter les spawns a la minimap des voleurs
		voleurMinimapSpawns.clear();
		nbValues=-1;
		nbValues = mapcfg.getInt("voleurMinimapSpawns.number",-1);
		if (nbValues==-1) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> voleurMinimapSpawns.number n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
			return false;
		}
		for (i=0;i<nbValues;i++) {
			loc = mapcfg.getLocation("voleurMinimapSpawns."+i);
			if (!loc.equals(null)) {
				voleurMinimapSpawns.add(loc);
			}
		}
		//recolter les maps objects
		mapObjects.clear();
		nbValues=-1;
		nbValues = mapcfg.getInt("mapObjects.number",-1);
		if (nbValues==-1) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> mapObjects.number n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
			return false;
		}
		for (i=0;i<nbValues;i++) {
			Location _position = mapcfg.getLocation("mapObjects."+i+"location");
			Location _blockPosition = mapcfg.getLocation("mapObjects."+i+"blockPosition");
			BlockData _blockData = Bukkit.createBlockData(mapcfg.getString("mapObjects."+i+"blockData")); //le blockdata est enregistré sous forme de string dans le config puis transformé en blockdata ici
			Material _blockType = Material.valueOf(mapcfg.getString("mapObjects."+i+"blockType"));
			int sizex = mapcfg.getInt("mapObjects."+i+"sizeX",-1);
			int sizey = mapcfg.getInt("mapObjects."+i+"sizeY",-1);
			int sizez = mapcfg.getInt("mapObjects."+i+"sizeZ",-1);
			if (_position.equals(null)) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> mapObjects."+i+".location n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				return false;
			}
			if (_blockPosition.equals(null)) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> mapObjects."+i+".blockPosition n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				return false;
			}
			if (_blockData.equals(null)) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> mapObjects."+i+".blockData n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				return false;
			}
			if (_blockType.equals(null)) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> mapObjects."+i+".blockType n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				return false;
			}
			if (sizex==-1) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> mapObjects."+i+".sizeX n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				return false;
			}
			if (sizey==-1) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> mapObjects."+i+".sizeY n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				return false;
			}
			if (sizez==-1) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> mapObjects."+i+".sizeZ n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				return false;
			}
			mapObjects.add(new MapObject(this, _position, _blockPosition, _blockData, _blockType, sizex, sizey, sizez));
		}
		 //Location _position, Location _blockPosition,BlockData _blockData,Material _blocType,short sizex,short sizey,short sizez
		return false;
	}

	public ArrayList<MapObject> getMapObjects() {
		return mapObjects;
	}

	public void setMapObjects(ArrayList<MapObject> mapObjects) {
		this.mapObjects = mapObjects;
	}
}
