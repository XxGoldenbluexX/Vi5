package fr.vi5team.vi5.runes;

import java.util.ArrayList;

import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_smokePool extends BaseRune {

	private final ArrayList<AreaEffectCloud> flakPool=new ArrayList<AreaEffectCloud>();
	
	public Rune_smokePool(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}

	@Override
	public void cast() {
		Entity e = player.getWorld().spawnEntity(player.getLocation(), EntityType.AREA_EFFECT_CLOUD);
		if (e!=null) {
			if (e instanceof AreaEffectCloud) {
				AreaEffectCloud cloud = (AreaEffectCloud) e;
				cloud.clearCustomEffects();
				cloud.setDuration(200);
				cloud.setParticle(Particle.EXPLOSION_NORMAL);
				cloud.setDurationOnUse(0);
				cloud.setRadius(4);
				cloud.setRadiusPerTick(0);
				cloud.setSource(player);
				flakPool.add(cloud);
				setCooldown(20);
			}
		}
		showCastItem();
	}

	@Override
	public void tick() {
		ArrayList<AreaEffectCloud> toRemove = new ArrayList<AreaEffectCloud>();
		for (AreaEffectCloud cloud : flakPool) {
			if (cloud.isDead()) {
				toRemove.add(cloud);
			}
		}
		for (AreaEffectCloud cloud : toRemove) {
			flakPool.remove(cloud);
		}
	}

	@Override
	public void gameEnd() {
	}

	@Override
	public void gameStart() {
	}

	@Override
	public void enterZone() {
		Activate();
	}
}
