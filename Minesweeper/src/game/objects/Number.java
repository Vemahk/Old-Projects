package game.objects;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import game.Minesweeper;
import static game.Minesweeper.images;

public class Number extends Square {

	private Set<Square> nearby;
	private byte count;

	public Number(int x, int y) {
		super(x, y);
	}

	public void compile() {
		// Set count
		nearby = surrounding();
		for (Square s : nearby)
			if (s instanceof Bomb)
				count++;
	}

	@Override
	public boolean reveal() {
		boolean suc = super.reveal();
		if (!suc)
			return false;

		if (isHidden()) {
			setVisible();
			if(count==0) for(Square s : nearby) s.reveal();
		} else {
			byte nearFlags = 0;
			
			for (Square s : nearby)
				if (s.isFlagged())
					nearFlags++;
			if (nearFlags >= count)
				for (Square s : nearby)
					if (!s.isFlagged() && s.isHidden())
						s.reveal();
		}
		
		draw(Minesweeper.screen.getGraphics());
		return true;
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if(!isHidden() && count > 0)g.drawImage(images.get("num_"+count), getX()*16 +10, getY()*16+46, null);
	}

	private Set<Square> surrounding(){
		Set<Square> out = new HashSet<Square>();
		
		for (int ix = getX() - 1; ix <= getX() + 1; ix++) {
			for (int iy = getY() - 1; iy <= getY() + 1; iy++) {
				if (ix < 0 || ix >= Minesweeper.DIFFICULTY.getWidth() || iy < 0 || iy >= Minesweeper.DIFFICULTY.getHeight())
					continue;
				if (ix == getX() && iy == getY())
					continue;
				Square s = Minesweeper.getSquare(ix, iy);
				out.add(s);
			}
		}
		
		return out;
	}
	
}
