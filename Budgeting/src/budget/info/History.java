package budget.info;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import budget.display.Category;

public class History {

	private Category category;
	private ArrayList<Entry> entries;
	
	public History(Category c){
		category = c;
		entries = new ArrayList<>();
	}
	
	public Category getCategory(){
		return category;
	}
	
	public Iterator<Entry> getEntryIterator(){
		return entries.iterator();
	}
	
	public void addEntry(String reason, double amt){
		entries.add(new Entry(reason, amt));
		Collections.sort(entries);
	}
	
	public void save() throws IOException{
		File dir = new File(System.getProperty("user.home")+"/AppData/Local/JAVA Budgeting/History/");
		if(!dir.exists()) dir.mkdirs();
		
		File file = new File(dir, category.getName()+".dat");
		if(!file.exists()) file.createNewFile();
		
		PrintWriter writer = new PrintWriter(file);
		
		for(Entry e : entries)
			writer.println(e.forSave());
		
		writer.close();
	}
	
	public void load(){
		File dir = new File(System.getProperty("user.home")+"/AppData/Local/JAVA Budgeting/History/");
		if(!dir.exists()){
			dir.mkdirs();
			return;
		}
		
		File file = new File(dir, category.getName()+".dat");
		try{
			if(!file.exists()){
				file.createNewFile();
				return;
			}
			
			Scanner read = new Scanner(file);
			while(read.hasNextLine()){
				String ln = read.nextLine();
				String[] dat = ln.split(":");
				entries.add(new Entry(dat[1], Double.parseDouble(dat[2]), Long.parseLong(dat[0])));
			}
			read.close();
			Collections.sort(entries);
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	
	
}
