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
			),
	WARD("Warder",RunesType.SPELL,RunesTiers.PRIMAIRE,
			makeDisplayItem(Material.REDSTONE_TORCH, ChatColor.GOLD+"Warder", ChatColor.LIGHT_PURPLE+"Drop to use, it spot thieves 3 blocks around it"),
			makeDisplayItem(Material.REDSTONE_TORCH, ChatColor.GOLD+"Warder", ChatColor.LIGHT_PURPLE+"Drop to use, it spot thieves 3 blocks around it")
			),
	GUARDSPEED("Sprinter",RunesType.PASSIF,RunesTiers.TERTIAIRE,
			makeDisplayItem(Material.FEATHER, ChatColor.GOLD+"Sprinter", ChatColor.LIGHT_PURPLE+"Movement speed get increased by 20%"),
			makeDisplayItem(Material.FEATHER, ChatColor.GOLD+"Sprinter", ChatColor.LIGHT_PURPLE+"Movement speed is increased by 20%")
			),
	COP("Cop",RunesType.PASSIF,RunesTiers.PRIMAIRE,
			makeDisplayItem(Material.DIAMOND_SWORD, ChatColor.GOLD+"Cop", ChatColor.LIGHT_PURPLE+"A special weapon to one-hit-kill thieves"),
			makeDisplayItem(Material.DIAMOND_SWORD, ChatColor.GOLD+"Cop", ChatColor.LIGHT_PURPLE+"A special weapon to one-hit-kill thieves")
			),
	TOUGH("Tough",RunesType.PASSIF,RunesTiers.TERTIAIRE,
			makeDisplayItem(Material.IRON_CHESTPLATE, ChatColor.GOLD+"Tough", ChatColor.LIGHT_PURPLE+"All damage get reduced by 20%"),
			makeDisplayItem(Material.IRON_CHESTPLATE, ChatColor.GOLD+"Tough", ChatColor.LIGHT_PURPLE+"All damage are reduced by 20%")
			),
	DOUBLE_JUMP("DoubleJump",RunesType.PASSIF,RunesTiers.SECONDAIRE,
			makeDisplayItem(Material.RABBIT_FOOT, ChatColor.GOLD+"DoubleJump", ChatColor.LIGHT_PURPLE+"Get a second jump after the first one"),
			makeDisplayItem(Material.RABBIT_FOOT, ChatColor.GOLD+"DoubleJump", ChatColor.LIGHT_PURPLE+"Press jump twice to perform another jump")
			),
	SHADOW("Shadow",RunesType.SPELL,RunesTiers.SECONDAIRE,
			makeDisplayItem(Material.COAL, ChatColor.GOLD+"Shadow", ChatColor.LIGHT_PURPLE+"Place a show to recall later on it, and you die if a guard find it!"),
			makeDisplayItem(Material.COAL, ChatColor.GOLD+"Shadow", ChatColor.LIGHT_PURPLE+"Place a show to recall later on it, and you die if a guard find it!")
			),
	LANTERN("Lantern",RunesType.SPELL,RunesTiers.SECONDAIRE,
			makeDisplayItem(Material.LANTERN, ChatColor.GOLD+"Lantern", ChatColor.LIGHT_PURPLE+"Place a lantern for a friend to pick it and so teleport to you"),
			makeDisplayItem(Material.LANTERN, ChatColor.GOLD+"Lantern", ChatColor.LIGHT_PURPLE+"Place a lantern for a friend to pick it and so teleport to you")
			),
	BUSH("Bush",RunesType.PASSIF,RunesTiers.PRIMAIRE,
			makeDisplayItem(Material.GRASS, ChatColor.GOLD+"Bush", ChatColor.LIGHT_PURPLE+"Standing in 2 blocks bushes makes you invisible"),
			makeDisplayItem(Material.GRASS, ChatColor.GOLD+"Bush", ChatColor.LIGHT_PURPLE+"Standing in 2 blocks bushes makes you invisible")
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
