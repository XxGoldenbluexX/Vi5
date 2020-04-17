package fr.vi5team.vi5;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vi5team.vi5.enums.InterfaceType;
import fr.vi5team.vi5.enums.RunesList;
import fr.vi5team.vi5.enums.Vi5Team;
import fr.vi5team.vi5.enums.VoleurStatus;
import fr.vi5team.vi5.runes.RuneInviSneak;

public class PlayerWrapper implements Listener {
	
	private Vi5Team team=Vi5Team.SPECTATEUR;
	private BaseRune primaire;
	private BaseRune secondaire;
	private BaseRune tertiaire;
	private short nbItemStealed=0;
	private VoleurStatus currentStatus=VoleurStatus.OUTSIDE;
	private boolean enterStealCooldown=true;
	
	private final Game game;//référence a la game ou le joueur appartient, null si il n'en a pas
	boolean ready=false;
	private final Player player;
	private ItemStack readyItem;
	private ItemStack menuItem;
	private ItemStack TeamSelectionItem;
	private RunesList voleurPrimaire=RunesList.INVI;
	private RunesList voleurSecondaire=null;
	private RunesList voleurTertiaire=null;
	private RunesList gardePrimaire=null;
	private RunesList gardeSecondaire=null;
	private RunesList gardeTertiaire=null;
	
