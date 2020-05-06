package fr.vi5team.vi5.runes;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;
import fr.vi5team.vi5.enums.Vi5Team;

public class Rune_piege extends BaseRune {
	ArrayList<Location> piegePos = new ArrayList<Location>();
	private final byte MAX_PIEGE=2;
	private byte posedPiege=0;
	public Rune_piege(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(piegePos.size()>=1) {
			Player p = event.getPlayer();
			PlayerWrapper pWrap = mainref.getPlayerWrapper(p);
			if(pWrap!=null) {
				if(pWrap.getGame()==wraper.getGame()) {
					if(pWrap.getTeam()==Vi5Team.VOLEUR) {
						if(!pWrap.isUnSpottable()) {
							for(Location piegeLoc : piegePos) {
								if (p.getLocation().distanceSquared(piegeLoc)<=1) {
									piegePos.remove(piegeLoc);
									for(Player pGarde : wraper.getGame().getGardeList()) {
										pGarde.playSound(pGarde.getLocation(), Sound.ENTITY_STRAY_DEATH, SoundCategory.MASTER, 0.5f, 0);
										pGarde.playSound(pGarde.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, SoundCategory.MASTER, 1, 2);
										pGarde.sendMessage(ChatColor.GREEN+"Un voleur est tombé dans un piège!");
									}
									p.playSound(p.getLocation(), Sound.ENTITY_STRAY_DEATH, SoundCategory.MASTER, 0.5f, 0);
									p.playSound(p.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, SoundCategory.MASTER, 1, 2);
									p.setWalkSpeed(p.getWalkSpeed()-0.04f);
									p.sendMessage(ChatColor.RED+"Vous êtes tombés dans un piège à ours -> -"+(1-(p.getWalkSpeed()/0.2f))*100+"% vitesse totale");
								}
							}
						}
					}
				}
			}
		}
	}
	public ArrayList<Location> getPiegePos() {
		return piegePos;
	}
	@Override
	public void cast() {
		if(posedPiege<MAX_PIEGE) {
			if(player.isOnGround()) {
				piegePos.add(player.getLocation());
				posedPiege++;
				player.sendMessage(ChatColor.GREEN+"Piège placé en: "+ChatColor.GOLD+ChatColor.UNDERLINE+player.getLocation().getBlockX()+","+player.getLocation().getBlockY()+","+player.getLocation().getBlockZ());
				showAdaptedHotbarItem();
				player.playSound(player.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, SoundCategory.MASTER, 2, 0);
			}else {
				player.sendMessage(ChatColor.GREEN+"Vous devez être au sol!");
			}
		}		
	}

	@Override
	public void tick() {	
		if(player.getInventory().getItemInMainHand().getType()==Material.IRON_TRAPDOOR||player.getInventory().getItemInOffHand().getType()==Material.IRON_TRAPDOOR) {
			for(Player garde : wraper.getGame().getGardeList()) {
				PlayerWrapper wrap = mainref.getPlayerWrapper(garde);
				if(wrap.getRuneSecondaire() instanceof Rune_piege) {
					ArrayList<Location> piegesLoc = ((Rune_piege)wrap.getRuneSecondaire()).getPiegePos();
					for(Location piegeLoc : piegesLoc) {
						player.spawnParticle(Particle.PORTAL, piegeLoc, 1);
					}
				}
			}
		}
    		
	}

	@Override
	public void gameEnd() {
		for(Player thief : wraper.getGame().getVoleurList()) {
			thief.setWalkSpeed(0.2f);
		}	
	}

	@Override
	public void gameStart() {
		Activate();
		showAdaptedHotbarItem();
		for(Player thief : wraper.getGame().getVoleurList()) {
			thief.setWalkSpeed(0.2f);
		}	
	}

	@Override
	public void enterZone() {	
	}
	private void showAdaptedHotbarItem() {
		ItemStack item;
		ItemMeta meta;
		if(MAX_PIEGE-posedPiege!=2) {
			item=new ItemStack(Material.IRON_TRAPDOOR, 1);
		}else {
			item=new ItemStack(Material.IRON_TRAPDOOR, 2);
		}
		meta=item.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE+"Pièges(s) restant(s): "+ChatColor.AQUA+(MAX_PIEGE-posedPiege)+"/"+MAX_PIEGE);
		item.setItemMeta(meta);
		setCastItem(item);
		showCastItem();
	}

}
