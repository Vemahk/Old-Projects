package vem.cmdint;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import vem.cmdint.command.ICommand;
import vem.cmdint.config.FileConfiguration;
import vem.cmdint.logger.Logger;

public class Addon {

	private final String addonName;
	private final FileConfiguration config;

	private boolean canSave = true;

	protected Addon(String nm) {
		Main.addons.add(this);
		addonName = nm;
		System.out.println();
		Logger.info("Addon '" + addonName + "' found!");

		saveDefaultConfig();
		config = new FileConfiguration(Main.addonFolder + "/" + nm + "/config.yml");
	}

	protected void registerCommands(ICommand... i) {
		for (ICommand x : i)
			Main.cmdRegister(x);
	}

	public void saveConfig() {
		if (canSave)
			getConfig().save();
	}

	protected void flagSaveConfig(boolean flag) {
		canSave = false;
	}

	protected FileConfiguration getConfig() {
		return config;
	}

	protected File getConfigFolder() {
		return new File(getConfigFolderPath());
	}

	protected String getConfigFolderPath() {
		File out = new File(Main.addonFolder + "/" + getName() + "/");
		if (!out.exists())
			out.mkdir();
		return out.getAbsolutePath();
	}

	protected void saveDefaultConfig() {
		File current = new File(Main.addonFolder + "/" + getName() + "/config.yml");
		if (current.exists())
			return;
		try {
			Scanner file = new Scanner(getClass().getResourceAsStream("/config.yml"));

			current.getParentFile().mkdirs();
			current.createNewFile();

			PrintWriter writer = new PrintWriter(current);
			while (file.hasNextLine())
				writer.println(file.nextLine());

			writer.close();
			file.close();
		} catch (Exception e) {
			Logger.warning("Addon '" + getName() + "' does not have a config.yml in the jar.");
		}
	}

	protected void reloadDefaultConfig() {
		try {
			Scanner file = new Scanner(getClass().getResourceAsStream("/config.yml"));
			File current = new File(Main.addonFolder + "/" + getName() + "/config.yml");
			if (!current.exists())
				current.createNewFile();

			PrintWriter writer = new PrintWriter(current);
			while (file.hasNextLine())
				writer.println(file.nextLine());
			writer.close();
			file.close();
		} catch (Exception e) {
			Logger.error("Addon '" + getName() + "' does not have a config.yml in the jar.");
		}
	}

	public String getName() {
		return addonName;
	}

}
