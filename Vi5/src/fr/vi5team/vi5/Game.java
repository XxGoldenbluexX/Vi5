package fr.vi5team.vi5;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.vi5team.vi5.enums.Vi5Team;
import fr.vi5team.vi5.enums.VoleurStatus;

public class Game implements Listener {
	
	private MapWall mapWall;
	private Vi5Main mainref;
	private World world;
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
	private final WeakHashMap<Player,PlayerWrapper> playersInGame = new WeakHashMap<Player,PlayerWrapper>();
	public WeakHashMap<String,ArrayList<Location>> wallsInMapLocationsList = new WeakHashMap<String,ArrayList<Location>>();
	public WeakHashMap<String,ArrayList<Double>> wallsInMapCenterList = new WeakHashMap<String,ArrayList<Double>>();
	public Game(Vi5Main main,ConfigManager cfgm,String _name, World _world) {
		mainref=main;
		cfgManager=cfgm;
		name=_name;
		world=_world;
	}
	/*@EventHandler
	public void onPlayerMoveItem(InventoryMoveItemEvent event) {
		if(is_Started()) {
			Inventory source = event.getSource();
			Inventory dest = event.getDestination();
			if(source!=null&&dest!=null) {
				if(source.getHolder()!=dest.getHolder()) {
					event.setCancelled(true);
				}
			}
		}
	}*/
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		Entity receiver = event.getEntity();
		if(damager.getType()==EntityType.PLAYER&&receiver.getType()==EntityType.PLAYER){
			Player pDamager=(Player)damager;
			Player pReceiver = (Player)receiver;
			PlayerWrapper damagerWrap = mainref.getPlayerWrapper(pDamager);
			PlayerWrapper receiverWrap = mainref.getPlayerWrapper(pReceiver);
			if(damagerWrap.getTeam()==receiverWrap.getTeam()) {
				event.setDamage(0);
			}
		}
	}
	public MapWall getMapWall() {
		return mapWall;
	}
	public void endGame() {
		if (mapWall!=null) {
			mapWall.removeAllWalls();
			HandlerList.unregisterAll(mapWall);
			mapWall=null;
		}
		started=false;
		gameTick.cancel();
		for (MapObject o : mapObjects) {
			o.unregisterEvents();
		}
		mapObjects.clear();
		for (MapEnterZone o : mapEnterZones) {
			o.unregisterEvents();
		}
		mapEnterZones.clear();
		for (MapLeaveZone o : mapLeaveZones) {
			o.unregisterEvents();
		}
		mapLeaveZones.clear();
		for (Player p : playersInGame.keySet()) {
			PlayerWrapper wrap = playersInGame.get(p);
			p.getInventory().clear();
			p.getActivePotionEffects().clear();
			wrap.gameEnd();
			for (AttributeModifier m : p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers()) {
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(m);
			}
			p.setGameMode(GameMode.SPECTATOR);
			p.setAllowFlight(true);
			wrap.setReady(false);
			wrap.showMenuHotbar();
			p.getActivePotionEffects().clear();
		}
		messagePlayersInGame(ChatColor.GOLD+"La partie est terminée!");
		messagePlayersInGame(ChatColor.GOLD+"Les voleurs se sont enfuit avec "+ChatColor.AQUA+totalObjVolés+ChatColor.GOLD+" objects!");
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
				mainref.getPmanager().registerEvents(wrap, mainref);
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
				wrap.setCurrentStatus(VoleurStatus.ESCAPED);
				totalObjVolés+=wrap.getNbItemStealed();
				nbVoleurAlive--;
				if (nbVoleurAlive<=0) {
					System.out.println("leaved");
					endGame();
				}
			}
		}
	}
	public void playerEnterMap(Player player) {
		if (hasPlayer(player)) {
			PlayerWrapper wrap = playersInGame.get(player);
			if (wrap.getTeam()==Vi5Team.VOLEUR && wrap.getCurrentStatus()==VoleurStatus.OUTSIDE) {
				wrap.setCurrentStatus(VoleurStatus.INSIDE);
				wrap.enterZone();
				player.sendMessage(ChatColor.GOLD+"You entered the complex");
				showPlayer(player);
				wrap.setUnSpottable(true);
				wrap.setLeaveCooldown(true);
				wrap.setEnterStealCooldown(true);
				new BukkitRunnable() {

					@Override
					public void run() {
						if (wrap.getCurrentStatus()==VoleurStatus.INSIDE) {
							wrap.setUnSpottable(false);
							player.sendMessage(ChatColor.LIGHT_PURPLE+""+ChatColor.UNDERLINE+"You are now spottable");
							wrap.setLeaveCooldown(false);
						}
					}
					
				}.runTaskLater(mainref, 200);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.sendMessage(ChatColor.GREEN+"You can now capture an object");
						wrap.setEnterStealCooldown(false);
					}
				}.runTaskLater(mainref, 600);
			}
		}
	}
	@EventHandler
	public void onPlayerChangeSlot(PlayerItemHeldEvent event) {
        Player p = event.getPlayer();
        if(!is_Started() && hasPlayer(p)) {
                mainref.getPlayerWrapper(p).showMenuHotbar();
        }
    }
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (hasPlayer(player) && started) {
			PlayerWrapper wrap = playersInGame.get(player);
			if(wrap.getTeam()==Vi5Team.VOLEUR) {
				messagePlayersInGame(ChatColor.RED+player.getName()+" died with "+ChatColor.GREEN+wrap.getNbItemStealed()+ChatColor.GOLD+" object(s)!");
				player.setGameMode(GameMode.SPECTATOR);
				wrap.setCurrentStatus(VoleurStatus.ESCAPED);
				nbVoleurAlive--;
				if (nbVoleurAlive<=0) {
					endGame();
				}
			}else if (wrap.getTeam()==Vi5Team.GARDE) {
				player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
				player.teleport(gardeSpawn);
			}
		}
	}
	
	public boolean removePlayer(Player player) {
		if (playersInGame.containsKey(player)) {
			if (started && playersInGame.get(player).getTeam()==Vi5Team.VOLEUR && playersInGame.get(player).getCurrentStatus()!=VoleurStatus.ESCAPED) {
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
		player.setPlayerListName(player.getName());
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
	public void start(boolean forced,CommandSender sender) {
		//lancement de la partie, que les joueurs soient prêts ou non;
		if (!forced) {
			if (!is_playersReady()) {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.RED+"Everyone is not ready!");
				sender.sendMessage("");
				return;
			}
		}
		if (!loadMap(mapname)) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"["+name+"]"+ChatColor.DARK_RED+"Impossible de charger la map, la partie ne peut se lancer");
			sender.sendMessage(ChatColor.AQUA+"["+name+"]"+ChatColor.DARK_RED+"Impossible de charger la map, la partie ne peut se lancer");
			return;
		}else {
			totalObjVolés=0;
			nbVoleurAlive=0;
			started=true;
			mapWall = new MapWall(mainref,world, this.getMapName());
			mainref.getPmanager().registerEvents(mapWall, mainref);
			for (Player p : playersInGame.keySet()) {
				PlayerWrapper wrap = playersInGame.get(p);
				wrap.setReady(false);
				p.getInventory().clear();
				wrap.gameStart();
				wrap.setOmnispotted(false);
				wrap.setGlow(false);
				wrap.setDecouvert(false);
				wrap.setUnGlowable(false);
				wrap.setInvisible(false);
				wrap.setInsondable(false);
				wrap.setJammed(false);
				wrap.setUnSpottable(false);
				p.setAllowFlight(false);
				p.getActivePotionEffects().clear();
				for (AttributeModifier m : p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers()) {
					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(m);
				}
				p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 10, false, false, true));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 10, false, false, false));
				if (wrap.getTeam()==Vi5Team.GARDE) {
					p.teleport(gardeSpawn);
					p.setGameMode(GameMode.ADVENTURE);
					wrap.setCurrentStatus(VoleurStatus.INSIDE);
					p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
				}else if (wrap.getTeam()==Vi5Team.VOLEUR) {
					wrap.setNbItemStealed((short) 0);
					p.teleport(voleurMinimapSpawn);
					hideVoleur(p);
					nbVoleurAlive++;
					p.setGameMode(GameMode.ADVENTURE);
					wrap.setCurrentStatus(VoleurStatus.OUTSIDE);
					wrap.setUnSpottable(true);
				}else if (wrap.getTeam()==Vi5Team.SPECTATEUR) {
					p.setGameMode(GameMode.SPECTATOR);
					p.teleport(voleurMinimapSpawn);
				}
			}
			System.out.println("nbVoleurAlive="+nbVoleurAlive);
			for (MapObject o : mapObjects) {
				o.setBlock();
			}
			messagePlayersInGame(ChatColor.GOLD+"Game start!");
			//creer un runnable qui tick les MapObjects et autres
			gameTick=new BukkitRunnable() {
				@Override
				public void run() {
					tickMapObjects();
					tickRunes();
				}
			};
			gameTick.runTaskTimer(mainref, 0, 1);
		}
	};
	public void tickRunes() {
		for (PlayerWrapper p : playersInGame.values()) {
			p.tickRunes();
		}
	}
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
				messageTeam(Vi5Team.VOLEUR, ChatColor.GREEN+""+ChatColor.UNDERLINE+"Thieves can now steal an object!");
			}
		}.runTaskLater(mainref, 600);
	}
	
	public boolean loadMap(String map_name) {
		YamlConfiguration mapcfg = cfgManager.getMapConfig(map_name);
		if (mapcfg==null){
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[configManager] -> impossible de charger le fichier config pour "+ChatColor.ITALIC+map_name);
			return false;
		}
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
		List<String> objList = cfgManager.getObjectNamesList(map_name);
		for (String objname : objList) {
			System.out.println("trying to load "+objname);
			boolean valid=true;
			Location _position = mapcfg.getLocation("mapObjects."+objname+".centerLocation");
			Location _blockPosition = mapcfg.getLocation("mapObjects."+objname+".blockLocation");
			BlockData _blockData = Bukkit.createBlockData(mapcfg.getString("mapObjects."+objname+".blockData","")); //le blockdata est enregistré sous forme de string dans le config puis transformé en blockdata ici
			Material _blockType = Material.valueOf(mapcfg.getString("mapObjects."+objname+".blockType"));
			double sizex = mapcfg.getDouble("mapObjects."+objname+".sizex",-1);
			double sizey = mapcfg.getDouble("mapObjects."+objname+".sizey",-1);
			double sizez = mapcfg.getDouble("mapObjects."+objname+".sizez",-1);
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
				MapObject o=new MapObject(this,objname, _position, _blockPosition, _blockData, _blockType, sizex, sizey, sizez);
				mapObjects.add(o);
				mainref.getPmanager().registerEvents(o, mainref);
				System.out.println(objname+"loaded");
			}
		}
		//get escapes
		mapLeaveZones.clear();
		objList = cfgManager.getMapEscapesList(map_name);
		for (String objname : objList) {
			boolean valid=true;
			Location loc = mapcfg.getLocation("mapEscapes."+objname+".centerLocation");
			double sizex = mapcfg.getDouble("mapEscapes."+objname+".sizex",-1);
			double sizey = mapcfg.getDouble("mapEscapes."+objname+".sizey",-1);
			double sizez = mapcfg.getDouble("mapEscapes."+objname+".sizez",-1);
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
				MapLeaveZone o = new MapLeaveZone(this,loc,new Vector(sizex,sizey,sizez));
				mapLeaveZones.add(o);
				mainref.getPmanager().registerEvents(o, mainref);
				System.out.println(objname+"loaded");
			}
		}
		//get entrance
			mapEnterZones.clear();
			objList = cfgManager.getMapEntrancesList(map_name);
			for (String objname : objList) {
				boolean valid=true;
				Location loc = mapcfg.getLocation("mapEntrances."+objname+".centerLocation");
				double sizex = mapcfg.getDouble("mapEntrances."+objname+".sizex",-1);
				double sizey = mapcfg.getDouble("mapEntrances."+objname+".sizey",-1);
				double sizez = mapcfg.getDouble("mapEntrances."+objname+".sizez",-1);
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
					MapEnterZone o = new MapEnterZone(this,loc,new Vector(sizex,sizey,sizez));
					mapEnterZones.add(o);
					mainref.getPmanager().registerEvents(o, mainref);
					System.out.println(objname+"loaded");
				}
			}
		return true;
	}

	public ArrayList<MapObject> getMapObjects() {
		return mapObjects;
	}
	public ArrayList<Player> getGardeList(){
		ArrayList<Player> l = new ArrayList<Player>();
		for (Player p : playersInGame.keySet()) {
			if (getPlayerWrapper(p).getTeam()==Vi5Team.GARDE) {
				l.add(p);
			}
		}
		return l;
	}
	public ArrayList<Player> getVoleurInsideList(){
		ArrayList<Player> l = new ArrayList<Player>();
		for (Player p : playersInGame.keySet()) {
			if (getPlayerWrapper(p).getTeam()==Vi5Team.VOLEUR && getPlayerWrapper(p).getCurrentStatus()==VoleurStatus.INSIDE) {
				l.add(p);
			}
		}
		return l;
	}
	public ArrayList<Player> getVoleurList(){
		ArrayList<Player> l = new ArrayList<Player>();
		for (Player p : playersInGame.keySet()) {
			if (getPlayerWrapper(p).getTeam()==Vi5Team.VOLEUR) {
				l.add(p);
			}
		}
		return l;
	}
	public Set<Player> getPlayerList(){
		return playersInGame.keySet();
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
	public WeakHashMap<Player, PlayerWrapper> playersInGame() {
		return playersInGame;
	}
	public Vi5Main getMainRef() {
		return mainref;
	}
}
