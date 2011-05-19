package main;

public class RunThread extends Thread {
	final public long STEP_WAIT_MS = 40;

	final public int STATE_RUNNING = 0;
	final public int STATE_WORKING = 1;
	final public int STATE_PAUSED = 2;
	final public int STATE_STOP = 3;
	private int state = STATE_RUNNING;

	private final Main main;
	private final RunThread runthread = this;
	private long startmillis;
	private double frames = 0;

	public RunThread(Main main) {
		this.main = main;
		start();
	}
	public void run() {
		try {
			startmillis = System.currentTimeMillis();
			while(true) {
				try {
					if(state == STATE_RUNNING) {
//						state = STATE_WORKING;
						frames ++;
						main.getFPSLabel().setText("fps: "+(frames / ((double)(System.currentTimeMillis() - startmillis) / 1000)));

						main.getDataHandler().act();
						main.getMyScaleCanvas().draw();
						main.getMyCanvas().draw();
					} else {
						startmillis = System.currentTimeMillis();
						frames = 0;
	
						if(state == STATE_STOP)
							break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Thread.sleep(STEP_WAIT_MS);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getRunState() {
		return state;
	}
	public void pauseSim() {
		state = STATE_PAUSED;
	}
	public void resumeSim() {
		state = STATE_RUNNING;
	}


	class WorkThread extends Thread {
		public WorkThread() {
			start();
		}
		@Override
		public void run() {
			try {
				while(true) {

					//TODO

					synchronized(runthread) {
						state = STATE_RUNNING;
						synchronized (this) {
							wait();
						}
					}
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
