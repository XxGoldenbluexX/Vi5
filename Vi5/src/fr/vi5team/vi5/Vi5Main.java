package fr.vi5team.vi5;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer;
import fr.vi5team.vi5.commands.Vi5BaseCommand;
import fr.vi5team.vi5.enums.DieCancelType;
import fr.vi5team.vi5.events.PlayerKillEvent;
import fr.vi5team.vi5.interfaces.Vi5Interfaces;

public class Vi5Main extends JavaPlugin implements Listener {
	
	private ConfigManager cfgmanager;
	private final ArrayList<Game> gamesList= new ArrayList<Game>();
	private PluginManager pmanager;
	private ProtocolManager protocolManager;
	private final Vi5Interfaces InterfaceManager = new Vi5Interfaces(this);
	
	@Override
	public void onEnable() {
		super.onEnable();
		pmanager=Bukkit.getPluginManager();
		protocolManager = ProtocolLibrary.getProtocolManager();
		cfgmanager = new ConfigManager(this);
		getCommand("vi5").setExecutor(new Vi5BaseCommand(this));
		pmanager.registerEvents(this,this);
		pmanager.registerEvents(InterfaceManager,this);
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
	public Game createGame(String name) {
		Game g=new Game(this,cfgmanager,name);
		gamesList.add(g);
		pmanager.registerEvents(g, this);
		return g;
	}
	public void deleteGame(Game g) {
		gamesList.remove(g);
	}
	public PlayerWrapper getPlayerWrapper(Player player) {
		for (final Game game : gamesList) {
			PlayerWrapper wrap = game.getPlayerWrapper(player);
			if (wrap!=null) {
				return wrap;
			}
		}
		return null;
	}
	public Game getGame(String name) {
		for (Game g : gamesList) {
			if (g.getName().equalsIgnoreCase(name)) {
				return g;
			}
		}
		return null;
	}
	
	@EventHandler
	public void PlayerDamagedEvent(EntityDamageByEntityEvent event) {
		Entity ent = event.getEntity();
		if (ent instanceof Player) {
			Player victim = (Player)ent;
			ent=event.getDamager();
			if (ent instanceof Player) {
				Player attacker = (Player)ent;
				if (victim.getHealth()-event.getDamage()<1) {
					PlayerKillEvent evt=new PlayerKillEvent(attacker, victim);
					pmanager.callEvent(evt);
					if (evt.is_Canceled()) {
						if (evt.getCancelType()==DieCancelType.FULL_CANCEL) {
							event.setDamage(0);
						}else if (evt.getCancelType()==DieCancelType.ONE_HEART){
							event.setDamage(victim.getHealth()-1);
						}
						return;
					}else {
						return;
					}
				}
			}
		}
	}
	
	public boolean packetGlowPlayer(Player player,Player glowed) {
		PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
	     packet.getIntegers().write(0, glowed.getEntityId()); //Set packet's entity id
	     WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
	     Serializer serializer = Registry.get(Byte.class); //Found this through google, needed for some stupid reason
	     watcher.setEntity(player); //Set the new data watcher's target
	     watcher.setObject(0, serializer, (byte) (0x40)); //Set status to glowing, found on protocol page
	     packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
	     try {
	    	 protocolManager.sendServerPacket(player, packet);
	         return true;
	     } catch (InvocationTargetException e) {
	         e.printStackTrace();
	         return false;
	     }
	     //MERCI INTERNET PUTAIN
	}
	
	public ProtocolManager getProtocolManager() {
		return protocolManager;
	}
	
	@EventHandler
	public void HangingBreakEvent(HangingBreakByEntityEvent event) {
		Entity ent = event.getEntity();
		Entity destroyer = event.getRemover();
		if ((ent instanceof Painting || ent instanceof ItemFrame)&&(destroyer instanceof Player)) {
			if (((Player)event.getRemover()).getGameMode()==GameMode.ADVENTURE) {
				event.setCancelled(true);
				return;
			}
		}
		return;
	}
	
	@EventHandler
	public void vehicleDestroyEvent(VehicleDestroyEvent event) {
		Vehicle v = event.getVehicle();
		Entity ent = event.getAttacker();
		if (ent instanceof Player) {
			if (((Player)ent).getGameMode()==GameMode.ADVENTURE && v instanceof Minecart) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void primeExplostionEvent(ExplosionPrimeEvent event) {
		if (event.getEntity() instanceof EnderCrystal) {
			event.setCancelled(true);
			return;
		}
	}

	public PluginManager getPmanager() {
		return pmanager;
	}

	public Vi5Interfaces getInterfaceManager() {
		return InterfaceManager;
	}
}
