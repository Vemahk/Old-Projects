package game;

import static game.Minesweeper.images;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import game.objects.Bomb;
import game.objects.Square;

public class Listener extends MouseAdapter{
	
	@Override
	public void mousePressed(MouseEvent e){
		Minesweeper.screen.setFace(1);
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		Minesweeper.screen.setFace(0);
		int x = e.getX();
		int y = e.getY();
		
		if(e.getButton() == MouseEvent.BUTTON1){
			if(Minesweeper.inBounds((x-10)/16, (y-46)/16)){
				x = (x-10)/16;
				y = (y-46)/16;
				
				if(!Minesweeper.screen.hasStarted()){
					Minesweeper.screen.start();
					byte xx = Minesweeper.WIDTH;
					byte yy = Minesweeper.HEIGHT;
					short b = Minesweeper.BOMBS;

					while (b > 0) {
						byte dx = (byte) (Math.random() * xx);
						byte dy = (byte) (Math.random() * yy);
						if (!(Minesweeper.screen.field[dx][dy] instanceof Bomb) && ((dx <= x-2 || dx >= x+2) || (dy <= y-2 || dy >= y+2))){
							Minesweeper.screen.field[dx][dy] = new Bomb(dx, dy);
							b--;
						}
					}
					
					for (byte ix = 0; ix < xx; ix++)
						for (byte iy = 0; iy < yy; iy++)
							if (Minesweeper.screen.field[ix][iy] instanceof game.objects.Number)
								((game.objects.Number) Minesweeper.screen.field[ix][iy]).compile();
				}
				
				if(Minesweeper.screen.hasLost() || Minesweeper.screen.hasWon()) return;
				Square s = Minesweeper.screen.getSquare((byte)x, (byte)y);
				s.reveal();
			}else{
				if(Minesweeper.between(x, y, Minesweeper.screen.getWidth()/2 - 13, Minesweeper.screen.getWidth()/2 + 13, 10, 33)){
					//Restart
					Minesweeper.restart();
				}
			}
		}else if (e.getButton() == MouseEvent.BUTTON3){
			if(!Minesweeper.screen.hasStarted())
				Minesweeper.screen.start();
			if(Minesweeper.screen.hasLost() || Minesweeper.screen.hasWon()) return;
			x-=10;x/=16;
			y-=46;y/=16;
			if(Minesweeper.inBounds(x, y)){
				Square s = Minesweeper.screen.getSquare((byte)x, (byte)y);
				s.toggleState();
			}
		}
		if(Minesweeper.isDone())
			Minesweeper.screen.finish();
	}
	
	public void drawFace(boolean down){
		if(Minesweeper.screen.hasLost() || Minesweeper.screen.hasWon()) return;
		Graphics g = Minesweeper.screen.getGraphics();
		if(down)
			g.drawImage(images.get("ooo"), Minesweeper.screen.getWidth()/2 - 13, 10, null);
		else
			g.drawImage(images.get("default"), Minesweeper.screen.getWidth()/2 - 13, 10, null);
	}
	
}
