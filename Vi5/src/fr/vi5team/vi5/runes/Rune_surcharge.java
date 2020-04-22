package fr.vi5team.vi5.runes;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_surcharge extends BaseRune {

	public Rune_surcharge(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}

	@Override
	public void cast() {
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 9, false, false, true));
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 25, 1, false, false, true));
		player.getWorld().playSound(player.getLocation(),Sound.ENTITY_POLAR_BEAR_WARNING, SoundCategory.MASTER, 0.5f, 0.5f);
		setCooldown(20);
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
	}

	@Override
	public void enterZone() {
	}

}
