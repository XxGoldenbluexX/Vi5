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
	private final ArrayList<Player> playersInPool = new ArrayList<Player>();
	private static final float POOL_RAY=5;
	
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
				cloud.setRadius(POOL_RAY);
				cloud.setRadiusPerTick(0);
				cloud.setSource(player);
				flakPool.add(cloud);
				setCooldown(20);
			}
		}
		showCastItem();
	}
	
	private ArrayList<Player> getThiefsInRange(AreaEffectCloud cloud){
		ArrayList<Player> l = new ArrayList<Player>();
		for (Player p : wraper.getGame().getVoleurInsideList()) {
			if (p.getLocation().distanceSquared(cloud.getLocation())<=(POOL_RAY*POOL_RAY)) {
				double pz=p.getLocation().getY();
				double cz=cloud.getLocation().getY();
				if (pz>=cz-1.5 && pz<=cz+1.5) {
					l.add(p);
				}
			}
		}
		return l;
	}
	
	@Override
	public void tick() {
		ArrayList<AreaEffectCloud> CloudToRemove = new ArrayList<AreaEffectCloud>();
		ArrayList<Player> PToRemove = new ArrayList<Player>();
		PToRemove.addAll(playersInPool);
		for (AreaEffectCloud cloud : flakPool) {
			for (Player p : getThiefsInRange(cloud)) {
				if (playersInPool.contains(p)) {
					PToRemove.remove(p);
				}else {
					playersInPool.add(p);
				}
			}
			if (cloud.isDead()) {
				CloudToRemove.add(cloud);
			}
		}
		for (AreaEffectCloud cloud : CloudToRemove) {
			flakPool.remove(cloud);
		}
		for (Player p : playersInPool) {
			PlayerWrapper w = wraper.getGame().getPlayerWrapper(p);
			w.setInsondable(true);
			w.setInvisible(true);
		}
		for (Player p : PToRemove) {
			PlayerWrapper w = wraper.getGame().getPlayerWrapper(p);
			w.setInsondable(false);
			w.setInvisible(false);
			playersInPool.remove(p);
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
