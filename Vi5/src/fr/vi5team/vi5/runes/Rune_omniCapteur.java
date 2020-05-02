package fr.vi5team.vi5.runes;

import java.util.ArrayList;
import java.util.WeakHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Rune_omniCapteur extends BaseRune {

	private static final byte MAX_OMNI=1;
	private static final double OMNI_SQUARED_RANGE=16;
	private static final double OMNI_SQUARED_PICK_UP_RANGE=4;
	private byte nbOmni=MAX_OMNI;
	private final WeakHashMap<ArmorStand,ArrayList<Player>> omniSpotList = new WeakHashMap<ArmorStand,ArrayList<Player>>();
	
	public Rune_omniCapteur(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}

	@Override
	public void cast() {
		ArmorStand n= omniPickUpNear();
		if (n!=null) {
			n.remove();
			nbOmni++;
			omniSpotList.remove(n);
			setCooldown(2);
			showAdaptedHotbarItem();
			player.getWorld().playSound(player.getLocation(),Sound.BLOCK_LADDER_HIT, SoundCategory.MASTER, 0.8f, 2f);
			player.getWorld().playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_HAT, SoundCategory.MASTER, 1f, 0.1f);
		}else {
			if (nbOmni>=1) {
				ArmorStand omni = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
				if (omni!=null) {
					omni.setHelmet(new ItemStack(Material.REDSTONE_BLOCK));
					omni.setBasePlate(false);
					omni.setArms(false);
					omni.setMarker(true);
					omni.setSmall(true);
					omniSpotList.put(omni,new ArrayList<Player>());
					nbOmni--;
					showAdaptedHotbarItem();
					player.getWorld().playSound(player.getLocation(),Sound.ENTITY_VEX_HURT, SoundCategory.MASTER, 2f, 0.1f);
					player.getWorld().playSound(player.getLocation(),Sound.ITEM_FLINTANDSTEEL_USE, SoundCategory.MASTER, 2f, 0.1f);
				}
			}else {
				showAdaptedHotbarItem();
			}
		}
	}
	private void showAdaptedHotbarItem() {
		ItemStack item;
		ItemMeta meta;
		if (nbOmni>=1) {
			item=new ItemStack(Material.REDSTONE_TORCH,nbOmni);
			meta=item.getItemMeta();
			meta.setDisplayName(ChatColor.GOLD+"Omnicapteurs: "+ChatColor.AQUA+nbOmni+"/"+MAX_OMNI);
			item.setItemMeta(meta);
		}else {
			item=new ItemStack(Material.LEVER);
			meta=item.getItemMeta();
			meta.setDisplayName(ChatColor.RED+"Omnicapteurs posés!");
			item.setItemMeta(meta);
		}
		setCastItem(item);
		showCastItem();
	}
	
	private ArmorStand omniPickUpNear() {
		ArrayList<ArmorStand> l = new ArrayList<ArmorStand>();
		for (ArmorStand a : omniSpotList.keySet()) {
			if (player.getLocation().distanceSquared(a.getLocation())<=OMNI_SQUARED_PICK_UP_RANGE) {
				l.add(a);
			}
		}
		if (l.size()<1) {
			return null;
		}else {
			ArmorStand nearest = l.get(0);
			double nearDist = player.getLocation().distanceSquared(nearest.getLocation());
			for (ArmorStand a : l) {
				double d = a.getLocation().distanceSquared(player.getLocation());
				if (d<nearDist) {
					nearest = a;
					nearDist=d;
				}
			}
			return nearest;
		}
	}

	@Override
	public void tick() {
		if (!wraper.isJammed()) {
			for (ArmorStand omni : omniSpotList.keySet()) {
				for (Player p : wraper.getGame().getVoleurInsideList()) {
					if (!omniSpotList.get(omni).contains(p) && p.getLocation().distanceSquared(omni.getLocation())<=OMNI_SQUARED_RANGE) {
						omniSpotList.get(omni).add(p);
						PlayerWrapper wrap = wraper.getGame().getPlayerWrapper(p);
						wrap.setGlow(true);
						wrap.setOmnispotted(true);
						if (wrap.isSondable()) {
							player.playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.MASTER, 0.3f, 0.1f);
							player.playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_COW_BELL, SoundCategory.MASTER, 2f, 0.5f);
							player.playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_BASS, SoundCategory.MASTER, 2f, 0.1f);
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD+"Voleur repéré!"));
							p.sendMessage(ChatColor.AQUA+""+ChatColor.UNDERLINE+"Une balise vous a repéré!");
						}
					}else if (omniSpotList.get(omni).contains(p) && p.getLocation().distanceSquared(omni.getLocation())>OMNI_SQUARED_RANGE){
						PlayerWrapper wrap = wraper.getGame().getPlayerWrapper(p);
						omniSpotList.get(omni).remove(p);
						wrap.setGlow(false);
						wrap.setOmnispotted(false);
					}
				}
			}
		}
	}

	@Override
	public void gameEnd() {
		System.out.println("endOmni");
		for (ArmorStand omni : omniSpotList.keySet()) {
			System.out.println(omni.getName()+" deleted");
			omni.remove();
			omniSpotList.clear();
		}
	}

	@Override
	public void gameStart() {
		Activate();
		showAdaptedHotbarItem();
		nbOmni=MAX_OMNI;
		player.getInventory().setItem(0, makeSpamSword(Material.DIAMOND_SWORD,7));
	}

	@Override
	public void enterZone() {
	}

}
