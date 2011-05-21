package main;

public class RunThread extends Thread {
	final public long STEP_WAIT_MS = 10;

	final public int STATE_RUNNING = 0;
	final public int STATE_WORKING = 1;
	final public int STATE_PAUSED = 2;
	final public int STATE_STOP = 3;
	private int state = STATE_RUNNING;

	private final Main main;

	private long[] millis = new long[10];
	private long lastmillis;
	private int millispos=0;

	public RunThread(Main main) {
		this.main = main;
		start();
	}
	public void run() {
		try {
			lastmillis = System.currentTimeMillis();
			while(true) {
				if(state == STATE_RUNNING) {
					// calculate fps
					millis[millispos] = System.currentTimeMillis()-lastmillis;
					lastmillis = System.currentTimeMillis();
					recalcFpS();
					millispos ++;
					if(millispos == millis.length)
						millispos = 0;

					// work
					main.getDataHandler().act();
					main.getMyScaleCanvas().draw();
					main.getMyCanvas().draw();
				} else {
					if(state == STATE_STOP)
						break;
				}
				Thread.sleep(main.getMillisToWait());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void recalcFpS() {
		long sum = 0;
		for(int i = 0; i < millis.length; i++) {
			sum += millis[i];
		}
		sum /= millis.length;
		main.getFPSLabel().setText("ms per frame: "+sum);
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
}
