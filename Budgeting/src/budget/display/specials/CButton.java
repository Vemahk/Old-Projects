package budget.display.specials;

import javax.swing.JButton;

import budget.display.Overview;

public class CButton extends JButton{
	private static final long serialVersionUID = 204009975316822971L;
	
	private DummyCategory dc;
	
	public CButton(String s, DummyCategory c, Overview parent){
		super(s);
		dc = c;
		this.addActionListener(parent);
	}
	
	public DummyCategory getDummyCategory(){
		return dc;
	}
	
}
