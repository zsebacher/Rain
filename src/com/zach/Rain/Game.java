package com.zach.Rain;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "Rain";
	
	public static int width = 300;
	public static int height = width/16 * 9;
	public static int scale = 3;
	
	private Thread thread;
	private JFrame frame;
	private Screen screen;
	private Keyboard key;
	private boolean running = false;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

	int x=0,y=0;
	
	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		
		screen = new Screen(width, height);
		frame = new JFrame(Game.NAME);
		key = new Keyboard();
		
		addKeyListener(key);
	}
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();	
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime(); // time in nanoseconds
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0; // setting how many times to run the update method in a cycle
		double delta = 0;
		int frames = 0;
		int updates = 0;
		
		while(running) {
			long now = System.nanoTime(); 
			delta += (now-lastTime) / ns; 
			lastTime = now;
			while(delta >= 1) { // this will only happen 60 times a seconds
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			// once per second
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(Game.NAME + " | ups: " + updates + ", fps: " + frames);
				frames = 0;
				updates = 0;
			}
			
		}
		stop();
	}	
	
	public void update() {
		key.update();
		if(key.up) y--;
		if(key.down) y++;
		if(key.left) x--;
		if(key.right) x++; 

	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();
		screen.render(x,y);
		
		for(int i=0; i<pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();	
		
		
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		

		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLayout(new BorderLayout());
		game.frame.add(game);
		game.frame.pack();
		game.frame.setResizable(false);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
	}
}
