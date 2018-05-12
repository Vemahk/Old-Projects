package game.objects;

import static game.Minesweeper.images;

import java.awt.Graphics;

import game.Minesweeper;

public class Clock {
	
	private short time = 0;
	private Thread timeTracker = new Thread(){
		public void run(){
			while(Minesweeper.screen.hasStarted()){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					return;
				}
				time++;
				Minesweeper.screen.repaint();
			}
		}
	};
	
	public void start(){
		timeTracker.start();
	}
	
	public void stop(){
		timeTracker.interrupt();
	}
	
	public void draw(Graphics g){
		String toDraw = "" + time;
		
		while(toDraw.length() < 3) toDraw = "0" + toDraw;
		while(toDraw.length() > 3) toDraw = toDraw.substring(1);
		
		for(int i=0;i<3;i++)
			g.drawImage(images.get(toDraw.substring(i, i+1)), Minesweeper.screen.getWidth()-49 + i*13, 13, null);
	}
	
}
