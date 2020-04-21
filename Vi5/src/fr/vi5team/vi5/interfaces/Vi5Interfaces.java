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
import fr.vi5team.vi5.enums.Vi5Team;

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
		Inventory inter=Bukkit.createInventory(null, 9);
		PlayerWrapper wrap = mainref.getPlayerWrapper(player);
		if (wrap==null) {
			return;
		}
		Material teamglass=Material.WHITE_STAINED_GLASS_PANE;
		switch (wrap.getTeam()) {
		case GARDE:
			teamglass=Material.BLUE_STAINED_GLASS_PANE;
			break;
		case SPECTATEUR:
			teamglass=Material.LIME_STAINED_GLASS_PANE;
			break;
		case VOLEUR:
			teamglass=Material.RED_STAINED_GLASS_PANE;
			break;
		default:
			teamglass=Material.WHITE_STAINED_GLASS_PANE;
			break;
		}
		ItemStack itm;
		switch (type) {
		case MAIN:
			/////
			inter=Bukkit.createInventory(null, 27, ChatColor.GOLD+"SETTINGS");
			itm = new ItemStack(teamglass);
			for (short i=0;i<9;i++) {
				inter.setItem(i, itm);
				inter.setItem(i+18, itm);
			}
			switch (wrap.getTeam()){
			case GARDE:
				itm = new ItemStack(teamglass);
				inter.setItem(9, itm);
				itm=makeGuiItem(false, Material.SUNFLOWER, ChatColor.GOLD+"Launch the game", ChatColor.LIGHT_PURPLE+"Click here to launch the game");
				inter.setItem(10, itm);
				itm = new ItemStack(teamglass);
				inter.setItem(11, itm);
				if (wrap.is_ready()) {
					itm=makeGuiItem(false, Material.EMERALD_BLOCK, ChatColor.GREEN+"Ready!", ChatColor.LIGHT_PURPLE+"Click here to set you not ready");
					inter.setItem(12, itm);
				}else {
					itm=makeGuiItem(false, Material.REDSTONE_BLOCK, ChatColor.DARK_RED+"Not Ready", ChatColor.LIGHT_PURPLE+"Click here to set you ready");
					inter.setItem(12, itm);
				}
				itm = new ItemStack(teamglass);
				inter.setItem(13, itm);
				if (wrap.getGardePrimaire()!=null && wrap.getGardeSecondaire()!=null && wrap.getGardeTertiaire()!=null) {
					itm=makeGuiItem(false, Material.ARMOR_STAND, ChatColor.GREEN+"Your Runes","",
							ChatColor.GOLD+"Primaire: "+wrap.getGardePrimaire().getDisplayName(),
							ChatColor.GOLD+"Secondaire: "+wrap.getGardeSecondaire().getDisplayName(),
							ChatColor.GOLD+"Tertiaire: "+wrap.getGardeTertiaire().getDisplayName());
				}
				inter.setItem(14, itm);
				itm = new ItemStack(teamglass);
				inter.setItem(15, itm);
				itm=makeGuiItem(false, Material.BLUE_BANNER, ChatColor.AQUA+"Change team",ChatColor.LIGHT_PURPLE+"Click here to change your team");
				inter.setItem(16, itm);
				itm = new ItemStack(teamglass);
				inter.setItem(17, itm);
				break;
			case VOLEUR:
				itm = new ItemStack(teamglass);
				inter.setItem(9, itm);
				itm=makeGuiItem(false, Material.SUNFLOWER, ChatColor.GOLD+"Launch the game", ChatColor.LIGHT_PURPLE+"Click here to launch the game");
				inter.setItem(10, itm);
				itm = new ItemStack(teamglass);
				inter.setItem(11, itm);
				if (wrap.is_ready()) {
					itm=makeGuiItem(false, Material.EMERALD_BLOCK, ChatColor.GREEN+"Ready!", ChatColor.LIGHT_PURPLE+"Click here to set you not ready");
					inter.setItem(12, itm);
				}else {
					itm=makeGuiItem(false, Material.REDSTONE_BLOCK, ChatColor.DARK_RED+"Not Ready", ChatColor.LIGHT_PURPLE+"Click here to set you ready");
					inter.setItem(12, itm);
				}
				itm = new ItemStack(teamglass);
				inter.setItem(13, itm);
				if (wrap.getVoleurPrimaire()!=null && wrap.getVoleurSecondaire()!=null && wrap.getVoleurTertiaire()!=null) {
					itm=makeGuiItem(false, Material.ARMOR_STAND, ChatColor.GREEN+"Your Runes","",
							ChatColor.GOLD+"Primaire: "+wrap.getVoleurPrimaire().getDisplayName(),
							ChatColor.GOLD+"Secondaire: "+wrap.getVoleurSecondaire().getDisplayName(),
							ChatColor.GOLD+"Tertiaire: "+wrap.getVoleurTertiaire().getDisplayName());
				}
				inter.setItem(14, itm);
				itm = new ItemStack(teamglass);
				inter.setItem(15, itm);
				itm=makeGuiItem(false, Material.RED_BANNER, ChatColor.AQUA+"Change team",ChatColor.LIGHT_PURPLE+"Click here to change your team");
				inter.setItem(16, itm);
				itm = new ItemStack(teamglass);
				inter.setItem(17, itm);
				break;
			case SPECTATEUR:
				for (short i=9;i<18;i++) {
					if (i==13) {
						itm = new ItemStack(teamglass);
						inter.setItem(i, itm);
					}else{
						itm=makeGuiItem(false, Material.GREEN_BANNER, ChatColor.AQUA+"Change team",ChatColor.LIGHT_PURPLE+"Click here to change your team");
						inter.setItem(i, itm);
					}
				}
				break;
			default:
				break;
			}
			break;
		/////
		case TEAM:
			inter=Bukkit.createInventory(null, 27, ChatColor.DARK_PURPLE+"Select your team");
			itm = new ItemStack(Material.RED_STAINED_GLASS_PANE);
			inter.setItem(0, itm);
			inter.setItem(1, itm);
			inter.setItem(2, itm);
			inter.setItem(9, itm);
			inter.setItem(11, itm);
			inter.setItem(18, itm);
			inter.setItem(19, itm);
			inter.setItem(20, itm);
			itm = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
			inter.setItem(3, itm);
			inter.setItem(4, itm);
			inter.setItem(5, itm);
			inter.setItem(12, itm);
			inter.setItem(14, itm);
			inter.setItem(21, itm);
			inter.setItem(22, itm);
			inter.setItem(23, itm);
			itm = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
			inter.setItem(6, itm);
			inter.setItem(7, itm);
			inter.setItem(8, itm);
			inter.setItem(15, itm);
			inter.setItem(17, itm);
			inter.setItem(24, itm);
			inter.setItem(25, itm);
			inter.setItem(26, itm);
			itm = new ItemStack(Material.RED_BANNER);
			inter.setItem(10, itm);
			itm = new ItemStack(Material.GREEN_BANNER);
			inter.setItem(13, itm);
			itm = new ItemStack(Material.BLUE_BANNER);
			inter.setItem(16, itm);
			break;
		default:
			break;
		}
		player.openInventory(inter);
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
					PlayerWrapper wrap = mainref.getPlayerWrapper(player);
					switch (playersInterfaceType.get(player)) {
					case MAIN:
						switch (itemClicked.getType()) {
						case SUNFLOWER:
							wrap.getGame().start(false, player);
							closeInterface(player);
							break;
						case EMERALD_BLOCK:
							wrap.setReady(false);
							if (wrap.is_ready()) {
								ItemStack itm=makeGuiItem(false, Material.EMERALD_BLOCK, ChatColor.GREEN+"Ready!", ChatColor.LIGHT_PURPLE+"Click here to set you not ready");
								inventory.setItem(12, itm);
							}else {
								ItemStack itm=makeGuiItem(false, Material.REDSTONE_BLOCK, ChatColor.DARK_RED+"Not Ready", ChatColor.LIGHT_PURPLE+"Click here to set you ready");
								inventory.setItem(12, itm);
							}
							break;
						case REDSTONE_BLOCK:
							wrap.setReady(true);
							if (wrap.is_ready()) {
								ItemStack itm=makeGuiItem(false, Material.EMERALD_BLOCK, ChatColor.GREEN+"Ready!", ChatColor.LIGHT_PURPLE+"Click here to set you not ready");
								inventory.setItem(12, itm);
							}else {
								ItemStack itm=makeGuiItem(false, Material.REDSTONE_BLOCK, ChatColor.DARK_RED+"Not Ready", ChatColor.LIGHT_PURPLE+"Click here to set you ready");
								inventory.setItem(12, itm);
							}
							break;
						case ARMOR_STAND:
							switch (wrap.getTeam()) {
							case GARDE:
								openMenu(player, InterfaceType.RUNES_GARDE);
								break;
							case SPECTATEUR:
								break;
							case VOLEUR:
								openMenu(player, InterfaceType.RUNES_VOLEUR);
								break;
							default:
								break;
							}
							break;
						case BLUE_BANNER:
							openMenu(player, InterfaceType.TEAM);
							break;
						case GREEN_BANNER:
							openMenu(player, InterfaceType.TEAM);
							break;
						case RED_BANNER:
							openMenu(player, InterfaceType.TEAM);
							break;
						case WHITE_BANNER:
							openMenu(player, InterfaceType.TEAM);
							break;
						default:
							break;
						}
						break;
					case RUNES_GARDE:
						break;
					case RUNES_VOLEUR:
						break;
					case TEAM:
						switch (itemClicked.getType()) {
						case RED_BANNER:
							wrap.setTeam(Vi5Team.VOLEUR);
							break;
						case RED_STAINED_GLASS_PANE:
							wrap.setTeam(Vi5Team.VOLEUR);
							break;
						case GREEN_BANNER:
							wrap.setTeam(Vi5Team.SPECTATEUR);
							break;
						case LIME_STAINED_GLASS_PANE:
							wrap.setTeam(Vi5Team.SPECTATEUR);
							break;
						case BLUE_BANNER:
							wrap.setTeam(Vi5Team.GARDE);
							break;
						case BLUE_STAINED_GLASS_PANE:
							wrap.setTeam(Vi5Team.GARDE);
							break;
						default:
							break;
						}
						openMenu(player, InterfaceType.MAIN);
						break;
					default:
						break;
					}
				}
			}
		}
	}
}
