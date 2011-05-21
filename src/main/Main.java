package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	private JLabel fpslabel;

	DataHandler handler = new DataHandler();

	public Main() {
		super("EM Fields");
		setLayout(new BorderLayout(2, 2));

		JPanel scaleplot = new JPanel();
			benchmarkCanvas = new BenchmarkCanvas(handler);
			scaleplot.add(benchmarkCanvas);
		add(scaleplot, BorderLayout.NORTH);

		JPanel plot = new JPanel();
			canvas = new MapCanvas(handler);
			plot.add(canvas);
		add(plot, BorderLayout.WEST);

		JPanel leftpanel = new JPanel(new BorderLayout(2, 2));
			leftpanel.setPreferredSize(new Dimension(200, -1));
			JPanel leftnorthpanel = new JPanel(new GridLayout(2, 1, 2, 2));
				pausebutton = new JButton("pause");
					pausebutton.addActionListener(new MyActionListener());
				leftnorthpanel.add(pausebutton);
	
				fpslabel = new JLabel("fps: ?");
				leftnorthpanel.add(fpslabel);
			leftpanel.add(leftnorthpanel, BorderLayout.NORTH);
		add(leftpanel, BorderLayout.CENTER);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);

		handler.addChargedParticle(new Conductor(100, 100, 1, 50, 1));
		for(int i = 0; i < 2; i++)
			handler.addChargedParticle(new Electron(100, 120));

		benchmarkCanvas.init();
		canvas.init();
		runthread = new RunThread(this);
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

	private class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if(event.getActionCommand().equals("pause")) {
				runthread.pauseSim();
				pausebutton.setText("resume");
			} else if(event.getActionCommand().equals("resume")) {
				runthread.resumeSim();
				pausebutton.setText("pause");
			}
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}
