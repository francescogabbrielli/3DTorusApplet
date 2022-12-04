package it.francescogabbrielli.apps.torusapplet;

import java.util.EventListener;

/**
 *
 * @author Francesco Gabbrielli
 */
public interface SimulationListener extends EventListener {

	/** Notifies the simulation has been re-inited */
	public void init(SimulationEvent evt);

	/** Notifies the simulation is running and updating atoms */
	public void update(SimulationEvent evt);

	/** Notifies one of the simulation parameters has been changed */
	public void changed(SimulationEvent evt);

}
