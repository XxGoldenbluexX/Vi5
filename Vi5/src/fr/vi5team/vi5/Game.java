package fr.vi5team.vi5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
import org.bukkit.scoreboard.Team;

import fr.vi5team.vi5.enums.Vi5Team;
import fr.vi5team.vi5.enums.VoleurStatus;

public class Game implements Listener {
	
	private Vi5Main mainref;
	private String name="Vi5Game";
	private String mapname="SolarIndustries";
	private ConfigManager cfgManager;
	private boolean started=false;
	private short nbVoleurAlive=0;
	private short totalObjVol�s=0;
	private ArrayList<Location> gardeSpawns=new ArrayList<Location>();
	private ArrayList<Location> voleurMinimapSpawns=new ArrayList<Location>();
	private ArrayList<MapObject> mapObjects=new ArrayList<MapObject>();
	private BukkitRunnable gameTick;
	
	HashMap<Player,PlayerWrapper> playersInGame = new HashMap<Player,PlayerWrapper>();//Liste des joueurs pr�sents dans la partie et de leur wrapper
	
	public Game(Vi5Main main,ConfigManager cfgm) {
		mainref=main;
		cfgManager=cfgm;
	}
	public void endGame() {
		started=false;
		gameTick.cancel();
		for (Player p : playersInGame.keySet()) {
			p.setGameMode(GameMode.SPECTATOR);
		}
		messagePlayersInGame(ChatColor.GOLD+"La partie est termin�e!");
		messagePlayersInGame(ChatColor.GOLD+"Les voleurs se sont enfuit avec "+ChatColor.AQUA+totalObjVol�s);
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
				messagePlayersInGame(ChatColor.GOLD+player.getName()+ChatColor.DARK_GREEN+" joined this vi5");
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
				totalObjVol�s+=wrap.getNbItemStealed();
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
			p.sendMessage(ChatColor.AQUA+"["+name+"]"+message);
		}
	}
	public void messageTeam(Vi5Team team ,String message) {
		for (Player p : playersInGame.keySet()) {
			if(mainref.getPlayerWrapper(p).getTeam()==team) {
				p.sendMessage(ChatColor.AQUA+"["+name+"]"+message);
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
		//lancement de la partie, que les joueurs soient pr�ts ou non;
		if (!loadMap(mapname)) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"["+name+"]"+ChatColor.DARK_RED+"Impossible de charger la map, la partie ne peut se lancer");
			return;
		}else {
			//TODO lancer la game
			for (Player p : playersInGame.keySet()) {
				PlayerWrapper wrap = playersInGame.get(p);
				if (wrap.getTeam()==Vi5Team.GARDE) {
					int random =new  Random().nextInt(((gardeSpawns.size()-0) + 1) + 0);
					if (random>gardeSpawns.size()) {
						random=gardeSpawns.size();
					}else if (random<0) {
						random=0;
					}
					p.teleport(gardeSpawns.get(random));
				}else if (wrap.getTeam()==Vi5Team.VOLEUR) {
					int random =new  Random().nextInt(((voleurMinimapSpawns.size()-0) + 1) + 0);
					p.teleport(voleurMinimapSpawns.get(random));
					hideVoleur(p);
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
	
	public boolean loadMap(String map_name) {
		YamlConfiguration mapcfg = cfgManager.getMapConfig(map_name);
		if (mapcfg.equals(null)){
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> impossible de charger le fichier config pour "+ChatColor.ITALIC+map_name);
			return false;
		}
		Location loc;//variable utilis�e pour les maneuvres
		int i=0;
		//r�colter les spawns des gardes
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
		//r�colter les spawns a la minimap des voleurs
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
			String _name=mapcfg.getString("mapObjects."+i+"name","DefaultObject");
			Location _position = mapcfg.getLocation("mapObjects."+i+"location");
			Location _blockPosition = mapcfg.getLocation("mapObjects."+i+"blockPosition");
			BlockData _blockData = Bukkit.createBlockData(mapcfg.getString("mapObjects."+i+"blockData")); //le blockdata est enregistr� sous forme de string dans le config puis transform� en blockdata ici
			Material _blockType = Material.valueOf(mapcfg.getString("mapObjects."+i+"blockType"));
			int sizex = mapcfg.getInt("mapObjects."+i+"sizeX",-1);
			int sizey = mapcfg.getInt("mapObjects."+i+"sizeY",-1);
			int sizez = mapcfg.getInt("mapObjects."+i+"sizeZ",-1);
			if (_name.equals(null)) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> mapObjects."+i+".name n'a pas de valeur valide pour la map "+ChatColor.ITALIC+map_name);
				return false;
			}
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
			mapObjects.add(new MapObject(this,_name, _position, _blockPosition, _blockData, _blockType, sizex, sizey, sizez));
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
	public short getTotalObjVol�s() {
		return totalObjVol�s;
	}
	public void setTotalObjVol�s(short totalObjVol�s) {
		this.totalObjVol�s = totalObjVol�s;
	}
}
