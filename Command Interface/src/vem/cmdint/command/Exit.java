package vem.cmdint.command;

import vem.cmdint.Addon;
import vem.cmdint.Main;
import vem.cmdint.logger.Logger;

public class Exit implements ICommand{

	public Exit(){
		Main.cmdRegister(this);
	}
	
	@Override
	public void onCommand(String args) {
		Logger.info("Shutting down...");
		for(Addon a : Main.addons) a.saveConfig();
		System.exit(1);
	}

	@Override
	public String getLabel() {
		return "exit";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"stop", "terminate"};
	}
	
	public String getHelp(){
		return "Closes down the program.";
	}
	
	public String getUsage(){
		return "exit";
	}
}
