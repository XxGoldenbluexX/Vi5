package fr.vi5team.vi5.interfaces;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.InterfaceType;

public class Vi5Interfaces implements Listener{
	
	private final Vi5Main mainref;
	private final WeakHashMap<Player,Inventory> playersInterfaces = new WeakHashMap<Player,Inventory>();
	private final WeakHashMap<Player,InterfaceType> playersInterfaceType = new WeakHashMap<Player,InterfaceType>();
	
	public Vi5Interfaces(Vi5Main main) {
		mainref=main;
	}
	
	private static ItemStack makeGuiItem(boolean selected,Material mat, String name, String... lore) {
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
	
	public boolean closeInterface(Player player) {
		if (playersInterfaces.containsKey(player)) {
			player.closeInventory();
			playersInterfaces.remove(player);
			playersInterfaceType.remove(player);
			return true;
		}else {
			player.closeInventory();
			return false;
		}
	}
	
	public void openMenu(Player player,InterfaceType type) {
		closeInterface(player);
		playersInterfaceType.put(player, InterfaceType.MAIN);
		Inventory inter = Bukkit.createInventory(null,9*3, ChatColor.LIGHT_PURPLE+mainref.getPlayerWrapper(player).getGame().getName());
		switch (type) {
		case GARDE_PRIMAIRE:
			break;
		case GARDE_SECONDAIRE:
			break;
		case GARDE_TERTAIRE:
			break;
		case MAIN:
			ItemStack itm = makeGuiItem(false, Material.ORANGE_STAINED_GLASS_PANE, ChatColor.GOLD+"Runes Primaires", ChatColor.LIGHT_PURPLE+"Clickez ici pour choisir votre rune primaire");
			for (short i=0;i<3;i++) {
				inter.setItem(i, itm);
				inter.setItem(i+9, itm);
			}
			itm = makeGuiItem(false, Material.PURPLE_STAINED_GLASS_PANE, ChatColor.GOLD+"Runes Secondaires", ChatColor.LIGHT_PURPLE+"Clickez ici pour choisir votre rune secondaire");
			for (short i=3;i<6;i++) {
				inter.setItem(i, itm);
				inter.setItem(i+9, itm);
			}
			itm = makeGuiItem(false, Material.LIGHT_BLUE_STAINED_GLASS_PANE, ChatColor.GOLD+"Runes Tertiaires", ChatColor.LIGHT_PURPLE+"Clickez ici pour choisir votre rune tertiaire");
			for (short i=6;i<9;i++) {
				inter.setItem(i, itm);
				inter.setItem(i+9, itm);
			}
			break;
		case TEAM:
			break;
		case VOLEUR_PRIMAIRE:
			break;
		case VOLEUR_SECONDAIRE:
			break;
		case VOLEUR_TERTIAIRE:
			break;
		default:
			break;
		}
		playersInterfaces.put(player, inter);
	}
	
	public void openMainMenu(Player player,PlayerWrapper wrap) {
		closeInterface(player);
		Inventory inter = Bukkit.createInventory(null,9*3, ChatColor.LIGHT_PURPLE+mainref.getPlayerWrapper(player).getGame().getName());
		inter.setItem(4, makeGuiItem(false,Material.SUNFLOWER,ChatColor.AQUA+"Launch the game"));
		inter.setItem(19, makeGuiItem(false,Material.BLUE_BANNER,ChatColor.BLUE+"Join the Guards"));
		inter.setItem(22, makeGuiItem(false,Material.LIME_BANNER,ChatColor.GREEN+"Join the Spectators"));
		inter.setItem(25, makeGuiItem(false,Material.RED_BANNER,ChatColor.RED+"Join the Thiefs"));
		player.openInventory(inter);
		playersInterfaceType.put(player, InterfaceType.MAIN);
		playersInterfaces.put(player, inter);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		HumanEntity hentity/*et non hentai*/ = event.getWhoClicked();
		Inventory inventory = event.getInventory();
		ItemStack itemClicked = event.getCurrentItem();
		if (hentity instanceof Player) {
			Player player = (Player)hentity;
			if (playersInterfaces.containsKey(player)) {
				if (inventory.equals(playersInterfaces.get(player))) {
					event.setCancelled(true);
					switch (playersInterfaceType.get(player)) {
					case GARDE_PRIMAIRE:
						break;
					case GARDE_SECONDAIRE:
						break;
					case GARDE_TERTAIRE:
						break;
					case MAIN:
						break;
					case TEAM:
						break;
					case VOLEUR_PRIMAIRE:
						break;
					case VOLEUR_SECONDAIRE:
						break;
					case VOLEUR_TERTIAIRE:
						break;
					default:
						break;
					}
				}
			}
		}
	}
}
