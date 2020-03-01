package fr.vi5team.vi5.interfaces;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Vi5Interfaces {
	public void OpenMainInterface(Player player) {
		Inventory inv = Bukkit.createInventory(player, 9, "Test");
		player.openInventory(inv);
	}
}
