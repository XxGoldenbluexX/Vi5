package fr.vi5team.vi5.runes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_fisherman extends BaseRune{

	public Rune_fisherman(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cast() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameEnd() {
		// TODO Auto-generated method stub
		
	}
	private void showAdaptedHotbarItem() {
		ItemStack item;
		ItemMeta meta;
		item=new ItemStack(Material.FISHING_ROD);
		meta=item.getItemMeta();
		meta.setUnbreakable(true);
		item.setItemMeta(meta);
		setCastItem(item);
		showCastItem();
	}
	@Override
	public void gameStart() {
		Activate();
		showAdaptedHotbarItem();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterZone() {
		// TODO Auto-generated method stub
		
	}

}
