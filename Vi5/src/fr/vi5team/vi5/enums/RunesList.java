package fr.vi5team.vi5.enums;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.Vi5Team;
import fr.vi5team.vi5.runes.BaseRune;
import fr.vi5team.vi5.runes.Rune_inviSneak;
import fr.vi5team.vi5.runes.Rune_omniCapteur;
import fr.vi5team.vi5.runes.Rune_speed;
import fr.vi5team.vi5.runes.Rune_surcharge;

public enum RunesList {
	
	
	INVI("Invisible",RunesType.PASSIF,RunesTiers.PRIMAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.GLASS_PANE,ChatColor.GOLD+"Invisible",ChatColor.LIGHT_PURPLE+"Makes you invisible while sneaking"),//Item dans le menu
			makeDisplayItem(Material.GLASS_PANE,ChatColor.GOLD+"Invisible",ChatColor.LIGHT_PURPLE+"Makes you invisible while sneaking")//Item dans la hotbar en jeu
			),
	OMNI("Omnicapteur",RunesType.SPELL,RunesTiers.PRIMAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.REDSTONE_TORCH, ChatColor.GOLD+"Omnicapteur", ChatColor.LIGHT_PURPLE+"Drop to use, it spot thieves 3 blocks around it"),
			makeDisplayItem(Material.REDSTONE_TORCH, ChatColor.GOLD+"Omnicapteur", ChatColor.LIGHT_PURPLE+"Drop to use, it spot thieves 3 blocks around it")
			),
	SURCHARGE("Overcharge",RunesType.SPELL,RunesTiers.SECONDAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.FIREWORK_ROCKET, ChatColor.GOLD+"Overcharge", ChatColor.LIGHT_PURPLE+"Give an insane amount of movement speed and strength","20"+ChatColor.LIGHT_PURPLE+"s cooldown"),
			makeDisplayItem(Material.FIREWORK_ROCKET, ChatColor.GOLD+"Overcharge", ChatColor.LIGHT_PURPLE+"Drop it to have an insane amount of movement speed and strength")
			),
	SPEED("Sprinter",RunesType.PASSIF,RunesTiers.TERTIAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.FEATHER, ChatColor.GOLD+"Sprinter", ChatColor.LIGHT_PURPLE+"Movement speed get increased by 20%"),
			makeDisplayItem(Material.FEATHER, ChatColor.GOLD+"Sprinter", ChatColor.LIGHT_PURPLE+"Movement speed is increased by 20%")
			),
	COP("Cop",RunesType.PASSIF,RunesTiers.PRIMAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.DIAMOND_SWORD, ChatColor.GOLD+"Cop", ChatColor.LIGHT_PURPLE+"A special weapon to one-hit-kill thieves"),
			makeDisplayItem(Material.END_ROD, ChatColor.GOLD+"Cop", ChatColor.LIGHT_PURPLE+"A special weapon to one-hit-kill thieves")
			),
	TOUGH("Tough",RunesType.PASSIF,RunesTiers.TERTIAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.IRON_CHESTPLATE, ChatColor.GOLD+"Tough", ChatColor.LIGHT_PURPLE+"All damage get reduced by 20%"),
			makeDisplayItem(Material.IRON_CHESTPLATE, ChatColor.GOLD+"Tough", ChatColor.LIGHT_PURPLE+"All damage are reduced by 20%")
			),
	DOUBLE_JUMP("DoubleJump",RunesType.PASSIF,RunesTiers.SECONDAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.RABBIT_FOOT, ChatColor.GOLD+"DoubleJump", ChatColor.LIGHT_PURPLE+"Get a second jump after the first one"),
			makeDisplayItem(Material.RABBIT_FOOT, ChatColor.GOLD+"DoubleJump", ChatColor.LIGHT_PURPLE+"Press jump twice to perform another jump")
			),
	SHADOW("Shadow",RunesType.SPELL,RunesTiers.SECONDAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.COAL, ChatColor.GOLD+"Shadow", ChatColor.LIGHT_PURPLE+"Place a show to recall later on it, and you die if a guard find it!"),
			makeDisplayItem(Material.COAL, ChatColor.GOLD+"Shadow", ChatColor.LIGHT_PURPLE+"Place a show to recall later on it, and you die if a guard find it!")
			),
	LANTERN("Lantern",RunesType.SPELL,RunesTiers.SECONDAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.LANTERN, ChatColor.GOLD+"Lantern", ChatColor.LIGHT_PURPLE+"Place a lantern for a friend to pick it and so teleport to you"),
			makeDisplayItem(Material.LANTERN, ChatColor.GOLD+"Lantern", ChatColor.LIGHT_PURPLE+"Place a lantern for a friend to pick it and so teleport to you")
			),
	BUSH("Bush",RunesType.PASSIF,RunesTiers.PRIMAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.GRASS, ChatColor.GOLD+"Bush", ChatColor.LIGHT_PURPLE+"Standing in 2 blocks bushes makes you invisible"),
			makeDisplayItem(Material.GRASS, ChatColor.GOLD+"Bush", ChatColor.LIGHT_PURPLE+"Standing in 2 blocks bushes makes you invisible")
			),
	BUILDER("Builder",RunesType.SPELL,RunesTiers.SECONDAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.STONE_BRICK_WALL, ChatColor.GOLD+"Builder", ChatColor.LIGHT_PURPLE+"Allow you to add 2 additionnals walls in the map"),
			makeDisplayItem(Material.STONE_BRICK_WALL, ChatColor.GOLD+"Builder", ChatColor.LIGHT_PURPLE+"Allow you to add 2 additionnals walls in the map")
			),
	PECHEUR("Fisherman",RunesType.PASSIF,RunesTiers.TERTIAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.FISHING_ROD, ChatColor.GOLD+"Fisherman", ChatColor.LIGHT_PURPLE+"Get an extra-fishing rod to grab those thieves"),
			makeDisplayItem(Material.FISHING_ROD, ChatColor.GOLD+"Fisherman", ChatColor.LIGHT_PURPLE+"Get an extra-fishing rod to grab those thieves")
			),
	CROCHETEUR("Trickster",RunesType.PASSIF,RunesTiers.SECONDAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.TRIPWIRE_HOOK, ChatColor.GOLD+"Trickster", ChatColor.LIGHT_PURPLE+"Allow to pass through guards' walls"),
			makeDisplayItem(Material.TRIPWIRE_HOOK, ChatColor.GOLD+"Trickster", ChatColor.LIGHT_PURPLE+"Allow to pass through guards' walls")
			),
	SCANNER("Scanner",RunesType.SPELL,RunesTiers.PRIMAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.CLOCK, ChatColor.GOLD+"Scanner", ChatColor.LIGHT_PURPLE+"Allow you to detect all guards in a large ray every x seconds"),
			makeDisplayItem(Material.CLOCK, ChatColor.GOLD+"Scanner", ChatColor.LIGHT_PURPLE+"Allow you to detect all guards in a large ray every x seconds")
			);
	//////////////////////////////////
	private final String DisplayName;
	private final RunesType Type;
	private final RunesTiers Tiers;
	private final ItemStack MenuItem;
	private final ItemStack HotbarItem;
	private final Vi5Team Team;
	//////////////////////////////////
	
	RunesList (String _name,RunesType _type,RunesTiers _tiers,Vi5Team _team, ItemStack _menuItem,ItemStack _hotbarItem){
		DisplayName=_name;
		Type=_type;
		Tiers=_tiers;
		MenuItem=_menuItem;
		HotbarItem=_hotbarItem;
		Team=_team;
	}
	
	//-----------------------------
	private static ItemStack makeDisplayItem(Material m ,String nam, String... lo) {
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

	public Vi5Team getTeam() {
		return Team;
	}
	public BaseRune spawn(RunesList r,Vi5Main main,PlayerWrapper w,Player p) {
		switch (r) {
		case BUSH:
			break;
		case COP:
			break;
		case DOUBLE_JUMP:
			break;
		case SPEED:
			return new Rune_speed(main, w, p, r);
		case SURCHARGE:
			return new Rune_surcharge(main, w, p, r);
		case INVI:
			return new Rune_inviSneak(main, w, p, r);
		case LANTERN:
			break;
		case SHADOW:
			break;
		case TOUGH:
			break;
		case OMNI:
			return new Rune_omniCapteur(main, w, p, r);
		case BUILDER:
			break;
		default:
			return null;
		}
		return null;
	}
	public static ArrayList<RunesList> getGardePrimaires(){
		ArrayList<RunesList> l = new ArrayList<RunesList>();
		for (RunesList r : RunesList.values()) {
			if (r.getTeam()==Vi5Team.GARDE && r.getTiers()==RunesTiers.PRIMAIRE) {
				l.add(r);
			}
		}
		return l;
	}
	public static ArrayList<RunesList> getGardeSecondaires(){
		ArrayList<RunesList> l = new ArrayList<RunesList>();
		for (RunesList r : RunesList.values()) {
			if (r.getTeam()==Vi5Team.GARDE && r.getTiers()==RunesTiers.SECONDAIRE) {
				l.add(r);
			}
		}
		return l;
	}
	public static ArrayList<RunesList> getGardeTertiaires(){
		ArrayList<RunesList> l = new ArrayList<RunesList>();
		for (RunesList r : RunesList.values()) {
			if (r.getTeam()==Vi5Team.GARDE && r.getTiers()==RunesTiers.TERTIAIRE) {
				l.add(r);
			}
		}
		return l;
	}
	public static ArrayList<RunesList> getVoleurPrimaires(){
		ArrayList<RunesList> l = new ArrayList<RunesList>();
		for (RunesList r : RunesList.values()) {
			if (r.getTeam()==Vi5Team.VOLEUR && r.getTiers()==RunesTiers.PRIMAIRE) {
				l.add(r);
			}
		}
		return l;
	}
	public static ArrayList<RunesList> getVoleurSecondaires(){
		ArrayList<RunesList> l = new ArrayList<RunesList>();
		for (RunesList r : RunesList.values()) {
			if (r.getTeam()==Vi5Team.VOLEUR && r.getTiers()==RunesTiers.SECONDAIRE) {
				l.add(r);
			}
		}
		return l;
	}
	public static ArrayList<RunesList> getVoleurTertiaires(){
		ArrayList<RunesList> l = new ArrayList<RunesList>();
		for (RunesList r : RunesList.values()) {
			if (r.getTeam()==Vi5Team.VOLEUR && r.getTiers()==RunesTiers.TERTIAIRE) {
				l.add(r);
			}
		}
		return l;
	}
}
