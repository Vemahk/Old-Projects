package game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import game.objects.Bomb;
import game.objects.Square;

import static game.Minesweeper.*;

public class Listener extends MouseAdapter{
	
	@Override
	public void mousePressed(MouseEvent e){
		screen.setFace(1);
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		screen.setFace(0);

		int x = (e.getX()-10) >> 4;
		int y = (e.getY()-46) >> 4;
		boolean onBoard = inBounds(x, y);
		
		if(!hasStarted() && onBoard){
			start();
			byte xx = WIDTH;
			byte yy = HEIGHT;
			short b = BOMBS;

			while (b > 0) {
				int dx = (int) (Math.random() * xx);
				int dy = (int) (Math.random() * yy);
				if (!(field[dx][dy] instanceof Bomb) && ((dx <= x-2 || dx >= x+2) || (dy <= y-2 || dy >= y+2))){
					field[dx][dy] = new Bomb(dx, dy);
					b--;
				}
			}
			
			for (int ix = 0; ix < xx; ix++)
				for (int iy = 0; iy < yy; iy++)
					if (field[ix][iy] instanceof game.objects.Number)
						((game.objects.Number) field[ix][iy]).compile();
		}
		
		if(e.getButton() == MouseEvent.BUTTON1){
			if(onBoard) {
				if(hasLost() || hasWon()) return;
				Square s = getSquare(x, y);
				s.reveal();
			}else{
				if(between(e.getX(), e.getY(), screen.getWidth()/2 - 13, screen.getWidth()/2 + 13, 10, 33)){
					//Restart
					restart();
				}
			}
		}else if (e.getButton() == MouseEvent.BUTTON3){
			if(!hasStarted())
				start();
			
			if(hasLost() || hasWon())
				return;
			
			if(onBoard)
				getSquare(x, y).toggleState();
		}
		
		if(isDone())
			finish();
	}
}