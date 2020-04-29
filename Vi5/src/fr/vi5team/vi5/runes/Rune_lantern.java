package fr.vi5team.vi5.runes;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_lantern extends BaseRune {

	private static final byte MAX_LANTERN = 2;
	private final ArrayList<FallingBlock> lanterns = new ArrayList<FallingBlock>();
	private byte nb_lantern = MAX_LANTERN;
	
	public Rune_lantern(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}
	
	private void showAdaptedItem() {
		ItemStack itm = new ItemStack(Material.LANTERN);
		ItemMeta meta = itm.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE+"Lantern: "+ChatColor.AQUA+nb_lantern);
		itm.setItemMeta(meta);
		setCastItem(itm);
		showCastItem();
	}

	@Override
	public void cast() {
		if (nb_lantern>0) {
			FallingBlock l = player.getWorld().spawnFallingBlock(player.getLocation(), Bukkit.createBlockData(Material.LANTERN));
			if (l!=null) {
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, SoundCategory.MASTER, 1, 2);
				l.setGravity(false);
				l.setTicksLived(1);
				lanterns.add(l);
				nb_lantern--;
				showAdaptedItem();
				setCooldown(2);
			}
		}
	}

	@Override
	public void tick() {
		for (Player p : wraper.getGame().getVoleurInsideList()) {
			FallingBlock l=null;
			for (FallingBlock b : lanterns) {
				if (p.getLocation().distanceSquared(b.getLocation())<=1) {
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, SoundCategory.MASTER, 1, 1.6f);
					p.teleport(player);
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, SoundCategory.MASTER, 1, 1.6f);
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, SoundCategory.MASTER, 1, 1.6f);
					p.playSound(p.getLocation(), Sound.ENTITY_SHULKER_SHOOT, SoundCategory.MASTER, 2, 0.1f);
					player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_SHOOT, SoundCategory.MASTER, 2, 0.1f);
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1.5f);
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1.5f);
					nb_lantern++;
					showAdaptedItem();
					l=b;
				}
			}
			if (l!=null) {
				lanterns.remove(l);
			}
		}
	}

	@Override
	public void gameEnd() {
		for (FallingBlock s : lanterns) {
			s.remove();
			lanterns.clear();
		}
	}

	@Override
	public void gameStart() {
	}

	@Override
	public void enterZone() {
		Activate();
	}

}
