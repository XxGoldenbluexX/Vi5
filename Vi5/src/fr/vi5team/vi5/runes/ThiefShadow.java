package fr.vi5team.vi5.runes;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import fr.vi5team.vi5.BaseRune;
import fr.vi5team.vi5.Game;
import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;
import fr.vi5team.vi5.enums.Vi5Team;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ThiefShadow extends BaseRune{
	boolean isPlaced;
	Game g;
	ArmorStand Shadow;
	Location loc;
	HashMap<Player, PlayerWrapper> playersInGame;
	public ThiefShadow(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, RunesList.SHADOW);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cast() {
		if(!isPlaced) {
			loc = player.getLocation();
			Shadow = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			Shadow.setVisible(true);
			Shadow.setHelmet(new ItemStack(Material.COAL_BLOCK));
			Shadow.setArms(false);
			Shadow.setCollidable(false);
			Shadow.setInvulnerable(true);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN+"You have placed your shadow, "+ChatColor.RED+"beware of near guards!"));
			ItemStack it = Rune.getHotbarItem().clone();
			player.getInventory().setItem(2, it);
			isPlaced = true;
		}
		if(isPlaced) {
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
			player.teleport(Shadow);
			Shadow.remove();
			isPlaced=false;
		}
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(isPlaced) {
			Player p=event.getPlayer();
			if(playersInGame.get(p).getTeam()==Vi5Team.GARDE) {
				if(p.getLocation()==loc) {
					Shadow.remove();
					player.setHealth(0);
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN+"You safely recalled on your shadow, "+ChatColor.RED+"but guards heard it!"));	
					isPlaced=false;
				}
			}	
		}
	}
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameEnd() {
		// TODO Auto-generated method stub
		if(isPlaced) {
			Shadow.remove();
		}
	}

	@Override
	public void gameStart() {
		isPlaced = false;
		g=wraper.getGame();
		playersInGame = g.playersInGame();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterZone() {
		// TODO Auto-generated method stub
		
	}

}
