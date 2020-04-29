package fr.vi5team.vi5.runes;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_cop extends BaseRune {

	public Rune_cop(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}

	@Override
	public void cast() {
	}

	@Override
	public void tick() {
	}

	@Override
	public void gameEnd() {
	}

	@Override
	public void gameStart() {
		Activate();
		ItemStack itm = makeSpamSword(Material.DIAMOND_SWORD, 1000);
		ItemMeta meta = itm.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD+"Thief Slayer");
		ArrayList<String> l = new ArrayList<String>();
		l.add(ChatColor.LIGHT_PURPLE+"This sword is able to one-shot thiefs");
		l.add(ChatColor.GRAY+"\"La matraque de th�o\"");
		meta.setLore(l);
		meta.addEnchant(Enchantment.MENDING, 1,true);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itm.setItemMeta(meta);
		player.getInventory().setItem(0, itm);
	}

	@Override
	public void enterZone() {
	}
}
