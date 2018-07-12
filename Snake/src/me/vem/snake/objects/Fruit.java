package me.vem.snake.objects;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import me.vem.snake.App;
import me.vem.snake.Snake.bPoint;

public class Fruit {
	public static List<Fruit> fruits = new LinkedList<>();
	
	public static Fruit getAt(bPoint p) {
		for(Fruit f : fruits)
			if(f.pos.equals(p))
				return f;
		return null;
	}
	
	private bPoint pos;
	
	public Fruit(){
		pos = new bPoint(-1, -1);
		move();
		fruits.add(this);
	}
	
	public void set(byte x, byte y){
		pos.x = x;
		pos.y = y;
	}
	
	public byte getX(){return pos.x;}
	public byte getY(){return pos.y;}
	
	public void move(){
		while(outOfBounds() || isInSnake() || isInOtherFruit()) {
			byte rx = (byte)(Math.random()*(App.WIDTH-2)+1);
			byte ry = (byte)(Math.random()*(App.HEIGHT-2)+1);
			set(rx, ry);
		}
	}
	
	private boolean outOfBounds() { return pos.x < 0 || pos.y < 0; }
	
	private boolean isInSnake(){
		Iterator<bPoint> iter = App.snake.iterator();
		while(iter.hasNext())
			if(iter.next().equals(pos))
				return true;
		
		return false;
	}
	
	private boolean isInOtherFruit(){
		for(Fruit f : Fruit.fruits){
			if(f==this)continue;
			if(pos.equals(f.pos))
				return true;
		}
		return false;
	}
	
}
