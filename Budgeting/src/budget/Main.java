package budget;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import budget.display.Category;
import budget.display.GUI;
import budget.info.Entry;

public class Main {

	public static GUI gui;
	
	public static void main(String[] args){
		
		SwingUtilities.invokeLater(new Runnable(){;
			public void run(){
				gui = new GUI("Budget App");
				try{
					File f = new File(System.getProperty("user.home")+"/AppData/Local/JAVA Budgeting/categories.dat");
					if(f.exists()){
						Scanner file = new Scanner(f);
						while(file.hasNextLine()) Category.load(file.nextLine(), gui);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void addGridItem(JPanel panel, JComponent comp, int x, int y, int width, int height, int align){
		GridBagConstraints gcon = new GridBagConstraints();
		
		gcon.gridx = x;
		gcon.gridy = y;
		gcon.gridwidth = width;
		gcon.gridheight = height;
		gcon.weightx = .5;
		gcon.weighty = .5;
		gcon.insets = new Insets(5,5,5,5);
		gcon.anchor = align;
		gcon.fill = GridBagConstraints.NONE;
		
		panel.add(comp, gcon);
	}
	
}
