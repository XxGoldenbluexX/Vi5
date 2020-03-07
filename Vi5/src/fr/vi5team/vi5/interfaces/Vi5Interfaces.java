package fr.vi5team.vi5.interfaces;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Vi5Interfaces implements Listener{
	private Inventory maingui;
	private Inventory guardgui;
	private Inventory thiefgui;
	
	public void onEnable() {
		maingui = Bukkit.createInventory(null, 9, "Vi5_Menu");
		maingui.setItem(1,makeGuiItem(maingui,Material.DIAMOND,ChatColor.BLUE+"Guard's Runes",ChatColor.DARK_PURPLE+"Choose your "+ChatColor.BLUE+"Guard's "+ChatColor.DARK_PURPLE+"runes"));
		maingui.setItem(4,makeGuiItem(maingui,Material.FIREWORK_ROCKET,ChatColor.GREEN+"Launch Game"));
		maingui.setItem(7,makeGuiItem(maingui,Material.REDSTONE,ChatColor.RED+"Thief's Runes",ChatColor.DARK_PURPLE+"Choose your "+ChatColor.RED+"Thief's "+ChatColor.DARK_PURPLE+"runes"));
	}
	public void openGuardGui(Player player) {
		guardgui = Bukkit.createInventory(player, 45, ChatColor.BLUE+"Guard's Runes");
		guardgui.setItem(0,makeGuiItem(guardgui,Material.ORANGE_DYE,ChatColor.GOLD+"Main Runes"));
		guardgui.setItem(18,makeGuiItem(guardgui,Material.PURPLE_DYE,ChatColor.DARK_PURPLE+"Secondary Runes"));
		guardgui.setItem(36,makeGuiItem(guardgui,Material.CYAN_DYE,ChatColor.AQUA+"Tertiary Runes"));
		guardgui.setItem(2,makeGuiItem(guardgui,Material.DIAMOND_SWORD,ChatColor.GOLD+"CRS",ChatColor.LIGHT_PURPLE+"Permet de tuer en un coup","",ChatColor.GRAY+""+ChatColor.ITALIC+"Grosse matraque dans tes fesses"));
		guardgui.setItem(4,makeGuiItem(guardgui,Material.REDSTONE_TORCH,ChatColor.GOLD+"Balise",ChatColor.LIGHT_PURPLE+"Permet de poser une balise",ChatColor.LIGHT_PURPLE+"qui révèle les voleurs à proximité"));
		guardgui.setItem(6,makeGuiItem(guardgui,Material.COMPASS,ChatColor.GOLD+"Sonnar",ChatColor.LIGHT_PURPLE+"Permet d'émettre un son toute les "+ChatColor.WHITE+"5s,",ChatColor.LIGHT_PURPLE+"Son aigu si un voleur est proche"));
		guardgui.setItem(20,makeGuiItem(guardgui,Material.FIREWORK_ROCKET,ChatColor.DARK_PURPLE+"Surcharge",ChatColor.LIGHT_PURPLE+"Permet d'augmenter grandement sa vitesse et sa force pour 1s",ChatColor.LIGHT_PURPLE+"Délai de récupération: "+ChatColor.WHITE+"20s"));
		guardgui.setItem(22,makeGuiItem(guardgui,Material.BRICK_WALL,ChatColor.DARK_PURPLE+"Mur",ChatColor.LIGHT_PURPLE+"Permet de poser 2 murs sur les blocs violets de la carte",ChatColor.LIGHT_PURPLE+"Les gardes et les voleurs [Crocheteurs]",ChatColor.LIGHT_PURPLE+"peuvent passer à travers"));
		guardgui.setItem(38,makeGuiItem(guardgui,Material.FEATHER,ChatColor.AQUA+"Rapidité",ChatColor.LIGHT_PURPLE+"Vitesse de déplacement plus élevée"));
		guardgui.setItem(40,makeGuiItem(guardgui,Material.FISHING_ROD,ChatColor.AQUA+"Pêcheur",ChatColor.LIGHT_PURPLE+"Permet d'acrocher un voleur",ChatColor.LIGHT_PURPLE+"pour garder sa trace ou le tirer vers soi"));
	}
	
	private ItemStack makeGuiItem(Inventory inv, Material mat, String name, String... lore) {
		ItemStack item=new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		List<String> listLore=new ArrayList<String>();
		
	if (inv.equals(guardgui)||inv.equals(thiefgui)){
		/*RUNE CHECK HERE*/
		listLore.add(ChatColor.DARK_GREEN+"OWNED");
		meta.addEnchant(Enchantment.DURABILITY, 3, true);
		}
		for (String i : lore) {
			listLore.add(i);
		}
		meta.setLore(listLore);
		item.setItemMeta(meta);
		return item;
	}
	
	public void openMainInterface(Player player, Inventory maingui) {
		player.openInventory(maingui);
	}
}
