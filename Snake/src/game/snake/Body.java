package game.snake;

public class Body extends Part {

	public Body(byte x, byte y, Part head){
		super(x,y);
		head.setBack(this);
	}
	
}
