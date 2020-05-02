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
import fr.vi5team.vi5.runes.BaseRune;
import fr.vi5team.vi5.runes.Rune_builder;
import fr.vi5team.vi5.runes.Rune_bush;
import fr.vi5team.vi5.runes.Rune_cop;
import fr.vi5team.vi5.runes.Rune_crocheteur;
import fr.vi5team.vi5.runes.Rune_doubleJump;
import fr.vi5team.vi5.runes.Rune_fisherman;
import fr.vi5team.vi5.runes.Rune_inviSneak;
import fr.vi5team.vi5.runes.Rune_lantern;
import fr.vi5team.vi5.runes.Rune_ombre;
import fr.vi5team.vi5.runes.Rune_omniCapteur;
import fr.vi5team.vi5.runes.Rune_scanner;
import fr.vi5team.vi5.runes.Rune_smokePool;
import fr.vi5team.vi5.runes.Rune_sonar;
import fr.vi5team.vi5.runes.Rune_speed;
import fr.vi5team.vi5.runes.Rune_surcharge;
import fr.vi5team.vi5.runes.Rune_thorns;
import fr.vi5team.vi5.runes.Rune_tough;

public enum RunesList {
	
	COP("CRS",RunesType.PASSIF,RunesTiers.PRIMAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.DIAMOND_SWORD, ChatColor.GOLD+"CRS", ChatColor.LIGHT_PURPLE+"Un bâton légendaire pouvant tuer en un coup!"),
			makeDisplayItem(Material.END_ROD, ChatColor.GOLD+"CRS", ChatColor.LIGHT_PURPLE+"Un bâton légendaire pouvant tuer en un coup!")
			),
	OMNI("Omnicapteur",RunesType.SPELL,RunesTiers.PRIMAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.REDSTONE_TORCH, ChatColor.GOLD+"Omnicapteur", ChatColor.LIGHT_PURPLE+"Repérez les voleurs à 3 blocks autour de la balise"),
			makeDisplayItem(Material.REDSTONE_TORCH, ChatColor.GOLD+"Omnicapteur", ChatColor.LIGHT_PURPLE+"Repérez les voleurs à 3 blocks autour de la balise")
			),
	SONAR("Sonar",RunesType.PASSIF,RunesTiers.PRIMAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.COMPASS, ChatColor.GOLD+"Sonar", ChatColor.LIGHT_PURPLE+"Toutes les 5 secondes, permet de savoir si un voleur est prêt de vous"),
			makeDisplayItem(Material.COMPASS, ChatColor.GOLD+"Sonar", ChatColor.LIGHT_PURPLE+"Toutes les 5 secondes, permet de savoir si un voleur est prêt de vous")
			),
	SURCHARGE("Surcharge",RunesType.SPELL,RunesTiers.SECONDAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.FIREWORK_ROCKET, ChatColor.LIGHT_PURPLE+"Surcharge", ChatColor.LIGHT_PURPLE+"Grand bonus en vitesse et force sur utilisation"),
			makeDisplayItem(Material.FIREWORK_ROCKET, ChatColor.LIGHT_PURPLE+"Surcharge", ChatColor.LIGHT_PURPLE+"Grand bonus en vitesse et force sur utilisation")
			),
	BUILDER("Mur",RunesType.SPELL,RunesTiers.SECONDAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.BRICK_WALL, ChatColor.LIGHT_PURPLE+"Mur", ChatColor.LIGHT_PURPLE+"Posez 2 murs en plus dans la carte"),
			makeDisplayItem(Material.BRICK_WALL, ChatColor.LIGHT_PURPLE+"Mur", ChatColor.LIGHT_PURPLE+"Posez 2 murs en plus dans la carte")
			),
	SPEED("Le Fast",RunesType.PASSIF,RunesTiers.TERTIAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.FEATHER, ChatColor.GREEN+"Le Fast", ChatColor.LIGHT_PURPLE+"Vitesse accrue de 20%"),
			makeDisplayItem(Material.FEATHER, ChatColor.GREEN+"Le Fast", ChatColor.LIGHT_PURPLE+"Vitesse accrue de 20%")
			),
	THORNS("Cactus",RunesType.PASSIF,RunesTiers.TERTIAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.CACTUS, ChatColor.GOLD+"Cactus", ChatColor.LIGHT_PURPLE+"Endommagez de 0 tous les voleurs quand vous en tapez un"),
			makeDisplayItem(Material.CACTUS, ChatColor.GOLD+"Cactus", ChatColor.LIGHT_PURPLE+"Endommagez de 0 tous les voleurs quand vous en tapez un")
			),
	PECHEUR("Pêcheur",RunesType.PASSIF,RunesTiers.TERTIAIRE,Vi5Team.GARDE,
			makeDisplayItem(Material.FISHING_ROD, ChatColor.GREEN+"Pêcheur", ChatColor.LIGHT_PURPLE+"Obtenez une canne à pêche pour attraper les voleurs"),
			makeDisplayItem(Material.FISHING_ROD, ChatColor.GREEN+"Pêcheur", ChatColor.LIGHT_PURPLE+"Obtenez une canne à pêche pour attraper les voleurs")
			),
	//RUNE VOLEUR MTN
	INVI("Invisible",RunesType.PASSIF,RunesTiers.PRIMAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.GLASS_PANE,ChatColor.GOLD+"Invisible",ChatColor.LIGHT_PURPLE+"Devenez invisible en vous accroupissant"),//Item dans le menu
			makeDisplayItem(Material.GLASS_PANE,ChatColor.GOLD+"Invisible",ChatColor.LIGHT_PURPLE+"Devenez invisible en vous accroupissant")//Item dans la hotbar en jeu
			),
	BUSH("Buisson",RunesType.PASSIF,RunesTiers.PRIMAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.GRASS, ChatColor.GOLD+"Buisson", ChatColor.LIGHT_PURPLE+"Les buissons de 2 de haut vous rendent invisible"),
			makeDisplayItem(Material.GRASS, ChatColor.GOLD+"Buisson", ChatColor.LIGHT_PURPLE+"Les buissons de 2 de haut vous rendent invisible")
			),
	SCANNER("Scanner",RunesType.SPELL,RunesTiers.PRIMAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.CLOCK, ChatColor.GOLD+"Scanner", ChatColor.LIGHT_PURPLE+"Vous permet de détecter tous les gardes toutes les 25 secondes"),
			makeDisplayItem(Material.CLOCK, ChatColor.GOLD+"Scanner", ChatColor.LIGHT_PURPLE+"Vous permet de détecter tous les gardes toutes les 25 secondes")
			),
	DOUBLE_JUMP("Double Saut",RunesType.PASSIF,RunesTiers.SECONDAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.RABBIT_FOOT, ChatColor.LIGHT_PURPLE+"Double Saut", ChatColor.LIGHT_PURPLE+"Sautez une seconde fois après le premier"),
			makeDisplayItem(Material.RABBIT_FOOT, ChatColor.LIGHT_PURPLE+"Double Saut", ChatColor.LIGHT_PURPLE+"Sautez une seconde fois après le premier")
			),
	SHADOW("Ombre",RunesType.SPELL,RunesTiers.SECONDAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.COAL, ChatColor.LIGHT_PURPLE+"Ombre", ChatColor.LIGHT_PURPLE+"Placez une ombre pour revenir dessus plus tard, et mourrez si un garde la trouve!"),
			makeDisplayItem(Material.COAL, ChatColor.LIGHT_PURPLE+"Ombre", ChatColor.LIGHT_PURPLE+"Placez une ombre pour revenir dessus plus tard, et mourrez si un garde la trouve!")
			),
	LANTERN("Lanterne",RunesType.SPELL,RunesTiers.SECONDAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.LANTERN, ChatColor.LIGHT_PURPLE+"Lanterne", ChatColor.LIGHT_PURPLE+"Placez une lanterne pour qu'un voleur la trouve et se téléporte à vous"),
			makeDisplayItem(Material.LANTERN, ChatColor.LIGHT_PURPLE+"Lanterne", ChatColor.LIGHT_PURPLE+"Placez une lanterne pour qu'un voleur la trouve et se téléporte à vous")
			),
	SMOKE_POOL("Bain de fumée",RunesType.SPELL,RunesTiers.SECONDAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.GUNPOWDER, ChatColor.LIGHT_PURPLE+"Bain de fumée", ChatColor.LIGHT_PURPLE+"Faites apparaître un cercle de fumée où vous êtes invisible et insondable"),
			makeDisplayItem(Material.GUNPOWDER, ChatColor.LIGHT_PURPLE+"Bain de fumée", ChatColor.LIGHT_PURPLE+"Faites apparaître un cercle de fumée où vous êtes invisible et insondable")
			),
	CROCHETEUR("Crocheteur",RunesType.PASSIF,RunesTiers.SECONDAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.TRIPWIRE_HOOK, ChatColor.LIGHT_PURPLE+"Crocheteur", ChatColor.LIGHT_PURPLE+"Vous permet de passer les murs des gardes"),
			makeDisplayItem(Material.TRIPWIRE_HOOK, ChatColor.LIGHT_PURPLE+"Crocheteur", ChatColor.LIGHT_PURPLE+"Vous permet de passer les murs des gardes")
			),
	TOUGH("Solide",RunesType.PASSIF,RunesTiers.TERTIAIRE,Vi5Team.VOLEUR,
			makeDisplayItem(Material.IRON_CHESTPLATE, ChatColor.GREEN+"Solide", ChatColor.LIGHT_PURPLE+"Obtenez 2 coeurs de plus"),
			makeDisplayItem(Material.IRON_CHESTPLATE, ChatColor.GREEN+"Solide", ChatColor.LIGHT_PURPLE+"Obtenez 2 coeurs de plus")
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
		case DOUBLE_JUMP:
			return new Rune_doubleJump(main, w, p, r);
		case SPEED:
			return new Rune_speed(main, w, p, r);
		case SURCHARGE:
			return new Rune_surcharge(main, w, p, r);
		case INVI:
			return new Rune_inviSneak(main, w, p, r);
		case TOUGH:
			return new Rune_tough(main, w, p, r);
		case OMNI:
			return new Rune_omniCapteur(main, w, p, r);
		case SHADOW:
			return new Rune_ombre(main, w, p, r);
		case BUILDER:
			return new Rune_builder(main, w, p, r);
		case CROCHETEUR:
			return new Rune_crocheteur(main, w, p, r);
		case PECHEUR:
			return new Rune_fisherman(main, w, p, r);
		case BUSH:
			return new Rune_bush(main, w, p, r);
		case SCANNER:
			return new Rune_scanner(main, w, p, r);
		case LANTERN:
			return new Rune_lantern(main, w, p, r);
		case COP:
			return new Rune_cop(main, w, p, r);
		case SONAR:
			return new Rune_sonar(main, w, p, r);
		case SMOKE_POOL:
			return new Rune_smokePool(main,w,p,r);
		case THORNS:
			return new Rune_thorns(main,w,p,r);
		default:
			break;
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
