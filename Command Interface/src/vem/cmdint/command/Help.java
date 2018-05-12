package vem.cmdint.command;

import vem.cmdint.Main;
import vem.cmdint.logger.Logger;

public class Help implements ICommand{

	public Help(){
		Main.cmdRegister(this);
	}
	
	@Override
	public void onCommand(String a) {
		if(a == ""){
			String programList = "";
			
			for(int i=0;i<Main.commands.size();i++){
				programList += Main.commands.get(i).getLabel();
				if(i < Main.commands.size()-1) programList += ", ";
			}
			
			Logger.info("[Help] Programs: "+programList);
		}else{
			String[] args = a.split(" ");
			if(Main.commandExists(args[0])){
				ICommand c = Main.getCommand(args[0]);
				Logger.info("[Help] "+c.getHelp());
				Logger.info("[Usage] \'"+c.getUsage()+"\'");
				String alias = "";
				for(int i=0;i<c.getAliases().length;i++){
					alias += c.getAliases()[i];
					if(i<c.getAliases().length - 1) alias += ", ";
				}
				Logger.info("[Aliases] "+alias);
			}
		}
	}
	
	public String getLabel(){
		return "help";
	}
	
	public String[] getAliases(){
		return new String[]{"?"};
	}
	
	public String getHelp(){
		return "Returns the help for all programs if no specific program is given, returns help for specific program.";
	}
	
	public String getUsage(){
		return "help [command]";
	}
	
}
