package game;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

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
		BufferedReader buff = new BufferedReader(new InputStreamReader(is));
		
		while(buff.ready()){
			String in = buff.readLine();
			images.put(in.substring(in.lastIndexOf('/') + 1, in.length()-4),
					ImageIO.read(Minesweeper.class.getResourceAsStream("resources/" + in)));
		}

		buff.close();
	}

	public static Difficulty DIFFICULTY;

	public static Square[][] field;

	public static short flagCount = 0;

	public static ArrayList<Bomb> bombs;

	public static Screen screen;
	public static Clock timer;
	
	private static boolean started;
	private static boolean lost;
	private static boolean won;

	public static void main(String[] args) throws IOException {
		compileImages("resources/graphics.dat");
		setDifficulty();

		buildFrame();
		restart();
	}

	public static void restart() {
		if (timer != null)
			timer.stop();
		timer = new Clock();
		
		started = false;
		lost = false;
		won = false;
		makeField();
		setFlagCount((short)0);
	}

	private static void buildFrame() {
		JFrame frame = new JFrame("Minesweeper");
		
		screen = new Screen();
		
		frame.add(screen);
		frame.pack();

		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		screen.requestFocus();
	}
	
	public static void makeField() {
		int x = DIFFICULTY.getWidth();
		int y = DIFFICULTY.getHeight();

		field = new Square[x][y];
		bombs = new ArrayList<Bomb>();

		for (int ix = 0; ix < x; ix++)
			for (int iy = 0; iy < y; iy++)
				if (field[ix][iy] == null)
					field[ix][iy] = new game.objects.Number(ix, iy);
		
		if(timer != null)
			timer.stop();
		
		timer = new Clock();
	}

	public static Square getSquare(int x, int y) {
		if (!inBounds(x, y)) return null;
		return field[x][y];
	}
	
	public static boolean inBounds(int x, int y) {
		return between(x, y, 0, DIFFICULTY.getWidth(), 0, DIFFICULTY.getHeight());
	}

	public static boolean between(int x, int y, int tx, int tx2, int ty, int ty2) {
		return x >= tx && x < tx2 && y >= ty && y < ty2;
	}

	public static void setFlagCount(int s) {
		flagCount = (short)s;
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

	public static void end() {
		timer.stop();
		lost = true;
		screen.setFace(2);
	}

	public static void finish() {
		timer.stop();
		won = true;
		screen.setFace(3);
	}

	public static boolean hasLost() {
		return lost;
	}

	public static boolean hasWon() {
		return won;
	}

	public static boolean hasStarted() {
		return started;
	}

	public static void start() {
		started = true;
		Minesweeper.timer.start();
	}
	
	public static boolean isDone() {
		for (Square[] ar : field)
			for (Square s : ar) 
				if (s instanceof Bomb)
					if (!s.isFlagged())
						return false;
					else;
				else if (s instanceof game.objects.Number)
					if (s.isHidden())
						return false;
		return true;
	}

	private static void setDifficulty() {
		DIFFICULTY = (Difficulty) JOptionPane.showInputDialog(null, "Select Difficulty", "", JOptionPane.QUESTION_MESSAGE,
				null, Difficulty.values(), Difficulty.EASY);
		
		if(DIFFICULTY == null)
			System.exit(0);
	}
	
	public static enum Difficulty{
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
}