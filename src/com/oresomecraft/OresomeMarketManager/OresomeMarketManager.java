package com.oresomecraft.OresomeMarketManager;

import java.util.logging.Logger;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

public class OresomeMarketManager extends JavaPlugin { // doesn't need to implement listener.

	public ClickListener listener;
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Economy econ;

	public void onDisable() {
		//bukkit now handles <plugin> has been enabled
	}

	public void onEnable() {
		listener = new ClickListener(this);
		getServer().getPluginManager().registerEvents(listener, this);
		/** always log things last.**/

		PluginDescriptionFile pdfFile = getDescription();

		if (!setupEconomy() ) {
			logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", pdfFile.getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

}
