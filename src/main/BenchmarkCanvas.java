package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import other.DataHandler;

public class BenchmarkCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	private static final int CANVAS_HEIGHT = 30;
	private static final int CENTERINDICATOR_HEIGHT = 10;
	private static final int CENTERINDICATOR_WIDTH = 5;

	private BufferStrategy strategy;
	private final DataHandler handler;

	public BenchmarkCanvas(DataHandler handler) {
		this.handler = handler;
		setBounds(0, 0, Main.FIELD_WIDTH*MapCanvas.PAINT_SCALE, CANVAS_HEIGHT);
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

		double maxcharge = handler.getMaxCharge();
		double mincharge = handler.getMinCharge();
		double minmaxdiff = maxcharge - mincharge;
		double diffperpixel = minmaxdiff / getWidth();

		double charge = mincharge;
		for(int i = 0; i < getWidth(); i++) {
			g.setColor(MapCanvas.getColorPosForCharge(charge, mincharge, maxcharge));
			g.drawLine(i, 0, i, getHeight()-CENTERINDICATOR_HEIGHT-1);

			charge += diffperpixel;
		}

		double centerPixel = Math.abs((mincharge / minmaxdiff) * (double)getWidth());
		g.setColor(Color.GREEN);
		g.fillRect((int)(centerPixel - CENTERINDICATOR_WIDTH/2), getHeight()-CENTERINDICATOR_HEIGHT,
				CENTERINDICATOR_WIDTH, CENTERINDICATOR_HEIGHT);

		// flip buffers
		g.dispose();
		strategy.show();
	}
}
