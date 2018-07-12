package me.vem.snake.objects;

import java.awt.Color;
import java.awt.Graphics;

public class MagicFruit extends Fruit{

	public static MagicFruit instance;
	
	public MagicFruit(){
		super();
		MagicFruit.instance = this;
	}
	
	private Color lastColor = new Color(255,0,0);
	public void draw(Graphics window){
		window.setColor(lastColor = nextColor());
		window.fillRect(getX()*20+1, getY()*20+1, 18, 18);
	}
	
	private byte to = 1; //0-Red, 1-Green, 2-Blue
	private Color nextColor(){
		int blue = lastColor.getBlue();
		int green = lastColor.getGreen();
		int red = lastColor.getRed();
		
		if(to==0){
			red+=17;
			blue-=17;
			if(red==255) to=1;
		}else if(to==1){
			green+=17;
			red-=17;
			if(green==255)to=2;
		}else{
			blue+=17;
			green-=17;
			if(blue==255)to=0;
		}
		
		return new Color(red, green, blue);
	}
	
}
