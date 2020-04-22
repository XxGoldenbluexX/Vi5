package fr.vi5team.vi5.runes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
	 * Réference a l'item qui représente actuellement la rune dans la hotbar
	 */
	private ItemStack castItem;
	private ItemStack hotbarItem;
	private ItemStack cooldownItem;
	public BaseRune(Vi5Main _mainref,PlayerWrapper _wraper, Player _player,RunesList _rune) {
		player=_player;
		wraper=_wraper;
		mainref=_mainref;
		Rune=_rune;
		castItem=Rune.getHotbarItem();
		hotbarItem=castItem;
		cooldownItem = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
	}

	public abstract void cast();
	public abstract void tick();
	public abstract void gameEnd();
	public abstract void gameStart();
	public abstract void enterZone();
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		if (event.getPlayer().equals(player)) {
			if (event.getItemDrop().getItemStack().isSimilar(hotbarItem)) {
				switch (Rune.getType()) {
				case PASSIF:
					event.setCancelled(true);
					break;
				case SPELL:
					if (event.getItemDrop().getItemStack().isSimilar(castItem)) {
					event.getItemDrop().remove();
					event.setCancelled(false);
					cast();
					}else {
						event.setCancelled(true);
					}
					break;
				default:
					break;
				}
			}
		}
	}
	public void showCastItem() {
		player.getInventory().setItem(Rune.getTiers().getInventorySlot(), castItem);
		hotbarItem=castItem;
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
	public void pretick() {
		cooldownTick();
		tick();
	}
	private void setCooldownItem() {
		cooldownItem.setAmount(Math.max(Math.floorDiv(Math.round((cooldown+1)*10), 10),1));
		ItemMeta meta = cooldownItem.getItemMeta();
		meta.setDisplayName(ChatColor.RED+Rune.getDisplayName()+": "+(float)Math.round(cooldown*10)/10f);
		cooldownItem.setItemMeta(meta);
		hotbarItem=cooldownItem;
		player.getInventory().setItem(Rune.getTiers().getInventorySlot(), cooldownItem);
	}
	public void Activate() {
		mainref.getPmanager().registerEvents(this,mainref);
		showCastItem();
	}
	public void setCooldown(float _cooldown) {
		cooldown=_cooldown;
		cooldownTick();
	}
	private void cooldownTick() {
		if (cooldown>0) {
			cooldown-=0.05;
			if (cooldown<=0) {
				cooldown=0;
				showCastItem();
			}else {
				setCooldownItem();
			}
		}
	}

	public ItemStack getHotbarItem() {
		return hotbarItem;
	}

	public void setHotbarItem(ItemStack hotbarItem) {
		this.hotbarItem = hotbarItem;
	}
}