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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.vi5team.vi5.enums.InterfaceType;
import fr.vi5team.vi5.enums.RunesList;
import fr.vi5team.vi5.enums.Vi5Team;
import fr.vi5team.vi5.enums.VoleurStatus;
import fr.vi5team.vi5.runes.BaseRune;

public class PlayerWrapper implements Listener {
	
	private Vi5Team team=Vi5Team.SPECTATEUR;
	private BaseRune primaire;
	private BaseRune secondaire;
	private BaseRune tertiaire;
	private short nbItemStealed=0;
	private VoleurStatus currentStatus=VoleurStatus.OUTSIDE;
	private boolean enterStealCooldown=true;
	private boolean leaveCooldown=true;
	
	private final Game game;//r�f�rence a la game ou le joueur appartient, null si il n'en a pas
	boolean ready=false;
	private final Player player;
	private ItemStack readyItem;
	private ItemStack menuItem;
	private ItemStack TeamSelectionItem;
	private RunesList voleurPrimaire=RunesList.INVI;
	private RunesList voleurSecondaire=RunesList.DOUBLE_JUMP;
	private RunesList voleurTertiaire=RunesList.TOUGH;
	private RunesList gardePrimaire=RunesList.OMNI;
	private RunesList gardeSecondaire=RunesList.SURCHARGE;
	private RunesList gardeTertiaire=RunesList.SPEED;
	
	//Status variables
	private boolean omnispotted=false;
	private boolean invisible=false;
	private boolean glow=false;
	private boolean decouvert=false;
	private boolean unGlowable=false;
	private boolean insondable=false;
	private boolean jammed=false;
	private boolean unSpottable=false;
	//
	
	public PlayerWrapper(Game game, Player player) {
		this.player=player;
		this.game=game;
		ready=false;
		showMenuHotbar();
	}
	
	public Game getGame() {
		return game;
	}
	public void showMenuHotbar() {
		ItemStack item = new ItemStack(Material.ANVIL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA+"Settings");
		ArrayList<String> lore= new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE+"Drop this to select settings");
		meta.setLore(lore);
		item.setItemMeta(meta);
		menuItem=item;
		player.getInventory().setItem(1, item);
		setReady(ready);
		setTeam(team,false);
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
		if (event.getPlayer().equals(player)) {
			if (action.equals(Action.RIGHT_CLICK_AIR)||action.equals(Action.RIGHT_CLICK_BLOCK)) {
				if (itm==null) {
					return;
				}
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
	}
	public boolean isSondable() {
		return (currentStatus==VoleurStatus.INSIDE && !unSpottable && (!insondable || omnispotted));
	}
	
	public void gameStart() {
		player.setWalkSpeed(0.2f);
		player.setAllowFlight(false);
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
			primaire.preGameEnd();
		}
		if (secondaire!=null) {
			secondaire.preGameEnd();
		}
		if (tertiaire!=null) {
			tertiaire.preGameEnd();
		}
		currentStatus=VoleurStatus.ESCAPED;
		primaire=null;
		secondaire=null;
		tertiaire=null;
	}
	
	public void tickRunes() {
		//EFFECTS
		if (invisible && !decouvert && !omnispotted) {
			if (!player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,Integer.MAX_VALUE,0,false,false,true));
			}
		}else {
			if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
			}
		}
		if (glow && (!unGlowable || omnispotted) && !unSpottable) {
			if (!player.hasPotionEffect(PotionEffectType.GLOWING)) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,Integer.MAX_VALUE,0,false,false,true));
			}
		}else {
			if (player.hasPotionEffect(PotionEffectType.GLOWING)) {
				player.removePotionEffect(PotionEffectType.GLOWING);
			}
		}
		//RUNES
		if (primaire!=null) {
			primaire.pretick();
		}
		if (secondaire!=null) {
			secondaire.pretick();
		}
		if (tertiaire!=null) {
			tertiaire.pretick();
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

	public void setTeam(Vi5Team team, boolean message) {
		this.team = team;
		ItemStack item = new ItemStack(Material.WHITE_BANNER);
		switch (team) {
		case GARDE:
			item = new ItemStack(Material.BLUE_BANNER);
			if (message) {
				player.sendMessage(ChatColor.BLUE+"You are now a guard");
			}
			player.setPlayerListName(ChatColor.BLUE+player.getName());
			break;
		case VOLEUR:
			item = new ItemStack(Material.RED_BANNER);
			if (message) {
				player.sendMessage(ChatColor.RED+"You are now a thief");
			player.setPlayerListName(ChatColor.RED+player.getName());
			}
			break;
		case SPECTATEUR:
			item = new ItemStack(Material.LIME_BANNER);
			if (message) {
			player.sendMessage(ChatColor.DARK_GREEN+"You are now a spectator");
				player.setPlayerListName(ChatColor.GREEN+player.getName());
			}
			break;
		default:
			break;
		}
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.BLUE+"Team");
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
		return rune.spawn(rune,game.getMainRef(),this,player);
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

	public boolean isOmnispotted() {
		return omnispotted;
	}

	public void setOmnispotted(boolean omnispotted) {
		this.omnispotted = omnispotted;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public boolean isGlow() {
		return glow;
	}

	public void setGlow(boolean glow) {
		this.glow = glow;
	}

	public boolean isDecouvert() {
		return decouvert;
	}

	public void setDecouvert(boolean decouvert) {
		this.decouvert = decouvert;
	}

	public boolean isUnGlowable() {
		return unGlowable;
	}

	public void setUnGlowable(boolean inGlowable) {
		this.unGlowable = inGlowable;
	}

	public boolean isInsondable() {
		return insondable;
	}

	public void setInsondable(boolean insondable) {
		this.insondable = insondable;
	}

	public boolean isJammed() {
		return jammed;
	}

	public void setJammed(boolean jammed) {
		this.jammed = jammed;
	}

	public boolean isUnSpottable() {
		return unSpottable;
	}

	public void setUnSpottable(boolean unSpottable) {
		this.unSpottable = unSpottable;
	}

	public boolean isLeaveCooldown() {
		return leaveCooldown;
	}

	public void setLeaveCooldown(boolean leaveCooldown) {
		this.leaveCooldown = leaveCooldown;
	}
}
