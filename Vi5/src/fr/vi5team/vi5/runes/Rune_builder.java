package fr.vi5team.vi5.runes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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
				player.sendMessage(ChatColor.RED+"There is no more possible walls on the map. Consider adding more later");
			}else {
				NB_WALLS++;
				if(NB_WALLS==MAX_WALLS) {
					player.sendMessage(ChatColor.GREEN+"You have placed your last wall: "+ChatColor.GOLD+ChatColor.UNDERLINE+nearestWall);		
				}else {
					player.sendMessage(ChatColor.GREEN+"You have placed the wall: "+ChatColor.GOLD+ChatColor.UNDERLINE+nearestWall);	
					}
				}
		}else {
			player.sendMessage(ChatColor.RED+"All your walls have already been placed!");	
			
		}
		showAdaptedHotbarItem();
		
	}
	private void showAdaptedHotbarItem() {
		ItemStack item;
		ItemMeta meta;
		item=new ItemStack(Material.BRICK_WALL, MAX_WALLS-NB_WALLS);
		meta=item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD+"You can place "+ChatColor.AQUA+(MAX_WALLS-NB_WALLS)+ChatColor.GOLD+" more walls");
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
