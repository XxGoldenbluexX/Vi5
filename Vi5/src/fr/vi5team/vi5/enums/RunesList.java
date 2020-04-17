package fr.vi5team.vi5.enums;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;

public enum RunesList {
	
	
	INVI("Invisible",RunesType.PASSIF,RunesTiers.PRIMAIRE,
			makeDisplayItem(Material.GLASS_PANE,ChatColor.GOLD+"Invisible",ChatColor.LIGHT_PURPLE+"Makes you invisible while sneaking"),//Item dans le menu
			makeDisplayItem(Material.GLASS_PANE,ChatColor.GOLD+"Invisible",ChatColor.LIGHT_PURPLE+"Makes you invisible while sneaking")//Item dans la hotbar en jeu
			);
	
	
	//////////////////////////////////
	private final String DisplayName;
	private final RunesType Type;
	private final RunesTiers Tiers;
	private final ItemStack MenuItem;
	private final ItemStack HotbarItem;
	//////////////////////////////////
	
	RunesList (String _name,RunesType _type,RunesTiers _tiers, ItemStack _menuItem,ItemStack _hotbarItem){
		DisplayName=_name;
		Type=_type;
		Tiers=_tiers;
		MenuItem=_menuItem;
		HotbarItem=_hotbarItem;
	}
	
	//-----------------------------
	public static ItemStack makeDisplayItem(Material m ,String nam, String... lo) {
		ItemStack it=new ItemStack(m);
		ItemMeta met = it.getItemMeta();
		List<String> l = new ArrayList<String>();
		for (String i : lo) {
			l.add(i);
		}
		met.setLore(l);
		met.setDisplayName(nam);
		it.setItemMeta(met);
		return it;
	}
	//-----------------------------

	public String getDisplayName() {
		return DisplayName;
	}

	public RunesType getType() {
		return Type;
	}

	public RunesTiers getTiers() {
		return Tiers;
	}

	public ItemStack getMenuItem() {
		return MenuItem;
	}

	public ItemStack getHotbarItem() {
		return HotbarItem;
	}

	public byte[] getNumberOfRunesByTier(){
		byte runesNumber[] = new byte[3];
		byte primaryNumber = 0;
		byte secondaryNumber = 0;
		byte tertiaryNumber = 0;
		for(RunesList rune : RunesList.values()) {
			if(rune.getTiers().equals(RunesTiers.PRIMAIRE)) {
				primaryNumber++;
			}
			else {
				if(rune.getTiers().equals(RunesTiers.SECONDAIRE)) {
					secondaryNumber++;
				}
				else {
					if(rune.getTiers().equals(RunesTiers.TERTIAIRE)) {
						tertiaryNumber++;
					}
				}
			}
		}
		runesNumber[0]=primaryNumber;
		runesNumber[1]=secondaryNumber;
		runesNumber[2]=tertiaryNumber;
		return runesNumber;
	}
}
