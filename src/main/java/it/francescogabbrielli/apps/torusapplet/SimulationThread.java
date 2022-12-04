package it.francescogabbrielli.apps.torusapplet;


/**
 *
 * @author Francesco Gabbrielli
 */
public class SimulationThread extends Thread {

	private final SimulationSystem system;

	private boolean ok;
	private boolean running;

	public SimulationThread() {
		system = new SimulationSystem(20, 150d, 5.3d);
		setDaemon(true);
		ok = true;
	}

	public SimulationSystem getSystem() {
		return system;
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		running = true;
		system.calcForces();
		system.measurements();
		SimulationEvent evt = new SimulationEvent(system);
		system.fireUpdate(evt);
		while (ok) {
			if(running) {
				long t = System.currentTimeMillis();
				synchronized(this) {
					system.integrate1();
					system.calcForces();
					system.integrate2();
					system.measurements();
					system.scaleTemp();
					system.fireUpdate(evt);
					if (system.isExploded())
						running = false;
				}
				try {
					sleep(Math.max(10, 40+t-System.currentTimeMillis()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void close() {
		ok = false;
	}

}
