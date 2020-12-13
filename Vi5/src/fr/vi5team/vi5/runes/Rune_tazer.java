package fr.vi5team.vi5.runes;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_tazer extends BaseRune {

	private Snowball lastSnowBall;
	
	public Rune_tazer(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}

	@Override
	public void cast() {
		lastSnowBall = player.launchProjectile(Snowball.class, player.getEyeLocation().getDirection());
		lastSnowBall.setVelocity(lastSnowBall.getVelocity().multiply(5));
		showCastItem();
		setCooldown(5);
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
		player.getInventory().setItem(0, makeSpamSword(Material.DIAMOND_SWORD,7));
	}

	@Override
	public void enterZone() {
	}
	
	@EventHandler
	public void onProjectileHit(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		Entity victim = event.getEntity();
		if (damager.equals(lastSnowBall) && victim instanceof LivingEntity) {
			lastSnowBall.remove();
			lastSnowBall=null;
			LivingEntity shockingStriked = (LivingEntity) victim;
			shockingStriked.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,60,1,false,false,true));
			shockingStriked.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,60,200,false,false,true));
			victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_BEEHIVE_WORK, SoundCategory.MASTER, 1,0.1f);
			new BukkitRunnable() {
				private int nbHit=0;
				@Override
				public void run() {
					nbHit++;
					shockingStriked.setNoDamageTicks(0);
					shockingStriked.damage(0.001);
					if (nbHit>30) {
						this.cancel();
					}
				}
			}.runTaskTimer(mainref, 1, 2);
		}
	}
}
