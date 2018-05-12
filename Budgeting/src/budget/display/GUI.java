package budget.display;

import static budget.Main.addGridItem;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 6396942454003900281L;

	ArrayList<Category> categories = new ArrayList<Category>();
	
	JTabbedPane tabbedPane;
	JTextField addCate, remCate, addMoney;
	JRadioButton male, female;
	JButton saveBtn, saveandexitBtn, cancelBtn, add_ok_btn, rem_ok_btn, addm_ok_btn, overviewBtn;
	JLabel total;
	
	public GUI(String title){
		super(title);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		addGridItem(panel, new JLabel("Add Category:"), 0, 0, 1, 1, GridBagConstraints.EAST);
		addGridItem(panel, new JLabel("Remove Category:"), 0, 1, 1, 1, GridBagConstraints.EAST);
		addGridItem(panel, new JLabel("Add Money:"), 0, 3, 1, 1, GridBagConstraints.EAST);
		addGridItem(panel, (JComponent)Box.createVerticalStrut(15), 0, 4, 1, 1, GridBagConstraints.EAST);
		addGridItem(panel, total = new JLabel("Total: $0.00"), 4, 0, 1, 1, GridBagConstraints.EAST);
		addGridItem(panel, addCate = new JTextField(10), 1, 0, 2, 1, GridBagConstraints.WEST);
		addGridItem(panel, remCate = new JTextField(10), 1, 1, 2, 1, GridBagConstraints.WEST);
		addGridItem(panel, addMoney = new JTextField("$",5), 1, 3, 1, 1, GridBagConstraints.WEST);
		
		add_ok_btn = new JButton("OK");
		rem_ok_btn = new JButton("OK");
		addm_ok_btn = new JButton("OK");
		
		add_ok_btn.addActionListener(this);
		rem_ok_btn.addActionListener(this);
		addm_ok_btn.addActionListener(this);
		
		addGridItem(panel, add_ok_btn, 3, 0, 1, 1, GridBagConstraints.WEST);
		addGridItem(panel, rem_ok_btn, 3, 1, 1, 1, GridBagConstraints.WEST);
		addGridItem(panel, addm_ok_btn, 3, 3, 1, 1, GridBagConstraints.WEST);
		
		//BottomButtons
		Box buttonBox = Box.createHorizontalBox();
		saveBtn = new JButton("Save");
		saveandexitBtn = new JButton("Save & Exit");
		cancelBtn = new JButton("Cancel");
		
		saveBtn.addActionListener(this);
		saveandexitBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
		
		buttonBox.add(Box.createHorizontalStrut(20));
		buttonBox.add(saveBtn);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(saveandexitBtn);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(cancelBtn);
		addGridItem(panel, buttonBox, 1, 5, 4, 1, GridBagConstraints.SOUTHEAST);
		
		overviewBtn = new JButton("Overview");
		overviewBtn.addActionListener(this);
		addGridItem(panel, overviewBtn, 0, 5, 1, 1, GridBagConstraints.SOUTHWEST);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Main", panel);
		tabbedPane.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				GUI.this.pack();
			}
		});
		
		add(tabbedPane);
		
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveandexitBtn || e.getSource() == saveBtn) {
			// Save stuff!
			saveData();
			JOptionPane.showMessageDialog(this, "Saved!", "", JOptionPane.INFORMATION_MESSAGE);
			if (e.getSource() == saveandexitBtn)
				System.exit(0);
		} else if (e.getSource() == cancelBtn) {
			if (JOptionPane.showConfirmDialog(this,
					"Are you sure?\nAll unsaved data will be lost.",
					"Exit Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				System.exit(0);
		} else if (e.getSource() == add_ok_btn) {
			if(addCate.getText().length() > 0){
				makeCategory(addCate.getText());
				pack();
			}
			addCate.setText("");
		} else if (e.getSource() == rem_ok_btn) {
			Category target = null;
			for (Category c : categories)
				if (c.getName().equals(remCate.getText())) {
					target = c;
					break;
				}
			tabbedPane.remove(target);
			categories.remove(target);
			pack();
			updateLabelText();
			remCate.setText("");
		} else if (e.getSource() == addm_ok_btn) {
			
			//Check percents of categories
			double totalPercent = 0;
			for(Category c : categories)
				if(c.isPercent()) totalPercent += c.getValue();
			
			if(totalPercent != 100.0){
				JOptionPane.showMessageDialog(this,
						"Your categories' percent is " + (totalPercent > 100 ? "greater" : "less")
								+ " than 100%!\nBecause of this, descrepancies would be made.\nTransaction will not take place until this is fixed.\nCurrent Total: "
								+ totalPercent+"%",
						"Warning!", JOptionPane.DEFAULT_OPTION);
				return;
			}
			
			//Get Entry Data
			String reason = JOptionPane.showInputDialog(this, "What is the reason for the input?", "");
			
			//Begin adding money
			String cutString = addMoney.getText().trim().replace("$", "");
			if(cutString.length() == 0) return;
			double addedVal = Double.parseDouble(cutString);
			
			//Static Values first
			for(Category c : categories){
				if(!c.isPercent()){
					if(addedVal > c.getValue()){
						addedVal -= c.getValue();
						c.addMoney(c.getValue(), reason);
					}else{
						c.addMoney(addedVal, reason);
						JOptionPane.showMessageDialog(this, "Money exausted before some categories.", "", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
			
			//Percents Next
			double max = addedVal;
			for(Category c : categories)
				if(c.isPercent()){
					double val = max * (c.getValue() / 100);
					c.addMoney(val, reason);
					addedVal -= val;
				}
			
			if(addedVal > .1){
				Category maxPercent = null;
				for(Category c : categories){
					if(c.isPercent()){
						if(maxPercent == null)
							maxPercent = c;
						else if(maxPercent.getValue() < c.getValue()) maxPercent = c;
					}
				}
				
				if(maxPercent == null){
					JOptionPane.showMessageDialog(this, "Excess Money after Distribution!\nNo excess category exists!\nMaking one...", "Error!", JOptionPane.ERROR_MESSAGE);
					makeCategory("Excess").addMoney(addedVal, reason);
					
				}else{
					maxPercent.addMoney(addedVal, reason);
					JOptionPane.showMessageDialog(this, "Excess $"+String.format("%.2f",addedVal)+"\nPutting into Category "+maxPercent.getName(), "", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
			
			updateLabelText();
			
			addMoney.setText("$");
		}else if(e.getSource() == overviewBtn){
			Overview v = new Overview(this);
		}
	}

	public void updateLabelText(){
		double all = 0;
		for(Category c : categories)
			all += c.getMoney();
		
		total.setText("Total: $"+String.format("%.2f", all));
	}

	public void saveData(){
		File dir = new File(System.getProperty("user.home")+"/AppData/Local/JAVA Budgeting/");
		if(!dir.exists())dir.mkdirs();
		
		File file = new File(dir, "categories.dat");
		try{
			PrintWriter pw = new PrintWriter(file);
			pw.close();
		}catch(Exception e){e.printStackTrace();}
		
		for(Category c : categories) c.save(file);
	}
	
	public Category makeCategory(String name){
		for(Category c : categories) if(c.getName().equals(name)){
			JOptionPane.showMessageDialog(this, "Category '"+name+"' already exists!", "", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		Category c = new Category(this, name);
		categories.add(c);
		tabbedPane.addTab(name, c);
		return c;
	}
}