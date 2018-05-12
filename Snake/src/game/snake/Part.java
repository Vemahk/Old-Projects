package game.snake;

public class Part {

	private byte x;
	private byte y;
	
	private Part front;
	private Part back;
	
	public Part(byte x, byte y){
		this.x = x;
		this.y = y;
	}
	
	public void setFront(Part f){
		front = f;
	}
	
	public void setBack(Part b){
		back = b;
		b.setFront(this);
	}
	
	public byte getX(){return x;}
	public byte getY(){return y;}
	public Part getHead(){return front;}
	public Part getTail(){return back;}
	
	public void move(byte x, byte y){
		this.x += x;
		this.y += y;
	}
	
	public void go(byte x, byte y){
		this.x = x;
		this.y = y;
	}
	
}
