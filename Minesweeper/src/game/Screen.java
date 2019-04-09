package game;

import static game.Minesweeper.images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import game.objects.Square;

public class Screen extends JPanel {
	private static final long serialVersionUID = 9082029290387621687L;

	private String face = "default";
	
	public Screen() {
		setBackground(Color.WHITE);
		addMouseListener(new Listener());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Horizontal Bars
		Image bar_h = images.get("bar_horizontal");
		for (int x = 10; x <= getWidth() - 11; x++) {
			g.drawImage(bar_h, x, 0, null);
			g.drawImage(bar_h, x, 36, null);
			g.drawImage(bar_h, x, getHeight() - 10, null);
		}

		// Vertical Bar
		Image bar_v = images.get("bar_vertical");
		for (int y = 10; y <= 35; y++) {
			g.drawImage(bar_v, 0, y, null);
			g.drawImage(bar_v, getWidth() - 10, y, null);
		}

		for (int y = 46; y <= getHeight() - 11; y++) {
			g.drawImage(bar_v, 0, y, null);
			g.drawImage(bar_v, getWidth() - 10, y, null);
		}

		// Corners
		g.drawImage(images.get("top_left_corner"), 0, 0, null);
		g.drawImage(images.get("top_right_corner"), getWidth() - 10, 0, null);
		g.drawImage(images.get("bottom_left_corner"), 0, getHeight() - 10, null);
		g.drawImage(images.get("bottom_right_corner"), getWidth() - 10, getHeight() - 10, null);
		g.drawImage(images.get("field_top_left_corner"), 0, 36, null);
		g.drawImage(images.get("field_top_right_corner"), getWidth() - 10, 36, null);

		// Top-Section Filler
		Image sliver = images.get("top_sliver");
		for (int x = 10; x <= getWidth() / 2 - 14; x++)
			g.drawImage(sliver, x, 10, null);

		for (int x = getWidth() / 2 + 13; x <= getWidth() - 11; x++)
			g.drawImage(sliver, x, 10, null);
		
		//Clock
		Minesweeper.timer.draw(g);
		
		//Face
		g.drawImage(images.get(face), Minesweeper.screen.getWidth() / 2 - 13, 10, null);
		
		//Field
		for (Square[] f : Minesweeper.field)
			for (Square s : f)
				s.draw(g);
		
		//Bombs_Left
		String bombsLeft = ""+(Minesweeper.DIFFICULTY.getBombs()-Minesweeper.flagCount);
		while(bombsLeft.length()<3){
			if(bombsLeft.startsWith("-"))
				bombsLeft = "-0"+bombsLeft.substring(1);
			else bombsLeft = "0"+bombsLeft;
		}
		
		for(int i=0;i<3;i++){
			String getS = bombsLeft.substring(i,i+1).equals("-") ? "minus" : bombsLeft.substring(i, i+1);
			g.drawImage(images.get(getS), 10 + i*13, 13, null);
		}
		
	}
	
	public void setFace(int i){
		if(i==0) face = "default";
		if(i==1) face = "ooo";
		if(i==2) face = "lose";
		if(i==3) face = "win";
		repaint();
	}

	public Dimension getPreferredSize() {
		return new Dimension(Minesweeper.DIFFICULTY.getWidth() * 16 + 10, Minesweeper.DIFFICULTY.getHeight()* 16 + 46);
	}
}
