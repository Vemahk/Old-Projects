package game.objects;

import java.awt.Graphics;

import game.Minesweeper;

import static game.Minesweeper.images;

public class Bomb extends Square{
	
	public Bomb(byte x, byte y){
		super(x,y);
		Minesweeper.bombs.add(this);
	}
	
	@Override
	public void setVisible(){
		if(!isFlagged())
			super.setVisible();
	}
	
	@Override
	public boolean reveal(){
		if(isFlagged()) return false;
		for(Bomb b : Minesweeper.bombs)
			b.setVisible();
		red = true;
		Minesweeper.screen.end();
		
		return true;
	}
	
	private boolean red = false;
	
	@Override
	public void draw(Graphics g){
		super.draw(g);
		
		if(!isHidden())
			if(red) g.drawImage(images.get("bomb_red"), getX()*16 + 10, getY()*16+46, null);
			else g.drawImage(images.get("bomb_normal"), getX()*16 + 10, getY()*16+46, null);
	}
}