	public PlayerWrapper(Game game, Player player) {
		this.player=player;
		this.game=game;
		ItemStack item = new ItemStack(Material.ANVIL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA+"Settings");
		ArrayList<String> lore= new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE+"Drop this to select settings");
		meta.setLore(lore);
		item.setItemMeta(meta);
		menuItem=item;
		player.getInventory().setItem(1, item);
		setReady(false);
		setTeam(Vi5Team.GARDE);
	}
	
	public Game getGame() {
		return game;
	}
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		ItemStack itm = event.getItemDrop().getItemStack();
		if (event.getPlayer().equals(player)) {
			if (itm.equals(readyItem)) {
				if (ready) {
					setReady(false);
				}else {
					setReady(true);
				}
				event.getItemDrop().remove();
				event.setCancelled(false);
			}else if (itm.equals(menuItem)) {
				game.getMainRef().getInterfaceManager().openMenu(player, InterfaceType.MAIN);
				event.setCancelled(true);
			}else if (itm.equals(TeamSelectionItem)) {
				game.getMainRef().getInterfaceManager().openMenu(player, InterfaceType.TEAM);
				event.setCancelled(true);
			}else if (game.is_Started()) {
				event.getItemDrop().remove();
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		ItemStack itm = event.getItem();
		if (action.equals(Action.RIGHT_CLICK_AIR)||action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (itm.equals(readyItem)) {
				if (ready) {
					setReady(false);
				}else {
					setReady(true);
				}
				event.setCancelled(true);
			}else if (itm.equals(menuItem)) {
				game.getMainRef().getInterfaceManager().openMenu(player, InterfaceType.MAIN);
				event.setCancelled(true);
			}else if (itm.equals(TeamSelectionItem)) {
				game.getMainRef().getInterfaceManager().openMenu(player, InterfaceType.TEAM);
				event.setCancelled(true);
			}
		}
	}
	
	public void gameStart() {
		switch (team) {
		case GARDE:
			primaire=makeRune(gardePrimaire);
			secondaire=makeRune(gardeSecondaire);
			tertiaire=makeRune(gardeTertiaire);
			break;
		case SPECTATEUR:
			break;
		case VOLEUR:
			primaire=makeRune(voleurPrimaire);
			secondaire=makeRune(voleurSecondaire);
			tertiaire=makeRune(voleurTertiaire);
			break;
		}
		if (primaire!=null) {
			primaire.gameStart();
		}
		if (secondaire!=null) {
			secondaire.gameStart();
		}
		if (tertiaire!=null) {
			tertiaire.gameStart();
		}
	}
	
	public void enterZone() {
		if (primaire!=null) {
			primaire.enterZone();
		}
		if (secondaire!=null) {
			secondaire.enterZone();
		}
		if (tertiaire!=null) {
			tertiaire.enterZone();
		}
	}
	
	public void gameEnd() {
		if (primaire!=null) {
			primaire.gameEnd();
		}
		if (secondaire!=null) {
			secondaire.gameEnd();
		}
		if (tertiaire!=null) {
			tertiaire.gameEnd();
		}
		primaire=null;
		secondaire=null;
		tertiaire=null;
	}
	
	public void tickRunes() {
		if (primaire!=null) {
			primaire.tick();
		}
		if (secondaire!=null) {
			secondaire.tick();
		}
		if (tertiaire!=null) {
			tertiaire.tick();
		}
	}
	
	public boolean is_ingame() {
		//permet de savoir si le joueur a rejoint une partie (/!\ NE DIT PAS SI LA PARTIE EST EN COURS)
		return !(game.equals(null));
	}
	
	public boolean is_ready() {
		return ready;
	}
	
	public void setReady(boolean r) {
		ready=r;
		if (ready) {
			ItemStack item = new ItemStack(Material.EMERALD_BLOCK);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_GREEN+"Ready");
			ArrayList<String> lore= new ArrayList<String>();
			lore.add(ChatColor.LIGHT_PURPLE+"Drop this to set yourself not ready");
			meta.setLore(lore);
			item.setItemMeta(meta);
			player.getInventory().setItem(0, item);
			readyItem=item;
		}else {
			ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED+"Not Ready");
			ArrayList<String> lore= new ArrayList<String>();
			lore.add(ChatColor.LIGHT_PURPLE+"Drop this to set yourself ready");
			meta.setLore(lore);
			item.setItemMeta(meta);
			player.getInventory().setItem(0, item);
			readyItem=item;
		}
	}

	public Vi5Team getTeam() {
		return team;
	}

	public void setTeam(Vi5Team team) {
		this.team = team;
		ItemStack item = new ItemStack(Material.WHITE_BANNER);
		switch (team) {
		case GARDE:
			item = new ItemStack(Material.BLUE_BANNER);
			player.sendMessage(ChatColor.BLUE+"You are now a guard");
			break;
		case VOLEUR:
			item = new ItemStack(Material.RED_BANNER);
			player.sendMessage(ChatColor.RED+"You are now a thief");
			break;
		case SPECTATEUR:
			item = new ItemStack(Material.LIME_BANNER);
			player.sendMessage(ChatColor.DARK_GREEN+"You are now a spectator");
			break;
		default:
			break;
		}
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.BLUE+"Game settings");
		ArrayList<String> lore= new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE+"Drop this to select your team or launch game");
		meta.setLore(lore);
		item.setItemMeta(meta);
		TeamSelectionItem=item;
		player.getInventory().setItem(2, item);
	}

	public BaseRune getRunePrimaire() {
		return primaire;
	}

	public void setRunePrimaire(BaseRune primaire) {
		this.primaire = primaire;
	}

	public BaseRune getRuneSecondaire() {
		return secondaire;
	}

	public void setRuneSecondaire(BaseRune secondaire) {
		this.secondaire = secondaire;
	}

	public BaseRune getRuneTertiaire() {
		return tertiaire;
	}

	public void setRuneTertiaire(BaseRune tertiaire) {
		this.tertiaire = tertiaire;
	}

	public short getNbItemStealed() {
		return nbItemStealed;
	}

	public void setNbItemStealed(short nbItemStealed) {
		this.nbItemStealed = nbItemStealed;
	}
	
	public void addItemStealed() {
		nbItemStealed++;
	}
	
	public void resetItemStealed() {
		nbItemStealed=0;
	}
	
	public VoleurStatus getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(VoleurStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isEnterStealCooldown() {
		return enterStealCooldown;
	}

	public void setEnterStealCooldown(boolean enterStealCooldown) {
		this.enterStealCooldown = enterStealCooldown;
	}
	
	public BaseRune makeRune(RunesList rune) {
		if (rune==null) {
			return null;
		}
		switch (rune) {
		case INVI:
			return new RuneInviSneak(game.getMainRef(),this,player);
		}
		return null;
	}

	public RunesList getVoleurPrimaire() {
		return voleurPrimaire;
	}

	public void setVoleurPrimaire(RunesList voleurPrimaire) {
		this.voleurPrimaire = voleurPrimaire;
	}

	public RunesList getVoleurSecondaire() {
		return voleurSecondaire;
	}

	public void setVoleurSecondaire(RunesList voleurSecondaire) {
		this.voleurSecondaire = voleurSecondaire;
	}

	public RunesList getVoleurTertiaire() {
		return voleurTertiaire;
	}

	public void setVoleurTertiaire(RunesList voleurTertiaire) {
		this.voleurTertiaire = voleurTertiaire;
	}

	public RunesList getGardePrimaire() {
		return gardePrimaire;
	}

	public void setGardePrimaire(RunesList gardePrimaire) {
		this.gardePrimaire = gardePrimaire;
	}

	public RunesList getGardeSecondaire() {
		return gardeSecondaire;
	}

	public void setGardeSecondaire(RunesList gardeSecondaire) {
		this.gardeSecondaire = gardeSecondaire;
	}

	public RunesList getGardeTertiaire() {
		return gardeTertiaire;
	}

	public void setGardeTertiaire(RunesList gardeTertiaire) {
		this.gardeTertiaire = gardeTertiaire;
	}
}
