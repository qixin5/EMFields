package actors;

import other.ChargedParticle;
import other.DataHandler;
import other.Vector;

public class Conductor implements ChargedParticle {
	static final int DISTANCE_INFLUENCED = 40;
	final double[][] emMap;
	final int[][] shadowMap;
	final double charge;
	int xpos, ypos;

	public Conductor(int xpos, int ypos, int width, int height, double charge) {
		this.xpos = xpos;
		this.ypos = ypos;
		this.charge = charge;

		emMap = new double[width+2*DISTANCE_INFLUENCED][height+2*DISTANCE_INFLUENCED];
		for(int x = 0; x < emMap.length; x++) {
			for(int y = 0; y < emMap[x].length; y++) {
				if(x >= DISTANCE_INFLUENCED && x < width+DISTANCE_INFLUENCED
						&& y >= DISTANCE_INFLUENCED && y < height+DISTANCE_INFLUENCED)
					emMap[x][y] = charge; 
				else {
					double xdiff;
					if(x < DISTANCE_INFLUENCED)
						xdiff = DISTANCE_INFLUENCED - x;
					else if (x >= width+DISTANCE_INFLUENCED)
						xdiff = x - width - DISTANCE_INFLUENCED;
					else
						xdiff = 0;

					double ydiff;
					if(y < DISTANCE_INFLUENCED)
						ydiff = DISTANCE_INFLUENCED - y;
					else if (y >= height+DISTANCE_INFLUENCED)
						ydiff = y - height - DISTANCE_INFLUENCED;
					else
						ydiff = 0;

					double diff = Math.sqrt(xdiff*xdiff + ydiff*ydiff);
					if(diff > DISTANCE_INFLUENCED)
						continue;

					emMap[x][y] = DataHandler.calcEMForce(charge, diff, DISTANCE_INFLUENCED);
				}
			}
		}

		shadowMap = new int[width][height];
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				shadowMap[x][y] = 255;
	}

	@Override
	public double getCharge() {
		return charge;
	}
	@Override
	public double[][] getEMMap() {
		return emMap;
	}
	@Override
	public int[][] getShadowMap() {
		return shadowMap;
	}
	@Override
	public int[][] getTrack() {
		return null;
	}
	@Override
	public int getTrackPos() {
		return -1;
	}

	@Override
	public int getX() {
		return xpos;
	}
	@Override
	public int getY() {
		return ypos;
	}

	@Override
	public Vector getLocation() {
		return new Vector(xpos, ypos);
	}
	@Override
	public Vector getVelocity() {
		return null;
	}

	@Override
	public void act(double[][] map) {
		//TODO ???
	}
}
