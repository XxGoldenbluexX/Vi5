package fr.vi5team.vi5.runes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_bush extends BaseRune{
	private ArrayList<Material> bushMaterial;
	private double SQUARED_SPOT_RANGE = 1;
	private boolean isInBush=false;
	public Rune_bush(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cast() {
		// TODO Auto-generated method stub
		
	}
	public void fillBushMaterial(){
		bushMaterial.add(Material.ROSE_BUSH);
		bushMaterial.add(Material.TALL_GRASS);
		bushMaterial.add(Material.LARGE_FERN);
		bushMaterial.add(Material.PEONY);
	}
	private boolean guardNear() {
		for (Player p : wraper.getGame().getGardeList()) {
			if (p.getLocation().distanceSquared(player.getLocation())<=SQUARED_SPOT_RANGE) {
				return true;
			}
		}
		return false;
	}
	public void onPlayerMove(PlayerMoveEvent event) {
		if(event.getPlayer()==player) {
			if(bushMaterial.contains(player.getLocation().getBlock().getType())){
				isInBush=true;
			}else {
				if(!bushMaterial.contains(player.getLocation().getBlock().getType())){
					isInBush=false;
					wraper.setInvisible(false);
				}
			}
		}
	}
	@Override
	public void tick() {	
		if (isInBush) {
			if (guardNear() || wraper.isJammed()) {
				wraper.setInvisible(false);
			}else {
				wraper.setInvisible(true);
			}
		}
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
		fillBushMaterial();
		// TODO Auto-generated method stub
	}

}
