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
