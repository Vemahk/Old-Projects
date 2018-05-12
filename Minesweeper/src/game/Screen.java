package game;

import static game.Minesweeper.images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JPanel;

import game.objects.Bomb;
import game.objects.Clock;
import game.objects.Square;

public class Screen extends JPanel {
	private static final long serialVersionUID = 9082029290387621687L;

	public Square[][] field;
	private boolean started = false;
	private boolean lost = false;
	private boolean won = false;

	private String face = "default";
	
	public Screen() {
		setBackground(Color.WHITE);

		addMouseListener(new Listener());
		makeField();
		
		setVisible(true);
	}

	public void end() {
		face = "lose";
		Minesweeper.timer.stop();
		lost = true;
		repaint();
	}

	public void finish() {
		face = "win";
		Minesweeper.timer.stop();
		won = true;
		repaint();
	}

	public boolean hasLost() {
		return lost;
	}

	public boolean hasWon() {
		return won;
	}

	public void restart() {
		started = false;
		lost = false;
		won = false;
		makeField();
		Minesweeper.setFlagCount((short)0);
	}

	public boolean hasStarted() {
		return started;
	}

	public void start() {
		started = true;
		Minesweeper.timer.start();
	}

	public Square getSquare(byte x, byte y) {
		if (x < 0 || x >= Minesweeper.WIDTH || y < 0 || y >= Minesweeper.HEIGHT)
			return null;
		return field[x][y];
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
		for (Square[] f : field)
			for (Square s : f)
				s.draw(g);
		
		//Bombs_Left
		String bombsLeft = ""+(Minesweeper.BOMBS-Minesweeper.flagCount);
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
	
	public void makeField() {
		byte x = Minesweeper.WIDTH;
		byte y = Minesweeper.HEIGHT;

		field = new Square[x][y];
		Minesweeper.bombs = new ArrayList<Bomb>();

		for (byte ix = 0; ix < x; ix++)
			for (byte iy = 0; iy < y; iy++)
				if (field[ix][iy] == null)
					field[ix][iy] = new game.objects.Number(ix, iy);
		
		if(Minesweeper.timer != null)
			Minesweeper.timer.stop();
		
		Minesweeper.timer = new Clock();
	}

	public Dimension getPreferredSize() {
		return new Dimension(Minesweeper.WIDTH * 16 + 18, Minesweeper.HEIGHT * 16 + 54);
	}
}
