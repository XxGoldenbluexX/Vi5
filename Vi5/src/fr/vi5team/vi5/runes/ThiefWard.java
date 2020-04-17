package fr.vi5team.vi5.runes;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.vi5team.vi5.BaseRune;
import fr.vi5team.vi5.Game;
import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;
import fr.vi5team.vi5.enums.Vi5Team;

public class ThiefWard extends BaseRune {
	boolean isPlaced;
	ArmorStand thiefWard;
	Game g;
	byte wardRange = 3;
	HashMap<Player, PlayerWrapper> playersInGame;
	public ThiefWard(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
		isPlaced = false;
		g=wraper.getGame();
		playersInGame = g.playersInGame();
	}
	@Override
	public void cast() {
		if(!isPlaced) {
			Location ploc = player.getLocation();
			thiefWard = (ArmorStand) ploc.getWorld().spawnEntity(ploc, EntityType.ARMOR_STAND);
			thiefWard.setSmall(true);
			thiefWard.setVisible(true);
			//thiefWard.setCustomName(ChatColor.RED+"| SCANNING... |");
			//thiefWard.setCustomNameVisible(true);
			thiefWard.setHelmet(new ItemStack(Material.REDSTONE_BLOCK));
			thiefWard.setArms(false);
			thiefWard.setCollidable(false);
			thiefWard.setInvulnerable(true);
			isPlaced = true;
		}
	}
	public boolean checkZone(Player p) {
		Location wardLoc = thiefWard.getLocation();
		Location ploc = p.getLocation();
		if(ploc.getX()>=wardLoc.getX()-wardRange&&ploc.getX()<=wardLoc.getX()+wardRange) {
			if(ploc.getZ()>=wardLoc.getZ()-wardRange&&ploc.getZ()<=wardLoc.getZ()+wardRange) {
				if(ploc.getY()>=wardLoc.getY()-wardRange&&ploc.getY()<=wardLoc.getY()+wardRange) {
					return true;
				}
			}
		}
		return false;
	}
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		if(isPlaced) {
			for(Player p : playersInGame.keySet()) {
				if(playersInGame.get(p).getTeam()==Vi5Team.VOLEUR) {
					if(checkZone(p)) {
						p.setGlowing(true);
					}
					else {
						if(p.isGlowing()) {
							p.setGlowing(false);
						}
					}
				}
			}			
		}
	}
}