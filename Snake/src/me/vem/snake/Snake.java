package me.vem.snake;

import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.LinkedList;

import me.vem.snake.objects.Fruit;
import me.vem.snake.objects.MagicFruit;

public class Snake {

	private LinkedList<bPoint> snake;
	
	/**
	 * 0 is Right,
	 * 1 is Down,
	 * 2 is Left,
	 * 3 is Up.
	 */
	private int dir;
	
	public int queue = -1;
	public int lenQueue = 0;
	
	public Snake() {
		snake = new LinkedList<>();
		snake.add(new bPoint(2, 2));
		snake.add(new bPoint(1, 2));
		snake.add(new bPoint(1, 1));
	}

	public int size() {
		return snake.size();
	}
	
	public Iterator<bPoint> iterator(){
		return snake.iterator();
	}
	
	public synchronized void run() {
		//Turn
		if(queue >= 0 && Math.abs(queue - dir) != 2) {
			dir = queue;
			queue = -1;
		}
		
		int dx = 0;
		int dy = 0;
		
		switch(dir) {
		case 0: dx++; break;
		case 1: dy++; break;
		case 2: dx--; break;
		default: dy--;
		}
		
		bPoint nhead = snake.getFirst().moveClone(dx, dy);
		if(snake.contains(nhead) || outOfBounds(nhead))
			App.stop();
		snake.addFirst(nhead);
		
		if(lenQueue == 0)
			snake.removeLast();
		else{
			lenQueue--;
			if(size()%15 == 0)
				new Fruit();
		}
		
		Fruit on = Fruit.getAt(nhead);
		if(on!=null) {
			if(on instanceof MagicFruit) {
				Fruit.fruits.remove(on);
				MagicFruit.instance = null;
				lenQueue+=15;
			}else {
				on.move();
				lenQueue+=3;
				
				if(MagicFruit.instance == null && Math.random() < .1)
					new MagicFruit();
			}
		}
	}
	
	private boolean outOfBounds(bPoint p) {
		return p.x <= 0 || p.y <= 0 || p.x >= App.WIDTH-1 || p.y >= App.HEIGHT-1;
	}

	public void setDir(int ndir) {
		this.dir = ndir;
	}
	
	public static int keyToDir(int keyCode) {
		int out = 0;
		switch(keyCode) {
		case KeyEvent.VK_UP: out++;
		case KeyEvent.VK_LEFT: out++;
		case KeyEvent.VK_DOWN: out++;
		}
		return out;
	}
	
	public static class bPoint{
		
		public byte x;
		public byte y;
		
		public bPoint(int x, int y) {
			this.x = (byte)x;
			this.y = (byte)y;
		}
		
		public bPoint moveClone(int dx, int dy) {
			return new bPoint(x + dx, y + dy);
		}

		@Override
		public Object clone() { return new bPoint(x, y); }
		
		public String toString() {
			return x+"x"+y;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			bPoint other = (bPoint) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
	}
}