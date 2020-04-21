package fr.vi5team.vi5.runes;

import java.util.ArrayList;
import java.util.WeakHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cast() {
		ArmorStand n= omniPickUpNear();
		if (n!=null) {
			n.remove();
			nbOmni++;
			omniSpotList.remove(n);
			showAdaptedHotbarItem();
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
			meta.setDisplayName(ChatColor.GOLD+"Omnicapteurs: "+ChatColor.AQUA+nbOmni);
			item.setItemMeta(meta);
		}else {
			item=new ItemStack(Material.LEVER);
			meta=item.getItemMeta();
			meta.setDisplayName(ChatColor.RED+"No more Omnicapteurs");
			item.setItemMeta(meta);
		}
		setCastItem(item);
		showHotbarItem();
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
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD+"A thief has been spotted!"));
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
		for (ArmorStand omni : omniSpotList.keySet()) {
			omni.remove();
			omniSpotList.clear();
		}
	}

	@Override
	public void gameStart() {
		Activate();
		nbOmni=MAX_OMNI;
	}

	@Override
	public void enterZone() {
		// TODO Auto-generated method stub
		
	}

}
