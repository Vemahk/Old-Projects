package budget.display.specials;

import javax.swing.JOptionPane;

import budget.display.Category;
import budget.display.Overview;

public class DummyCategory {

	private Overview frame;
	private Category parent;
	private double money;
	
	public DummyCategory(Category c, Overview frame){
		parent = c;
		money = c.getMoney();
		this.frame = frame;
	}
	
	public Category getCategory(){
		return parent;
	}
	
	public String getName(){
		return parent.getName();
	}
	
	public double getMoney(){
		return money;
	}
	
	public void addMoney(double x){
		money += x;
	}
	
	public void removeMoney(double x){
		money -= x;
	}
	
	public void transferMoney(DummyCategory other, double amt){
		if(amt > money){
			JOptionPane.showMessageDialog(frame, "There is not enough money for this transaction!", "Not enough money!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		money-=amt;
		other.addMoney(amt);
	}
	
	public void applyChanges(){
		parent.setMoney(money);
	}
	
}
