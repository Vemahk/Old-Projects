package me.sam.bored.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import me.sam.bored.logger.Logger;

public class FileConfiguration {

	private final File yamlFile;

	private ConfigurationSection contents;

	public FileConfiguration(String s) {
		this(new File(s));
	}

	public FileConfiguration(File file) {
		yamlFile = file;
		contents = new ConfigurationSection("", "", null);
		load();
	}

	public File getFile() {
		return yamlFile;
	}

	public void load() {
		if (!yamlFile.exists()) {
			try {
				yamlFile.getParentFile().mkdirs();
				yamlFile.createNewFile();
			} catch (IOException e) {
				Logger.error("Failed to create file!");
				e.printStackTrace();
			}
			return;
		}
		List<String> data = new ArrayList<String>();
		try {
			Scanner file = new Scanner(yamlFile);
			while (file.hasNextLine())
				data.add(file.nextLine());
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < data.size();) {
			if (leadingSpaces(data.get(i)) == 0) {
				String nm = data.get(i).substring(0, data.get(i).indexOf(": "));
				String val = data.get(i).substring(data.get(i).indexOf(": ") + 2).trim();
				ConfigurationSection csi = new ConfigurationSection(nm, val, contents);

				int first = -1;
				while (++i < data.size() && leadingSpaces(data.get(i)) > 0) {
					if (first < 0)
						first = leadingSpaces(data.get(i));
					try {
						csi.addData(data.get(i).substring(first));
					} catch (Exception e) {
						Logger.error(first + ": " + i + ": " + data.get(i) + ": " + data.size());
						e.printStackTrace();
					}
				}
				csi.compile();
			}
		}
		data.clear();
	}

	private int leadingSpaces(String s) {
		int out;
		for (out = 0; s.charAt(out) == ' ';)
			out++;
		return out;
	}

	public void set(String path, Object o) {
		contents.set(path, o);
	}

	public ConfigurationSection getConfigurationSection(String path) {
		return contents.getConfigurationSection(path);
	}

	public void save() {
		try {
			PrintWriter writer = new PrintWriter(yamlFile);
			writer.print(toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		String out = "";
		for (ConfigurationSection cs : contents.getSubSections())
			out += cs.toString(0);
		return out;
	}

}
