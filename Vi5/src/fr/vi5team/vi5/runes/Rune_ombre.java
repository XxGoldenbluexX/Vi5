package fr.vi5team.vi5.runes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vi5team.vi5.Game;
import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_ombre extends BaseRune {
	
	private enum OmbreStatus {
		READY,
		POSED,
		WASTED
	}
	
	private OmbreStatus status=OmbreStatus.READY;
	private ArmorStand ombreRef;

	public Rune_ombre(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (wraper.getGame().getGardeList().contains(p)) {
			
		}
	}

	@Override
	public void cast() {
		switch (status) {
		case POSED:
			if (ombreRef!=null) {
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.MASTER, 1, 1);
				player.teleport(ombreRef);
				ombreRef.remove();
				ombreRef=null;
				ItemStack itm=new ItemStack(Material.CHARCOAL);
				ItemMeta meta = itm.getItemMeta();
				meta.setDisplayName(ChatColor.GRAY+"Shadow (Used)");
				itm.setItemMeta(meta);
				setCastItem(itm);
				setCooldown(2);
				status=OmbreStatus.WASTED;
			}
			break;
		case READY:
			ombreRef = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
			if (ombreRef!=null) {
				ombreRef.setHelmet(new ItemStack(Material.COAL_BLOCK));
				ombreRef.setMarker(true);
				ItemStack itm=new ItemStack(Material.CHARCOAL);
				ItemMeta meta = itm.getItemMeta();
				meta.setDisplayName(ChatColor.LIGHT_PURPLE+"Shadow (Dropped)");
				itm.setItemMeta(meta);
				setCastItem(itm);
				setCooldown(2);
				status=OmbreStatus.POSED;
			}
			break;
		case WASTED:
			player.sendMessage("Your shadow has disapeared");
			return;
		default:
			return;
		}
		setCooldown(2);
	}

	@Override
	public void tick() {
		if (status==OmbreStatus.POSED) {
			if (ombreRef!=null) {
				Game g = wraper.getGame();
				for (Player p : g.getGardeList()) {
					if (p.getLocation().distanceSquared(ombreRef.getLocation())<=1) {
						ombreRef.remove();
						ombreRef=null;
						for (Player w : g.getPlayerList()) {
							w.playSound(w.getLocation(), Sound.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1, 1);
							w.playSound(w.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.MASTER, 1, 1);
						}
						g.messagePlayersInGame(ChatColor.BLUE+p.getName()+ChatColor.GOLD+" killed "+ChatColor.RED+player.getName()+ChatColor.GOLD+" with his shadow");
						player.damage(player.getHealth(), p);
					}
				}
			}
		}
	}

	@Override
	public void gameEnd() {
	}

	@Override
	public void gameStart() {
	}

	@Override
	public void enterZone() {
		Activate();
	}
}
