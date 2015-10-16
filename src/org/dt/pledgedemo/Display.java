package org.dt.pledgedemo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Display extends JPanel{
	
	private volatile boolean running = true;
	private BufferedImage screenbuffer;
	private Graphics sbGraphics;
	private final int WIDTH, HEIGHT;

	public Display(int w, int h) {
		super();
		this.WIDTH = w * MazeGame.BLOCK_SIZE;
		this.HEIGHT = h * MazeGame.BLOCK_SIZE;
		this.setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
		screenbuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		sbGraphics = screenbuffer.getGraphics();
	}
	
	public void run() {
		long timenow = 0;		
		long lasttime = 0;
		while (running) {
			timenow = System.nanoTime();
			if (timenow - lasttime >= MazeGame.FRAMETIME) {
				render(this.getGraphics());
				lasttime = timenow;
			}
		}
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	public void render(Graphics g) {
		for (int y = 0; y < MazeGame.game.getHeight(); ++y) {
			for (int x = 0; x < MazeGame.game.getWidth(); ++x) {
				//render maze
				if (MazeGame.game.maze[x][y] == ' ')
					sbGraphics.setColor(Color.GRAY);
				else if (MazeGame.game.maze[x][y] == '#')
					sbGraphics.setColor(Color.BLACK);
				else if (MazeGame.game.maze[x][y] == 'X') {
					sbGraphics.setColor(Color.RED);
				}
				sbGraphics.fillRect(x * MazeGame.BLOCK_SIZE, y * MazeGame.BLOCK_SIZE, MazeGame.BLOCK_SIZE, MazeGame.BLOCK_SIZE);
				//render player
				if (MazeGame.game.player != null) {
					sbGraphics.setColor(Color.BLUE);
					sbGraphics.fillRect(MazeGame.game.player.xpos - MazeGame.PLAYER_SIZE / 2, MazeGame.game.player.ypos - MazeGame.PLAYER_SIZE / 2, MazeGame.PLAYER_SIZE, MazeGame.PLAYER_SIZE);
				}
				//render pledge
				if (MazeGame.game.pledge != null) {
					sbGraphics.setColor(Color.GREEN);
					sbGraphics.fillRect(MazeGame.game.pledge.xpos - MazeGame.PLAYER_SIZE / 2, MazeGame.game.pledge.ypos - MazeGame.PLAYER_SIZE / 2, MazeGame.PLAYER_SIZE, MazeGame.PLAYER_SIZE);
				}
			}
		}
		g.drawImage(screenbuffer, 0, 0, null);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		render(g);
	}

}
