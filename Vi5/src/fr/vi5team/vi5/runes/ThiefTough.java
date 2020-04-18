package fr.vi5team.vi5.runes;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.vi5team.vi5.BaseRune;
import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class ThiefTough extends BaseRune {

	public ThiefTough(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, RunesList.TOUGH);
	}

	@Override
	public void cast() {
		
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void gameEnd() {
		if(!(player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)==null)) {
			player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		}
		
	}

	@Override
	public void gameStart() {
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, false, false));
		
		
	}

	@Override
	public void enterZone() {
		
	}

}
