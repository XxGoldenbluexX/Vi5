package fr.vi5team.vi5.runes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vi5team.vi5.MapWall;
import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_builder extends BaseRune{
	private static final byte MAX_WALLS=2;
	private byte NB_WALLS=0;
	private MapWall mapWall;
	public Rune_builder(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
		mapWall=wraper.getGame().getMapWall();
	}
	@Override
	public void cast() {
		if(NB_WALLS<MAX_WALLS) {
			String nearestWall = mapWall.getNearestWall(player);
			if(nearestWall==null) {
				player.sendMessage(ChatColor.RED+"Aucun mur disponible. Pensez à en ajouter plus tard!");
			}else {
				NB_WALLS++;
				player.playSound(player.getLocation(), Sound.BLOCK_BARREL_CLOSE, SoundCategory.MASTER, 1, 0);
				player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, SoundCategory.MASTER, 1, 0);
				player.sendMessage(ChatColor.GREEN+"Mur placé: "+ChatColor.GOLD+ChatColor.UNDERLINE+nearestWall);
				showAdaptedHotbarItem();
			}
		}
	}
	private void showAdaptedHotbarItem() {
		ItemStack item;
		ItemMeta meta;
		if(MAX_WALLS-NB_WALLS!=2) {
			item=new ItemStack(Material.BRICK_WALL, 1);
		}else {
			item=new ItemStack(Material.BRICK_WALL, 2);
		}
		meta=item.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE+"Mur(s) restant(s): "+ChatColor.AQUA+(MAX_WALLS-NB_WALLS)+"/"+MAX_WALLS);
		item.setItemMeta(meta);
		setCastItem(item);
		showCastItem();
	}
	@Override
	public void tick() {
	}
	@Override
	public void gameEnd() {
	}
	@Override
	public void gameStart() {
		Activate();
		showAdaptedHotbarItem();
	}

	@Override
	public void enterZone() {
		
	}

}
