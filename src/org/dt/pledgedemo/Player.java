package org.dt.pledgedemo;

public class Player {
	
	public int xpos, ypos;
	protected boolean running = true;
	
	protected static final int VELOCITY = 200;//speed in pixels/second
	public static final long UPDATE_TIME = 1_000_000_000 / VELOCITY; //only update if moved at least 1 pixel
	public static final long MAX_UPDATE_TIME = (long)(1_000_000_000.0f * (float)MazeGame.BLOCK_SIZE / (float)VELOCITY);
	
	//(left turning) hand angle (in multiples of pi/2)
	protected int hand = 3;
	protected boolean handdown = false;
	
	//local variables of update(), declared here to improve performance
	protected int collision; 		//to save the result of move(...);
	protected int xsensor, ysensor;	//sensor position to test for wall contact
	protected int xreturn, yreturn;	//opposite of current direction (to return to the wall)
	protected int distance;			//

	public Player(int xpos, int ypos) {
		this.xpos = xpos;
		this.ypos = ypos;
	}
	
	/**
	 * Prevents the player from moving on update
	 */
	public final void stop() {
		running = false;
	}
	
	/**
	 * Moves the player every update
	 */
	public final void start() {
		running = true;
	}
	
	/**
	 * Moves the Player in either the x or the y direction.
	 * @param x amount of pixels to move in the x direction
	 * @param y amount of pixels to move in the y direction
	 * @return angle of collision with wall, 4 if no collision took place
	 */
	protected final int move(int x, int y) {
		if (x != 0) {
			xpos = xpos + x;
			if (MazeGame.game.getBlockAt(xpos, ypos) == '#') {
				if (x > 0) {
					//collision with right wall
					xpos = (xpos / MazeGame.BLOCK_SIZE) * MazeGame.BLOCK_SIZE - 1;
					return 3;
				}
				else {
					//collision with left wall
					xpos = ((xpos / MazeGame.BLOCK_SIZE) + 1) * MazeGame.BLOCK_SIZE;
					return 1;
				}
			}
		}
		else if (y != 0) {
			ypos = ypos + y;
			if (MazeGame.game.getBlockAt(xpos, ypos) == '#') {
				if (y > 0) {
					//collision with bottom wall
					ypos = (ypos / MazeGame.BLOCK_SIZE) * MazeGame.BLOCK_SIZE - 1;
					return 2;
				}
				else {
					//collision with top wall
					ypos = ((ypos / MazeGame.BLOCK_SIZE) + 1) * MazeGame.BLOCK_SIZE;
					return 0;
				}
			}
		}
		//no collision at all
		return 4;
	}

	public void update(long deltaT) {
		if (!running)
			return;
		distance = (int)(VELOCITY * ((float)deltaT / 1_000_000_000.0f));
		//right hand rule
		if (hand == 0) {
			//hand: top wall; facing: left
			collision = move(-distance, 0);
			xsensor = xpos;
			ysensor = ypos - 1;
			
		}
		else if (hand == 1) {
			//hand: left wall; facing: bottom
			collision = move(0, distance);
			xsensor = xpos - 1;
			ysensor = ypos;
		}
		else if (hand == 2) {
			//hand: bottom wall; facing: right
			collision = move(distance, 0);
			xsensor = xpos;
			ysensor = ypos + 1; 
		}
		else {
			//hand: right wall; facing: up
			collision = move(0, -distance);
			xsensor = xpos + 1;
			ysensor = ypos; 
		}
		//test for contact loss ("turn right" if neccessary)
		if (handdown && MazeGame.game.getBlockAt(xsensor, ysensor) != '#') {
			//derive path for return to wall
			xreturn = distance * (ypos - ysensor);
			yreturn = distance * (xsensor - xpos);
			//move in the direction of contact loss
			move(xsensor - xpos, ysensor - ypos);
			//move back to wall (along return path)
			move(xreturn, yreturn);
			--hand;
			if (hand < 0)
				hand = 3;
		}
		if (collision != 4) {
			//place right hand on colliding wall, "turn left"
			handdown = true;
			hand = collision;
		}
	}

}