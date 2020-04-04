package fr.vi5team.vi5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.vi5team.vi5.enums.Vi5Team;
import fr.vi5team.vi5.enums.VoleurStatus;

public class Game implements Listener {
	
	private Vi5Main mainref;
	private String name="Vi5Game";
	private String mapname="SolarIndustries";
	private ConfigManager cfgManager;
	private boolean started=false;
	private short nbVoleurAlive=0;
	private short totalObjVolés=0;
	private Location gardeSpawn;
	private Location voleurMinimapSpawn;
	private ArrayList<MapObject> mapObjects=new ArrayList<MapObject>();
	private ArrayList<MapEnterZone> mapEnterZones=new ArrayList<MapEnterZone>();
	private ArrayList<MapLeaveZone> mapLeaveZones=new ArrayList<MapLeaveZone>();
	private BukkitRunnable gameTick;
	
	HashMap<Player,PlayerWrapper> playersInGame = new HashMap<Player,PlayerWrapper>();//Liste des joueurs présents dans la partie et de leur wrapper
	
	public Game(Vi5Main main,ConfigManager cfgm,String _name) {
		mainref=main;
		cfgManager=cfgm;
		name=_name;
	}
	public void endGame() {
		started=false;
		gameTick.cancel();
		for (Player p : playersInGame.keySet()) {
			p.setGameMode(GameMode.SPECTATOR);
		}
		messagePlayersInGame(ChatColor.GOLD+"La partie est terminée!");
		messagePlayersInGame(ChatColor.GOLD+"Les voleurs se sont enfuit avec "+ChatColor.AQUA+totalObjVolés);
	}
	
	public boolean addPlayer(Player player) {
		if (playersInGame.containsKey(player)) {
			return false;
		}else{
			if (mainref.isPlayerIngame(player)) {
				return false;
			}else {
				PlayerWrapper wrap = new PlayerWrapper(this,player);
				playersInGame.put(player, wrap);
				messagePlayersInGame(ChatColor.GOLD+player.getName()+ChatColor.DARK_GREEN+" joined this vi5 game!");
				return true;
			}
		}
	}
	
	public PlayerWrapper getPlayerWrapper(Player player) {
		return playersInGame.get(player);
	}
	public void playerLeaveMap(Player player) {
		if (hasPlayer(player)) {
			PlayerWrapper wrap = playersInGame.get(player);
			if (wrap.getTeam()==Vi5Team.VOLEUR && wrap.getCurrentStatus()==VoleurStatus.INSIDE) {
				messagePlayersInGame(ChatColor.RED+player.getName()+ChatColor.GOLD+" leaved the place with "+ChatColor.AQUA+wrap.getNbItemStealed()+ChatColor.GOLD+" objects!");
				player.setGameMode(GameMode.SPECTATOR);
				totalObjVolés+=wrap.getNbItemStealed();
				nbVoleurAlive--;
				if (nbVoleurAlive<=0) {
					endGame();
				}
			}
		}
	}
	public void playerEnterMap(Player player) {
		if (hasPlayer(player)) {
			PlayerWrapper wrap = playersInGame.get(player);
			if (wrap.getTeam()==Vi5Team.VOLEUR && wrap.getCurrentStatus()==VoleurStatus.OUTSIDE) {
				showPlayer(player);
				player.sendMessage(ChatColor.GOLD+"You entered the complex");
			}
		}
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		PlayerWrapper wrap = playersInGame.get(player);
		if(wrap.getTeam()==Vi5Team.VOLEUR) {
			messagePlayersInGame(ChatColor.RED+player.getName()+" died with "+ChatColor.GREEN+wrap.getNbItemStealed()+ChatColor.GOLD+" object(s)!");
			player.setGameMode(GameMode.SPECTATOR);
			nbVoleurAlive--;
			if (nbVoleurAlive<=0) {
				endGame();
			}
		}
	}
	
	public boolean removePlayer(Player player) {
		if (playersInGame.containsKey(player)) {
			if (started && playersInGame.get(player).getTeam()==Vi5Team.VOLEUR) {
				nbVoleurAlive--;
				if (nbVoleurAlive<=0) {
					endGame();
				}
			}
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
			p.sendMessage(ChatColor.AQUA+"["+name+"] "+message);
		}
	}
	public void messageTeam(Vi5Team team ,String message) {
		for (Player p : playersInGame.keySet()) {
			if(mainref.getPlayerWrapper(p).getTeam()==team) {
				p.sendMessage(ChatColor.AQUA+"["+name+"] "+message);
			}
		}
	}
	
