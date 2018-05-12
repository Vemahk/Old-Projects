package budget.display;

import static budget.Main.addGridItem;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import budget.info.Entry;
import budget.info.History;

public class Category extends JPanel implements ActionListener, Comparable<Category>{
	private static final long serialVersionUID = -3336033861061335379L;
	
	private GUI parent;
	private JLabel total, portionVal;
	private JButton remBtn, cngPortion, histBtn, cngPriority;
	private JCheckBox stat;
	
	private String name;
	private double money;
	private boolean percent;
	private double value;
	private int priority;
	private History history;
	
	public Category(GUI parent, String name){
		super();
		setLayout(new GridBagLayout());
		
		this.parent = parent;
		this.name = name;
		money = 0;
		percent = true;
		value = 0;
		priority = 1;
		
		total = new JLabel("Total: $"+String.format("%.2f", money));
		addGridItem(this, total, 3, 0, 1, 1, GridBagConstraints.EAST);
		addGridItem(this, new JLabel("Portion:"),0,0,1,1,GridBagConstraints.EAST);
		
		String pv = String.format("%.2f", value);
		if(percent) pv+="%";
		else pv = "$" + pv;
		
		portionVal = new JLabel(pv);
		addGridItem(this, portionVal, 1, 0, 1, 1, GridBagConstraints.WEST);
		
		Box btnBox = Box.createHorizontalBox();
		cngPortion = new JButton("Change Portion");
		cngPortion.addActionListener(this);
		
		remBtn = new JButton("Remove Money");
		remBtn.addActionListener(this);
		
		cngPriority = new JButton("Change Priority");
		cngPriority.addActionListener(this);
		
		btnBox.add(cngPriority);
		btnBox.add(Box.createHorizontalStrut(20));
		btnBox.add(remBtn);
		btnBox.add(Box.createHorizontalStrut(10));
		btnBox.add(cngPortion);
		
		addGridItem(this, btnBox, 1,2, 3, 1, GridBagConstraints.SOUTHEAST);

		histBtn = new JButton("View History");
		histBtn.addActionListener(this);

		addGridItem(this, histBtn, 0,2,1,1, GridBagConstraints.SOUTHWEST);
		
		stat = new JCheckBox("Static",false);
		stat.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				cngPortion.setEnabled(!stat.isSelected());
			}
		});
		addGridItem(this, stat, 2, 0, 1, 1, GridBagConstraints.WEST);
		
		history = new History(this);
		history.load();
	}
	
	public String getName(){ return name; }
	public double getMoney(){ return money; }
	public boolean isPercent(){ return percent; }
	public double getValue(){ return value; }
	public History getHistory(){ return history; }
	
	public void setPercent(boolean t){ percent = t; }
	public void setValue(double v){
		value = v;

		if(percent)
			portionVal.setText(String.format("%.2f", value)+"%");
		else
			portionVal.setText("$"+String.format("%.2f", value));
	}
	
	public void setStatic(boolean t){
		stat.setSelected(t);
		cngPortion.setEnabled(!t);
	}
	
	public void addMoney(double x, String reason){
		changeMoney(x, reason);
	}
	
	public void removeMoney(double x, String reason){
		if(x > money)
			JOptionPane.showMessageDialog(parent,
					"You don't have enough money here for that transaction.",
					"Not Enough Money", JOptionPane.ERROR_MESSAGE);
		else changeMoney(-x, reason);
	}
	
	private void changeMoney(double x, String reason){
		money += x;
		updateText();
		parent.updateLabelText();
		history.addEntry(reason, x);
	}
	
	public void setMoney(double x){
		money = x;
		updateText();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cngPortion) {

			String s = JOptionPane.showInputDialog(parent, "Input new value", "Change Portion", JOptionPane.DEFAULT_OPTION);
			if(s==null) return;
			else if(!(s.startsWith("$") ^ s.endsWith("%"))){
				JOptionPane.showMessageDialog(parent, "Invalid number format.\nex. $4, $4.50, 10%, 15.75%", "Invalid Format", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(s.startsWith("$")){
				percent = false;
				setValue(Double.parseDouble(s.substring(1)));
			}else{
				percent = true;
				setValue(Double.parseDouble(s.substring(0,s.length()-1)));
			}
		} else if (e.getSource() == remBtn) {
			String s = JOptionPane.showInputDialog(parent, "Input amount to remove\nex. $2.00, $2, 2.00 or 2", "Remove Money", JOptionPane.DEFAULT_OPTION).replace("$","");
			double amt = Double.parseDouble(s);
			
			String reason = JOptionPane.showInputDialog(parent, "What is the reason for this transaction?", "", JOptionPane.DEFAULT_OPTION);
			
			removeMoney(amt, reason);
			
		}else if(e.getSource() == histBtn){
			
			JFrame histFrame = new JFrame("History");
			
			JPanel histPanel = new JPanel(new GridBagLayout());
			
			Iterator<Entry> iter = history.getEntryIterator();
			for(int i=0; iter.hasNext();i++){
				addGridItem(histPanel, new JLabel(iter.next().toString()), 0, i, 1, 1, GridBagConstraints.CENTER);
			}
			
			histFrame.add(histPanel);
			
			histFrame.pack();
			histFrame.setResizable(false);
			histFrame.setLocationRelativeTo(this);
			histFrame.setVisible(true);
		}else if(e.getSource() == cngPriority){
			try{
				priority = Integer.parseInt(JOptionPane.showInputDialog("What is the new priority?"));
			}catch(Exception e1){
				JOptionPane.showMessageDialog(this, "NaN", "NaN", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void updateText(){
		total.setText("Total: $"+String.format("%.2f", money));
	}
	
	public int compareTo(Category other){
		if(!percent && other.percent) return -1;
		if(percent && !other.percent) return 1;
		if(!(percent || other.percent))
			return priority - other.priority;
		return 0;
	}
	
	public void save(File file){
		try{
			FileWriter fw = new FileWriter(file, true);
			
			fw.append(name+":"+money+":"+value+":"+percent+":"+stat.isSelected()+"\n");
			
			fw.close();
			
			history.save();
		}catch(IOException e){
			System.err.println("Could not save to file!");
			e.printStackTrace();
		}
	}
	
	public static void load(String s, GUI gui){
		String[] data = s.split(":");
		Category c = gui.makeCategory(data[0]);
		c.money = (Double.parseDouble(data[1]));
		c.setPercent(data[3].equals("true"));
		c.setValue(Double.parseDouble(data[2]));
		c.setStatic(data[4].equals("true"));
		c.updateText();
		gui.updateLabelText();
	}
}
