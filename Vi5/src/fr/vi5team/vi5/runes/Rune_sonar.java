package fr.vi5team.vi5.runes;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;
import fr.vi5team.vi5.enums.Vi5Team;

public class Rune_sonar extends BaseRune{
	byte cooldown=0;
	public Rune_sonar(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
		// TODO Auto-generated constructor stub
	}
	public void makeSound() {
		boolean thiefNear=false;
		for(Player p : wraper.getGame().getPlayerList()) {
			if(mainref.getPlayerWrapper(p).getTeam()==Vi5Team.VOLEUR){
				if (player.getLocation().distanceSquared(p.getLocation())<=25) {
					thiefNear=true;
					break;
				}
			}
		}
		if(thiefNear) {
			player.getWorld().playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, 1, 2);
			player.getWorld().playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, 2, 1);
		}else {
			player.getWorld().playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, 1, 0.5f);
			player.getWorld().playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, 2, 0);
		}
	}
	@Override
	public void cast() {	
	}

	@Override
	public void tick() {
		cooldown++;
		if(cooldown>=100) {
			makeSound();
			cooldown=0;
		}
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
