package fr.vi5team.vi5.runes;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.vi5team.vi5.BaseRune;
import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class RuneInviSneak extends BaseRune {

	private boolean isSneaking=false;
	
	public RuneInviSneak(Vi5Main _mainref, PlayerWrapper _wraper, Player _player) {
		super(_mainref, _wraper, _player, RunesList.INVI);
	}

	@Override
	public void cast() {
	}
	
	@EventHandler
	public void onPlayerSneak(PlayerToggleSneakEvent event) {
		if (event.getPlayer().equals(getPlayer())) {
			isSneaking=event.isSneaking();
		}
	}

	@Override
	public void tick() {
		boolean gardeNear=false;
		for (Player p : getWraper().getGame().getAllGarde()) {
			if (getPlayer().getLocation().distanceSquared(p.getLocation())<=9) {
				gardeNear=true;
			}
		}
		if (isSneaking) {
			if (gardeNear) {
				getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
				getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, SoundCategory.MASTER, 0.1F, 2F);
			}else {
				getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,200,0,false,false,true));
			}
		}else {
			getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
		}
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
		// TODO Auto-generated method stub
		
	}
}