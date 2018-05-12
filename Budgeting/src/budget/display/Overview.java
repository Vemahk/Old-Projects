package budget.display;

import static budget.Main.addGridItem;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import budget.display.specials.CButton;
import budget.display.specials.DummyCategory;

public class Overview extends JFrame implements ActionListener{
	private static final long serialVersionUID = 8528118466491018238L;
	
	private GUI parent;
	
	ArrayList<DummyCategory> dummyCategories = new ArrayList<DummyCategory>();
	ArrayList<CButton> transBtns = new ArrayList<CButton>();
	ArrayList<JLabel> moneyLabels = new ArrayList<>();
	
	JButton appCngBtn, transferBtn;
	
	JPanel panel;
	
	public Overview(GUI parent){
		super("Overview");
		this.parent = parent;
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		panel = new JPanel(new GridBagLayout());
		
		moneyLabels.clear();
		int i = 0;
		for(i=0;i<parent.categories.size();i++){
			DummyCategory c = new DummyCategory(parent.categories.get(i),this);
			dummyCategories.add(c);
			JLabel label = new JLabel("$"+c.getMoney());
			moneyLabels.add(label);
			addGridItem(panel, new JLabel(c.getName()+":"), 0, i, 1, 1, GridBagConstraints.EAST);
			addGridItem(panel, label, 1, i, 1, 1, GridBagConstraints.WEST);
		}
		
		Box btnBox = Box.createHorizontalBox();
		
		appCngBtn = new JButton("Apply Changes");
		transferBtn = new JButton("Transfer Money");
		
		transferBtn.addActionListener(this);
		appCngBtn.addActionListener(this);
		
		btnBox.add(transferBtn);
		btnBox.add(Box.createHorizontalStrut(5));
		btnBox.add(appCngBtn);
		
		addGridItem(panel, btnBox, 0, i, 2, 1, GridBagConstraints.SOUTH);
		
		add(panel);
		
		pack();
		this.setLocationRelativeTo(parent);
		setResizable(false);
		setVisible(true);
	}
	
	private void updateLabels(){
		for(int i=0;i<dummyCategories.size();i++)
			moneyLabels.get(i).setText("$"+dummyCategories.get(i).getMoney());
	}
	
	private DummyCategory from;
	private DummyCategory to;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.transferBtn){
			int i=0;
			for(i=0;i<dummyCategories.size();i++){
				CButton c = new CButton("From", dummyCategories.get(i), this);
				transBtns.add(c);
				addGridItem(panel, c, 2, i, 1, 1, GridBagConstraints.CENTER);
			}
			
			transferBtn.setEnabled(false);
			appCngBtn.setEnabled(false);
			pack();
		}else if(e.getSource() == this.appCngBtn){
			for(DummyCategory c : dummyCategories) c.applyChanges();
			this.dispose();
		}else if(e.getSource() instanceof CButton && isTransferButton((CButton)e.getSource())){
			if(from == null){
				CButton c = (CButton)e.getSource();
				c.setEnabled(false);
				from = c.getDummyCategory();
				for(CButton ci : transBtns)
					if(ci != c) ci.setText("To");
				
			}else{
				to = ((CButton)e.getSource()).getDummyCategory();
				for(CButton c : transBtns) panel.remove(c);
				pack();
				
				String s = JOptionPane.showInputDialog(this, "What amount would you like to tranfer?\ni.e. $2, $2.00, 2, 2.00", "Transfer Money", JOptionPane.DEFAULT_OPTION).replace("$", "");
				try{
					double amt = Double.parseDouble(s);
					from.transferMoney(to, amt);
				}catch(Exception e1){
					JOptionPane.showMessageDialog(this, "Error in amount input.", "Invalid Syntax", JOptionPane.ERROR_MESSAGE);
				}
				
				updateLabels();
				transferBtn.setEnabled(true);
				appCngBtn.setEnabled(true);
			}
		}
	}
	
	public boolean isTransferButton(CButton b){
		for(CButton j : transBtns) if(b==j) return true;
		return false;
	}
}
