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
import fr.vi5team.vi5.enums.RunesList;
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
		openMenu(player,type,Bukkit.createInventory(null, 27, ChatColor.GOLD+"SETTINGS"));
	};
	public void openMenu(Player player,InterfaceType type, Inventory inter) {
		playersInterfaceType.put(player, type);
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
			inter.clear();
			itm = new ItemStack(Material.PAPER);
			ItemMeta metaa = itm.getItemMeta();
			metaa.setDisplayName(ChatColor.GOLD+"Maps");
			itm.setItemMeta(metaa);
			inter.setItem(0, itm);
			itm = new ItemStack(teamglass);
			for (short i=0;i<9;i++) {
				if (i!=0) {
				inter.setItem(i, itm);
				}
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
							ChatColor.LIGHT_PURPLE+"Secondaire: "+wrap.getGardeSecondaire().getDisplayName(),
							ChatColor.GREEN+"Tertiaire: "+wrap.getGardeTertiaire().getDisplayName());
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
							ChatColor.LIGHT_PURPLE+"Secondaire: "+wrap.getVoleurSecondaire().getDisplayName(),
							ChatColor.GREEN+"Tertiaire: "+wrap.getVoleurTertiaire().getDisplayName());
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
					if (i!=13) {
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
			inter.clear();
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
		/////
		case RUNES_GARDE:
			if (inter.getSize()!=54) {
				inter = Bukkit.createInventory(null, 54, ChatColor.BLUE+"Runes");
			}else {
				inter.clear();
			}
			itm = makeGuiItem(false, Material.ANVIL, ChatColor.DARK_RED+"BACK");
			inter.setItem(0, itm);
			itm = makeGuiItem(false,teamglass, ChatColor.BLUE+"Guard Runes");
			for (byte i=1;i<6;i++) {
				inter.setItem(i*9, itm);
			}
			itm = makeGuiItem(false, Material.ORANGE_STAINED_GLASS_PANE, ChatColor.GOLD+"Primary Runes");
			inter.setItem(1, itm);
			inter.setItem(10, itm);
			itm = makeGuiItem(false, Material.MAGENTA_STAINED_GLASS_PANE, ChatColor.GOLD+"Secondary Runes");
			inter.setItem(19, itm);
			inter.setItem(28, itm);
			itm = makeGuiItem(false, Material.LIME_STAINED_GLASS_PANE, ChatColor.GOLD+"Tertiary Runes");
			inter.setItem(37, itm);
			inter.setItem(46, itm);
			//LA CA VA CHIER
			ArrayList<RunesList> l = RunesList.getGardePrimaires();
			byte spot=2;
			for (byte i=0;i<l.size();i++) {
				if (spot==9) {
					spot+=2;
				}
				if (spot==18) {
					break;
				}
				ItemStack itms = l.get(i).getMenuItem().clone();
				if (wrap.getGardePrimaire()==l.get(i)) {
					itms.addUnsafeEnchantment(Enchantment.MENDING,1);
					ItemMeta meta = itms.getItemMeta();
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					itms.setItemMeta(meta);
				}
				inter.setItem(spot,itms);
				spot++;
			};
			l = RunesList.getGardeSecondaires();
			spot=20;
			for (byte i=0;i<l.size();i++) {
				if (spot==27) {
					spot+=2;
				}
				if (spot==36) {
					break;
				}
				ItemStack itms = l.get(i).getMenuItem().clone();
				if (wrap.getGardeSecondaire()==l.get(i)) {
					itms.addUnsafeEnchantment(Enchantment.MENDING,1);
					ItemMeta meta = itms.getItemMeta();
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					itms.setItemMeta(meta);
				}
				inter.setItem(spot,itms);
				spot++;
			}
			l = RunesList.getGardeTertiaires();
			spot=38;
			for (byte i=0;i<l.size();i++) {
				if (spot==45) {
					spot+=2;
				}
				if (spot==54) {
					break;
				}
				ItemStack itms = l.get(i).getMenuItem().clone();
				if (wrap.getGardeTertiaire()==l.get(i)) {
					itms.addUnsafeEnchantment(Enchantment.MENDING,1);
					ItemMeta meta = itms.getItemMeta();
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					itms.setItemMeta(meta);
				}
				inter.setItem(spot,itms);
				spot++;
			}
			break;
		/////
		case RUNES_VOLEUR:
			if (inter.getSize()!=54) {
				inter = Bukkit.createInventory(null, 54, ChatColor.BLUE+"Runes");
			}else {
				inter.clear();
			}
			itm = makeGuiItem(false, Material.ANVIL, ChatColor.DARK_RED+"BACK");
			inter.setItem(0, itm);
			itm = makeGuiItem(false,teamglass, ChatColor.BLUE+"Thief Runes");
			for (byte i=1;i<6;i++) {
				inter.setItem(i*9, itm);
			}
			itm = makeGuiItem(false, Material.ORANGE_STAINED_GLASS_PANE, ChatColor.GOLD+"Primary Runes");
			inter.setItem(1, itm);
			inter.setItem(10, itm);
			itm = makeGuiItem(false, Material.MAGENTA_STAINED_GLASS_PANE, ChatColor.GOLD+"Secondary Runes");
			inter.setItem(19, itm);
			inter.setItem(28, itm);
			itm = makeGuiItem(false, Material.LIME_STAINED_GLASS_PANE, ChatColor.GOLD+"Tertiary Runes");
			inter.setItem(37, itm);
			inter.setItem(46, itm);
			//LA CA VA CHIER
			ArrayList<RunesList> lv = RunesList.getVoleurPrimaires();
			byte spott=2;
			for (byte i=0;i<lv.size();i++) {
				if (spott==9) {
					spott+=2;
				}
				if (spott==18) {
					break;
				}
				ItemStack itms = lv.get(i).getMenuItem().clone();
				if (wrap.getVoleurPrimaire()==lv.get(i)) {
					itms.addUnsafeEnchantment(Enchantment.MENDING,1);
					ItemMeta meta = itms.getItemMeta();
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					itms.setItemMeta(meta);
				}
				inter.setItem(spott,itms);
				spott++;
			};
			lv = RunesList.getVoleurSecondaires();
			spott=20;
			for (byte i=0;i<lv.size();i++) {
				if (spott==27) {
					spott+=2;
				}
				if (spott==36) {
					break;
				}
				ItemStack itms = lv.get(i).getMenuItem().clone();
				if (wrap.getVoleurSecondaire()==lv.get(i)) {
					itms.addUnsafeEnchantment(Enchantment.MENDING,1);
					ItemMeta meta = itms.getItemMeta();
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					itms.setItemMeta(meta);
				}
				inter.setItem(spott,itms);
				spott++;
			}
			lv = RunesList.getVoleurTertiaires();
			spott=38;
			for (byte i=0;i<lv.size();i++) {
				if (spott==45) {
					spott+=2;
				}
				if (spott==54) {
					break;
				}
				ItemStack itms = lv.get(i).getMenuItem().clone();
				if (wrap.getVoleurTertiaire()==lv.get(i)) {
					itms.addUnsafeEnchantment(Enchantment.MENDING,1);
					ItemMeta meta = itms.getItemMeta();
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					itms.setItemMeta(meta);
				}
				inter.setItem(spott,itms);
				spott++;
			}
			break;
		/////
		case MAP_LIST:
			inter.clear();
			ArrayList<String> mapList = mainref.getCfgmanager().getMapList();
			ItemStack itmx = new ItemStack(Material.ANVIL);
			ItemMeta metaInfLol = itmx.getItemMeta();
			metaInfLol.setDisplayName(ChatColor.DARK_RED+"RETOUR");
			itmx.setItemMeta(metaInfLol);
			inter.setItem(0, itmx);
			for (byte i=0;i<mapList.size();i++) {
				ItemStack itmm = new ItemStack(Material.PAPER);
				ItemMeta metax = itmm.getItemMeta();
				metax.setDisplayName(mapList.get(i));
				if (wrap.getGame().getMapName().equals(mapList.get(i))) {
					metax.addEnchant(Enchantment.MENDING, 1, true);
					metax.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				}
				itmm.setItemMeta(metax);
				inter.setItem(i+1, itmm);
			}
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
					if (itemClicked==null) {
						return;
					}
					switch (playersInterfaceType.get(player)) {
					case MAIN:
						switch (itemClicked.getType()) {
						case PAPER:
							openMenu(player, InterfaceType.MAP_LIST, inventory);
							break;
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
								openMenu(player, InterfaceType.RUNES_GARDE,inventory);
								break;
							case SPECTATEUR:
								break;
							case VOLEUR:
								openMenu(player, InterfaceType.RUNES_VOLEUR,inventory);
								break;
							default:
								break;
							}
							break;
						case BLUE_BANNER:
							openMenu(player, InterfaceType.TEAM,inventory);
							break;
						case GREEN_BANNER:
							openMenu(player, InterfaceType.TEAM,inventory);
							break;
						case RED_BANNER:
							openMenu(player, InterfaceType.TEAM,inventory);
							break;
						case WHITE_BANNER:
							openMenu(player, InterfaceType.TEAM,inventory);
							break;
						default:
							break;
						}
						break;
					case RUNES_GARDE:
						switch (itemClicked.getType()) {
						case ANVIL:
							closeInterface(player);
							openMenu(player, InterfaceType.MAIN);
						default:
							break;
						}
						for (RunesList r : RunesList.values()) {
							if (itemClicked.isSimilar(r.getMenuItem())) {
								switch (r.getTiers()) {
								case PRIMAIRE:
									wrap.setGardePrimaire(r);
									break;
								case SECONDAIRE:
									wrap.setGardeSecondaire(r);
									break;
								case TERTIAIRE:
									wrap.setGardeTertiaire(r);
									break;
								default:
									break;
								}
								openMenu(player, InterfaceType.RUNES_GARDE, inventory);
							}
						}
						break;
					case RUNES_VOLEUR:
						switch (itemClicked.getType()) {
						case ANVIL:
							closeInterface(player);
							openMenu(player, InterfaceType.MAIN);
						default:
							break;
						}
						for (RunesList r : RunesList.values()) {
							if (itemClicked.isSimilar(r.getMenuItem())) {
								switch (r.getTiers()) {
								case PRIMAIRE:
									wrap.setVoleurPrimaire(r);
									break;
								case SECONDAIRE:
									wrap.setVoleurSecondaire(r);
									break;
								case TERTIAIRE:
									wrap.setVoleurTertiaire(r);
									break;
								default:
									break;
								}
								openMenu(player, InterfaceType.RUNES_VOLEUR, inventory);
							}
						}
						break;
					case TEAM:
						switch (itemClicked.getType()) {
						case RED_BANNER:
							wrap.setTeam(Vi5Team.VOLEUR,true);
							break;
						case RED_STAINED_GLASS_PANE:
							wrap.setTeam(Vi5Team.VOLEUR,true);
							break;
						case GREEN_BANNER:
							wrap.setTeam(Vi5Team.SPECTATEUR,true);
							break;
						case LIME_STAINED_GLASS_PANE:
							wrap.setTeam(Vi5Team.SPECTATEUR,true);
							break;
						case BLUE_BANNER:
							wrap.setTeam(Vi5Team.GARDE,true);
							break;
						case BLUE_STAINED_GLASS_PANE:
							wrap.setTeam(Vi5Team.GARDE,true);
							break;
						default:
							break;
						}
						openMenu(player, InterfaceType.MAIN,inventory);
						break;
					case MAP_LIST:
						switch (itemClicked.getType()) {
						case ANVIL:
							openMenu(player, InterfaceType.MAIN, inventory);
							break;
						case PAPER:
							wrap.getGame().setMapName(itemClicked.getItemMeta().getDisplayName());
							openMenu(player, InterfaceType.MAP_LIST, inventory);
							break;
						default:
							break;
						}
						break;
					default:
						break;
					}
				}
			}
		}
	}
}
