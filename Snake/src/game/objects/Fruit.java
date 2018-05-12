package game.objects;

import game.Snake;
import game.snake.Part;

public class Fruit {
	
	private byte x;
	private byte y;
	
	public Fruit(){
		move();
	}
	
	public void set(byte x, byte y){
		this.x = x;
		this.y = y;
	}
	
	public byte getX(){return x;}
	public byte getY(){return y;}
	
	public void move(){
		byte randX = (byte)(Math.random()*(Snake.WIDTH-2)+1);
		byte randY = (byte)(Math.random()*(Snake.HEIGHT-2)+1);
		set(randX, randY);
		
		if(isInSnake() || isInOtherFruit())move();
	}
	
	public boolean isInSnake(){
		
		Part snake = Snake.head;
		while(snake!=null){
			if(snake.getX() == x && snake.getY()==y) return true;
			snake = snake.getTail();
		}
		
		return false;
	}
	
	public boolean isInOtherFruit(){
		for(Fruit f : Snake.fruits){
			if(f==this)continue;
			if(f.getX()==x && f.getY()==y)
				return true;
		}
		return false;
	}
	
}
