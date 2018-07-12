package game.objects;

import static game.Minesweeper.images;

import java.awt.Graphics;

import game.Minesweeper;

public class Square {
	
	private boolean visible;
	private boolean flagged;
	private boolean questioned;
	private final byte x;
	private final byte y;
	
	public Square(int x, int y){
		this.x = (byte)x;
		this.y = (byte)y;
		visible = false;
	}
	
	public byte getX(){return x;}
	public byte getY(){return y;}
	public boolean isHidden(){return !visible;}
	public boolean isFlagged(){return flagged;}
	public boolean isQuestioned(){return questioned;}
	
	public void setVisible(){
		visible = true;
		questioned = false;
	}
	
	public void toggleState(){
		if(visible) return;
		if(!(flagged || questioned)){
			flagged = true;
			Minesweeper.addFlag();
		}else if(flagged){
			flagged = false;
			questioned = true;
			Minesweeper.removeFlag();
		}else if(questioned){
			questioned = false;
		}
		
		draw(Minesweeper.screen.getGraphics());
	}

	public boolean reveal(){
		if(isFlagged()) return false;
		return true;
	}
	
	public void draw(Graphics g){
		int dx = 10 + x*16;
		int dy = 46 + y*16;
		if(isHidden()){
			if(flagged){
				if(Minesweeper.hasLost() && this instanceof game.objects.Number)
					g.drawImage(images.get("notabomb"), dx, dy, null);
				else
					g.drawImage(images.get("flag"), dx, dy, null);
			}else if(questioned){
				g.drawImage(images.get("question"), dx, dy, null);
			}else{
				g.drawImage(images.get("unknown"), dx, dy, null);
			}
		}else{
			g.drawImage(images.get("blank"), dx, dy, null);
		}
		
	}
	
	public String toString(){
		return "Square "+x+" : "+y;
	}
}
