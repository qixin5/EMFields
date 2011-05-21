package main;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import other.DataHandler;
import actors.Conductor;
import actors.Electron;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	public static final int FIELD_WIDTH = 200;
	public static final int FIELD_HEIGHT = 200;

	private RunThread runthread;

	private BenchmarkCanvas benchmarkCanvas;
	private MapCanvas canvas;

	private JButton pausebutton;
	private JSlider speedslider;
	private JLabel fpslabel, minchargelabel, maxchargelabel;
	private Checkbox autoScale, drawShadows, drawTracks;

	DataHandler handler = new DataHandler(this);

	public Main() {
		super("EM Fields");
		setLayout(new BorderLayout(2, 2));

		JPanel scaleplot = new JPanel(new BorderLayout(2, 2));
			minchargelabel = new JLabel(""+handler.getMinCharge());
				minchargelabel.setPreferredSize(new Dimension(70, -1));
			scaleplot.add(minchargelabel, BorderLayout.WEST);

			benchmarkCanvas = new BenchmarkCanvas(handler);
			scaleplot.add(benchmarkCanvas);

			maxchargelabel = new JLabel(""+handler.getMaxCharge());
				maxchargelabel.setPreferredSize(new Dimension(70, -1));
			scaleplot.add(maxchargelabel, BorderLayout.EAST);
		add(scaleplot, BorderLayout.NORTH);

		JPanel plot = new JPanel();
			canvas = new MapCanvas(this, handler);
			plot.add(canvas);
		add(plot, BorderLayout.WEST);

		JPanel leftpanel = new JPanel(new BorderLayout(2, 2));
			leftpanel.setPreferredSize(new Dimension(200, -1));
			JPanel leftnorthpanel = new JPanel(new GridLayout(6, 1, 2, 4));
				pausebutton = new JButton("start");
					pausebutton.addActionListener(new MyActionListener());
				leftnorthpanel.add(pausebutton);

				speedslider = new JSlider(0, 200, 5);
					speedslider.setPaintLabels(true);
					speedslider.setLabelTable(speedslider.createStandardLabels(50));

					speedslider.setPaintTicks(true);
					speedslider.setMinorTickSpacing(10);
					speedslider.setMajorTickSpacing(50);
				leftnorthpanel.add(speedslider);
	
				fpslabel = new JLabel("fps: ?");
				leftnorthpanel.add(fpslabel);

				autoScale = new Checkbox("auto scale", true);
				leftnorthpanel.add(autoScale);

				drawShadows = new Checkbox("draw shadows", true);
				leftnorthpanel.add(drawShadows);

				drawTracks = new Checkbox("draw tracks", false);
				leftnorthpanel.add(drawTracks);
			leftpanel.add(leftnorthpanel, BorderLayout.NORTH);
		add(leftpanel, BorderLayout.CENTER);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);

		handler.addChargedParticle(new Conductor(100, 100, 1, 1, 1));
		for (int i = 0; i < 1; i ++)
			handler.addChargedParticle(new Electron(100, 120));

		benchmarkCanvas.init();
		canvas.init();
		runthread = new RunThread(this);
		runthread.pauseSim();
	}
	public JLabel getFPSLabel() {
		return fpslabel;
	}
	public DataHandler getDataHandler() {
		return handler;
	}
	public MapCanvas getMyCanvas() {
		return canvas;
	}
	public BenchmarkCanvas getMyScaleCanvas() {
		return benchmarkCanvas;
	}
	public JLabel getMinChargeLabel() {
		return minchargelabel;
	}
	public JLabel getMaxChargeLabel() {
		return maxchargelabel;
	}

	public long getMillisToWait() {
		return speedslider.getValue();
	}
	public boolean autoScale() {
		return autoScale.getState();
	}
	public boolean drawShadows() {
		return drawShadows.getState();
	}
	public boolean drawTracks() {
		return drawTracks.getState();
	}

	private class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if(event.getActionCommand().equals("pause")) {
				runthread.pauseSim();
				pausebutton.setText("resume");
			} else if(event.getActionCommand().equals("resume") || event.getActionCommand().equals("start")) {
				runthread.resumeSim();
				pausebutton.setText("pause");
			}
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}
