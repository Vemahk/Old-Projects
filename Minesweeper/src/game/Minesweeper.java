package game;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.objects.Bomb;
import game.objects.Clock;
import game.objects.Square;

public class Minesweeper {

	public static HashMap<String, Image> images = new HashMap<String, Image>();
	
	public static void compileImages(String s) throws IOException{
		
		InputStream is = Minesweeper.class.getResourceAsStream(s);
		
		StringBuffer read = new StringBuffer();
		
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = is.read(buffer)) > 0)
			for(int i=0;i<len;i++)
				read.append((char)buffer[i]);
		
		Scanner scan = new Scanner(read.toString());
		while(scan.hasNextLine()){
			String in = scan.nextLine();
			images.put(in.substring(in.lastIndexOf('/') + 1, in.length()-4),
					ImageIO.read(Minesweeper.class.getResourceAsStream("resources/" + in)));
		}
		scan.close();
	}

	public static byte WIDTH;
	public static byte HEIGHT;
	public static short BOMBS;

	public static short flagCount = 0;

	public static ArrayList<Bomb> bombs;

	public static Screen screen;
	public static Clock timer;

	public static void main(String[] args) {
		
		try{
			compileImages("resources/graphics.dat");
		}catch(IOException e){
			e.printStackTrace();
		}
		
		compileDifficulty();

		buildFrame();
		restart();
		
	}

	public static void restart() {
		if (timer != null)
			timer.stop();
		timer = new Clock();
		screen.restart();
	}

	private static void buildFrame() {
		JFrame frame = new JFrame("Minesweeper");

		screen = new Screen();
		frame.getContentPane().add(screen);
		frame.pack();

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

		screen.requestFocus();
	}

	public static boolean inBounds(int xi, int yi) {
		return xi >= 0 && xi < WIDTH && yi >= 0 && yi < HEIGHT;
	}

	public static boolean inBounds(byte xi, byte yi) {
		return inBounds((int) xi, (int) yi);
	}

	public static boolean between(int x, int y, int tx, int tx2, int ty, int ty2) {
		return x >= tx && x < tx2 && y >= ty && y < ty2;
	}

	public static void setFlagCount(short s) {
		flagCount = s;
		screen.repaint();
	}

	public static void addFlag() {
		flagCount++;
		screen.repaint();
	}

	public static void removeFlag() {
		flagCount--;
		screen.repaint();
	}

	public static boolean isDone() {
		for (Square[] ar : screen.field)
			for (Square s : ar) {
				if (s instanceof Bomb)
					if (!s.isFlagged())
						return false;
				if (s instanceof game.objects.Number)
					if (s.isHidden())
						return false;
			}
		return true;
	}

	private static void compileDifficulty() {
		Difficulty diff = (Difficulty) JOptionPane.showInputDialog(null, "Select Difficulty", "", JOptionPane.QUESTION_MESSAGE,
				null, Difficulty.values(), Difficulty.values()[0]);
		WIDTH = diff.getWidth();
		HEIGHT = diff.getHeight();
		BOMBS = diff.getBombs();
	}
}

enum Difficulty{
	EASY(8, 8, 10, "Easy"),
	MED(16, 16, 40, "Medium"),
	HARD(31, 16, 99, "Hard"),
	LOL(50, 30, 250, "lol wat 8/8 gr8gamem8");
	
	private byte width;
	private byte height;
	private short bombs;
	private String name;
	
	private Difficulty(int w, int h, int b, String name) {
		width = (byte)w;
		height = (byte)h;
		bombs = (short)b;
		this.name = name;
	}
	
	public byte getWidth() { return width; }
	public byte getHeight() { return height; }
	public short getBombs() { return bombs; }
	public String getName() { return name; }
	public String toString() { return getName(); }
}