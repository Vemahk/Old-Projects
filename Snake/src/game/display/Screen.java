package game.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import game.Snake;
import game.objects.Fruit;
import game.snake.Part;

public class Screen extends JPanel {
	private static final long serialVersionUID = 3563589734847559776L;
	
	public Screen()
	{
		this.setBackground(Color.BLUE.brighter());
		this.setVisible(true);
		this.addKeyListener(new KeyListener() {

			private long lastChanged = 0;

			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode() - 37;
				boolean check = (code >= 0 && code <= 3) &&//Is the Left, Up, Right, or Down arrow
						code != Snake.head.getDir() && //Isn't the direction it is going
						Math.abs(code - Snake.head.getDir()) != 2; //Isn't the direction behind it.

				if (check) {
					if (Snake.tickCount == lastChanged || Snake.head.hasChangedDir()) {
						Snake.head.addQueue(code);
						return;
					}

					Snake.head.setDir((byte) code);
					lastChanged = Snake.tickCount;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

		});
	}

	public Dimension getPreferredSize(){
		return new Dimension(Snake.WIDTH * 20, Snake.HEIGHT * 20);
	}
	
	public void paintComponent(Graphics window) {
		if(Snake.head==null || Snake.fruits == null)return;
		super.paintComponent(window);
		
		//Paint background
		window.setColor(Color.BLUE);
		window.fillRect(0, 0, getWidth(), getHeight());
		
		//Paint Border
		window.setColor(Color.DARK_GRAY);
		window.fillRect(0, 0, getWidth(), 20);
		window.fillRect(0, 0, 20, getHeight());
		window.fillRect(0, getHeight()-20, getWidth(), 20);
		window.fillRect(getWidth()-20,0,20,getHeight());
		
		//Paint Fruit
		window.setColor(Color.RED);
		for(Fruit f : Snake.fruits)
			window.fillRect(f.getX()*20+1, f.getY()*20+1, 18, 18);
		
		//Paint Snake
		window.setColor(Color.YELLOW);
		Part part = Snake.head;
		while(part!=null){
			window.fillRect(part.getX()*20+1, part.getY()*20+1, 18, 18);
			part = part.getTail();
		}
		window.setColor(Color.WHITE);
		window.drawString("Snake Length: "+Snake.head.getCount(), 0, 10);
		
		if(Snake.mfruit!=null) Snake.mfruit.draw(window);
	}
	
	
}