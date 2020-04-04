package fr.vi5team.vi5.interfaces;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Vi5Interfaces implements Listener{
	private Inventory maingui;
	private ArrayList<Inventory> guardGUIlist = new ArrayList<Inventory>();
	private ArrayList<Inventory> thiefGUIlist = new ArrayList<Inventory>();
	
	private enum InterfacedRunes{
		INVI,BUSH,SCANNER,
		OMBRE,MERDE,DOUBLE_JUMP,//A completer
		CRS,BALISE,SONNAR,
		SURCHARGE,MUR,
		RAPIDITE,PECHEUR//<-oui, pecheur c'est le nom qui représente le mieu cette rune
	}
	
	public void onEnable() {
		maingui = Bukkit.createInventory(null, 9, "Vi5_Menu");
		maingui.setItem(1,makeGuiItem(false,Material.DIAMOND,ChatColor.BLUE+"Guard's Runes",ChatColor.DARK_PURPLE+"Choose your "+ChatColor.BLUE+"Guard's "+ChatColor.DARK_PURPLE+"runes"));
		maingui.setItem(4,makeGuiItem(false,Material.FIREWORK_ROCKET,ChatColor.GREEN+"Launch Game"));
		maingui.setItem(7,makeGuiItem(false,Material.REDSTONE,ChatColor.RED+"Thief's Runes",ChatColor.DARK_PURPLE+"Choose your "+ChatColor.RED+"Thief's "+ChatColor.DARK_PURPLE+"runes"));
	}
	public void openGuardGui(Player player) {
		Inventory guardgui = Bukkit.createInventory(player, 45, ChatColor.BLUE+"Guard's Runes");
		guardgui.setItem(0,makeGuiItem(false,Material.ORANGE_DYE,ChatColor.GOLD+"Main Runes"));
		guardgui.setItem(18,makeGuiItem(false,Material.PURPLE_DYE,ChatColor.DARK_PURPLE+"Secondary Runes"));
		guardgui.setItem(36,makeGuiItem(false,Material.CYAN_DYE,ChatColor.AQUA+"Tertiary Runes"));
		guardgui.setItem(2,makeGuiItem(playerHasRune(InterfacedRunes.CRS),Material.DIAMOND_SWORD,ChatColor.GOLD+"CRS",ChatColor.LIGHT_PURPLE+"Permet de tuer en un coup","",ChatColor.GRAY+""+ChatColor.ITALIC+"Grosse matraque dans tes fesses"));
		guardgui.setItem(4,makeGuiItem(playerHasRune(InterfacedRunes.BALISE),Material.REDSTONE_TORCH,ChatColor.GOLD+"Balise",ChatColor.LIGHT_PURPLE+"Permet de poser une balise",ChatColor.LIGHT_PURPLE+"qui révèle les voleurs à proximité"));
		guardgui.setItem(6,makeGuiItem(playerHasRune(InterfacedRunes.SONNAR),Material.COMPASS,ChatColor.GOLD+"Sonnar",ChatColor.LIGHT_PURPLE+"Permet d'émettre un son toute les "+ChatColor.WHITE+"5s,",ChatColor.LIGHT_PURPLE+"Son aigu si un voleur est proche"));
		guardgui.setItem(20,makeGuiItem(playerHasRune(InterfacedRunes.SURCHARGE),Material.FIREWORK_ROCKET,ChatColor.DARK_PURPLE+"Surcharge",ChatColor.LIGHT_PURPLE+"Permet d'augmenter grandement sa vitesse et sa force pour 1s",ChatColor.LIGHT_PURPLE+"Délai de récupération: "+ChatColor.WHITE+"20s"));
		guardgui.setItem(22,makeGuiItem(playerHasRune(InterfacedRunes.MUR),Material.BRICK_WALL,ChatColor.DARK_PURPLE+"Mur",ChatColor.LIGHT_PURPLE+"Permet de poser 2 murs sur les blocs violets de la carte",ChatColor.LIGHT_PURPLE+"Les gardes et les voleurs [Crocheteurs]",ChatColor.LIGHT_PURPLE+"peuvent passer à travers"));
		guardgui.setItem(38,makeGuiItem(playerHasRune(InterfacedRunes.RAPIDITE),Material.FEATHER,ChatColor.AQUA+"Rapidité",ChatColor.LIGHT_PURPLE+"Vitesse de déplacement plus élevée"));
		guardgui.setItem(40,makeGuiItem(playerHasRune(InterfacedRunes.PECHEUR),Material.FISHING_ROD,ChatColor.AQUA+"Pêcheur",ChatColor.LIGHT_PURPLE+"Permet d'acrocher un voleur",ChatColor.LIGHT_PURPLE+"pour garder sa trace ou le tirer vers soi"));
		player.openInventory(guardgui);
		guardGUIlist.add(guardgui);
	}
	
	private ItemStack makeGuiItem(boolean selected,Material mat, String name, String... lore) {
		//Tu aura besoin du player pour vérifier si il a la rune
		ItemStack item=new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		List<String> listLore=new ArrayList<String>();

		if (selected) {
			listLore.add(ChatColor.DARK_GREEN+"\n"+ChatColor.UNDERLINE+"SELECTED");
			meta.addEnchant(Enchantment.DURABILITY, 3, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		for (String i : lore) {
			listLore.add(i);
		}
		meta.setLore(listLore);
		item.setItemMeta(meta);
		return item;
	}
	public boolean playerHasRune(InterfacedRunes rune) {
		//RUNES CHECK ICI TODO
		return false;
	}
	
	public void openMainInterface(Player player) {
		player.openInventory(maingui);
	}
	public void openThiefInterface(Player player) {
		
	}
	@SuppressWarnings("unused")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		HumanEntity hentity/*et non hentai*/ = event.getWhoClicked();
		Inventory inventory = event.getInventory();
		ItemStack itemClicked = event.getCurrentItem();
		if (hentity instanceof Player) {
			Player player = (Player)hentity;
			if (inventory.equals(maingui)) {
				//Si un click dans le main inventory
				//Tu peut utiliser la variable itemClicked qui est l'objet clické et player qui est le joueur qui a clické
			}else if (guardGUIlist.contains(inventory)) {
				//Si un click dans un garde inventory
			}else if (thiefGUIlist.contains(inventory)) {
				//Si un click dans un voleur inventory
			}
		}
	}
	@EventHandler
	public void onEnventoryClose(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		guardGUIlist.remove(inv);
		thiefGUIlist.remove(inv);
	}
}
