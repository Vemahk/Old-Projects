package me.vem.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import me.vem.snake.Snake.bPoint;
import me.vem.snake.objects.Fruit;
import me.vem.snake.objects.MagicFruit;

public class App {

	private static ArrayList<Score> scores;
	
	private static boolean end = false;
	
	public static final int FPS = 60;
	public static final int UPS = 20;
	
	public static int WIDTH = 60;
	public static int HEIGHT = 30;
	
	public static Snake snake;
	
	public static void main(String[] args) {
		
		bPoint[] opt = new bPoint[]{new bPoint(40, 30), new bPoint(40, 40), new bPoint(60, 30),
									new bPoint(60, 40), new bPoint(70, 30), new bPoint(70, 40)};
		bPoint s = (bPoint)JOptionPane.showInputDialog(null, "Pick a field size:\n(Note that window size will be w*20 by h*20)", "Field Size", JOptionPane.PLAIN_MESSAGE, null, opt, opt[0]);
		if(s!=null){
			WIDTH = s.x;
			HEIGHT = s.y;
		}else return;
		
		File score = new File(System.getProperty("user.home") + "/Documents/JAVASnake/Score-"+s+".dat");
		getScores(score);
		
		JFrame frame = buildFrame();
		
		Thread graphics = new Thread() {
			public void run() {
				while(true) {
					
					frame.repaint();
					
					try{
						Thread.sleep(1000/FPS);
					}catch(InterruptedException ex){
						System.err.println("Couldn't 'Sleep'?");
					}
				}
			}
		};
		
		Thread update = new Thread(){
			public void run(){
				while (true) {
					end = false;
					snake = new Snake();

					Fruit.fruits.clear();
					new Fruit();
					MagicFruit.instance = null;
					
					while (tick()) 
						snake.run();

					if (scores.size() == 0){
						String s2 = JOptionPane.showInputDialog(frame, "You just made a new high score of "+snake.size()+"\nWhat is your name?");
						if(s2 == null || "".equals(s2))
							s2 = "Unknown";
						scores.add(new Score(snake.size(), s2));
					}else if(scores.get(0).getVal() < snake.size()){
						String s3 = JOptionPane.showInputDialog(frame, "You beat "+scores.get(0).getName()+"'s high score of " + scores.get(0).getVal() + "!\nWhat is your name?");
						if(s3 == null || "".equals(s3))
							s3 = "Unknown";
						scores.add(new Score(snake.size(), s3));
					}
					

					Collections.sort(scores);
					while(scores.size()>10)scores.remove(10);
					writeScore(score);

					int test = JOptionPane.showConfirmDialog(frame, "Play again?", "", JOptionPane.YES_NO_OPTION);
					if (test != 0){
						frame.dispose();
						System.exit(0);
					}
				}
			}
			
			public boolean tick(){
				try{
					Thread.sleep(1000/UPS);
				}catch(InterruptedException ex){
					System.err.println("Couldn't 'Sleep'?");
				}
				return !end;
			}
		};
		
		graphics.start();
		update.start();
		
	}

	public static JFrame buildFrame(){
		JFrame frame = new JFrame("JAVA - Snake");
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel screen = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				if(App.snake==null || Fruit.fruits == null)
					return;
				
				super.paintComponent(g);
				
				//Paint Border
				synchronized(snake) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(0, 0, getWidth(), 20);
					g.fillRect(0, 0, 20, getHeight());
					g.fillRect(0, getHeight()-20, getWidth(), 20);
					g.fillRect(getWidth()-20,0,20,getHeight());
					
					//Paint Fruit
					g.setColor(Color.RED);
					for(Fruit f : Fruit.fruits)
						g.fillRect(f.getX()*20+1, f.getY()*20+1, 18, 18);
					
					//Paint Snake
					g.setColor(Color.YELLOW);
					Iterator<bPoint> iter = snake.iterator();
					while(iter.hasNext()){
						bPoint n = iter.next();
						g.fillRect(n.x*20+1, n.y*20+1, 18, 18);
					}
					g.setColor(Color.WHITE);
					g.drawString("Snake Length: "+App.snake.size(), 0, 10);
					
					if(MagicFruit.instance!=null) MagicFruit.instance.draw(g);
				}
			}
		};
		screen.setPreferredSize(new Dimension(App.WIDTH * 20, App.HEIGHT * 20));
		screen.setBackground(Color.BLUE);
		
		frame.add(screen);
		
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		frame.addKeyListener(new KeyListener() {
			@Override public void keyReleased(KeyEvent e) { }
			@Override public void keyTyped(KeyEvent e) { }
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					frame.dispose();
					System.exit(0);
					return;
				}
				App.snake.queue = Snake.keyToDir(e.getKeyCode());
			}
		});
		
		return frame;
	}

	private static void writeScore(File f) {
		try {
			PrintWriter writer = new PrintWriter(f);

			for (int i = 0; i < scores.size(); i++)
				writer.println(scores.get(i));

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void getScores(File f) {
		scores = new ArrayList<Score>();
		try {
			f.getParentFile().mkdirs();
			if (!f.exists())
				f.createNewFile();
			else {
				Scanner scan = new Scanner(f);
				while (scan.hasNextLine()){
					String[] ln =scan.nextLine().split(":");
					scores.add(new Score(Short.parseShort(ln[0]), ln[1]));
				}
				scan.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void stop() {
		end = true;
	}
}

class Score implements Comparable<Score>{
	
	private int value;
	private String n;
	
	public Score(int i, String name){
		value = i;
		n = name;
	}
	
	public int getVal(){return value;}
	public String getName(){return n;}
	
	public int compareTo(Score s){
		return s.value - value;
	}
	
	public String toString(){
		return value+":"+n;
	}
}