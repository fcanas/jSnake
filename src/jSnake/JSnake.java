package jSnake;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;


public class JSnake {
	public static int BlockSize = 15;
	public static int BoardSize = 40;
	public static Random random = new Random();
	public static void main(String[] args) {
		JFrame window = new  JFrame("Snake!");
		GamePanel panel = new GamePanel();
		panel.setPreferredSize(new Dimension(BoardSize * BlockSize, BoardSize * BlockSize));
		window.add(panel);
		
		panel.snake = new Snake();
		panel.snake.locations.add(new Point(0, 1));
		panel.snake.locations.add(new Point(0, 2));
		panel.snake.locations.add(new Point(0, 3));
		
		panel.apples = new Apples();
		panel.apples.add(new Point(14, 15));
		panel.apples.add(new Point(20, 38));
		panel.apples.add(new Point(13, 3));
		
		panel.snake.apples = panel.apples;
		
		window.pack();
		window.setVisible(true);
		
		Timer timer = new Timer(100, panel);
		timer.setInitialDelay(100);
		timer.start(); 
		
		window.addKeyListener(panel.snake);
	}
}

interface Drawable {
	void draw(Graphics g);
}

@SuppressWarnings("serial")
class GamePanel extends JPanel implements ActionListener {
	Snake snake;
	Apples apples;
	protected void paintComponent(Graphics g) {
		snake.draw(g);
		apples.draw(g);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		snake.advance();
		repaint();
	}
}

class Apples implements Drawable {
	Set<Point> apples = new HashSet<Point>();
	public void add(Point p) {
		apples.add(p);
	}
	public void draw(Graphics g) {
		g.setColor(Color.red);
		for (Point a : apples) {
			a.draw(g);
		}
	}
}

enum Direction {
	Up, Down, Left, Right
}

class Snake implements Drawable, KeyListener{
	Apples apples;
	Direction direction = Direction.Down;
	int belly = 0;
	public ArrayList<Point> locations = new ArrayList<Point>();
	public void draw(Graphics g) {
		g.setColor(Color.black);
		for(Point p : locations) {
			p.draw(g);
		}
	}
	Point head() {
		return locations.get(locations.size() - 1);
	}
	public void advance() {
		if (belly > 0) {
			belly--;
		} else {
			locations.remove(0);
		}
		locations.add(head().nudge(direction));
		if (apples.apples.contains(head())) {
			eat(head());
		}
	}
	
	public void eat(Point a) {
		apples.apples.remove(a);
		apples.add(new Point(JSnake.random.nextInt(JSnake.BoardSize), JSnake.random.nextInt(JSnake.BoardSize)));
		belly++;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case 37:
			direction = direction==Direction.Right?Direction.Right:Direction.Left;
			break;
		case 38:
			direction = direction==Direction.Down?Direction.Down:Direction.Up;
			break;
		case 39:
			direction = direction==Direction.Left?Direction.Left:Direction.Right;
			break;
		case 40:
			direction = direction==Direction.Up?Direction.Up:Direction.Down;
			break;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}

class Point implements Drawable {
	int x;
	int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int hashCode() {
		return (x * y) ^ (x + y);
	}
	
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o instanceof Point) {
			Point p = (Point)o;
			return p.x == this.x && p.y == this.y;
		}
		return false;
	}
	
	public void draw(Graphics g) {
		g.fillRect(x * JSnake.BlockSize, y * JSnake.BlockSize, JSnake.BlockSize, JSnake.BlockSize);
	}

	public Point nudge(Direction direction) {
		int newX = (x + (direction==Direction.Left?-1:direction==Direction.Right?1:0)) % JSnake.BoardSize;
		int newY = (y + (direction==Direction.Up?-1:direction==Direction.Down?1:0)) % JSnake.BoardSize;
		if (newX < 0) {
			newX += JSnake.BoardSize;
		}
		if (newY < 0) {
			newY += JSnake.BoardSize;
		}
		return new Point(newX, newY);
	}
}