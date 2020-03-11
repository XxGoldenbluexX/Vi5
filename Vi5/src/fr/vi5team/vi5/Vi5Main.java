package fr.vi5team.vi5;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.vi5team.vi5.commands.Vi5BaseCommand;
import fr.vi5team.vi5.enums.DieCancelType;
import fr.vi5team.vi5.events.PlayerKillEvent;

public class Vi5Main extends JavaPlugin implements Listener {
	
	private ConfigManager cfgmanager;
	private ArrayList<Game> gamesList= new ArrayList<Game>();
	private PluginManager pmanager;
	
	@Override
	public void onEnable() {
		super.onEnable();
		pmanager=Bukkit.getPluginManager();
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
	
	public PlayerWrapper getPlayerWrapper(Player player) {
		for (final Game game : gamesList) {
			PlayerWrapper wrap = game.getPlayerWrapper(player);
			if (wrap!=null) {
				return wrap;
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
}
