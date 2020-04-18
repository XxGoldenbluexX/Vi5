package fr.vi5team.vi5.runes;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.vi5team.vi5.BaseRune;
import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class GuardCop extends BaseRune {

	public GuardCop(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, RunesList.COP);
	}

	@Override
	public void cast() {
		
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void gameEnd() {
		
	}

	@Override
	public void gameStart() {
		ItemStack it = player.getInventory().getItem(1);
		it.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, Integer.MAX_VALUE);
		player.getInventory().setItem(21, it);
		
	}

	@Override
	public void enterZone() {
		
	}

}
