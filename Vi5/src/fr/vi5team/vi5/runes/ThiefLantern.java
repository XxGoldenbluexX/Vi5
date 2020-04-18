package fr.vi5team.vi5.runes;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import fr.vi5team.vi5.BaseRune;
import fr.vi5team.vi5.Game;
import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;
import fr.vi5team.vi5.enums.Vi5Team;

public class ThiefLantern extends BaseRune {
	byte maxLantern = 2;
	//Pour augmenter le max, changer 'maxLantern' et 'fillHashMap'
	byte currentLantern;
	HashMap<Byte,FallingBlock> lanternLocations;
	HashMap<Player, PlayerWrapper> playersInGame;
	Game g;
	public ThiefLantern(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, RunesList.LANTERN);
	}
	
	@SuppressWarnings("deprecation")
	public void fillHashMap() {
		Location ploc = player.getLocation();
		if(!lanternLocations.containsKey((byte)0)) {
			FallingBlock lantern0 = ploc.getWorld().spawnFallingBlock(ploc, Material.LANTERN, (byte) 0);
			lanternLocations.put((byte)0, lantern0);
		}else {
			FallingBlock lantern1 = ploc.getWorld().spawnFallingBlock(ploc, Material.LANTERN, (byte) 0);
			lanternLocations.put((byte)1, lantern1);
		}
	}
	@Override
	public void cast() {
		lanternLocations.put((byte) 0, null);
		if((currentLantern<maxLantern)) {
			fillHashMap();
			currentLantern++;
			}	
		if(currentLantern==1) {
			ItemStack it = Rune.getHotbarItem().clone();
			player.getInventory().setItem(2, it);
		}
	}
	public void onPlayerMove(PlayerMoveEvent event) {
		if(currentLantern>=1) {
			Player p=event.getPlayer();
			if(playersInGame.get(p).getTeam()==Vi5Team.VOLEUR&&p!=player) {
				for(byte nbLantern : lanternLocations.keySet()) {
					if(lanternLocations.get(nbLantern).getLocation().equals(p.getLocation())) {
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 2);
						p.teleport(player);
						lanternLocations.remove(nbLantern);
						if(currentLantern==2) {
							ItemStack it = Rune.getHotbarItem().clone();
							player.getInventory().setItem(2, it);
						}
						currentLantern--;
					}
					
				}
			}
		}
	}
	@Override
	public void tick() {
	}

	@Override
	public void gameEnd() {
		if(currentLantern>0) {
			for(FallingBlock lantern : lanternLocations.values()) {
				lantern.remove();
			}
		}
		
	}

	@Override
	public void gameStart() {
		g=wraper.getGame();
		playersInGame = g.playersInGame();
		currentLantern=0;
	}

	@Override
	public void enterZone() {	
	}

}
