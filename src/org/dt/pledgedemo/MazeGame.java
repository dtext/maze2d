package org.dt.pledgedemo;

import org.dt.toolbox.FormBuilder;
import org.dt.toolbox.TextFile;

import java.util.ArrayList;

import javax.swing.JFrame;

public class MazeGame implements Runnable{

	public static final long FRAMETIME = 1_000_000_000 / 60;
	public static final int BLOCK_SIZE = 30;
	public static final int PLAYER_SIZE = 15;
	
	public static MazeGame game;
	public Display display;
	public Player player;
	public Player pledge;
	
	public char[][] maze;
	
	//width and height of maze
	private int width;
	private int height;
	
	public MazeGame(String levelFile) {
		ArrayList<String> lines = new TextFile(levelFile).getAllLines();
		
		width = lines.get(0).length();
		height = lines.size();
		maze = new char[width][height];
		
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				maze[x][y] = lines.get(y).charAt(x);
				if (maze[x][y] == 'S') {
					int blockpos = BLOCK_SIZE / 2;
					player = new Player(x * BLOCK_SIZE + blockpos, y * BLOCK_SIZE + blockpos);
					pledge = new PledgePlayer(x * BLOCK_SIZE + blockpos, y * BLOCK_SIZE + blockpos);
					maze[x][y] = ' ';
				}
			}
		}	
	}
	
	public final int getWidth() {
		return width;
	}
	
	public final int getHeight()  {
		return height;
	}
	
	public final char getBlockAt(int x, int y) {
		return maze[x / BLOCK_SIZE][y / BLOCK_SIZE];
	}
	
	@Override
	public final void run() {
		long timenow = System.nanoTime();
		long lasttime = timenow;
		long deltaT;
		
		//options
//        player.stop();
//        pledge.stop();
		
		while (getBlockAt(player.xpos, player.ypos) != 'X' && getBlockAt(pledge.xpos, pledge.ypos) != 'X') {
			timenow = System.nanoTime();
			deltaT = timenow - lasttime;
			
			if (deltaT >= Player.MAX_UPDATE_TIME)
				lasttime = timenow; //keep collision working by skipping a tick
			else if(deltaT >= Player.UPDATE_TIME) {
				player.update(deltaT);
				pledge.update(deltaT);
				lasttime = timenow;
			}
		}
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			game = new MazeGame("level1.txt");
		} else {
			game = new MazeGame(args[0]);
		}
		game.display = new Display(game.width, game.height);
		javax.swing.JFrame f = FormBuilder.createJFrame(game.display, "MazeGame");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Thread tGame = new Thread(game);
		tGame.start();
		
		game.display.run();
	}
}
