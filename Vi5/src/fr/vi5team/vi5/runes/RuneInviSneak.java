package fr.vi5team.vi5.runes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.vi5team.vi5.BaseRune;
import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;
import fr.vi5team.vi5.enums.Vi5Team;

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
		isSneaking=event.isSneaking();
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		PlayerWrapper wrap = getMainref().getPlayerWrapper(event.getPlayer());
		if (wrap!=null) {
			if (wrap.getTeam()==Vi5Team.GARDE && event.getPlayer().getLocation().distanceSquared(getPlayer().getLocation())<=9) {
				getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
			}
		}
	}

	@Override
	public void tick() {
		if (isEnabled()) {
			if (isSneaking) {
				getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,10,0,false,false,true));
			}else {
				getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
			}
		}
	}
}