package fr.vi5team.vi5.runes;

import java.util.ArrayList;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_scanner extends BaseRune{
	ArrayList<ArmorStand> armorList = new ArrayList<ArmorStand>();
	public Rune_scanner(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}
	public void summonArmorStand() {
		for(Player p : wraper.getGame().getGardeList()) {
			ArmorStand armorStand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
			armorStand.setInvulnerable(true);
			armorStand.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 140, 1, false, false, false));
			armorStand.setVisible(false);
			armorList.add(armorStand);
			}
	}
	@Override
	public void cast() {
		summonArmorStand();
		setCooldown(25);
	}

	@Override
	public void tick() {
		if(!armorList.isEmpty()) {
			for(ArmorStand stand : armorList) {
				if(!stand.hasPotionEffect(PotionEffectType.GLOWING)) {
					stand.remove();
					armorList.remove(stand);
				}
			}
		}
	}

	@Override
	public void gameEnd() {
		if(!armorList.isEmpty()) {
			for(ArmorStand stand : armorList) {
				stand.remove();
			}
			armorList.clear();
		}
	}

	@Override
	public void gameStart() {
	}

	@Override
	public void enterZone() {
		Activate();
	}
}
