package fr.vi5team.vi5.runes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;
import fr.vi5team.vi5.enums.Vi5Team;

public class Rune_thorns extends BaseRune{

	public Rune_thorns(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
		// TODO Auto-generated constructor stub
	}
	@EventHandler
	public void onEntityHit(EntityDamageByEntityEvent event) {
		if(wraper.getGame().getVoleurInsideList().size()>1) {
			Entity damager = event.getDamager();
			Entity receiver = event.getEntity();
			if(damager instanceof Player && receiver instanceof Player) {
				Player pDamager = (Player)damager;
				PlayerWrapper damagerWrap = mainref.getPlayerWrapper(pDamager);
				Player pReceiver = (Player)receiver;
				PlayerWrapper receiverWrap = mainref.getPlayerWrapper(pReceiver);
				if(damagerWrap!=null&&receiverWrap!=null) {
					if(damagerWrap.is_ingame()&&receiverWrap.is_ingame()) {
						if(pDamager==player&&receiverWrap.getTeam()==Vi5Team.VOLEUR) {
							for(Player thief : wraper.getGame().getVoleurInsideList()) {
								if(thief!=pReceiver) {
									thief.damage(0.01);
								}
							}
						}
					}
				}
			}
		}
	}
	@Override
	public void cast() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameStart() {
		Activate();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterZone() {
		// TODO Auto-generated method stub
		
	}

}
