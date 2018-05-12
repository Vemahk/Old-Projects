package game.snake;

import game.Snake;
import game.objects.Fruit;
import game.objects.MagicFruit;

public class Head extends Part{
	private byte dir;
	private short num = 1;
	
	public Head(byte x, byte y){
		super(x,y);
		dir = 2;
		
		new Body((byte)(x-1), (byte)(y-1), new Body((byte)(x-1),y, this));
	}
	
	private long lastTurned = 0;
	public boolean justTurned(){
		return lastTurned == Snake.tickCount;
	}
	
	public void setDir(byte dir){
		if(justTurned())
			return;
			
		if(Math.abs(dir - Snake.head.getDir()) != 2){
			this.dir = dir;
			lastTurned = Snake.tickCount;
		}
		
	}
	
	public byte getDir(){return dir;}
	public short getCount(){return num;}

	private boolean dirChanged = false;
	public boolean hasChangedDir(){
		if(dirChanged){
			dirChanged = false;
			return true;
		}
		return false;
	}
	
	/**
	 * dir: 0-Up, 1-Right, 2-Down, 3-Left
	 */
	public void move(){
		//Check dir, then move
		byte z=0, o=1, n=-1;
		if(dir==0) this.move(n,z);
		if(dir==1) this.move(z,n);
		if(dir==2) this.move(o,z);
		if(dir==3) this.move(z,o);
	}
	
	public void addQueue(int i){
		if(queued==-1 && Math.abs(i-dir)!=2)queued=(byte)i;
	}
	
	private byte queued = -1;
	private byte addTail = 0;
	public void run(){
		//Check queued
		
		if(queued > 0){
			setDir(queued);
			queued = -1;
			dirChanged = true;
		}
		
		//Move
		Part tail = getTail()!=null ? getTail() : this;
		short num = 1;
		while(tail!=this){
			num++;
			if(tail.getTail()==null) break;
			tail = tail.getTail();
		}
		this.num = num;
		
		Part part = tail;
		while(part!=this){
			part.go(part.getHead().getX(), part.getHead().getY());
			part = part.getHead();
		}
		move();
		
		//Check should stop
		if(invalidSpace()){
			Snake.stop();
			return;
		}
		
		//Growth
		if(addTail>0){
			new Body((byte)-1, (byte)-1, tail);
			addTail--;
			if((getCount()+1)%30==0) Snake.fruits.add(new Fruit());
		}
		
		//Check fruit
		Fruit f = getFruit();
		if(f!=null){
			if(f instanceof MagicFruit){
				Snake.mfruit = null;
				addTail+=12;
			}else{
				f.move();
				if(Snake.mfruit == null){
					boolean rand = Math.random()<.05;
					if(rand){
						Snake.mfruit = new MagicFruit();
					}
				}
			}
			addTail+=3;
		}
		
		
	}
	
	public Fruit getFruit(){
		for(Fruit f : Snake.fruits){
			if(f.getX()==getX() && f.getY()==getY()){
				return f;
			}
		}
		if(Snake.mfruit!=null){
			if(Snake.mfruit.getX()==getX() && Snake.mfruit.getY()==getY()){
				MagicFruit saved = Snake.mfruit;
				Snake.mfruit = null;
				return saved;
			}
		}
		return null;
	}
	
	public boolean invalidSpace(){ 
		return getX()<=0||getY()<=0||getX()>=(Snake.WIDTH-1)||getY()>=(Snake.HEIGHT-1)||isInOther();
	}
	
	public boolean isInOther(){
		Part tail = getTail();
		while(tail!=null){
			if(getX()==tail.getX()&&getY()==tail.getY())
				return true;
			tail = tail.getTail();
		}
		return false;
	}
}
