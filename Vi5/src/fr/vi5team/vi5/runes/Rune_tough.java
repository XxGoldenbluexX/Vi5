package fr.vi5team.vi5.runes;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_tough extends BaseRune {

	private AttributeModifier modifier;
	
	public Rune_tough(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
		// TODO Auto-generated constructor stub
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
		if (modifier!=null) {
			player.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(modifier);
		}
		player.setHealth(20);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameStart() {
		
	}

	@Override
	public void enterZone() {
		Activate();
		modifier=new AttributeModifier("Rune_tough",4,AttributeModifier.Operation.ADD_NUMBER);
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(modifier);
		player.setHealth(24);
		// TODO Auto-generated method stub
		
	}

}
