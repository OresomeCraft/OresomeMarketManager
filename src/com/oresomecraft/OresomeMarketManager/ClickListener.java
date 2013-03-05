package com.oresomecraft.OresomeMarketManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class ClickListener implements Listener {

    private OresomeMarketManager plugin;
    public ClickListener(OresomeMarketManager pl) {
	plugin = pl;
    }
    
    @EventHandler
	public void onSignChange(SignChangeEvent event) {
		if (event.getLines()[0] != null && event.getLines()[0].equals("[Market]")) {
		    if (!event.getPlayer().hasPermission("OresomeMarketManager.create")) {
			event.getPlayer().sendMessage(ChatColor.RED + "You don't have permission to do that!");
			event.getBlock().breakNaturally();
		    }
		}
    }
    
    @EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
	Player player = event.getPlayer();
	String name = player.getName();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.SIGN_POST){
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).equals("[Market]") && (sign.getLine(1).equals("") || sign.getLine(2).equals(""))) {
				    player.sendMessage(ChatColor.RED + "This sign is not configured correctly!");
				} else {
				    if (plugin.getConfig().getBoolean(event.getPlayer().getName() + ".ownsmarket") == true) {
					player.sendMessage(ChatColor.RED + "Sorry, only one market plot per player.");
				    } else {
				    ProtectedRegion region = WorldGuardManager.getProtectedRegion(player.getWorld(), sign.getLine(1));
				    if (sign.getLine(0).equals("[Market]") && !sign.getLine(1).isEmpty() && !sign.getLine(2).isEmpty()) {
					if (OresomeMarketManager.econ.getBalance(name) < Double.parseDouble(sign.getLine(2))) {
					    player.sendMessage(ChatColor.RED + "You don't have enough money to buy this market plot!");
					} else {
					OresomeMarketManager.econ.withdrawPlayer(player.getName(), Double.parseDouble(sign.getLine(2)));
					WorldGuardManager.addMember(region, player);
					player.sendMessage(ChatColor.GREEN + "You brought market plot '" + sign.getLine(1) + "'! Enjoy.");
					event.getClickedBlock().breakNaturally();
					
					if (plugin.getConfig().get(name) == null) {

					    plugin.getConfig().set(name + ".ownsmarket", false);
					    plugin.getConfig().set(name + ".regionname", "none");

					    plugin.saveConfig();
					    plugin.reloadConfig();

					}
					
					plugin.getConfig().set(name + ".ownsmarket", true);
					plugin.getConfig().set(name + ".regionname", sign.getLine(1));
					
					plugin.saveConfig();
					plugin.reloadConfig();
					
			    }
			}
		    }

		}
	    }
	}

    }

}
