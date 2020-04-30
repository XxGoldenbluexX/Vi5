package fr.vi5team.vi5.runes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_sonar extends BaseRune{
	byte cooldown=0;
	public Rune_sonar(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
		// TODO Auto-generated constructor stub
	}
	public void makeSound() {
		ArrayList<Player> playerScanned = new ArrayList<Player>();
		for(Player p : wraper.getGame().getVoleurInsideList()) {
			if(mainref.getPlayerWrapper(p).isSondable()){
				if (player.getLocation().distanceSquared(p.getLocation())<=25) {
					playerScanned.add(p);
				}
			}
		}
		if(playerScanned.size()>0) {
			player.playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, 1, 2);
			player.playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, 2, 1);
			for(Player pScanned : playerScanned) {
				pScanned.playSound(pScanned.getLocation(),Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, 1, 2);
				pScanned.playSound(pScanned.getLocation(),Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, 2, 1);
			}
		}else {
			player.playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, 1, 0.5f);
			player.playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, 2, 0);
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
		player.getInventory().setItem(0, makeSpamSword(Material.DIAMOND_SWORD,7));
	}

	@Override
	public void enterZone() {
	}

}
