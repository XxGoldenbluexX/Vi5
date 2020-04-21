package fr.vi5team.vi5.runes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

/**
 * 
 * @author XxGoldenbluexX
 *
 */

public abstract class BaseRune implements Listener {
	/**
	 * Réference au joueur a qui appartient cette rune
	 */
	protected final Player player;
	/**
	 * Réference au PlayerWrapper du joueur a qui appartient cette rune
	 */
	protected final PlayerWrapper wraper;
	/**
	 * Réference au plugin
	 */
	protected final Vi5Main mainref;
	/**
	 * Réference a la rune (RunesList) que cette classe représente
	 */
	protected final RunesList Rune;
	/**
	 * Représente le cooldown de la rune
	 */
	private float cooldown=0;
	/**
	 * BukkitRunnable qui sert a calculer le coolodown
	 */
	private final BukkitRunnable cooldownTimer = new BukkitRunnable() {
		@Override
		public void run() {cooldownTick();}
	};
	/**
	 * Réference a l'item qui représente actuellement la rune dans la hotbar
	 */
	private ItemStack castItem;
	public BaseRune(Vi5Main _mainref,PlayerWrapper _wraper, Player _player,RunesList _rune) {
		player=_player;
		wraper=_wraper;
		mainref=_mainref;
		Rune=_rune;
		castItem=Rune.getHotbarItem();
	}

	public abstract void cast();
	public abstract void tick();
	public abstract void gameEnd();
	public abstract void gameStart();
	public abstract void enterZone();
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		if (event.getPlayer().equals(player)) {
			if (event.getItemDrop().getItemStack().equals(castItem)) {
				switch (Rune.getType()) {
				case PASSIF:
					event.setCancelled(true);
					break;
				case SPELL:
					event.getItemDrop().remove();
					event.setCancelled(false);
					break;
				default:
					break;
				}
				cast();
			}
		}
	}
	public void showHotbarItem() {
		player.getInventory().setItem(Rune.getTiers().getInventorySlot(), castItem);
		return;
	}
	/**
	 * Set the item used to cast the spell.
	 * THIS WILL NOT PUT IT IN THE HOTBAR (use showHotBarItem to do it)
	 * @param it
	 * The item you will set.
	 */
	public void setCastItem(ItemStack it) {
		castItem=it;
	}
	private void setCooldownItem() {
		ItemStack it = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE,Math.round(cooldown));
		ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(ChatColor.RED+Rune.getDisplayName()+": "+cooldown);
		it.setItemMeta(meta);
		player.getInventory().setItem(Rune.getTiers().getInventorySlot(), it);
	}
	public void Activate() {
		mainref.getPmanager().registerEvents(this,mainref);
		showHotbarItem();
	}
	private void cooldownTick() {
		cooldown--;
		if (cooldown<=0) {
			cooldown=0;
			showHotbarItem();
		}else {
			setCooldownItem();
			if (cooldown<1) {
				cooldownTimer.runTaskLater(mainref,(long)(cooldown*20));
			}else {
				cooldownTimer.runTaskLater(mainref,20L);
			}
		}
	}
}