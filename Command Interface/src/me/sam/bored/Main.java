package me.sam.bored;

import static me.sam.bored.logger.Logger.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import me.sam.bored.command.Exit;
import me.sam.bored.command.Help;
import me.sam.bored.command.ICommand;
import me.sam.bored.logger.Logger;

public class Main {

	public static ArrayList<ICommand> commands = new ArrayList<ICommand>();
	public static ArrayList<Addon> addons = new ArrayList<Addon>();

	public static Scanner in = new Scanner(System.in);
	
	public static final String addonFolder = "_Addons";
	
	public static void main(String[] args) {

		info("Program startup");
		registerCommands();
		registerAddons();
		
		line();
		info("Done!");
		line();
		
		String inLine = in.nextLine();
		
		while (true) {
			executeCommand(inLine);
			inLine = in.nextLine();
		}
	}

	public static void executeCommand(String line) {
		String command = line;
		String args = "";

		if (line.contains(" ")) {
			command = line.substring(0, line.indexOf(' '));
			args = line.substring(line.indexOf(' ') + 1);
		}

		if (commandExists(command)) {
			ICommand c = getCommand(command);
			c.onCommand(args);
			System.out.println();
		}
	}

	public static ICommand getCommand(String cmdLabel) {
		for (ICommand c : commands) {
			if (c.getLabel().equals(cmdLabel))
				return c;
			else {
				for (String s : c.getAliases()) {
					if (s.equals(cmdLabel))
						return c;
				}
			}
		}
		error("No such command '" + cmdLabel + "'!");
		return null;
	}

	public static boolean commandExists(String cmdLabel) {
		return getCommand(cmdLabel) != null;
	}

	public static void registerAddons() {
		File dir = new File(addonFolder+"/");
		if (!dir.exists())
			dir.mkdir();
		else {
			for (File file : dir.listFiles()) {
				if (!file.isDirectory() && file.getName().endsWith(".jar")) {
					try {
						@SuppressWarnings("resource")
						JarFile jar = new JarFile(file.getAbsolutePath());
						Enumeration<?> e = jar.entries();

						URL[] urls = { new URL("jar:file:" + file.getAbsolutePath() + "!/") };
						URLClassLoader cl = URLClassLoader.newInstance(urls);

						while (e.hasMoreElements()) {
							JarEntry je = (JarEntry)e.nextElement();
							if (je.isDirectory() || !je.getName().endsWith(".class")) {
								continue;
							}
							// -6 because of .class
							String className = je.getName().substring(0, je.getName().length() - 6);
							className = className.replace('/', '.');
							Class<?> c = cl.loadClass(className);
							if (c.getSuperclass() == Addon.class)
								c.newInstance();
						}
					} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void registerCommands() {
		new Help();
		new Exit();
	}

	/**
	 * 
	 * Registers command c so that the main program can access it. Checks the
	 * registered program's command label & aliases with already registered
	 * labels & aliases and throws a fatal error if a match is found. Labels are
	 * case sensitive.
	 * 
	 * @param c
	 *            implements ICommand
	 */
	public static void cmdRegister(ICommand c) {
		// Check for command label duplicates
		for (ICommand c2 : commands) {

			// Check c's label with all other labels & aliases
			if (c.getLabel().equals(c2.getLabel()))
				fatalError("Command label match! '" + c.getLabel() + "' from classes " + c.getClass().getName()
						+ " and " + c2.getClass().getName() + "!");
			else
				for (String s : c2.getAliases())
					if (c.getLabel().equals(s))
						fatalError("Command label match! '" + c.getLabel() + "' from classes " + c.getClass().getName()
								+ " and " + c2.getClass().getName() + "!");

			// Check aliases of c with all labels and aliases with others
			for (String s : c.getAliases()) {
				if (s.equals(c2.getLabel()))
					fatalError("Command label match! '" + s + "' from classes " + c.getClass().getName() + " and "
							+ c2.getClass().getName() + "!");
				else
					for (String s2 : c2.getAliases())
						if (s.equals(s2))
							fatalError("Command label match! '" + s + "' from classes " + c.getClass().getName()
									+ " and " + c2.getClass().getName() + "!");
			}
		}

		// Add command
		Main.commands.add(c);

		Logger.info("Registered Command \'" + c.getLabel() + "\' with alias(es) " + Arrays.toString(c.getAliases()));
	}

}
