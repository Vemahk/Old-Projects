package game;

import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.display.Screen;
import game.objects.Fruit;
import game.objects.MagicFruit;
import game.snake.Head;

public class Snake {

	private static ArrayList<Score> scores;
	
	public static long tickCount = 0;
	private static boolean end = false;
	
	public static final byte FPS = 20;
	
	public static byte WIDTH = 60;
	public static byte HEIGHT = 30;
	
	public static Head head;
	public static ArrayList<Fruit> fruits;
	public static MagicFruit mfruit;

	public static Screen screen;	
	public static void main(String[] args) {
		
		Thread t = new Thread(){
			public void run(){
				
				Object[] options = new Object[]{"40x30", "60x30", "40x40", "60x40", "70x30", "70x40"};
				String s = (String)JOptionPane.showInputDialog(null, "Pick a field size:\n(Note that window size will be w*20 by h*20)", "Field Size", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if(s!=null && s.length()!=0){
					WIDTH = Byte.parseByte(s.split("x")[0]);
					HEIGHT = Byte.parseByte(s.split("x")[1]);
				}else return;
				
				File score = new File(System.getProperty("user.home") + "/Documents/JAVASnake/Score-"+s+".dat");
				getScores(score);
				
				JFrame frame = buildFrame();
				
				while (true) {
					end = false;
					head = new Head((byte) 2, (byte) 2);

					mfruit = null;
					fruits = new ArrayList<Fruit>();
					
					fruits.add(new Fruit());
					while (tick()) {
						head.run();
						screen.repaint();
					}

					if (scores.size() == 0){
						String s2 = JOptionPane.showInputDialog(frame, "You just made a new high score of "+head.getCount()+"\nWhat is your name?");
						scores.add(new Score(head.getCount(), s2));
					}else if(scores.get(0).getVal() < head.getCount()){
						String s3 = JOptionPane.showInputDialog(frame, "You beat "+scores.get(0).getName()+"'s high score of " + head.getCount() + "!\nWhat is your name?");
						scores.add(new Score(head.getCount(), s3));
					}
					

					Collections.sort(scores);
					while(scores.size()>10)scores.remove(10);
					writeScore(score);

					int test = JOptionPane.showConfirmDialog(frame, "Play again?", "", JOptionPane.YES_NO_OPTION);
					if (test != 0){
						close(frame);
						System.exit(0);
					}
				}
			}
			
			public boolean tick(){
				try{
					Thread.sleep(1000/FPS);
				}catch(InterruptedException ex){
					System.err.println("Couldn't 'Sleep'");
				}
				tickCount++;
				return !end;
			}
		};
		
		t.start();
		
	}

	public static JFrame buildFrame(){
		JFrame frame = new JFrame("JAVASnake");
		
		frame.getContentPane().add(screen = new Screen());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		screen.requestFocus();
		
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
					String ln =scan.nextLine();
					scores.add(new Score(Short.parseShort(ln.split(":")[0]), ln.split(":")[1]));
				}
				scan.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void close(JFrame f) {
		f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
	}

	public static void stop() {
		end = true;
	}

}

class Score implements Comparable<Score>{
	
	private short value;
	private String n;
	
	public Score(short val, String name){
		value = val;
		n = name;
	}
	
	public short getVal(){return value;}
	public String getName(){return n;}
	
	public int compareTo(Score s){
		return -1 * ((Short)value).compareTo((Short)s.getVal());
	}
	
	public String toString(){
		return value+":"+n;
	}
}