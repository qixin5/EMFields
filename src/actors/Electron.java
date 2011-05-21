package actors;

import java.util.Random;

import main.Main;
import other.ChargedParticle;
import other.DataHandler;
import other.Vector;

public class Electron extends SmoothActor implements ChargedParticle {
	static final Random randgen = new Random(System.currentTimeMillis());
	/**
	 * The range of the effect of the em field of this electron to the map.
	 */
	public static final int DISTANCE_INFLUENCED = 20;
	/**
	 * The range of the effect of the em fields of the map to this electron.
	 */
	public static final int INFLUENCING_DISTANCE = 20;

	static final double charge = -1;
	static final double[][] emMap;
	static final int[][] shadowMap;

	static {
		int mapsize = (int)(DISTANCE_INFLUENCED*2);
		emMap = new double[mapsize][mapsize];

		int centerxy = mapsize/2;
		for(int x = 0; x < mapsize; x++) {
			for(int y = 0; y < mapsize; y++) {
				double xdiff = x - centerxy;
				double ydiff = y - centerxy;
				double diff = Math.sqrt(xdiff*xdiff + ydiff*ydiff);
				if (diff <= DISTANCE_INFLUENCED)
					emMap[x][y] = DataHandler.calcEMForce(charge, diff, DISTANCE_INFLUENCED);
			}
		}

		shadowMap = new int[1][1];
		shadowMap[0][0] = 255;
	}

	/**
	 * Ringbuffer for track.
	 * Uninitialised positions are <code>null</code>.
	 */
	int[][] track = new int[150][];
	/**
	 * The position in the ringbuffer for track.
	 */
	int trackpos = 0;

	public Electron() {
		location = new Vector(
				randgen.nextDouble()*Main.FIELD_WIDTH,
				randgen.nextDouble()*Main.FIELD_HEIGHT);
	}
	public Electron(int x, int y) {
		location = new Vector(x, y);
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
		return track;
	}
	@Override
	public int getTrackPos() {
		return trackpos;
	}

	@Override
	public int getX() {
		return (int)location.getX();
	}
	@Override
	public int getY() {
		return (int)location.getY();
	}

	public void act(double[][] map) {
		// update track
		track[trackpos] = new int[]{getX(), getY()};
		trackpos ++;
		if(trackpos == track.length)
			trackpos = 0;

		// update acceleration
		acceleration.setLength(0);
		int xpos = getX();
		int ypos = getY();
		double xdiff, ydiff, diff;
		Vector v = new Vector();
		for (int x = xpos-INFLUENCING_DISTANCE; x <= xpos+INFLUENCING_DISTANCE; x++) {
			if (x < 0 || x >= map.length)
				continue;

			for (int y = ypos-INFLUENCING_DISTANCE; y <= ypos+INFLUENCING_DISTANCE; y++) {
				if (y < 0 || y >= map[x].length)
					continue;

				xdiff = (double)(x - xpos);
				ydiff = (double)(y - ypos);
				diff = Math.sqrt(xdiff*xdiff + ydiff*ydiff);
				if (diff > INFLUENCING_DISTANCE)
					continue;

				v.setX(xdiff);
				v.setY(ydiff);
				v.setLength(DataHandler.calcEMForce(map[x][y], diff, INFLUENCING_DISTANCE) / 100);
				acceleration.add(v);
			}
		}

		// apply effect of acceleration on velocity and of velocity on location.
		applyMovement();
	}
}
