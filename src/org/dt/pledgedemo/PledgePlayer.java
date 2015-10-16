package org.dt.pledgedemo;

public final class PledgePlayer extends Player {
	
	private int turns = 0;

	public PledgePlayer(int xpos, int ypos) {
		super(xpos, ypos);
		//face north first
		hand = 3;
	}

	@Override
	public void update(long deltaT) {
		if (!running)
			return;
		distance = (int)(VELOCITY * ((float)deltaT / 1_000_000_000.0f));
		//pledge algorithm
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
			//turn right, decrease turning number
			--turns;
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
			if (turns == 0)
				//total turning == 0, leave wall 
				handdown = false;
		}
		if (collision != 4) {
			//place right hand on colliding wall, "turn left"
			++turns;
			handdown = true;
			hand = collision;
		}
	}
}