	public void titleTeam(Vi5Team team ,String title,String subtitle,int fadein,int stay,int fadeout) {
		for (Player p : playersInGame.keySet()) {
			if(mainref.getPlayerWrapper(p).getTeam()==team) {
				p.sendTitle(title, subtitle, fadein, stay, fadeout);
			}
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
	public void tickMapObjects() {
		for (MapObject obj : mapObjects) {
			obj.Tick();
		}
	}
	
	public void start() {
		//lancement de la partie, que les joueurs soient prêts ou non;
		if (!loadMap(mapname)) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"["+name+"]"+ChatColor.DARK_RED+"Impossible de charger la map, la partie ne peut se lancer");
			return;
		}else {
			for (Player p : playersInGame.keySet()) {
				PlayerWrapper wrap = playersInGame.get(p);
				if (wrap.getTeam()==Vi5Team.GARDE) {
					p.teleport(gardeSpawn);
				}else if (wrap.getTeam()==Vi5Team.VOLEUR) {
					p.teleport(voleurMinimapSpawn);
					hideVoleur(p);
					wrap.setCurrentStatus(VoleurStatus.OUTSIDE);
				}
			}
			//creer un runnable qui tick les MapObjects et autres
			gameTick=new BukkitRunnable() {
				@Override
				public void run() {
					tickMapObjects();
				}
			};
			gameTick.runTaskTimer(mainref, 0, 1);
		}
	};
	public void hideVoleur(Player player) {
		//cache un voleur a tout les gardes
		if (hasPlayer(player)) {
			PlayerWrapper wrap = getPlayerWrapper(player);
			if (wrap.getTeam()==Vi5Team.VOLEUR) {
				for (Player p : playersInGame.keySet()) {
					PlayerWrapper w = getPlayerWrapper(p);
					if (w.getTeam()==Vi5Team.GARDE) {
						p.hidePlayer(mainref, player);
					}
				}
			}
		}
	}
	public void showPlayer(Player player) {
		//remet un joueur visible pour tout les joueurs
		for (Player p : playersInGame.keySet()) {
			p.showPlayer(mainref, player);
		}
	}
	public void launchCaptureDelay() {
		for (MapObject o : mapObjects) {
			o.setCaptureCooldown(true);
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				for (MapObject o : mapObjects) {
					o.setCaptureCooldown(false);
				}
			}
		}.runTaskLater(mainref, 600);
	}
	
	public boolean loadMap(String map_name) {
		YamlConfiguration mapcfg = cfgManager.getMapConfig(map_name);
		if (mapcfg==null){
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> impossible de charger le fichier config pour "+ChatColor.ITALIC+map_name);
			return false;
		}
		int i=0;//variable utilisée pour les maneuvres
		//récolter le spawn des gardes
		gardeSpawn = mapcfg.getLocation("gardeSpawn");
		if (gardeSpawn==null) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> gardeSpawn n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
			return false;
		}
		//récolter les spawns a la minimap des voleurs
		voleurMinimapSpawn = mapcfg.getLocation("voleurMinimapSpawn");
		if (voleurMinimapSpawn==null) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> voleurMinimapSpawn n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
			return false;
		}
		//recolter les maps objects
		mapObjects.clear();
		List<String> objList = mapcfg.getStringList(map_name+".objectList");
		for (String objname : objList) {
			boolean valid=true;
			Location _position = mapcfg.getLocation("mapObjects."+objname+"centerLocation");
			Location _blockPosition = mapcfg.getLocation("mapObjects."+objname+"blockLocation");
			BlockData _blockData = Bukkit.createBlockData(mapcfg.getString("mapObjects."+objname+"blockData")); //le blockdata est enregistré sous forme de string dans le config puis transformé en blockdata ici
			Material _blockType = Material.valueOf(mapcfg.getString("mapObjects."+objname+"blockType"));
			int sizex = mapcfg.getInt("mapObjects."+i+"sizex",-1);
			int sizey = mapcfg.getInt("mapObjects."+i+"sizey",-1);
			int sizez = mapcfg.getInt("mapObjects."+i+"sizez",-1);
			if (_position==null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapObjects."+objname+".centerLocation n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				valid=false;
			}
			if (_blockPosition==null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapObjects."+objname+".blockLocation n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				valid=false;
			}
			if (_blockData==null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapObjects."+objname+".blockData n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				valid=false;
			}
			if (_blockType==null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapObjects."+objname+".blockType n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				valid=false;
			}
			if (sizex<=0) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapObjects."+objname+".sizex n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				valid=false;
			}
			if (sizey<=0) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapObjects."+objname+".sizey n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				valid=false;
			}
			if (sizez<=0) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapObjects."+objname+".sizez n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				valid=false;
			}
			if (valid) {
				mapObjects.add(new MapObject(this,objname, _position, _blockPosition, _blockData, _blockType, sizex, sizey, sizez));
			}
		}
		//get escapes
		mapLeaveZones.clear();
		objList = mapcfg.getStringList(map_name+".escapeList");
		for (String objname : objList) {
			boolean valid=true;
			Location loc = mapcfg.getLocation("mapEscapes."+objname+"centerLocation");
			int sizex = mapcfg.getInt("mapEscapes."+objname+"sizex",-1);
			int sizey = mapcfg.getInt("mapEscapes."+objname+"sizey",-1);
			int sizez = mapcfg.getInt("mapEscapes."+objname+"sizez",-1);
			if (loc==null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapEscapes."+objname+".centerLocation n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				valid=false;
			}
			if (sizex<=0) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapEscapes."+objname+".sizex n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				valid=false;
			}
			if (sizey<=0) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapEscapes."+objname+".sizey n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				valid=false;
			}
			if (sizez<=0) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapEscapes."+objname+".sizez n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				valid=false;
			}
			if (valid) {
				mapLeaveZones.add(new MapLeaveZone(this,loc,new Vector(sizex,sizey,sizez)));
			}
		}
		//get entrance
			mapEnterZones.clear();
			objList = mapcfg.getStringList(map_name+".escapeList");
			for (String objname : objList) {
				boolean valid=true;
				Location loc = mapcfg.getLocation("mapEntrances."+objname+"centerLocation");
				int sizex = mapcfg.getInt("mapEntrances."+objname+"sizex",-1);
				int sizey = mapcfg.getInt("mapEntrances."+objname+"sizey",-1);
				int sizez = mapcfg.getInt("mapEntrances."+objname+"sizez",-1);
				if (loc==null) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapEntrances."+objname+".centerLocation n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
					valid=false;
				}
				if (sizex<=0) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapEntrances."+objname+".sizex n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
					valid=false;
				}
				if (sizey<=0) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapEntrances."+objname+".sizey n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
					valid=false;
				}
				if (sizez<=0) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"mapEntrances."+objname+".sizez n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
					valid=false;
				}
				if (valid) {
					mapEnterZones.add(new MapEnterZone(this,loc,new Vector(sizex,sizey,sizez)));
				}
			}
		return true;
	}

	public ArrayList<MapObject> getMapObjects() {
		return mapObjects;
	}

	public void setMapObjects(ArrayList<MapObject> mapObjects) {
		this.mapObjects = mapObjects;
	}
	public short getNbVoleurAlive() {
		return nbVoleurAlive;
	}
	public void setNbVoleurAlive(short nbVoleurAlive) {
		this.nbVoleurAlive = nbVoleurAlive;
	}
	public short getTotalObjVolés() {
		return totalObjVolés;
	}
	public void setTotalObjVolés(short totalObjVolés) {
		this.totalObjVolés = totalObjVolés;
	}
	public String getMapName() {
		return mapname;
	}
	public void setMapName(String mapname) {
		this.mapname = mapname;
	}
	public ArrayList<MapEnterZone> getMapEnterZones() {
		return mapEnterZones;
	}
	public void setMapEnterZones(ArrayList<MapEnterZone> mapEnterZones) {
		this.mapEnterZones = mapEnterZones;
	}
	public ArrayList<MapLeaveZone> getMapLeaveZones() {
		return mapLeaveZones;
	}
	public void setMapLeaveZones(ArrayList<MapLeaveZone> mapLeaveZones) {
		this.mapLeaveZones = mapLeaveZones;
	}
}
