package other;

import java.util.ArrayList;

import main.Main;

public class DataHandler {
	public static final int BORDER = 20;
	public static final double FRICTION = 1.01;

	final Main main;
	ArrayList<ChargedParticle> particles = new ArrayList<ChargedParticle>();
	double[][] map = new double[Main.FIELD_WIDTH][Main.FIELD_HEIGHT];
	double maxcharge = 4;
	double mincharge = -4;

	public DataHandler(Main main) {
		this.main = main;
	}

	public double[][] getEMMap() {
		return map;
	}
	public double getMaxCharge() {
		return maxcharge;
	}
	public double getMinCharge() {
		return mincharge;
	}

	public void act() {
		resetMap();
		for(ChargedParticle p : particles) {
			// act
			p.act(map);

			// limit position
			if(p.getX() < BORDER) {
				p.getLocation().setX(BORDER);
				p.getVelocity().mirrorY();
			}
			else if(p.getX() >= Main.FIELD_WIDTH-BORDER) {
				p.getLocation().setX(Main.FIELD_WIDTH-1-BORDER);
				p.getVelocity().mirrorY();
			}
			if(p.getY() < BORDER) {
				p.getLocation().setY(BORDER);
				p.getVelocity().mirrorX();
			}
			else if(p.getY() >= Main.FIELD_HEIGHT-BORDER) {
				p.getLocation().setY(Main.FIELD_HEIGHT-1-BORDER);
				p.getVelocity().mirrorX();
			}
			// apply friction
			if(p.getVelocity() != null)
				p.getVelocity().setLength(p.getVelocity().getLength()/FRICTION);

			// draw charges
			double[][] pmap = p.getEMMap();
			drawEMMapTile(pmap, p.getX(), p.getY());
		}

		if(main.autoScale())
			recalcMaxMinCharge();
	}
	protected void resetMap() {
		for (int x = 0; x < map.length; x ++) {
			for (int y = 0; y < map[x].length; y ++) {
				map[x][y] = 0D;
			}
		}
	}
	public void drawEMMapTile(double[][] pmap, int xpos, int ypos) {
		int pmapx = -1; // the position in the particle map
		int pmapxhalf = pmap.length/2;
		for (int x = xpos-pmapxhalf; x < xpos+pmapxhalf; x++) {
			pmapx ++;
			if (x < 0 || x >= map.length)
				continue;

			int pmapy = -1;
			int pmapyhalf = pmap[pmapx].length/2;
			for (int y = ypos-pmapyhalf; y < ypos+pmapyhalf; y++) {
				pmapy ++;
				if (y < 0 || y >= map[x].length)
					continue;

				map[x][y] += pmap[pmapx][pmapy];
			}
		}
	}
	public void recalcMaxMinCharge() {
		maxcharge = 0;
		mincharge = 0;
		double val = 0;
		for (int x = 0; x < map.length; x ++) {
			for (int y = 0; y < map[x].length; y ++) {
				val = map[x][y];
				if(val > maxcharge)
					maxcharge = val;
				else if(val < mincharge)
					mincharge = val;
			}
		}

//		int round = mincharge*1000D;
		main.getMinChargeLabel().setText(""+mincharge);
		main.getMaxChargeLabel().setText(""+maxcharge);
	}

	public ArrayList<ChargedParticle> getParticles() {
		return particles;
	}
	public boolean addChargedParticle(ChargedParticle p) {
		return particles.add(p);
	}
	public boolean removeChargedParticle(ChargedParticle p) {
		return particles.remove(p);
	}

	public static double calcEMForce(double charge, double dist, double maxdist) {
		return charge * Math.pow(((dist-maxdist)/maxdist), 2);
	}
}
