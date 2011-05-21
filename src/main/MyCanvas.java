package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import other.ChargedParticle;
import other.DataHandler;

public class MyCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	/**
	 * The length in pixels of one cell of the map.
	 */
	public static final int PAINT_SCALE = 3;

	public static final Color[] colorLookUpTable;
	public static final Color shadowColor = new Color(0f, 0f, 0f, 0.5f);

	static {
		// there are 511 colors
		// index 0 is value -255 and index 510 is value 255;
		colorLookUpTable = new Color[511];
		int i = 0;
		for(float val = -255f; val <= 255; val += 1f) {
			// val in range from -255 to 255 (both inclusive)
			// hue in range from 0 to 240 (both inclusive)
			float hue = Math.abs(((val+255)/511)*240/360);
			colorLookUpTable[i] = new Color(Color.HSBtoRGB(hue, 1, 1));
			i ++;
		}
	}

	private BufferStrategy strategy;
	private final DataHandler handler;

	public MyCanvas(DataHandler handler) {
		this.handler = handler;
		setBounds(0, 0, Main.FIELD_WIDTH*PAINT_SCALE, Main.FIELD_HEIGHT*PAINT_SCALE);
		setIgnoreRepaint(true);
	}
	public void init() {
		createBufferStrategy(2);
		strategy = getBufferStrategy();
	}

	public void draw() {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.setBackground(Color.BLACK);
		g.clearRect(0, 0, getWidth(), getHeight());

		// em map
		double[][] charges = handler.getEMMap();
		double charge;
		for(int x = 0; x < charges.length; x++) {
			for(int y = 0; y < charges[x].length; y++) {
				charge = charges[x][y];

				g.setColor(getColorPosForCharge(charge,
						handler.getMinCharge(),
						handler.getMaxCharge()));

				g.fillRect(x*PAINT_SCALE, y*PAINT_SCALE, PAINT_SCALE, PAINT_SCALE);
			}
		}

		// shadows
		g.setColor(shadowColor);
		int[][] shadow;
		int xshift, yshift;
		for(ChargedParticle p : handler.getParticles()) {
			shadow = p.getShadowMap();
			xshift = p.getX() - shadow.length/2;
			yshift = p.getY() - shadow.length/2;
			for(int x = 0; x < shadow.length; x++) {
				for(int y = 0; y < shadow[x].length; y++) {
					g.fillRect((x+xshift)*PAINT_SCALE, (y+yshift)*PAINT_SCALE, PAINT_SCALE, PAINT_SCALE);
				}
			}
		}

		// border
		drawBorder(g);

		// flip buffers
		g.dispose();
		strategy.show();
	}
	static final int BORDERPOS = DataHandler.BORDER*PAINT_SCALE;
	void drawBorder(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawRect(BORDERPOS,
				BORDERPOS,
				getWidth() - 2*BORDERPOS,
				getHeight() - 2*BORDERPOS);
	}

	public static Color getColorPosForCharge(double charge, double min, double max) {
		int val = 0;
		if(charge < 0)
			val = (int)(charge/min * -255);
		else
			val = (int)(charge/max * 255);
		val += 255;

		if(val < 0 || val >= colorLookUpTable.length)
			return Color.WHITE;
		else
			return colorLookUpTable[val];
	}
}
