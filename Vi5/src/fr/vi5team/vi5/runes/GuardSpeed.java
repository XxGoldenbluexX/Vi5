package fr.vi5team.vi5.runes;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.vi5team.vi5.BaseRune;
import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class GuardSpeed extends BaseRune {

	public GuardSpeed(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, RunesList.GUARDSPEED);
	}

	@Override
	public void cast() {
		
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void gameEnd() {
		player.setWalkSpeed((float) 0.2);
		
	}

	@Override
	public void gameStart() {
		player.setWalkSpeed((float) 0.24);
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.ITALIC+"Workout paid out"));
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN+"Speed is increased by 20%!"));
		
	}

	@Override
	public void enterZone() {
		
	}

}
