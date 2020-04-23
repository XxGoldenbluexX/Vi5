package fr.vi5team.vi5.runes;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_scanner extends BaseRune{

	public Rune_scanner(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cast() {
		for(Player p : wraper.getGame().getGardeList()) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 140, 1, false, false, true));
		setCooldown(25);
	}
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterZone() {
		Activate();
		// TODO Auto-generated method stub
		
	}

}
