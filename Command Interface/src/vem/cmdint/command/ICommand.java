package vem.cmdint.command;

public interface ICommand {
	
	public void onCommand(String a);
	
	public String getLabel();
	
	public String[] getAliases();
	
	public String getHelp();
	
	public String getUsage();
}
